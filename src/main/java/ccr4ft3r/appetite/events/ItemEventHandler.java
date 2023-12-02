package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.items.FrozenFoodItem;
import ccr4ft3r.appetite.registry.ModTags;
import ccr4ft3r.appetite.util.BiomeUtil;
import ccr4ft3r.appetite.util.PlayerUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static ccr4ft3r.appetite.registry.ModItems.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public class ItemEventHandler {

    @SubscribeEvent
    public static void onItemExpire(ItemExpireEvent event) {
        if (!MainConfig.CONFIG_DATA.enableFishFreezing.get())
            return;
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
        if (!MainConfig.CONFIG_DATA.enableFishFreezing.get())
            return;
        if (event.isCanceled() || event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof final ItemEntity itemEntity)) return;
        Item item = itemEntity.getItem().getItem();
        boolean isCold = BiomeUtil.isCold(itemEntity);
        if (FROZEN_FOOD_PER_FOOD_ITEM.get(item) != null && isCold || item instanceof FrozenFoodItem && !isCold)
            itemEntity.lifespan = MainConfig.CONFIG_DATA.ticksToFreezeOrMelt.get();
    }

    @SubscribeEvent
    public static void onCompletedUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;
        if (!event.getItem().is(ModTags.IS_FROZEN_FOOD))
            return;
        PlayerUtil.initiateFrozenFoodEffect(player);
    }

    private static void replaceExpiredItem(ItemExpireEvent event, ItemEntity itemEntity, Item replaceWith) {
        itemEntity.setItem(new ItemStack(replaceWith, itemEntity.getItem().getCount()));
        event.setExtraLife(6000);
        event.setCanceled(true);
    }
}