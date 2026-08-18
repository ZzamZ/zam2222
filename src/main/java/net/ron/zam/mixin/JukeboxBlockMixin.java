package net.ron.zam.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.ron.zam.common.item.RecordSleeveItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxBlock.class)
public class JukeboxBlockMixin {

    @Inject(method = "useItemOn", at = @At("RETURN"), cancellable = true)
    private void zam$insertRecordSleeve(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue() != InteractionResult.TRY_WITH_EMPTY_HAND) {
            return;
        }

        if (!(stack.getItem() instanceof RecordSleeveItem)) {
            return;
        }

        if (state.is(Blocks.JUKEBOX)
                && state.getValue(JukeboxBlock.HAS_RECORD)) {
            return;
        }

        cir.setReturnValue(InteractionResult.SUCCESS);

        if (level.isClientSide()) {
            return;
        }

        ItemStack insertedSleeve = stack.consumeAndReturn(1, player);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof JukeboxBlockEntity jukebox) {
            jukebox.setTheItem(insertedSleeve);

            level.gameEvent(
                    GameEvent.BLOCK_CHANGE,
                    pos,
                    GameEvent.Context.of(player, state)
            );
        }

        player.awardStat(Stats.PLAY_RECORD);
    }
}