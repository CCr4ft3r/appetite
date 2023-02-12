package ccr4ft3r.appetite.mixins;

import ccr4ft3r.appetite.util.PlayerUtil;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
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
    protected void onUse(BlockState p_52769_, World p_52770_, BlockPos p_52771_, PlayerEntity player, Hand p_52773_, BlockRayTraceResult p_52774_, CallbackInfoReturnable<ActionResultType> cir) {
        if (cir.isCancelled() || !cir.getReturnValue().consumesAction())
            return;
        PlayerUtil.exhaust(player, getProfile().enableOpenClosing, true, getProfile().afterOpenClosing, 1, 0);
    }
}