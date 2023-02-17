package ccr4ft3r.appetite.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.*;

public class MainConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final Data CONFIG_DATA = new Data(BUILDER);
    public static final ForgeConfigSpec CONFIG = BUILDER.build();

    public static class Data {

        public ForgeConfigSpec.EnumValue<AppetiteProfile> profileToUse;
        public ForgeConfigSpec.BooleanValue enablesRules;
        public ForgeConfigSpec.BooleanValue enableExtendedLogging;
        public ForgeConfigSpec.BooleanValue allowFrozenAppetite;
        public ForgeConfigSpec.ConfigValue<List<String>> dimensionBlacklist;
        public ForgeConfigSpec.IntValue ticksToFreezeOrMelt;
        public ForgeConfigSpec.IntValue frozenAppetitePerDay;
        public ForgeConfigSpec.BooleanValue enableFishFreezing;

        public Data(ForgeConfigSpec.Builder builder) {
            builder.push("1 - General");
            profileToUse = builder.comment("Sets which profile Appetite should use to tweak the hunger system (profile files are located in config/appetite). " +
                    "\nSet this to CUSTOM and edit the custom-profile.toml to adapt each aspect to your preferences.")
                .defineEnum("profileToUse", AppetiteProfile.HUNGRY);
            enablesRules = builder.comment("Enables appetite to cause exhaustion defined by the choosed profile. Can be used to disable Appetite temporary without uninstalling it.")
                .define("enablesRules", true);
            dimensionBlacklist = builder.comment("Sets the dimensions (ids) in which Appetite rules should be disabled, for example: [\"minecraft:the_nether\", \"minecraft:the_end\"])")
                .define("dimensionBlacklist", new ArrayList<>(), (s) -> s instanceof List && ((List<?>) s).stream().allMatch(d -> d != null && d.toString().matches("^.+:.+$")));
            enableExtendedLogging = builder.comment("Enables extended mod logging - only used for trouble shooting")
                .define("enableExtendedLogging", false);
            builder.pop();

            builder.push("2 - Frozen Appetite");
            allowFrozenAppetite = builder.comment("Specifies whether the 'Frozen Appetite' effect can be caused by events. This effect freezes the player's hunger for the intended amount of time",
                    "Thereby the hunger and exhaustion value will not drop at all for the whole time of this effect.",
                    "If this option is disabled the homonymous potion and froozen food will only cause a negative effect after the amount of frozenAppetitePerDay.")
                .define("enableFrozenAppetite", true);
            frozenAppetitePerDay = builder.comment("Sets the amount of frozen food items and potions of 'Frozen Appetite' a player can consume per day before they cause a negative effect instead of the wanted one.")
                .defineInRange("frozenAppetitePerDay", 3, 1, 10);
            enableFishFreezing = builder.comment("Determines whether dropped fish should freeze in cold biomes and melt in warm biomes.")
                .define("enableFishFreezing", true);
            ticksToFreezeOrMelt = builder.comment("Specifies the amount of ticks a dropped fish item will freeze or a dropped frozen food item will melt and will transform into the other one.")
                .defineInRange("ticksToFreezeOrMelt", 1200, 200, 6000);
        }
    }
}