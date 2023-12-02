package ccr4ft3r.appetite.mixins;

import ccr4ft3r.appetite.config.MainConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {

    @Inject(method = "award", at = @At(value = "HEAD"), cancellable = true)
    private void cancelAwarding(Advancement advancement, String p_135990_, CallbackInfoReturnable<Boolean> cir) {
        if (advancement.getId().toString().equals("appetite:drop_fish") && !MainConfig.CONFIG_DATA.allowFrozenAppetite.get())
            cir.setReturnValue(false);
    }
}