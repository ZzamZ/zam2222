package net.ron.zam.api.cassette;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.data.CassetteData;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CassetteReloadListener implements IdentifiableResourceReloadListener {

    private static final String DIRECTORY = "cassette_tape";
    private final Map<Identifier, CassetteData> map = new HashMap<>();

    @Nullable
    public CassetteData get(Identifier id) {
        return map.get(id);
    }

    public Map<Identifier, CassetteData> all() {
        return map;
    }

    @Override
    public Identifier getFabricId() {
        return ZAMMod.id("cassettes");
    }

    @Override
    public CompletableFuture<Void> reload(SharedState shared, Executor prepExecutor,
                                          PreparationBarrier barrier, Executor applyExecutor) {
        return CompletableFuture
                .supplyAsync(() -> scan(shared.resourceManager()), prepExecutor)
                .thenCompose(barrier::wait)
                .thenAcceptAsync(loaded -> {
                    map.clear();
                    map.putAll(loaded);
                    ZAMMod.LOGGER.info("ZAM's Mod: loaded {} cassette definitions", map.size());
                }, applyExecutor);
    }

    private Map<Identifier, CassetteData> scan(ResourceManager manager) {
        Map<Identifier, CassetteData> result = new HashMap<>();
        for (Map.Entry<Identifier, Resource> entry : manager.listResources(DIRECTORY, id -> id.getPath().endsWith(".json")).entrySet()) {
            Identifier fileId = entry.getKey();
            String path = fileId.getPath();
            String cleanPath = path.substring(DIRECTORY.length() + 1, path.length() - ".json".length());
            Identifier id = Identifier.fromNamespaceAndPath(fileId.getNamespace(), cleanPath);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(entry.getValue().open(), StandardCharsets.UTF_8))) {
                JsonElement json = JsonParser.parseReader(reader);
                CassetteData.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE, json)
                        .resultOrPartial(err -> ZAMMod.LOGGER.error("Failed to parse cassette {}: {}", id, err))
                        .ifPresent(data -> result.put(id, data));
            } catch (IOException e) {
                ZAMMod.LOGGER.error("Failed to read cassette {}", id, e);
            }
        }
        return result;
    }
}
