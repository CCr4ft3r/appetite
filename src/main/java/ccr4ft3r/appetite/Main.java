package ccr4ft3r.appetite;

import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.config.ProfileConfig;
import ccr4ft3r.appetite.network.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModConstants.MOD_ID)
public class Main {

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigLoading);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigReloading);
        registerConfigs();
        PacketHandler.registerMessages();
    }

    private static void registerConfigs() {
        ModLoadingContext.get().registerConfig(Type.COMMON, MainConfig.CONFIG, ModConstants.MOD_ID + "-common.toml");
        ModLoadingContext.get().registerConfig(Type.COMMON, ProfileConfig.CONFIG_PECKISH, ModConstants.MOD_ID + "/peckish-profile.toml");
        ModLoadingContext.get().registerConfig(Type.COMMON, ProfileConfig.CONFIG_HUNGRY, ModConstants.MOD_ID + "/hungry-profile.toml");
        ModLoadingContext.get().registerConfig(Type.COMMON, ProfileConfig.CONFIG_STARVING, ModConstants.MOD_ID + "/starving-profile.toml");
        ModLoadingContext.get().registerConfig(Type.COMMON, ProfileConfig.CONFIG_CUSTOM, ModConstants.MOD_ID + "/custom-profile.toml");
    }

    @SubscribeEvent
    public void onConfigLoading(ModConfigEvent.Loading configEvent) {
        LogUtils.getLogger().info("Loaded config {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public void onConfigReloading(ModConfigEvent.Reloading configEvent) {
        LogUtils.getLogger().info("Reloaded config {}", configEvent.getConfig().getFileName());
        ProfileConfig.updateChoosedProfile();
    }
}