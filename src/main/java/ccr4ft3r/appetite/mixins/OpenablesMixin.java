package ccr4ft3r.appetite.mixins;

import ccr4ft3r.appetite.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static ccr4ft3r.appetite.config.ProfileConfig.*;

@Mixin({DoorBlock.class, FenceGateBlock.class, TrapDoorBlock.class, ChestBlock.class})
public class OpenablesMixin extends Block {

    public OpenablesMixin(Properties p_52737_) {
        super(p_52737_);
    }

    @Inject(method = "use", at = @At("RETURN"))
    protected void onUse(BlockState p_52769_, Level p_52770_, BlockPos p_52771_, Player player, InteractionHand p_52773_, BlockHitResult p_52774_, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.isCancelled() || !cir.getReturnValue().consumesAction())
            return;
        PlayerUtil.exhaust(player, getProfile().enableOpenClosing, true, getProfile().afterOpenClosing, 1, 0);
    }
}