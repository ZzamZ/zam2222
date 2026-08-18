package net.ron.zam.common.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class LootBoxItem extends Item {
    private final ResourceKey<LootTable> lootTable;

    public LootBoxItem(ResourceKey<LootTable> lootTable, Properties properties) {
        super(properties);
        this.lootTable = lootTable;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        if (!(level instanceof ServerLevel serverLevel) || this.lootTable == null) {
            return InteractionResult.FAIL;
        }

        ItemStack heldStack = player.getItemInHand(hand);
        LootParams lootParams = new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY);

        List<ItemStack> loot = serverLevel.getServer()
                .reloadableRegistries()
                .getLootTable(this.lootTable)
                .getRandomItems(lootParams);

        if (loot.isEmpty()) {
            return InteractionResult.FAIL;
        }

        for (ItemStack stack : loot) {
            this.giveItem(player, stack);
        }

        if (!player.hasInfiniteMaterials()) {
            heldStack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    private void giveItem(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        } else if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.inventoryMenu.sendAllDataToRemote();
        }
    }
}