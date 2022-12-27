package ccr4ft3r.appetite.mixins;

import ccr4ft3r.appetite.events.ClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static ccr4ft3r.appetite.ModConstants.*;

@Mixin(targets = {"squeek.appleskin.client.HUDOverlayHandler"})
public class HudOverlayHandlerMixin {
    @ModifyConstant(method = "drawHungerOverlay(IILnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;IIFZ)V", constant = @Constant(intValue = VANILLA_MAX_FOOD_LEVEL), remap = false)
    private static int getMaxFoodLevelForHungerBar(int i) {
        return ClientHandler.PLAYER_DATA.getHungerbarMaximum() * 2;
    }

    @ModifyConstant(method = "drawSaturationOverlay(FFLnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", constant = @Constant(floatValue = VANILLA_MAX_FOOD_LEVEL), remap = false)
    private static float getMaxFoodLevelForSaturationBar(float constant) {
        return ClientHandler.PLAYER_DATA.getHungerbarMaximum() * 2;
    }

    @ModifyConstant(method = "shouldShowEstimatedHealth", constant = @Constant(intValue = VANILLA_FOOD_HEAL_LEVEL), remap = false)
    private static int getFoodHealLevel(int constant) {
        return ClientHandler.PLAYER_DATA.getHungerbarMaximum() * 2 - 2;
    }
}