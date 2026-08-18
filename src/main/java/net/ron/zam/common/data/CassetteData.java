package net.ron.zam.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record CassetteData(Identifier assetId, int color, Optional<Identifier> soundEvent,
                           Optional<Integer> soundDuration, int comparatorOutput) {

    private static final Codec<Integer> ARGB_CODEC = Codec.STRING.comapFlatMap(
            s -> {
                String stripped = s.startsWith("#") ? s.substring(1)
                        : s.startsWith("0x") || s.startsWith("0X") ? s.substring(2) : s;
                try {
                    return DataResult.success((int) Long.parseLong(stripped, 16));
                } catch (NumberFormatException e) {
                    return DataResult.error(() -> "Not a hex color: " + s);
                }
            },
            i -> String.format("0x%08x", i));

    public static final Codec<CassetteData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Identifier.CODEC.fieldOf("asset_id").forGetter(CassetteData::assetId),
            ARGB_CODEC.fieldOf("color").forGetter(CassetteData::color),
            Identifier.CODEC.optionalFieldOf("sound_event").forGetter(CassetteData::soundEvent),
            Codec.INT.optionalFieldOf("sound_duration").forGetter(CassetteData::soundDuration),
            Codec.INT.optionalFieldOf("comparator_output", 1).forGetter(CassetteData::comparatorOutput)
    ).apply(inst, CassetteData::new));

    public Identifier framePath() {
        return Identifier.fromNamespaceAndPath(assetId.getNamespace(),
                "textures/cassette_tape/" + assetId.getPath() + ".png");
    }
}
