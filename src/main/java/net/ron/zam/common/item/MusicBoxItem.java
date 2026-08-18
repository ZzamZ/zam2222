package net.ron.zam.common.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.ron.zam.api.musicbox.PlayableRecord;
import net.ron.zam.api.musicbox.SoundTracker;
import net.ron.zam.common.component.LoopingComponent;
import net.ron.zam.common.component.PausedComponent;
import net.ron.zam.common.component.PlayingRecordComponent;
import net.ron.zam.registry.ZAMComponents;
import net.ron.zam.util.tooltips.MusicBoxTooltip;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class MusicBoxItem extends Item {

    private static final Map<Integer, ItemStack> PLAYING_RECORDS = new Int2ObjectArrayMap<>();

    public MusicBoxItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static void onLivingEntityUpdateClient(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;

        ItemStack newPlayingRecord = ItemStack.EMPTY;

        ItemStack carried = player.inventoryMenu.getCarried();
        if (hasRecord(carried) && !carried.has(ZAMComponents.PAUSED)) {
            newPlayingRecord = getRecord(carried);
        }

        if (newPlayingRecord.isEmpty()) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack handStack = player.getItemInHand(hand);

                if (hasRecord(handStack) && !handStack.has(ZAMComponents.PAUSED)) {
                    newPlayingRecord = getRecord(handStack);
                    break;
                }
            }
        }

        if (newPlayingRecord.isEmpty()) {
            for (ItemStack invStack : player.getInventory().getNonEquipmentItems()) {
                if (hasRecord(invStack) && !invStack.has(ZAMComponents.PAUSED)) {
                    newPlayingRecord = getRecord(invStack);
                    break;
                }
            }
        }

        updatePlaying(entity, newPlayingRecord);
    }

    private static void updatePlaying(Entity entity, ItemStack newRecord) {
        ItemStack oldRecord = PLAYING_RECORDS.getOrDefault(entity.getId(), ItemStack.EMPTY);

        if (ItemStack.matches(oldRecord, newRecord)) {
            return;
        }

        boolean wasPlaying = !oldRecord.isEmpty();
        boolean isPlaying = !newRecord.isEmpty();

        SoundTracker.playMusicBox(entity.getId(), newRecord);

        if (isPlaying) {
            PLAYING_RECORDS.put(entity.getId(), newRecord);
        } else {
            PLAYING_RECORDS.remove(entity.getId());
        }

        if (wasPlaying != isPlaying && !entity.level().isClientSide()) {
            entity.level().gameEvent(
                    isPlaying ? GameEvent.JUKEBOX_PLAY : GameEvent.JUKEBOX_STOP_PLAY,
                    entity.position(),
                    Context.of(entity)
            );
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isSecondaryUseActive()) {

            if (stack.has(ZAMComponents.PAUSED)) {
                stack.remove(ZAMComponents.PAUSED);
            } else {
                stack.set(
                        ZAMComponents.PAUSED,
                        PausedComponent.INSTANCE
                );
            }

            return InteractionResult.SUCCESS
                    .heldItemTransformedTo(stack);
        }

        if (stack.has(ZAMComponents.LOOPING)) {
            stack.remove(ZAMComponents.LOOPING);
        } else {
            stack.set(
                    ZAMComponents.LOOPING,
                    LoopingComponent.INSTANCE
            );
        }

        return InteractionResult.SUCCESS
                .heldItemTransformedTo(stack);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack musicbox, Slot slot, ClickAction clickAction, Player player) {
        ItemStack clickItem = slot.getItem();

        if (clickAction == ClickAction.SECONDARY && clickItem.isEmpty()) {
            ItemStack record = getRecord(musicbox);

            if (!record.isEmpty()) {
                player.playSound(
                        SoundEvents.BUNDLE_REMOVE_ONE,
                        0.8F,
                        0.8F + player.level().getRandom().nextFloat() * 0.4F
                );

                setRecord(musicbox, slot.safeInsert(record));

                return true;
            }
        }

        if (clickAction == ClickAction.PRIMARY
                && PlayableRecord.isPlayableRecord(clickItem)) {

            player.playSound(
                    SoundEvents.BUNDLE_INSERT,
                    0.8F,
                    0.8F + player.level().getRandom().nextFloat() * 0.4F
            );

            ItemStack old = getRecord(musicbox);

            setRecord(
                    musicbox,
                    slot.safeTake(clickItem.getCount(), 1, player).split(1)
            );

            slot.set(old);

            return true;
        }

        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack musicbox, ItemStack clickItem, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (!slot.allowModification(player)) {
            return false;
        }

        if (clickAction == ClickAction.SECONDARY && clickItem.isEmpty()) {
            ItemStack record = getRecord(musicbox);

            if (!record.isEmpty()) {
                player.playSound(
                        SoundEvents.BUNDLE_REMOVE_ONE,
                        0.8F,
                        0.8F + player.level().getRandom().nextFloat() * 0.4F
                );

                slotAccess.set(record);

                setRecord(musicbox, ItemStack.EMPTY);

                return true;
            }
        }

        if (clickAction == ClickAction.PRIMARY
                && PlayableRecord.isPlayableRecord(clickItem)) {

            ItemStack old = getRecord(musicbox);

            if (old.isEmpty() || clickItem.getCount() == 1) {
                player.playSound(
                        SoundEvents.BUNDLE_INSERT,
                        0.8F,
                        0.8F + player.level().getRandom().nextFloat() * 0.4F
                );

                setRecord(musicbox, clickItem.split(1));

                slotAccess.set(old);

                return true;
            }
        }

        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, consumer, flag);

        if (!hasRecord(stack)) {
            consumer.accept(
                    Component.literal("No disc inserted")
                            .withStyle(ChatFormatting.GRAY)
            );
        }

        if (Minecraft.getInstance().hasShiftDown()) {
            consumer.accept(Component.literal("Left Click on Music Box: Insert Disc").withStyle(ChatFormatting.GRAY));
            consumer.accept(Component.literal("Right Click on Music Box: Remove Disc").withStyle(ChatFormatting.GRAY));
            consumer.accept(Component.literal("Right Click/Use: Toggle Looping").withStyle(ChatFormatting.GRAY));
            consumer.accept(Component.literal("Right Click/Use + Shift: Pause").withStyle(ChatFormatting.GRAY));

        } else {

            consumer.accept(
                    Component.literal("Hold Shift for Controls")
                            .withStyle(ChatFormatting.DARK_GRAY)
            );
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (!hasRecord(stack)) {
            return Optional.empty();
        }

        return Optional.of(
                new MusicBoxTooltip(
                        getRecord(stack),
                        stack.has(ZAMComponents.PAUSED),
                        stack.has(ZAMComponents.LOOPING)
                )
        );
    }

    @Nullable
    public static InteractionHand getPlayingHand(LivingEntity entity) {
        if (!PLAYING_RECORDS.containsKey(entity.getId())) {
            return null;
        }
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = entity.getItemInHand(hand);
            if (hasRecord(stack) && !stack.has(ZAMComponents.PAUSED)) {
                return hand;
            }
        }
        return null;
    }

    public static boolean hasRecord(ItemStack stack) {
        return stack.has(ZAMComponents.PLAYING_RECORD);
    }

    public static ItemStack getRecord(ItemStack stack) {
        PlayingRecordComponent comp =
                stack.get(ZAMComponents.PLAYING_RECORD);

        return comp != null
                ? comp.stack()
                : ItemStack.EMPTY;
    }

    public static void setRecord(ItemStack stack, ItemStack record) {
        if (record.isEmpty()) {
            stack.remove(ZAMComponents.PLAYING_RECORD);
        } else {
            stack.set(
                    ZAMComponents.PLAYING_RECORD,
                    new PlayingRecordComponent(
                            record.copyWithCount(1)
                    )
            );
        }
    }
}