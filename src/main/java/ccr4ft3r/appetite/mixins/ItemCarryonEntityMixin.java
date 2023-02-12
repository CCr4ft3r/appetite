package ccr4ft3r.appetite.mixins;

import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static ccr4ft3r.appetite.data.ServerData.*;

@Mixin(targets = "tschipp.carryon.common.item.ItemCarryonEntity")
public class ItemCarryonEntityMixin {

    @Inject(method = "useOn", at = @At("RETURN"))
    public void useOnInjected(ItemUseContext context, CallbackInfoReturnable<ActionResultType> cir) {
        if (context.getPlayer() != null && context.getPlayer().level.isClientSide())
            return;
        if (cir.getReturnValue() != ActionResultType.FAIL)
            getPlayerData(context.getPlayer()).setCarrying(false);
    }
}