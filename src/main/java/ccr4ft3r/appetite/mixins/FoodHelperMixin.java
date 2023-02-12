package ccr4ft3r.appetite.mixins;

import ccr4ft3r.appetite.events.ClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static ccr4ft3r.appetite.ModConstants.*;

@Mixin(targets = {"squeek.appleskin.helpers.FoodHelper"})
public class FoodHelperMixin {

    private static int getMaxFoodLevel() {
        return ClientHandler.PLAYER_DATA.getHungerbarMaximum() * 2;
    }

    private static int getFoodHealLevel() {
        return getMaxFoodLevel() - 2;
    }

    @ModifyConstant(method = "getEstimatedHealthIncrement(Lnet/minecraft/item/ItemStack;Lsqueek/appleskin/api/food/FoodValues;Lnet/minecraft/entity/player/PlayerEntity;)F", constant = @Constant(intValue = VANILLA_MAX_FOOD_LEVEL), remap = false)
    private static int getMaxFoodLevelForEstimatedHealthIncrement(int i) {
        return getMaxFoodLevel();
    }

    @ModifyConstant(method = "getEstimatedHealthIncrement(IFF)F", constant = @Constant(intValue = VANILLA_FOOD_HEAL_LEVEL), remap = false)
    private static int getFoodHealLevelForEstimatedHealthIncrement(int i) {
        return getFoodHealLevel();
    }

    @ModifyConstant(method = "getEstimatedHealthIncrement(Lnet/minecraft/item/ItemStack;Lsqueek/appleskin/api/food/FoodValues;Lnet/minecraft/entity/player/PlayerEntity;)F", constant = @Constant(floatValue = VANILLA_FOOD_HEAL_LEVEL), remap = false)
    private static float getFoodHealLevelForEstimatedHealthIncrement(float i) {
        return getFoodHealLevel();
    }

    @ModifyConstant(method = "getEstimatedHealthIncrement(IFF)F", constant = @Constant(intValue = VANILLA_MAX_FOOD_LEVEL), remap = false)
    private static int getFoodHealLevelForEstimatedHealthIncrement2(int i) {
        return getMaxFoodLevel();
    }

}