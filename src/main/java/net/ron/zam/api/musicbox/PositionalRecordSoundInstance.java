package net.ron.zam.api.musicbox;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class PositionalRecordSoundInstance extends AbstractSoundInstance {

    public PositionalRecordSoundInstance(
            SoundEvent sound,
            double x,
            double y,
            double z,
            int attenuationDistance
    ) {
        super(sound, SoundSource.RECORDS, RandomSource.create());

        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.looping = false;
        this.delay = 0;
        this.relative = false;
        this.attenuation = Attenuation.LINEAR;
    }
}