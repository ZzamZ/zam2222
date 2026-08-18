package net.ron.zam.common.item.caserewards;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.Equippable;

import java.util.function.Consumer;

public class HatItem extends Item {

    private final String caseCollection;

    public HatItem(Properties properties, String caseCollection, SoundEvent equipSound) {
        super(properties.component(
                DataComponents.EQUIPPABLE,
                Equippable.builder(EquipmentSlot.HEAD)
                        .setSwappable(true)
                        .setDamageOnHurt(false)
                        .setEquipSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(equipSound))
                        .build()
        ));

        this.caseCollection = caseCollection;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, consumer, flag);

        if (caseCollection != null && !caseCollection.isEmpty()) {
            consumer.accept(Component.literal(caseCollection + " Collection")
                    .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        }
    }
}