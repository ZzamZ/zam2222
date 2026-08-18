package net.ron.zam.api.casesystem.cases;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.rarity.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class CaseJsonLoader {

    private CaseJsonLoader() {
    }

    public static CaseEntry load(Identifier id, JsonObject json) {
        JsonArray rewardsJson = requiredArray(json, "rewards");

        if (rewardsJson.isEmpty()) {
            throw new IllegalArgumentException(id + " has no rewards");
        }

        List<CaseReward> rewards = new ArrayList<>();

        for (JsonElement element : rewardsJson) {
            rewards.add(readReward(id, element.getAsJsonObject()));
        }

        return new CaseEntry(
                id,
                Component.literal(string(json, "title", prettyName(id.getPath()))),
                readItem(json.get("key_item")),
                Identifier.parse(requiredString(json, "item_model")),
                Identifier.parse(string(
                        json,
                        "texture",
                        ZAMMod.id("textures/gui/case_two.png").toString()
                )),
                json.has("rows")
                        ? json.get("rows").getAsInt()
                        : Math.max(1, Math.min(6, (rewards.size() + 8) / 9)),
                rewards,
                json.toString()
        );
    }

    private static CaseReward readReward(Identifier caseId, JsonObject json) {
        Rarity rarity = rarity(requiredString(json, "rarity"));

        if (json.has("pool")) {
            ItemDefinition display = readItem(json.get("display"));
            JsonArray poolJson = json.getAsJsonArray("pool");

            if (poolJson.isEmpty()) {
                throw new IllegalArgumentException(caseId + " has an empty reward pool");
            }

            List<ItemDefinition> pool = new ArrayList<>();

            for (JsonElement element : poolJson) {
                pool.add(readItem(element));
            }

            return new CaseReward(
                    display,
                    pool,
                    rarity,
                    true
            );
        }

        ItemDefinition reward = readItem(json.get("item"));

        return new CaseReward(
                reward,
                List.of(reward),
                rarity,
                false
        );
    }

    private static ItemDefinition readItem(JsonElement element) {
        if (element.isJsonPrimitive()) {
            return new ItemDefinition(
                    Identifier.parse(element.getAsString())
            );
        }

        JsonObject json = element.getAsJsonObject();

        return new ItemDefinition(
                Identifier.parse(requiredString(json, "id")),
                json.has("count") ? json.get("count").getAsInt() : 1
        );
    }

    private static Rarity rarity(String value) {
        try {
            return Rarity.valueOf(
                    value.toUpperCase(Locale.ROOT)
                            .replace('-', '_')
                            .replace(' ', '_')
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Unknown case rarity: " + value,
                    exception
            );
        }
    }

    private static String requiredString(JsonObject json, String key) {
        if (!json.has(key)) {
            throw new IllegalArgumentException(
                    "Missing required field \"" + key + "\""
            );
        }

        return json.get(key).getAsString();
    }

    private static JsonArray requiredArray(JsonObject json, String key) {
        if (!json.has(key) || !json.get(key).isJsonArray()) {
            throw new IllegalArgumentException(
                    "Missing required array \"" + key + "\""
            );
        }

        return json.getAsJsonArray(key);
    }

    private static String string(JsonObject json, String key, String fallback) {
        return json.has(key)
                ? json.get(key).getAsString()
                : fallback;
    }

    private static String prettyName(String path) {
        String[] words = path.substring(path.lastIndexOf('/') + 1)
                .replace('_', ' ')
                .split(" ");

        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            if (!result.isEmpty()) {
                result.append(' ');
            }

            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1));
        }

        return result.toString();
    }
}