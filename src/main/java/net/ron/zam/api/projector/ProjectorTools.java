package net.ron.zam.api.projector;

import net.fabricmc.loader.api.FabricLoader;
import net.ron.zam.ZAMMod;

import java.io.InputStream;
import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.util.zip.*;

public final class ProjectorTools {
    private static final Path DIR = FabricLoader.getInstance().getGameDir()
            .resolve("zam").resolve("projector").resolve("tools");

    private static final Path FFMPEG = DIR.resolve("ffmpeg.exe");
    private static final Path FFPROBE = DIR.resolve("ffprobe.exe");
    private static final Path YTDLP = DIR.resolve("yt-dlp.exe");
    private static final Path DENO = DIR.resolve("deno.exe");
    private static final Path COOKIES = DIR.resolve("cookies.txt");
    private static final long YTDLP_REFRESH = 24L * 60 * 60 * 1000;

    private static final String FFMPEG_URL =
            "https://www.gyan.dev/ffmpeg/builds/ffmpeg-release-essentials.zip";
    private static final String YTDLP_URL =
            "https://github.com/yt-dlp/yt-dlp-nightly-builds/releases/latest/download/yt-dlp.exe";
    private static final String DENO_URL =
            "https://github.com/denoland/deno/releases/latest/download/deno-x86_64-pc-windows-msvc.zip";

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL).build();

    private ProjectorTools() {}

    public static synchronized Path ffmpeg() throws Exception {
        Files.createDirectories(DIR);

        if (!valid(FFMPEG) || !valid(FFPROBE)) {
            ZAMMod.LOGGER.info("Downloading FFmpeg...");
            installFFmpeg();
        }

        return FFMPEG;
    }

    public static synchronized Path ytDlp() throws Exception {
        Files.createDirectories(DIR);

        if (!valid(YTDLP)) {
            ZAMMod.LOGGER.info("Downloading yt-dlp...");
            download(YTDLP_URL, YTDLP);
        } else if (System.currentTimeMillis() - Files.getLastModifiedTime(YTDLP).toMillis() > YTDLP_REFRESH) {
            try {
                ZAMMod.LOGGER.info("Updating yt-dlp...");
                download(YTDLP_URL, YTDLP);
            } catch (Exception e) {
                ZAMMod.LOGGER.warn("Could not update yt-dlp; using existing version");
            }
        }

        return YTDLP;
    }

    public static synchronized Path deno() throws Exception {
        Files.createDirectories(DIR);

        if (!valid(DENO)) {
            ZAMMod.LOGGER.info("Downloading Deno...");
            Path zip = DIR.resolve("deno.zip");

            try {
                download(DENO_URL, zip);
                extract(zip, "deno.exe", DENO);
            } finally {
                Files.deleteIfExists(zip);
            }
        }

        return DENO;
    }

    public static Path cookies() {
        return COOKIES;
    }

    private static void installFFmpeg() throws Exception {
        Path zip = DIR.resolve("ffmpeg.zip");

        try {
            download(FFMPEG_URL, zip);
            extract(zip, "ffmpeg.exe", FFMPEG);
            extract(zip, "ffprobe.exe", FFPROBE);
        } finally {
            Files.deleteIfExists(zip);
        }

        if (!valid(FFMPEG) || !valid(FFPROBE))
            throw new IllegalStateException("FFmpeg archive is missing required executables");
    }

    private static void extract(Path zip, String name, Path target) throws Exception {
        try (ZipInputStream input = new ZipInputStream(Files.newInputStream(zip))) {
            ZipEntry entry;

            while ((entry = input.getNextEntry()) != null) {
                if (!entry.isDirectory()
                        && Path.of(entry.getName()).getFileName().toString().equalsIgnoreCase(name)) {
                    Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
                    return;
                }
            }
        }

        throw new IllegalStateException("Archive is missing " + name);
    }

    private static void download(String url, Path target) throws Exception {
        Files.createDirectories(target.getParent());
        Path temp = target.resolveSibling(target.getFileName() + ".download");

        try {
            Files.deleteIfExists(temp);

            HttpResponse<InputStream> response = HTTP.send(
                    HttpRequest.newBuilder(URI.create(url))
                            .header("User-Agent", "ZAM-Mod")
                            .GET().build(),
                    HttpResponse.BodyHandlers.ofInputStream()
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300)
                throw new IllegalStateException("HTTP " + response.statusCode());

            try (InputStream input = response.body()) {
                Files.copy(input, temp, StandardCopyOption.REPLACE_EXISTING);
            }

            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    private static boolean valid(Path path) {
        try {
            return Files.isRegularFile(path) && Files.size(path) > 0;
        } catch (Exception ignored) {
            return false;
        }
    }
}