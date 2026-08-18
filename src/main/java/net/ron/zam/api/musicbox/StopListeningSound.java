package net.ron.zam.api.musicbox;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Wrapper for {@link SoundInstance} that respects {@link SoundStopListener}.
 *
 * @author Ocelot
 */
public class StopListeningSound implements SoundInstance, SoundStopListener, WrappedSoundInstance {
    private final SoundInstance source;
    private final SoundStopListener listener;
    private boolean ignoringEvents;

    public StopListeningSound(SoundInstance source, SoundStopListener listener) {
        this.source = source;
        this.listener = listener;
        this.ignoringEvents = false;
    }

    public static StopListeningSound create(SoundInstance source, SoundStopListener listener) {
        return source instanceof TickableSoundInstance tickable
            ? new TickableStopListeningSound(tickable, listener)
            : new StopListeningSound(source, listener);
    }

    public void stopListening() {
        this.ignoringEvents = true;
    }

    @Override
    public SoundInstance getParent() {
        return this.source;
    }

    @Override
    public Identifier getIdentifier() {
        return this.source.getIdentifier();
    }

    @Override
    public @Nullable WeighedSoundEvents resolve(SoundManager manager) {
        return this.source.resolve(manager);
    }

    @Override
    public Sound getSound() {
        return this.source.getSound();
    }

    @Override
    public SoundSource getSource() {
        return this.source.getSource();
    }

    @Override
    public boolean isLooping() {
        return this.source.isLooping();
    }

    @Override
    public boolean isRelative() {
        return this.source.isRelative();
    }

    @Override
    public int getDelay() {
        return this.source.getDelay();
    }

    @Override
    public float getVolume() {
        return this.source.getVolume();
    }

    @Override
    public float getPitch() {
        return this.source.getPitch();
    }

    @Override
    public double getX() {
        return this.source.getX();
    }

    @Override
    public double getY() {
        return this.source.getY();
    }

    @Override
    public double getZ() {
        return this.source.getZ();
    }

    @Override
    public Attenuation getAttenuation() {
        return this.source.getAttenuation();
    }

    @Override
    public boolean canStartSilent() {
        return this.source.canStartSilent();
    }

    @Override
    public boolean canPlaySound() {
        return this.source.canPlaySound();
    }

    @Override
    public CompletableFuture<AudioStream> getAudioStream(SoundBufferLibrary loader, Identifier id, boolean repeatInstantly) {
        return this.source.getAudioStream(loader, id, repeatInstantly);
    }

    @Override
    public void onStop() {
        if (!this.ignoringEvents) {
            this.listener.onStop();
        }
    }
}