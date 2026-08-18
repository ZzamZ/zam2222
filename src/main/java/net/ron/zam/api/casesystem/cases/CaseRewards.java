package net.ron.zam.api.casesystem.cases;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.ron.zam.ZAMMod;

import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CaseRewards {
    private static final Map<Identifier, CaseEntry> CASES = new HashMap<>();
    private static final String DIRECTORY = "cases";

    private CaseRewards() {
    }

    public static void init() {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(
                ZAMMod.id("cases"),
                new SimpleReloadListener<Map<Identifier, JsonObject>>() {

                    @Override
                    protected Map<Identifier, JsonObject> prepare(PreparableReloadListener.SharedState state) {
                        return loadJson(state.resourceManager());
                    }

                    @Override
                    protected void apply(Map<Identifier, JsonObject> jsonCases, PreparableReloadListener.SharedState state) {
                        Map<Identifier, CaseEntry> loaded = new HashMap<>();

                        jsonCases.forEach((id, json) -> {
                            try {
                                loaded.put(
                                        id,
                                        CaseJsonLoader.load(id, json)
                                );
                            } catch (Exception exception) {
                                ZAMMod.LOGGER.error(
                                        "Failed to decode case {}",
                                        id,
                                        exception
                                );
                            }
                        });

                        CASES.clear();
                        CASES.putAll(loaded);

                        ZAMMod.LOGGER.info(
                                "Loaded {} cases",
                                CASES.size()
                        );
                    }
                }
        );
    }

    private static Map<Identifier, JsonObject> loadJson(ResourceManager manager) {
        Map<Identifier, JsonObject> loaded = new HashMap<>();

        manager.listResources(
                DIRECTORY,
                id -> id.getPath().endsWith(".json")
        ).forEach((resourceId, resource) -> {
            try (Reader reader = resource.openAsReader()) {
                loaded.put(
                        caseId(resourceId),
                        JsonParser.parseReader(reader).getAsJsonObject()
                );
            } catch (Exception exception) {
                ZAMMod.LOGGER.error(
                        "Failed to read case {}",
                        resourceId,
                        exception
                );
            }
        });

        return loaded;
    }

    private static Identifier caseId(Identifier resource) {
        String path = resource.getPath()
                .substring(DIRECTORY.length() + 1);

        path = path.substring(
                0,
                path.length() - ".json".length()
        );

        return Identifier.fromNamespaceAndPath(
                resource.getNamespace(),
                path
        );
    }

    public static Optional<CaseEntry> getById(Identifier id) {
        return Optional.ofNullable(CASES.get(id));
    }

    public static CaseEntry getOrThrow(Identifier id) {
        return getById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown case: " + id)
                );
    }

    public static Collection<CaseEntry> values() {
        return List.copyOf(CASES.values());
    }
}