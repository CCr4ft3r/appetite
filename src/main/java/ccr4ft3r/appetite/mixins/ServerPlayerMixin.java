package ccr4ft3r.appetite.mixins;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static ccr4ft3r.appetite.config.ProfileConfig.*;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "restoreFrom", at = @At("HEAD"))
    private void restoreFoodStats(ServerPlayer p_9016_, boolean p_9017_, CallbackInfo ci) {
        if (getProfile().enableFoodRestore.get()) {
            ((PlayerAccessor) this).setFoodData(p_9016_.getFoodData());
            ((PlayerAccessor) this).getFoodData().setFoodLevel(getProfile().minFoodLevelAfterRestore.get());
        }
    }
}