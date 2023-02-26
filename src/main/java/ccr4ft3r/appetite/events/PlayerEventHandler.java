package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.capabilities.AppetiteCapabilityProvider;
import ccr4ft3r.appetite.data.capabilities.FrozenAppetiteCapability;
import ccr4ft3r.appetite.data.capabilities.HungerLevelingCapability;
import ccr4ft3r.appetite.data.capabilities.HungerLevelingProvider;
import ccr4ft3r.appetite.registry.ModMobEffects;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import tschipp.carryon.common.item.ItemCarryonBlock;
import tschipp.carryon.common.item.ItemCarryonEntity;

import static ccr4ft3r.appetite.data.capabilities.HungerLevelingProvider.*;
import static ccr4ft3r.appetite.util.CompatUtil.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer().getLevel().isClientSide())
            return;
        ServerData.forgetAbout(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        if (player.getLevel().isClientSide())
            return;
        ServerData.addMe(player);
        player.getCapability(HungerLevelingProvider.HUNGER_LEVELING_CAP).ifPresent(cap -> cap.updateFoodMax((ServerPlayer) player));
        if (ModList.get().isLoaded(ModConstants.CARRY_ON_MOD_ID) && existsClass("tschipp.carryon.common.carry.CarryOnDataManager")
        && existsClass("tschipp.carryon.common.item.ItemCarryonBlock") && existsClass("tschipp.carryon.common.item.ItemCarryonEntity")) {
            ServerData.getPlayerData(player).setCarrying(
                ItemCarryonBlock.getBlock(player.getMainHandItem()) != Blocks.AIR
                    || ItemCarryonEntity.hasEntityData(player.getMainHandItem())
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        event.getPlayer().getCapability(HungerLevelingProvider.HUNGER_LEVELING_CAP).ifPresent(cap -> cap.updateFoodMax((ServerPlayer) event.getPlayer()));
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(FrozenAppetiteCapability.class);
        event.register(HungerLevelingCapability.class);
    }

    @SubscribeEvent
    public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player player) || player.getLevel().isClientSide())
            return;
        if (!player.getCapability(AppetiteCapabilityProvider.FROZEN_APPETITE_CAP).isPresent())
            event.addCapability(new ResourceLocation(ModConstants.MOD_ID, "appetite"), new AppetiteCapabilityProvider());
        if (!player.getCapability(HUNGER_LEVELING_CAP).isPresent())
            event.addCapability(new ResourceLocation(ModConstants.MOD_ID, "hunger_leveling"), new HungerLevelingProvider());
    }

    @SubscribeEvent
    public static void onFrozenAppetiteAdded(PotionEvent.PotionAddedEvent event) {
        if (!(event.getEntity() instanceof Player player) || player.getLevel().isClientSide() ||
            event.getPotionEffect().getEffect() != ModMobEffects.FROZEN_APPETITE.get())
            return;
        ServerData.getPlayerData(player).setFoodData(player.getFoodData());
        player.getCapability(AppetiteCapabilityProvider.FROZEN_APPETITE_CAP, null).ifPresent(c -> {
            if (c.canUse(player.getLevel()))
                c.effectUsed(player.getLevel());
            else
                c.setShouldEffectBeRemoved(true);
        });
    }

    @SubscribeEvent
    public static void onLevelingUp(PlayerXpEvent.PickupXp event) {
        if (event.getPlayer().getLevel().isClientSide())
            return;
        event.getPlayer().getCapability(HUNGER_LEVELING_CAP).ifPresent((cap) -> cap.updateFoodMax((ServerPlayer) event.getPlayer()));
    }

    @SubscribeEvent
    public static void onBrainFreezed(PotionEvent.PotionAddedEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.getPotionEffect().getEffect() != ModMobEffects.BRAIN_FREEZE.get()
            || player.getLevel().isClientSide())
            return;

        boolean isFrozenAppetiteAlreadyAdded = player.hasEffect(ModMobEffects.FROZEN_APPETITE.get());
        boolean isFrozenAppetiteAllowed = MainConfig.CONFIG_DATA.allowFrozenAppetite.get();

        if (isFrozenAppetiteAlreadyAdded)
            player.displayClientMessage(new TranslatableComponent("effect.appetite.brain_freeze.message2"), true);
        else if (isFrozenAppetiteAllowed)
            player.displayClientMessage(new TranslatableComponent("effect.appetite.brain_freeze.message1",
                new TextComponent(MainConfig.CONFIG_DATA.frozenAppetitePerDay.get() + "")), true);
    }
}