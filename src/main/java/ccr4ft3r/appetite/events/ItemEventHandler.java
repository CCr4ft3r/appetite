package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.items.FrozenFoodItem;
import ccr4ft3r.appetite.util.BiomeUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import static ccr4ft3r.appetite.registry.ModItems.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public class ItemEventHandler {

    @SubscribeEvent
    public static void onItemExpire(ItemExpireEvent event) {
        if (!MainConfig.CONFIG_DATA.enableFishFreezing.get())
            return;
        World level = event.getEntity().level;
        if (event.isCanceled() || level.isClientSide()) return;
        final ItemEntity itemEntity = event.getEntityItem();
        final Item item = itemEntity.getItem().getItem();
        boolean isCold = BiomeUtil.isCold(itemEntity);
        RegistryObject<FrozenFoodItem> frozenFood = FROZEN_FOOD_PER_FOOD_ITEM.get(item);
        if (frozenFood != null && isCold) {
            replaceExpiredItem(event, itemEntity, frozenFood.get());
            level.playSound(null, itemEntity.blockPosition(), SoundEvents.TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.5f, 1f);
        } else if (item instanceof FrozenFoodItem && !isCold) {
            FrozenFoodItem frozenFoodItem = (FrozenFoodItem) item;
            replaceExpiredItem(event, itemEntity, frozenFoodItem.getUnfrozenItem());
            level.playSound(null, itemEntity.blockPosition(), SoundEvents.TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.5f, 1f);
        }
    }

    @SubscribeEvent
    public static void onItemDropped(EntityJoinWorldEvent event) {
        if (!MainConfig.CONFIG_DATA.enableFishFreezing.get())
            return;
        if (event.isCanceled() || event.getWorld().isClientSide()) return;
        if (!(event.getEntity() instanceof ItemEntity)) return;
        ItemEntity itemEntity = (ItemEntity) event.getEntity();
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