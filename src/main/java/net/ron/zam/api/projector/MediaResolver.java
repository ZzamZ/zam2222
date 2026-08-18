package net.ron.zam.api.projector;

import com.google.gson.*;
import net.ron.zam.ZAMMod;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;

public final class MediaResolver {
    private static final Map<String, Cached> CACHE = new ConcurrentHashMap<>();
    private static final long CACHE_TIME = 5 * 60_000L;
    private static final String FORMAT = "bv[vcodec^=avc1][height<=1080][fps<=60]+ba[ext=m4a]"
            + "/bv[height<=1080][fps<=60]+ba/b[height<=1080]";

    private MediaResolver() {}

    public static ResolvedMedia resolve(String input) throws Exception {
        String url = input.trim();
        if (!needsResolver(url)) return ResolvedMedia.direct(url);

        Cached cached = CACHE.get(url);
        long now = System.currentTimeMillis();

        if (cached != null && now - cached.time < CACHE_TIME)
            return cached.media;

        ResolvedMedia media = resolveHosted(url);
        CACHE.put(url, new Cached(media, now));
        return media;
    }

    public static void invalidate(String url) {
        CACHE.remove(url);
    }

    private static ResolvedMedia resolveHosted(String url) throws Exception {
        List<String> command = new ArrayList<>();
        command.add(ProjectorTools.ytDlp().toAbsolutePath().toString());
        Collections.addAll(command, "--ignore-config", "--quiet", "--no-warnings",
                "--no-playlist", "--dump-single-json", "--js-runtimes",
                "deno:" + ProjectorTools.deno().toAbsolutePath(), "-f", FORMAT);

        if (Files.isRegularFile(ProjectorTools.cookies())) {
            command.add("--cookies");
            command.add(ProjectorTools.cookies().toAbsolutePath().toString());
        }

        command.add(url);

        Process process = new ProcessBuilder(command).start();
        CompletableFuture<String> stdout = read(process.getInputStream(), "zam-ytdlp-out");
        CompletableFuture<String> stderr = read(process.getErrorStream(), "zam-ytdlp-err");

        if (!process.waitFor(60, TimeUnit.SECONDS)) {
            process.destroyForcibly();
            throw new IllegalStateException("Media resolver timed out");
        }

        String json = stdout.get(5, TimeUnit.SECONDS);
        String error = stderr.get(5, TimeUnit.SECONDS);

        if (process.exitValue() != 0 || json.isBlank())
            throw new IllegalStateException(error.isBlank() ? "yt-dlp failed" : error.trim());

        return parse(JsonParser.parseString(json).getAsJsonObject());
    }

    private static ResolvedMedia parse(JsonObject root) {
        Map<String, String> rootHeaders = headers(root);
        ResolvedMedia.Stream video = null, audio = null;

        if (root.has("requested_formats") && root.get("requested_formats").isJsonArray()) {
            for (JsonElement element : root.getAsJsonArray("requested_formats")) {
                JsonObject format = element.getAsJsonObject();
                ResolvedMedia.Stream stream = stream(format, rootHeaders);
                if (stream == null) continue;

                if (video == null && codec(format, "vcodec")) video = stream;
                if (audio == null && codec(format, "acodec")) audio = stream;
            }
        } else {
            ResolvedMedia.Stream stream = stream(root, rootHeaders);

            if (stream != null) {
                boolean hasVideo = codec(root, "vcodec"), hasAudio = codec(root, "acodec");
                if (hasVideo) video = stream;
                if (hasAudio) audio = stream;
                if (!hasVideo && !hasAudio) video = audio = stream;
            }
        }

        if (video == null)
            throw new IllegalStateException("No playable video stream was resolved");

        String title = string(root, "title");
        String creator = string(root, "channel");
        if (creator.isBlank()) creator = string(root, "uploader");

        return new ResolvedMedia(video, audio, title, creator);
    }

    private static String string(JsonObject object, String key) {
        return object.has(key) && !object.get(key).isJsonNull()
                ? object.get(key).getAsString() : "";
    }

    @Nullable
    private static ResolvedMedia.Stream stream(JsonObject object, Map<String, String> parentHeaders) {
        if (!object.has("url") || object.get("url").isJsonNull()) return null;

        Map<String, String> headers = new LinkedHashMap<>(parentHeaders);
        headers.putAll(headers(object));
        return new ResolvedMedia.Stream(object.get("url").getAsString(), headers);
    }

    private static Map<String, String> headers(JsonObject object) {
        Map<String, String> result = new LinkedHashMap<>();

        if (object.has("http_headers") && object.get("http_headers").isJsonObject())
            object.getAsJsonObject("http_headers").entrySet().forEach(entry -> {
                if (!entry.getValue().isJsonNull())
                    result.put(entry.getKey(), entry.getValue().getAsString());
            });

        return result;
    }

    private static boolean codec(JsonObject object, String key) {
        return object.has(key) && !object.get(key).isJsonNull()
                && !"none".equalsIgnoreCase(object.get(key).getAsString());
    }

    private static boolean needsResolver(String value) {
        if (!value.startsWith("http://") && !value.startsWith("https://")) return false;

        try {
            String host = URI.create(value).getHost();
            if (host == null) return false;

            host = host.toLowerCase(Locale.ROOT);
            return host.equals("youtu.be") || host.endsWith("youtube.com")
                    || host.endsWith("twitch.tv") || host.endsWith("vimeo.com")
                    || host.endsWith("kick.com");
        } catch (Exception ignored) {
            return false;
        }
    }

    private static CompletableFuture<String> read(InputStream input, String name) {
        CompletableFuture<String> future = new CompletableFuture<>();

        Thread thread = new Thread(() -> {
            try (input) {
                future.complete(new String(input.readAllBytes(), StandardCharsets.UTF_8));
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, name);

        thread.setDaemon(true);
        thread.start();
        return future;
    }

    private record Cached(ResolvedMedia media, long time) {}
}