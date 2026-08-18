package net.ron.zam.mixin;

import com.google.common.base.Suppliers;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.ron.zam.api.musicbox.JukeboxSongExt;
import net.ron.zam.api.musicbox.TrackData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Mixin(JukeboxSong.class)
public class JukeboxSongMixin implements JukeboxSongExt {
    @Shadow @Final private Holder<SoundEvent> soundEvent;
    @Shadow @Final private Component description;

    @Unique private final Supplier<List<TrackData>> zam$track = Suppliers.memoize(() -> {
        String id = this.soundEvent.value().location().toString();
        String[] parts = this.description.getString().split("-", 2);
        if (parts.length < 2) {
            return Collections.singletonList(new TrackData(id, "Minecraft", this.description));
        }

        return Collections.singletonList(new TrackData(id, parts[0].trim(), Component.literal(parts[1].trim()).withStyle(this.description.getStyle())));
    });

    @Override
    public List<TrackData> zam$tracks() {
        return this.zam$track.get();
    }
}