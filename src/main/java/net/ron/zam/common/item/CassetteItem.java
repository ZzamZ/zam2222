package net.ron.zam.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.TooltipDisplay;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.data.CassetteData;
import net.ron.zam.registry.ZAMComponents;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CassetteItem extends Item {

    public CassetteItem(Properties properties) {
        super(properties);
    }

    @Nullable
    public static Identifier getCassetteId(ItemStack stack) {
        return stack.get(ZAMComponents.CASSETTE_ID);
    }

    public static void assign(ItemStack stack, @Nullable Identifier tapeId) {
        if (tapeId == null) {
            stack.remove(ZAMComponents.CASSETTE_ID);
            stack.remove(DataComponents.DYED_COLOR);
            return;
        }

        stack.set(ZAMComponents.CASSETTE_ID, tapeId);

        CassetteData data = ZAMMod.CASSETTES.get(tapeId);

        if (data != null) {
            stack.set(
                    DataComponents.DYED_COLOR,
                    new DyedItemColor(data.color())
            );
        }
    }

    public static boolean isBlank(ItemStack stack) {
        return getCassetteId(stack) == null;
    }

    public static String translationKey(Identifier id) {
        return "cassette_tape."
                + id.getNamespace()
                + "."
                + id.getPath();
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext ctx,
            TooltipDisplay display,
            Consumer<Component> tooltip,
            TooltipFlag flag
    ) {
        Identifier id = getCassetteId(stack);

        if (id != null) {
            tooltip.accept(
                    Component.translatable(
                            translationKey(id)
                    ).withStyle(
                            net.minecraft.ChatFormatting.GRAY
                    )
            );

            if (flag.isAdvanced()) {
                tooltip.accept(
                        Component.literal(
                                id.toString()
                        ).withStyle(
                                net.minecraft.ChatFormatting.DARK_GRAY
                        )
                );
            }
        }

        super.appendHoverText(
                stack,
                ctx,
                display,
                tooltip,
                flag
        );
    }
}