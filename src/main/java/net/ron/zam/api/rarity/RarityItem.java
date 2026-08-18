package net.ron.zam.api.rarity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a prize entry with an associated rarity.
 * - Normal items: new RarityItem(stack, rarity)
 * - Gold mystery: RarityItem.mystery(goldIcon, equipmentPool, Rarity.RARE)
 * - Red mystery:  RarityItem.mystery(redIcon, hatPool, Rarity.VERY_RARE)
 */
public final class RarityItem {

    private final ItemStack displayStack;
    private final Rarity rarity;
    private final List<ItemStack> rewardPool;
    private final boolean requiresChoice;

    public RarityItem(ItemStack itemStack, Rarity rarity) {
        this.displayStack = itemStack.copy();
        this.rarity = Objects.requireNonNull(rarity, "rarity");

        List<ItemStack> one = new ArrayList<>(1);
        one.add(itemStack.copy());
        this.rewardPool = Collections.unmodifiableList(one);
        this.requiresChoice = false;
    }

    public static RarityItem mystery(ItemStack display, List<ItemStack> pool, Rarity rarity) {
        Objects.requireNonNull(display, "display");
        Objects.requireNonNull(pool, "pool");
        Objects.requireNonNull(rarity, "rarity");

        List<ItemStack> copy = new ArrayList<>(pool.size());
        for (ItemStack s : pool) copy.add(s.copy());

        return new RarityItem(display.copy(), rarity, Collections.unmodifiableList(copy), true);
    }

    private RarityItem(ItemStack displayStack, Rarity rarity, List<ItemStack> rewardPool, boolean requiresChoice) {
        this.displayStack = displayStack;
        this.rarity = rarity;
        this.rewardPool = rewardPool;
        this.requiresChoice = requiresChoice;
    }

    public boolean requiresChoice() {
        return this.rewardPool.size() > 1;
    }

    public ItemStack getItemStack() {
        return this.displayStack.copy();
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    /** 🔧 Restored legacy API */
    public int getRewardPoolSize() {
        return this.rewardPool.size();
    }

    /** 🔧 Restored legacy API */
    public ItemStack getSingleReward() {
        if (this.rewardPool.size() != 1) {
            throw new IllegalStateException("Expected exactly one reward, but pool size is " + this.rewardPool.size());
        }
        return this.rewardPool.get(0).copy();
    }

    public List<ItemStack> viewRewardPool() {
        List<ItemStack> out = new ArrayList<>(rewardPool.size());
        for (ItemStack s : rewardPool) out.add(s.copy());
        return out;
    }

    public ItemStack resolveReward(RandomSource random) {
        if (rewardPool.isEmpty()) {
            throw new IllegalStateException("Reward pool is empty");
        }

        if (rewardPool.size() == 1) {
            return rewardPool.get(0).copy();
        }

        return rewardPool.get(random.nextInt(rewardPool.size())).copy();
    }

    @Override
    public String toString() {
        return "RarityItem{" +
                "display=" + displayStack +
                ", rarity=" + rarity +
                ", poolSize=" + rewardPool.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RarityItem that)) return false;
        return Objects.equals(displayStack, that.displayStack)
                && rarity == that.rarity
                && Objects.equals(rewardPool, that.rewardPool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayStack, rarity, rewardPool);
    }
}
