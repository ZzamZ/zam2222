package net.ron.zam.api.casesystem.cases;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.ron.zam.api.rarity.Rarity;

import java.util.List;

public final class CaseBuilder {
    private final JsonObject json = new JsonObject();
    private final JsonArray rewards = new JsonArray();

    public CaseBuilder title(String title) {
        this.json.addProperty("title", title);
        return this;
    }

    public CaseBuilder keyItem(Item item) {
        this.json.addProperty("key_item", id(item));
        return this;
    }

    public CaseBuilder itemModel(Identifier model) {
        this.json.addProperty("item_model", model.toString());
        return this;
    }

    public CaseBuilder texture(Identifier texture) {
        this.json.addProperty("texture", texture.toString());
        return this;
    }

    public CaseBuilder rows(int rows) {
        this.json.addProperty("rows", rows);
        return this;
    }

    public CaseBuilder reward(Item item, Rarity rarity) {
        JsonObject reward = new JsonObject();

        reward.addProperty("item", id(item));
        reward.addProperty("rarity", rarity.name().toLowerCase());

        this.rewards.add(reward);
        return this;
    }

    public CaseBuilder choice(Item display, Rarity rarity, Item... items) {
        return choice(display, rarity, List.of(items));
    }

    public CaseBuilder choice(Item display, Rarity rarity, List<Item> items) {
        JsonObject reward = new JsonObject();
        JsonArray pool = new JsonArray();

        reward.addProperty("display", id(display));
        reward.addProperty("rarity", rarity.name().toLowerCase());

        for (Item item : items) {
            pool.add(id(item));
        }

        reward.add("pool", pool);
        this.rewards.add(reward);

        return this;
    }

    public JsonObject build() {
        JsonObject result = this.json.deepCopy();
        result.add("rewards", this.rewards.deepCopy());

        return result;
    }

    private static String id(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }
}