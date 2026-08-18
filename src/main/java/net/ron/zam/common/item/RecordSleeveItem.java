package net.ron.zam.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.ron.zam.util.tooltips.RecordSleeveTooltip;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class RecordSleeveItem extends BundleItem {

    public static final int MAX_DISCS = 8;

    private static final CustomModelData FILLED_CMD = new CustomModelData(List.of(1.0f), List.of(), List.of(), List.of());

    private static final Field MUTABLE_ITEMS_FIELD;

    static {
        Field field = null;
        try {
            field = BundleContents.Mutable.class.getDeclaredField("items");
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MUTABLE_ITEMS_FIELD = field;
    }

    public RecordSleeveItem(Properties props) {
        super(props.stacksTo(1));
    }

    private static boolean isDisc(ItemStack s) {
        return !s.isEmpty() && s.has(DataComponents.JUKEBOX_PLAYABLE);
    }

    private static int countDiscs(Iterable<ItemStack> items) {
        int n = 0;
        for (ItemStack s : items) {
            if (isDisc(s)) {
                if (++n >= MAX_DISCS) break;
            }
        }
        return n;
    }

    @SuppressWarnings("unchecked")
    private static List<ItemStack> getMutableList(BundleContents.Mutable mut) {
        if (MUTABLE_ITEMS_FIELD == null) return List.of();
        try {
            return (List<ItemStack>) MUTABLE_ITEMS_FIELD.get(mut);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private static void writeContents(ItemStack sleeve, BundleContents.Mutable mut) {
        BundleContents imm = mut.toImmutable();
        if (imm == null || imm.isEmpty()) {
            sleeve.remove(DataComponents.BUNDLE_CONTENTS);
        } else {
            sleeve.set(DataComponents.BUNDLE_CONTENTS, imm);
        }
        updateSleeveModel(sleeve);
    }

    private static void updateSleeveModel(ItemStack sleeve) {
        BundleContents c = sleeve.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        boolean hasDisc = c.itemCopyStream().anyMatch(RecordSleeveItem::isDisc);

        if (hasDisc) {
            sleeve.set(DataComponents.CUSTOM_MODEL_DATA, FILLED_CMD);
        } else {
            sleeve.remove(DataComponents.CUSTOM_MODEL_DATA);
        }
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack sleeve, Slot slot, ClickAction action, Player player) {
        if (sleeve.getCount() != 1) return false;

        BundleContents contents = sleeve.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        if (action == ClickAction.SECONDARY && slot.getItem().isEmpty()) {
            BundleContents.Mutable mut = new BundleContents.Mutable(contents);
            ItemStack removed = mut.removeOne();
            if (removed != null && isDisc(removed)) {
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                ItemStack leftover = slot.safeInsert(removed);
                if (!leftover.isEmpty()) {
                    mut.tryInsert(leftover);
                }
                writeContents(sleeve, mut);
                return true;
            }
            return false;
        }

        if (action == ClickAction.PRIMARY && isDisc(slot.getItem())) {
            if (countDiscs(contents.itemCopyStream().toList()) < MAX_DISCS) {
                BundleContents.Mutable mut = new BundleContents.Mutable(contents);
                List<ItemStack> items = getMutableList(mut);

                ItemStack taken = slot.remove(1);
                if (!taken.isEmpty()) {
                    taken.setCount(1);
                    items.add(taken);
                    writeContents(sleeve, mut);
                    player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack sleeve, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (sleeve.getCount() != 1 || !slot.allowModification(player)) return false;

        BundleContents contents = sleeve.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        if (action == ClickAction.SECONDARY && other.isEmpty()) {
            BundleContents.Mutable mut = new BundleContents.Mutable(contents);
            ItemStack removed = mut.removeOne();
            if (removed != null && isDisc(removed)) {
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                access.set(removed);
                writeContents(sleeve, mut);
                return true;
            }
            return false;
        }

        if (action == ClickAction.PRIMARY && isDisc(other)) {
            if (countDiscs(contents.itemCopyStream().toList()) < MAX_DISCS) {
                BundleContents.Mutable mut = new BundleContents.Mutable(contents);
                List<ItemStack> items = getMutableList(mut);

                ItemStack one = other.split(1);
                if (!one.isEmpty()) {
                    one.setCount(1);
                    items.add(one);
                    writeContents(sleeve, mut);
                    player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                    return true;
                }
            }
        }

        return false;
    }

    @Override public boolean isBarVisible(ItemStack s) { return false; }
    @Override public int getBarWidth(ItemStack s) { return 0; }
    @Override public int getBarColor(ItemStack s) { return 0; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        int count = countDiscs(contents.itemCopyStream().toList());

        if (count > 0) {
            consumer.accept(Component.literal(count + " / " + MAX_DISCS).withStyle(ChatFormatting.GRAY));
        }

        if (Minecraft.getInstance().hasShiftDown()) {
            consumer.accept(Component.literal("Stores music discs as a playlist").withStyle(ChatFormatting.GRAY));
            consumer.accept(Component.literal("Play in a Jukebox or Music Box").withStyle(ChatFormatting.GRAY));
        } else {
            consumer.accept(
                    Component.literal(
                            "Hold Shift for Info"
                    ).withStyle(ChatFormatting.DARK_GRAY)
            );
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        BundleContents c = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return Optional.of(new RecordSleeveTooltip(c));
    }

    @Override
    public void onDestroyed(ItemEntity entity) {
        BundleContents c = entity.getItem().get(DataComponents.BUNDLE_CONTENTS);
        if (c != null && !c.isEmpty()) {
            entity.getItem().remove(DataComponents.BUNDLE_CONTENTS);
            entity.getItem().remove(DataComponents.CUSTOM_MODEL_DATA);

            c.itemCopyStream().forEach(i ->
                    entity.level().addFreshEntity(new ItemEntity(
                            entity.level(), entity.getX(), entity.getY(), entity.getZ(), i
                    ))
            );
        }
    }
}