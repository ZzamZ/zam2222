package net.ron.zam.api.projector;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.renderer.texture.AbstractTexture;

import java.nio.ByteBuffer;
import java.util.OptionalDouble;

public final class BlazeVideoTexture extends AbstractTexture {
    private static final int BUFFER_COUNT = 3;

    private final int width, height, frameSize;
    private final GpuBuffer[] staging = new GpuBuffer[BUFFER_COUNT];

    private int index;

    public BlazeVideoTexture(String label, int width, int height) {
        this.width = width;
        this.height = height;
        this.frameSize = width * height * 4;

        var device = RenderSystem.getDevice();

        this.texture = device.createTexture(
                label,
                GpuTexture.USAGE_TEXTURE_BINDING | GpuTexture.USAGE_COPY_DST,
                GpuFormat.RGBA8_UNORM,
                width,
                height,
                1,
                1
        );

        this.textureView = device.createTextureView(this.texture);

        this.sampler = device.createSampler(
                AddressMode.CLAMP_TO_EDGE,
                AddressMode.CLAMP_TO_EDGE,
                FilterMode.LINEAR,
                FilterMode.LINEAR,
                1,
                OptionalDouble.empty()
        );

        long size = frameSize;

        for (int i = 0; i < BUFFER_COUNT; i++) {
            final int bufferIndex = i;

            staging[i] = device.createBuffer(
                    () -> label + "/staging_" + bufferIndex,
                    GpuBuffer.USAGE_MAP_WRITE
                            | GpuBuffer.USAGE_COPY_SRC
                            | GpuBuffer.USAGE_HINT_CLIENT_STORAGE,
                    size
            );
        }
    }

    public void upload(ByteBuffer source) {
        GpuBuffer buffer = staging[index];
        index = (index + 1) % BUFFER_COUNT;

        try (var mapped = buffer.map(false, true)) {
            ByteBuffer src = source.duplicate();
            ByteBuffer dst = mapped.data();

            src.rewind();
            dst.clear();
            dst.put(src);
        }

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

        encoder.copyBufferToTexture(
                buffer.slice(),
                0,
                0,
                width,
                height,
                getTexture(),
                0,
                0,
                width,
                height,
                0,
                0
        );

        encoder.submit();
    }

    @Override
    public void close() {
        for (GpuBuffer buffer : staging) {
            if (buffer != null && !buffer.isClosed())
                buffer.close();
        }

        super.close();
    }
}