package net.ron.zam.util;

public interface RecordSleeveJukeboxExtension {

    int zam$getRecordSleeveTrack();

    void zam$setRecordSleeveTrack(int track);

    boolean zam$isRecordSleevePlaying();

    void zam$setRecordSleevePlaying(boolean playing);

    boolean zam$isRestartPending();

    void zam$setRestartPending(boolean pending);
}