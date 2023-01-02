package ccr4ft3r.appetite.mixins;

import ccr4ft3r.appetite.IFoodData;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static ccr4ft3r.appetite.ModConstants.*;
import static ccr4ft3r.appetite.config.ProfileConfig.*;

@Mixin(FoodData.class)
public class FoodDataMixin implements IFoodData {
    private int foodbarMax;

    @Shadow
    private int foodLevel;

    @Shadow
    private int lastFoodLevel;

    private Integer getInitalFoodLevel() {
        return getProfile().initialHungerbarMaximum.get() * 2;
    }

    private Integer getCurrentMaxFoodLevel() {
        return foodbarMax * 2;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectedInit(CallbackInfo ci) {
        foodLevel = getInitalFoodLevel();
        lastFoodLevel = getInitalFoodLevel();
    }

    @ModifyConstant(method = "needsFood", constant = @Constant(intValue = VANILLA_MAX_FOOD_LEVEL))
    public int needsFoodMaxFoodLevel(int i) {
        return getCurrentMaxFoodLevel();
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = VANILLA_MAX_FOOD_LEVEL))
    public int tickMaxFoodLelvel(int i) {
        return getCurrentMaxFoodLevel();
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = VANILLA_FOOD_HEAL_LEVEL))
    public int tickHealFoodLelvel(int i) {
        return getCurrentMaxFoodLevel() - 2;
    }

    @Redirect(method = "eat(IF)V", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    public int getNewFoodLevel(int a, int b) {
        return Math.min(a, getCurrentMaxFoodLevel());
    }

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = VANILLA_MAX_EXHAUSTION))
    public float getMaxExhaustion(float constant) {
        return VANILLA_MAX_EXHAUSTION * ((float) getCurrentMaxFoodLevel() / VANILLA_MAX_FOOD_LEVEL);
    }

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = VANILLA_MAX_EXHAUSTION, ordinal = 1))
    public float getMaxExhaustionDelta(float constant) {
        return getMaxExhaustion(constant);
    }

    @Override
    public void setFoodbarMax(int foodbarMax) {
        this.foodbarMax = foodbarMax;
    }

    @Override
    public int getFoodbarMax() {
        return foodbarMax;
    }
}