package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.capabilities.AppetiteCapabilityProvider;
import ccr4ft3r.appetite.data.capabilities.FrozenAppetiteCapability;
import ccr4ft3r.appetite.data.capabilities.HungerLevelingCapability;
import ccr4ft3r.appetite.data.capabilities.HungerLevelingProvider;
import ccr4ft3r.appetite.registry.ModMobEffects;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import tschipp.carryon.common.carry.CarryOnDataManager;

import static ccr4ft3r.appetite.data.capabilities.HungerLevelingProvider.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().getLevel().isClientSide())
            return;
        ServerData.forgetAbout(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.getLevel().isClientSide())
            return;
        ServerData.addMe(player);
        player.getCapability(HungerLevelingProvider.HUNGER_LEVELING_CAP).ifPresent(cap -> cap.updateFoodMax((ServerPlayer) player));
        try {
            Class.forName("tschipp.carryon.common.carry.CarryOnDataManager");
            if (ModList.get().isLoaded(ModConstants.CARRY_ON_MOD_ID)) {
                ServerData.getPlayerData(player).setCarrying(CarryOnDataManager.getCarryData(player).isCarrying());
            }
        } catch (ClassNotFoundException ignored) {
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        event.getEntity().getCapability(HungerLevelingProvider.HUNGER_LEVELING_CAP).ifPresent(cap -> cap.updateFoodMax((ServerPlayer) event.getEntity()));
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
    public static void onFrozenAppetiteAdded(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof Player player) || player.getLevel().isClientSide() ||
            event.getEffectInstance().getEffect() != ModMobEffects.FROZEN_APPETITE.get())
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
        if (event.getEntity().getLevel().isClientSide())
            return;
        event.getEntity().getCapability(HUNGER_LEVELING_CAP).ifPresent((cap) -> cap.updateFoodMax((ServerPlayer) event.getEntity()));
    }

    @SubscribeEvent
    public static void onBrainFreezed(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof Player player) || event.getEffectInstance().getEffect() != ModMobEffects.BRAIN_FREEZE.get()
            || player.getLevel().isClientSide())
            return;

        boolean isFrozenAppetiteAlreadyAdded = player.hasEffect(ModMobEffects.FROZEN_APPETITE.get());
        boolean isFrozenAppetiteAllowed = MainConfig.CONFIG_DATA.allowFrozenAppetite.get();

        if (isFrozenAppetiteAlreadyAdded)
            player.displayClientMessage(MutableComponent.create(new TranslatableContents("effect.appetite.brain_freeze.message2")), true);
        else if (isFrozenAppetiteAllowed)
            player.displayClientMessage(MutableComponent.create(new TranslatableContents("effect.appetite.brain_freeze.message1",
                MainConfig.CONFIG_DATA.frozenAppetitePerDay.get() + "")), true);
    }
}