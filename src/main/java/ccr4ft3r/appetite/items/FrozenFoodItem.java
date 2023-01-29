package ccr4ft3r.appetite.items;

import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.data.capabilities.AppetiteCapabilityProvider;
import ccr4ft3r.appetite.registry.ModMobEffects;
import ccr4ft3r.appetite.registry.ModTabs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FrozenFoodItem extends Item {

    private final Item unfrozenItem;

    public FrozenFoodItem(Item unfrozenItem) {
        super(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().build()));
        this.unfrozenItem = unfrozenItem;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (!(entity instanceof Player player) || level.isClientSide())
            return super.finishUsingItem(itemStack, level, entity);

        boolean isFrozenAppetiteAllowed = MainConfig.CONFIG_DATA.allowFrozenAppetite.get();
        boolean isFrozenAppetiteAlreadyAdded = player.hasEffect(ModMobEffects.FROZEN_APPETITE.get());
        boolean canFrozenAppetiteBeUsed = player.getCapability(AppetiteCapabilityProvider.FROZEN_APPETITE_CAP).map(c -> c.canUse(level)).orElse(false);
        boolean shouldBeBrainfreezed = isFrozenAppetiteAlreadyAdded || !canFrozenAppetiteBeUsed;

        if (shouldBeBrainfreezed) {
            player.addEffect(new MobEffectInstance(ModMobEffects.BRAIN_FREEZE.get(), 160));
        } else if (isFrozenAppetiteAllowed) {
            player.addEffect(new MobEffectInstance(ModMobEffects.FROZEN_APPETITE.get(), 1200));
        }

        return super.finishUsingItem(itemStack, level, entity);
    }

    public Item getUnfrozenItem() {
        return unfrozenItem;
    }
}