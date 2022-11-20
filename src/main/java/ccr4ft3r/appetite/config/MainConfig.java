package ccr4ft3r.appetite.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MainConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final Data CONFIG_DATA = new Data(BUILDER);
    public static final ForgeConfigSpec CONFIG = BUILDER.build();

    public static class Data {

        public ForgeConfigSpec.EnumValue<AppetiteProfile> profileToUse;
        public ForgeConfigSpec.BooleanValue enabled;
        public ForgeConfigSpec.BooleanValue enableExtendedLogging;

        public Data(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            profileToUse = builder.comment("Sets which profile Appetite should use to tweak the hunger system (profile files are located in config/appetite). " +
                    "\nSet this to CUSTOM and edit the custom-profile.toml to adapt each aspect to your preferences.")
                .defineEnum("profileToUse", AppetiteProfile.HUNGRY);
            enabled = builder.comment("Enables appetite to cause exhaustion defined by the choosed profile. Can be used to disable Appetite temporary without uninstalling it.")
                .define("enabled", true);
            enableExtendedLogging = builder.comment("Enables extended mod logging - only used for trouble shooting")
                .define("enableExtendedLogging", false);
            builder.pop();
        }
    }
}