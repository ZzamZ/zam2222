package net.ron.zam.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.ron.zam.ZAMMod;

import java.util.*;
import java.util.stream.Collectors;

public class ZAMSavedData extends SavedData {
    private final Map<UUID, Set<Item>> items;

    public static final Codec<ZAMSavedData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(
                            Codec.STRING.xmap(UUID::fromString, UUID::toString),
                            Codec.list(Identifier.CODEC)
                    ).xmap(
                            map -> {
                                Map<UUID, Set<Item>> converted = new HashMap<>();
                                for (Map.Entry<UUID, List<Identifier>> entry : map.entrySet()) {
                                    Set<Item> itemSet = entry.getValue().stream()
                                            .map(rl -> BuiltInRegistries.ITEM.getOptional(rl)
                                                    .orElseThrow(() -> new IllegalStateException("Unknown item: " + rl)))
                                            .collect(Collectors.toSet());
                                    converted.put(entry.getKey(), itemSet);
                                }
                                return converted;
                            },
                            map -> {
                                Map<UUID, List<Identifier>> converted = new HashMap<>();
                                for (Map.Entry<UUID, Set<Item>> entry : map.entrySet()) {
                                    List<Identifier> rlList = entry.getValue().stream()
                                            .map(BuiltInRegistries.ITEM::getKey)
                                            .toList();
                                    converted.put(entry.getKey(), rlList);
                                }
                                return converted;
                            }
                    ).fieldOf("data").forGetter(d -> d.items)
            ).apply(instance, items -> new ZAMSavedData(items))
    );


    public static final SavedDataType<ZAMSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, "collected_items"),
            ZAMSavedData::new,
            ZAMSavedData.CODEC,
            DataFixTypes.SAVED_DATA_SCOREBOARD
    );

    public ZAMSavedData(Map<UUID, Set<Item>> items) {
        this.items = items;
    }

    public ZAMSavedData() {
        this(new HashMap<>());
    }

    private static ZAMSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    public static void setCollected(MinecraftServer server, Player player, Item item) {
        ZAMSavedData data = get(server);
        data.items.computeIfAbsent(player.getUUID(), u -> new HashSet<>()).add(item);
        data.setDirty();
    }

    public static boolean isCollected(MinecraftServer server, Player player, Item item) {
        ZAMSavedData data = get(server);
        return data.items.getOrDefault(player.getUUID(), Collections.emptySet()).contains(item);
    }
}
