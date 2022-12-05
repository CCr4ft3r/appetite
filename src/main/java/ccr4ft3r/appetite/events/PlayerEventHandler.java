package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.capabilities.AppetiteCapabilityProvider;
import ccr4ft3r.appetite.data.capabilities.FrozenAppetiteCapability;
import ccr4ft3r.appetite.registry.ModMobEffects;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
        if (event.getEntity().getLevel().isClientSide())
            return;
        ServerData.addMe(event.getEntity());
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(FrozenAppetiteCapability.class);
    }

    @SubscribeEvent
    public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player player) || player.getCapability(AppetiteCapabilityProvider.FROZEN_APPETITE_CAP).isPresent())
            return;
        event.addCapability(new ResourceLocation(ModConstants.MOD_ID, "appetite"), new AppetiteCapabilityProvider());
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