package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.items.FrozenFoodItem;
import ccr4ft3r.appetite.util.BiomeUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static ccr4ft3r.appetite.registry.ModItems.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public class ItemEventHandler {

    @SubscribeEvent
    public static void onItemExpire(ItemExpireEvent event) {
        Level level = event.getEntity().getLevel();
        if (event.isCanceled() || level.isClientSide()) return;
        final ItemEntity itemEntity = event.getEntity();
        final Item item = itemEntity.getItem().getItem();
        boolean isCold = BiomeUtil.isCold(itemEntity);
        RegistryObject<FrozenFoodItem> frozenFood = FROZEN_FOOD_PER_FOOD_ITEM.get(item);
        if (frozenFood != null && isCold) {
            replaceExpiredItem(event, itemEntity, frozenFood.get());
            level.playSound(null, itemEntity.blockPosition(), SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.5f, 1f);
        } else if (item instanceof FrozenFoodItem frozenFoodItem && !isCold) {
            replaceExpiredItem(event, itemEntity, frozenFoodItem.getUnfrozenItem());
            level.playSound(null, itemEntity.blockPosition(), SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.5f, 1f);
        }
    }

    @SubscribeEvent
    public static void onItemDropped(EntityJoinLevelEvent event) {
        if (event.isCanceled() || event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof final ItemEntity itemEntity)) return;
        Item item = itemEntity.getItem().getItem();
        boolean isCold = BiomeUtil.isCold(itemEntity);
        if (FROZEN_FOOD_PER_FOOD_ITEM.get(item) != null && isCold || item instanceof FrozenFoodItem && !isCold)
            itemEntity.lifespan = MainConfig.CONFIG_DATA.ticksToFreezeOrMelt.get();
    }

    private static void replaceExpiredItem(ItemExpireEvent event, ItemEntity itemEntity, Item replaceWith) {
        itemEntity.setItem(new ItemStack(replaceWith, itemEntity.getItem().getCount()));
        event.setExtraLife(6000);
        event.setCanceled(true);
    }
}