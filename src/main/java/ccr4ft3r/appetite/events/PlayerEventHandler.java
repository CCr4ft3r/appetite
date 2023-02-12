package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.capabilities.AppetiteCapabilityProvider;
import ccr4ft3r.appetite.data.capabilities.HungerLevelingProvider;
import ccr4ft3r.appetite.registry.ModMobEffects;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import tschipp.carryon.common.item.ItemCarryonBlock;
import tschipp.carryon.common.item.ItemCarryonEntity;

import static ccr4ft3r.appetite.data.capabilities.AppetiteCapabilityProvider.*;
import static ccr4ft3r.appetite.data.capabilities.HungerLevelingProvider.*;
import static ccr4ft3r.appetite.util.PlayerUtil.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer().level.isClientSide())
            return;
        ServerData.forgetAbout(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide())
            return;
        ServerData.addMe(player);
        player.getCapability(HungerLevelingProvider.HUNGER_LEVELING_CAP).ifPresent(cap -> cap.updateFoodMax((ServerPlayerEntity) player, 0));
        if (ModList.get().isLoaded(ModConstants.CARRY_ON_MOD_ID)) {
            ServerData.getPlayerData(player).setCarrying(
                ItemCarryonBlock.getBlock(player.getMainHandItem()) != Blocks.AIR
                    || ItemCarryonEntity.hasEntityData(player.getMainHandItem())
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        event.getPlayer().getCapability(HungerLevelingProvider.HUNGER_LEVELING_CAP).ifPresent(cap -> cap.updateFoodMax((ServerPlayerEntity) event.getPlayer(), 0));
    }

    @SubscribeEvent
    public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!isPlayerServerside(event.getObject()))
            return;
        PlayerEntity player = (PlayerEntity) event.getObject();
        if (!player.getCapability(FROZEN_APPETITE_CAP).isPresent()) {
            event.addCapability(new ResourceLocation(ModConstants.MOD_ID, "appetite"), new AppetiteCapabilityProvider());
        }
        if (!player.getCapability(HUNGER_LEVELING_CAP).isPresent())
            event.addCapability(new ResourceLocation(ModConstants.MOD_ID, "hunger_leveling"), new HungerLevelingProvider());
    }

    @SubscribeEvent
    public static void onFrozenAppetiteAdded(PotionEvent.PotionAddedEvent event) {
        if (!isPlayerServerside(event.getEntity()) || event.getPotionEffect().getEffect() != ModMobEffects.FROZEN_APPETITE.get())
            return;
        PlayerEntity player = (PlayerEntity) event.getEntity();
        ServerData.getPlayerData(player).setFoodData(player.getFoodData());
        player.getCapability(FROZEN_APPETITE_CAP, null).ifPresent(c -> {
            if (c.canUse(player.level))
                c.effectUsed(player.level);
            else
                c.setShouldEffectBeRemoved(true);
        });
    }

    @SubscribeEvent
    public static void onLevelingUp(PlayerXpEvent.LevelChange event) {
        if (event.getPlayer().level.isClientSide())
            return;
        event.getPlayer().getCapability(HUNGER_LEVELING_CAP).ifPresent((cap) -> cap.updateFoodMax((ServerPlayerEntity) event.getPlayer(), event.getLevels()));
    }

    @SubscribeEvent
    public static void onBrainFreezed(PotionEvent.PotionAddedEvent event) {
        if (!isPlayerServerside(event.getEntity()) || event.getPotionEffect().getEffect() != ModMobEffects.BRAIN_FREEZE.get())
            return;

        PlayerEntity player = (PlayerEntity) event.getEntity();
        boolean isFrozenAppetiteAlreadyAdded = player.hasEffect(ModMobEffects.FROZEN_APPETITE.get());
        boolean isFrozenAppetiteAllowed = MainConfig.CONFIG_DATA.allowFrozenAppetite.get();

        if (isFrozenAppetiteAlreadyAdded)
            player.displayClientMessage(new TranslationTextComponent("effect.appetite.brain_freeze.message2"), true);
        else if (isFrozenAppetiteAllowed)
            player.displayClientMessage(new TranslationTextComponent("effect.appetite.brain_freeze.message1",
                new StringTextComponent(MainConfig.CONFIG_DATA.frozenAppetitePerDay.get() + "")), true);
    }
}