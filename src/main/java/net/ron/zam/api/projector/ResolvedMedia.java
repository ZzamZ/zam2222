package net.ron.zam.api.projector;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record ResolvedMedia(Stream video, @Nullable Stream audio, String title, String creator) {
    public static ResolvedMedia direct(String url) {
        Stream stream = new Stream(url, Map.of());
        return new ResolvedMedia(stream, stream, "", "");
    }

    public record Stream(String url, Map<String, String> headers) {
        public Stream {
            url = url.trim();
            headers = Map.copyOf(headers);
        }
    }
}