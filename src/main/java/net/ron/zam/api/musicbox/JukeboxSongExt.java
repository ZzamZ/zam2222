package net.ron.zam.api.musicbox;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public interface JukeboxSongExt {
    List<TrackData> zam$tracks();
}