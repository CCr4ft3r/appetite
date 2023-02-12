package ccr4ft3r.appetite.items;

import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.data.capabilities.AppetiteCapabilityProvider;
import ccr4ft3r.appetite.registry.ModMobEffects;
import ccr4ft3r.appetite.registry.ModTabs;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class FrozenFoodItem extends Item {

    private final Item unfrozenItem;

    public FrozenFoodItem(Item unfrozenItem) {
        super(new Item.Properties().tab(ModTabs.MOD_TAB).food(new Food.Builder().alwaysEat().build()));
        this.unfrozenItem = unfrozenItem;
    }

    @Override
    @ParametersAreNonnullByDefault
    public ItemStack finishUsingItem(ItemStack itemStack, World level, LivingEntity entity) {
        if (!(entity instanceof PlayerEntity ) || level.isClientSide())
            return super.finishUsingItem(itemStack, level, entity);

        PlayerEntity player = (PlayerEntity) entity;
         boolean isFrozenAppetiteAllowed = MainConfig.CONFIG_DATA.allowFrozenAppetite.get();
        boolean isFrozenAppetiteAlreadyAdded = player.hasEffect(ModMobEffects.FROZEN_APPETITE.get());
        boolean canFrozenAppetiteBeUsed = player.getCapability(AppetiteCapabilityProvider.FROZEN_APPETITE_CAP).map(c -> c.canUse(level)).orElse(false);
        boolean shouldBeBrainfreezed = isFrozenAppetiteAlreadyAdded || !canFrozenAppetiteBeUsed;

        if (shouldBeBrainfreezed) {
            player.addEffect(new EffectInstance(ModMobEffects.BRAIN_FREEZE.get(), 160));
        } else if (isFrozenAppetiteAllowed) {
            player.addEffect(new EffectInstance(ModMobEffects.FROZEN_APPETITE.get(), 1200));
        }

        return super.finishUsingItem(itemStack, level, entity);
    }

    public Item getUnfrozenItem() {
        return unfrozenItem;
    }
}