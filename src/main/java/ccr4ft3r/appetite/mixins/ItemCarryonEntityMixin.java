package ccr4ft3r.appetite.mixins;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static ccr4ft3r.appetite.data.ServerData.*;

@Mixin(targets = "tschipp.carryon.common.item.ItemCarryonEntity")
public class ItemCarryonEntityMixin {

    @Inject(method = "useOn", at = @At("RETURN"))
    public void useOnInjected(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getPlayer() != null && context.getPlayer().getLevel().isClientSide())
            return;
        if (cir.getReturnValue() != InteractionResult.FAIL)
            getPlayerData(context.getPlayer()).setCarrying(false);
    }
}