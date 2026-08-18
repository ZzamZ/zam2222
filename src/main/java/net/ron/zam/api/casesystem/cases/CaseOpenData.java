package net.ron.zam.api.casesystem.cases;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record CaseOpenData(Identifier caseId, String json) {

    public static final StreamCodec<RegistryFriendlyByteBuf, CaseOpenData> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            CaseOpenData::caseId,
            ByteBufCodecs.STRING_UTF8,
            CaseOpenData::json,
            CaseOpenData::new
    );

    public static CaseOpenData of(CaseEntry entry) {
        return new CaseOpenData(entry.id(), entry.sourceJson());
    }
}