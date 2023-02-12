package ccr4ft3r.appetite;

import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.config.ProfileConfig;
import ccr4ft3r.appetite.events.CompatibilityHandler;
import ccr4ft3r.appetite.events.ExhaustionHandler;
import ccr4ft3r.appetite.network.PacketHandler;
import ccr4ft3r.appetite.registry.ModCaps;
import ccr4ft3r.appetite.registry.ModItems;
import ccr4ft3r.appetite.registry.ModMobEffects;
import ccr4ft3r.appetite.registry.ModPotions;
import ccr4ft3r.appetite.util.LogUtils;
import fr.raksrinana.fallingtree.forge.FallingTreeBlockBreakEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static ccr4ft3r.appetite.config.ProfileConfig.*;

@Mod(ModConstants.MOD_ID)
public class Main {

    public Main() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::onConfigLoading);
        eventBus.addListener(this::onConfigReloading);
        eventBus.addListener(this::onRegisterCaps);

        registerConfigs();
        registerObjects();

        PacketHandler.registerMessages();
        addCompatibilitiesListener();
    }

    private static void registerObjects() {
        ModMobEffects.register();
        ModPotions.register();
        ModItems.register();
    }

    private static void registerConfigs() {
        ModLoadingContext.get().registerConfig(Type.COMMON, MainConfig.CONFIG, ModConstants.MOD_ID + "-common.toml");
        ModLoadingContext.get().registerConfig(Type.COMMON, ProfileConfig.CONFIG_PECKISH, ModConstants.MOD_ID + "/peckish-profile.toml");
        ModLoadingContext.get().registerConfig(Type.COMMON, ProfileConfig.CONFIG_HUNGRY, ModConstants.MOD_ID + "/hungry-profile.toml");
        ModLoadingContext.get().registerConfig(Type.COMMON, ProfileConfig.CONFIG_STARVING, ModConstants.MOD_ID + "/starving-profile.toml");
        ModLoadingContext.get().registerConfig(Type.COMMON, ProfileConfig.CONFIG_CUSTOM, ModConstants.MOD_ID + "/custom-profile.toml");
    }

    private static void addCompatibilitiesListener() {
        if (ModList.get().isLoaded(ModConstants.PARAGLIDER_MOD_ID))
            MinecraftForge.EVENT_BUS.addListener(CompatibilityHandler::onParagliding);
        if (ModList.get().isLoaded(ModConstants.GO_PRONE_MOD_ID))
            MinecraftForge.EVENT_BUS.addListener(CompatibilityHandler::onCrawling);
        if (ModList.get().isLoaded(ModConstants.GRAPPLING_HOOK_MOD_ID))
            MinecraftForge.EVENT_BUS.addListener(CompatibilityHandler::onPullingUp);
        if (ModList.get().isLoaded(ModConstants.FALLING_TREE_MOD_ID))
            ExhaustionHandler.INCLUDE_EVENT_PER_CLASS.put(FallingTreeBlockBreakEvent.class, () -> getProfile().enableChoppingTrees.get());

        if (ModList.get().isLoaded(ModConstants.CARRY_ON_MOD_ID)) {
            MinecraftForge.EVENT_BUS.addListener(CompatibilityHandler::onPickingUp);
            MinecraftForge.EVENT_BUS.addListener(CompatibilityHandler::onPickingUpEntity);
            MinecraftForge.EVENT_BUS.addListener(CompatibilityHandler::onPlacingEntity);
        }
    }

    @SubscribeEvent
    public void onConfigLoading(ModConfig.Loading configEvent) {
        LogUtils.getLogger().info("Loaded config {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public void onConfigReloading(ModConfig.Reloading configEvent) {
        LogUtils.getLogger().info("Reloaded config {}", configEvent.getConfig().getFileName());
        ProfileConfig.updateChoosedProfile();
    }

    @SubscribeEvent
    public void onRegisterCaps(FMLCommonSetupEvent event) {
        ModCaps.register();
    }
}