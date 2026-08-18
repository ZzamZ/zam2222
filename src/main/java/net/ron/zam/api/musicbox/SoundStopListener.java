package net.ron.zam.api.musicbox;

@FunctionalInterface
public interface SoundStopListener {
    /**
     * Called just before the sound is removed from the map.
     */
    void onStop();
}