package ccr4ft3r.appetite.config;

import ccr4ft3r.appetite.ModConstants;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.ForgeConfigSpec;

import static java.lang.Integer.*;

public class ProfileConfig {

    private static final ForgeConfigSpec.Builder BUILDER_PECKISH = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder BUILDER_HUNGRY = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder BUILDER_STARVING = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder BUILDER_CUSTOM = new ForgeConfigSpec.Builder();

    private static final Data PROFILE_PECKISH = new Data(BUILDER_PECKISH, AppetiteProfile.PECKISH);
    private static final Data PROFILE_HUNGRY = new Data(BUILDER_HUNGRY, AppetiteProfile.HUNGRY);
    private static final Data PROFILE_STARVING = new Data(BUILDER_STARVING, AppetiteProfile.STARVING);
    private static final Data PROFILE_CUSTOM = new Data(BUILDER_CUSTOM, AppetiteProfile.CUSTOM);

    public static final ForgeConfigSpec CONFIG_PECKISH = BUILDER_PECKISH.build();
    public static final ForgeConfigSpec CONFIG_HUNGRY = BUILDER_HUNGRY.build();
    public static final ForgeConfigSpec CONFIG_STARVING = BUILDER_STARVING.build();
    public static final ForgeConfigSpec CONFIG_CUSTOM = BUILDER_CUSTOM.build();

    private static Data CURRENT_PROFILE;

    public static Data getProfile() {
        if (CURRENT_PROFILE == null)
            updateChoosedProfile();
        return CURRENT_PROFILE;
    }

    public static void updateChoosedProfile() {
        AppetiteProfile profile = MainConfig.CONFIG_DATA.profileToUse.get();
        switch (profile) {
            case CUSTOM -> CURRENT_PROFILE = PROFILE_CUSTOM;
            case PECKISH -> CURRENT_PROFILE = PROFILE_PECKISH;
            case HUNGRY -> CURRENT_PROFILE = PROFILE_HUNGRY;
            case STARVING -> CURRENT_PROFILE = PROFILE_STARVING;
        }
        LogUtils.getLogger().info("Exhaustion profile {} will be used for adding exhaustion.", profile);
    }

    public static class Data {

        public static final String ENABLE_WHILE = "Enable exhaustion while ";
        public static final String ENABLE_AT = "Enable exhaustion for ";
        public static final String AFTER_ACTION = "Decrease saturation or food bar value by 1 after ";
        public static final String AFTER_TIME = "Decrease saturation or food bar value by 1 after %s for X ticks";

        public ForgeConfigSpec.BooleanValue enableForShovelMineables;
        public ForgeConfigSpec.BooleanValue enableForAxeMineables;
        public ForgeConfigSpec.BooleanValue enableForPickaxeMineables;
        public ForgeConfigSpec.BooleanValue enableForTillingDirt;
        public ForgeConfigSpec.BooleanValue enableForPathingDirt;
        public ForgeConfigSpec.BooleanValue enableForStrippingLogs;
        public ForgeConfigSpec.BooleanValue enableForFishing;
        public ForgeConfigSpec.BooleanValue enableForAttacking;
        public ForgeConfigSpec.BooleanValue enableForBlocking;
        public ForgeConfigSpec.BooleanValue enableForPlacingBlocks;
        public ForgeConfigSpec.BooleanValue enableForTakingDamage;
        public ForgeConfigSpec.BooleanValue enableForShootingArrows;

        public ForgeConfigSpec.IntValue afterBreakingShovelMineables;
        public ForgeConfigSpec.IntValue afterBreakingAxeMineables;
        public ForgeConfigSpec.IntValue afterBreakingPickaxeMineables;
        public ForgeConfigSpec.IntValue afterTillingDirt;
        public ForgeConfigSpec.IntValue afterPathingDirt;
        public ForgeConfigSpec.IntValue afterStrippingLogs;
        public ForgeConfigSpec.IntValue afterFishing;
        public ForgeConfigSpec.IntValue afterAttacking;
        public ForgeConfigSpec.IntValue afterBlocking;
        public ForgeConfigSpec.IntValue afterPlacingBlocks;
        public ForgeConfigSpec.IntValue afterTakingDamage;
        public ForgeConfigSpec.IntValue afterShootingArrows;

        public ForgeConfigSpec.BooleanValue enableResting;
        public ForgeConfigSpec.BooleanValue enableSneaking;
        public ForgeConfigSpec.BooleanValue enableWalking;
        public ForgeConfigSpec.BooleanValue enableSwimming;
        public ForgeConfigSpec.BooleanValue enableSprinting;
        public ForgeConfigSpec.BooleanValue enablePaddling;
        public ForgeConfigSpec.BooleanValue enableClimbing;
        public ForgeConfigSpec.BooleanValue enableJumping;
        public ForgeConfigSpec.BooleanValue enableWalkingUp;

        public ForgeConfigSpec.IntValue afterResting;
        public ForgeConfigSpec.IntValue afterSwimming;
        public ForgeConfigSpec.IntValue afterSprinting;
        public ForgeConfigSpec.IntValue afterWalking;
        public ForgeConfigSpec.IntValue afterPaddling;
        public ForgeConfigSpec.IntValue afterSneaking;
        public ForgeConfigSpec.IntValue afterClimbing;
        public ForgeConfigSpec.IntValue afterJumping;
        public ForgeConfigSpec.IntValue afterWalkingUp;

        public ForgeConfigSpec.BooleanValue enableFreezing;
        public ForgeConfigSpec.IntValue afterFreezing;

        public ForgeConfigSpec.BooleanValue enableOpenClosing;
        public ForgeConfigSpec.IntValue afterOpenClosing;

        public ForgeConfigSpec.DoubleValue coldBiomeMultiplier;
        public ForgeConfigSpec.DoubleValue hotBiomeMultiplier;
        public ForgeConfigSpec.DoubleValue healingCostsMultiplier;
        public ForgeConfigSpec.BooleanValue enableArmorImpactOnExhaustion;
        public ForgeConfigSpec.DoubleValue armorLogarithmicImpact;

        public ForgeConfigSpec.BooleanValue enableParagliding;
        public ForgeConfigSpec.IntValue afterParagliding;
        public ForgeConfigSpec.BooleanValue enableCrawling;
        public ForgeConfigSpec.IntValue afterCrawling;
        public ForgeConfigSpec.BooleanValue enablePullingUp;
        public ForgeConfigSpec.IntValue afterPullingUp;
        public ForgeConfigSpec.BooleanValue enableChoppingTrees;
        public ForgeConfigSpec.BooleanValue enableCarry;
        public ForgeConfigSpec.DoubleValue carryMultiplier;
        public ForgeConfigSpec.BooleanValue enableRolling;
        public ForgeConfigSpec.IntValue afterRolling;

        public ForgeConfigSpec.BooleanValue enableHungerLeveling;
        public ForgeConfigSpec.IntValue initialHungerbarMaximum;
        public ForgeConfigSpec.IntValue raisingHungerbarAfter;

        private final AppetiteProfile profile;
        private final ForgeConfigSpec.Builder builder;

        public int getInitalHungerbarMaximum() {
            return enableHungerLeveling.get() ? initialHungerbarMaximum.get() : ModConstants.VANILLA_MAX_FOOD_LEVEL / 2;
        }

        public Data(ForgeConfigSpec.Builder builder, AppetiteProfile profile) {
            this.builder = builder;
            this.profile = profile;
            builder.comment("When setting the values, keep in mind that 20 ticks last one second (in the best case)." +
                "\nSo if you want to drop the hunger bar by 1 after 10 seconds of walking, you have to specify 200 (10 seconds * 20 ticks/second = 200 ticks)");

            builder.push("Exhaustion for breaking blocks");
            enableForShovelMineables = define(ENABLE_AT + "breaking blocks tagged with mineable/shovel (like dirt, sand, ...)", "enableForShovelMineables", true, true, true);
            enableForAxeMineables = define(ENABLE_AT + "breaking blocks tagged with mineable/axe (like logs, planks, ...)", "enableForAxeMineables", true, true, true);
            enableForPickaxeMineables = define(ENABLE_AT + "breaking blocks tagged with mineable/pickaxe (like stone, ores, ...)", "enableForPickaxeMineables", true, true, true);
            afterBreakingShovelMineables = defineRange(AFTER_ACTION + "breaking X blocks tagged with mineable/shovel (like dirt, sand, ...)", "afterBreakingShovelMineables", 1, 1600, 120, 80, 60);
            afterBreakingAxeMineables = defineRange(AFTER_ACTION + "breaking X blocks tagged with mineable/axe (like logs, planks, ...)", "afterBreakingAxeMineables", 1, 1600, 100, 70, 50);
            afterBreakingPickaxeMineables = defineRange(AFTER_ACTION + "breaking X blocks tagged with mineable/pickaxe (like stone, ores, ...)", "afterBreakingPickaxeMineables", 1, 1600, 80, 60, 40);
            builder.pop();

            builder.push("Exhaustion for modifying blocks");
            enableForTillingDirt = define(ENABLE_AT + "tilling dirt blocks", "enableForTillingDirt", true, true, true);
            enableForPathingDirt = define(ENABLE_AT + "pathing dirt blocks", "enableForPathingDirt", true, true, true);
            enableForStrippingLogs = define(ENABLE_AT + "stripping logs", "enableForStrippingLogs", true, true, true);
            afterTillingDirt = define(AFTER_ACTION + "tilling X dirt blocks", "afterTillingDirt", 120, 100, 80);
            afterPathingDirt = define(AFTER_ACTION + "pathing X dirt blocks", "afterPathingDirt", 120, 100, 80);
            afterStrippingLogs = define(AFTER_ACTION + "stripping X logs", "afterStrippingLogs", 120, 100, 80);
            builder.pop();

            builder.push("Exhaustion for Fights");
            enableForAttacking = define(ENABLE_AT + "attacking entities", "enableForAttackingEntities", false, true, true);
            enableForBlocking = define(ENABLE_AT + "blocking an attack with a shield", "enableForBlockingAttacks", false, true, true);
            enableForTakingDamage = define(ENABLE_AT + "taking damage", "enableForTakingDamage", false, true, true);
            enableForShootingArrows = define(ENABLE_AT + "shooting arrows", "enableForShootingArrows", true, true, true);
            afterAttacking = defineRange(AFTER_ACTION + "landing X attacks on entities", "afterAttackingEntities", 1, 80, 70, 50, 30);
            afterBlocking = define(AFTER_ACTION + "blocking X attacks with shield", "afterBlockingAttacks", 70, 50, 35);
            afterTakingDamage = defineRange(AFTER_ACTION + "taking damage X times", "afterTakingDamage", 1, 80, 80, 50, 35);
            afterShootingArrows = define(AFTER_ACTION + "shooting X arrows", "afterShootingArrows", 120, 60, 40);
            builder.pop();

            builder.push("Exhaustion for Interaction");
            afterPlacingBlocks = define(AFTER_ACTION + "placing X blocks", "afterPlacingBlocks", 200, 100, 75);
            enableForPlacingBlocks = define(ENABLE_AT + "placing blocks", "enableForPlacingBlocks", false, true, true);
            enableForFishing = define(ENABLE_AT + "fishing items", "enableForFishing", true, true, true);
            afterFishing = define(AFTER_ACTION + "fishing X items", "afterFishing", 60, 40, 20);
            enableOpenClosing = define(ENABLE_AT + "open and closing doors/trapdoors/gates/chests (without closing chests)", "enableForOpenAndClosing", false, true, true);
            afterOpenClosing = define(AFTER_ACTION + "open and closing X doors/trapdoors/gates/chests", "afterOpenAndClosing", 180, 90, 60);
            builder.pop();

            builder.push("Exhaustion for Movement");
            enableResting = define(ENABLE_WHILE + "resting (standing still, sitting, ...)", "enableWhileResting", false, false, true);
            enableSneaking = define(ENABLE_WHILE + "sneaking", "enableWhileSneaking", true, true, true);
            enableWalking = define(ENABLE_WHILE + "walking", "enableWhileWalking", false, true, true);
            enableSwimming = define(ENABLE_WHILE + "swimming", "enableWhileSwimming", true, true, true);
            enableSprinting = define(ENABLE_WHILE + "sprinting", "enableWhileSprinting", true, true, true);
            enablePaddling = define(ENABLE_WHILE + "paddling", "enableWhilePaddling", false, true, true);
            enableClimbing = define(ENABLE_WHILE + "climbing", "enableWhileClimbing", true, true, true);
            enableJumping = define(ENABLE_WHILE + "jumping", "enableForJumping", true, true, true);
            enableWalkingUp = define(ENABLE_WHILE + "walking up (stairs & slabs)", "enableWhileWalkingUp", true, true, true);
            enableCrawling = define(ENABLE_WHILE + "crawling (for vanilla, GoProne & Personality Mod)", "enableWhileCrawling", true, true, true);

            afterResting = defineTime(AFTER_TIME.formatted("resting"), "afterResting", 2400, 1200, 600);
            afterSneaking = defineTime(AFTER_TIME.formatted("sneaking"), "afterSneaking", 480, 240, 120);
            afterWalking = defineTime(AFTER_TIME.formatted("walking"), "afterWalking", 360, 160, 90);
            afterSwimming = defineTime(AFTER_TIME.formatted("swimming"), "afterSwimming", 240, 100, 60);
            afterSprinting = defineTimeRange(AFTER_TIME.formatted("sprinting"), "afterSprinting", 1, 14, 14, 12, 10);
            afterPaddling = defineTime(AFTER_TIME.formatted("paddling"), "afterPaddling", 180, 120, 80);
            afterClimbing = defineTime(AFTER_TIME.formatted("climbing"), "afterClimbing", 120, 80, 40);
            afterJumping = defineRange(AFTER_ACTION + "jumping X times", "afterJumping", 1, 160, 160, 80, 40);
            afterWalkingUp = defineTime(AFTER_TIME.formatted("walking up (stairs & slabs)"), "afterWalkingUp", 240, 120, 70);
            afterCrawling = defineTime(AFTER_TIME.formatted("crawling (for vanilla, GoProne & Personality Mod)"), "afterCrawling", 150, 90, 60);
            builder.pop();

            builder.push("Exhaustion for States");
            enableFreezing = define(ENABLE_WHILE + "freezing (while being inside powder snow)", "enableWhileFreezing", false, false, true);
            afterFreezing = defineTime(AFTER_TIME.formatted("freezing (while being inside powder snow)"), "afterFreezing", 60, 30, 15);
            builder.pop();

            builder.push("Exhaustion for other mods");
            enableParagliding = define(ENABLE_WHILE + "paragliding (Paragliders Mod)", "enableWhileParagliding", true, true, true);
            afterParagliding = defineTime(AFTER_TIME.formatted("paragliding (Paragliders Mod)"), "afterParagliding", 180, 120, 90);
            enablePullingUp = define(AFTER_TIME.formatted("pulling yourself up (Grappling Hook Mod)"), "enableWhilePullingUp", true, true, true);
            afterPullingUp = defineTime(AFTER_TIME.formatted("pulling yourself up (Grappling Hook Mod"), "afterPullingUp", 28, 14, 7);
            enableChoppingTrees = define(ENABLE_AT + "chopping trees (Falling Tree Mod). For changing the exhaustion see: afterBreakingAxeMineables", "enableForChoppingTrees", true, true, true);
            enableCarry = define(ENABLE_WHILE + "carrying objects (Carry on Mod", "enableForCarryingObjects", true, true, true);
            carryMultiplier = defineRange("Sets the multiplier for exhaustion when carrying objects (Carry On Mod)", "carryMultiplier", 1d, 10d, 1.5, 2d, 2.75);
            enableRolling = define(ENABLE_AT + "rolling (Combat Roll Mod", "enableRolling", true, true, true);
            afterRolling = defineRange(AFTER_ACTION + "rolling X times (Combat Roll Mod).", "afterRolling", 1, 75, 50, 30, 15);
            builder.pop();

            builder.push("Hunger leveling");
            enableHungerLeveling = define("Provides the option to lower the initial maximum of the player's hunger bar. The defined maximum " +
                "can be increased by leveling up (by gaining experience).", "enableHungerLeveling", false, true, true);
            initialHungerbarMaximum = defineRange("Determines the hunger indicator (drumstick amount) each player starts with when joining a world for the first time (vanilla's default is 10).", "initialHungerbarMaximum", 1, 10, 10, 7, 5);
            raisingHungerbarAfter = defineRange("Determines what level delta is needed to increase the initialHungerbarMaximum by one drumstick.", "raisingHungerbarAfter", 1, 20, 3, 6, 7);
            builder.pop();

            builder.push("Advanced Settings");
            coldBiomeMultiplier = defineRange("Sets the multiplier for exhaustion caused by the rules of Appetite when the player is in a cold biome",
                "coldBiomeMultiplier", 1d, 10d, 1d, 1d, 1d);
            hotBiomeMultiplier = defineRange("Sets the multiplier for exhaustion caused by the rules of Appetite when the player is in a hot biome",
                "hotBiomeMultiplier", 1d, 10d, 1d, 1d, 1d);
            healingCostsMultiplier = defineRange("Increases the amount of hunger that is used to regenerate hearts when damaged. At vanilla one regenerated heart costs 1.5 drumsticks. A multiplier of 1.3333 will costs 2 drumsticks, 2 will costs 3 and so on.",
                "healingCosts", 1d, 5d, 1d, 1.3333d, 2d);
            enableArmorImpactOnExhaustion = define("Determines that each armor wil have an individual impact on the added exhaustion. Lighter armor will have an lower impact and heavier armor an higher impact.", "ArmorImpactOnExhaustion", false, true, true);
            armorLogarithmicImpact = defineRange("Appetite is using the logarithm as a multiplier to modify the exhaustion defined by the rules, depending on the armor defense points. This value defines the logarithmic base whereas the defense points will be used as the numerus. The higher the value the higher the reward (lower exhaustion) for wearing lighter armor but the lower the additional exhaustion for heavier armor. The lower the value the higher the exhaustion for wearing heavy armor but the lower the reward for wearing lighter armor." +
                    "\nExample for rewarding the player (default mode): Wearing leather armor (numerus = 7) for the defined impact of 12 (base) leads to an multiplier of 0.78 (22% fewer exhaustion). Wearing diamond armor (numerus = 20) for the same impact leads to a multiplier of 1.2 (20% higher exhaustion). A calculated multiplier for leather armor lower than 1 is also used when the player is wearing no armor."
                    + "\nExample for higher difficulty: Wearing leather armor for the defined impact of 6 leads to an multiplier of 1.09 (9% more exhaustion). Wearing diamond armor for the same impact leads to a multiplier of 1.67 (67% higher exhaustion).",
                "armorLogarithmicImpact", 6d, 20d, 15d, 12d, 7d);
        }

        private ForgeConfigSpec.BooleanValue define(String comment, String property, boolean... profileValues) {
            return builder.comment(comment).define(property, get(profileValues));
        }

        private ForgeConfigSpec.IntValue defineRange(String comment, String property, Integer min, Integer max, Integer... profileValues) {
            return builder.comment(comment).defineInRange(property, get(profileValues), min, max);
        }

        private ForgeConfigSpec.IntValue define(String comment, String property, Integer... profileValues) {
            return defineRange(comment, property, 1, MAX_VALUE, profileValues);
        }

        private ForgeConfigSpec.DoubleValue define(String comment, String property, Double... profileValues) {
            return defineRange(comment, property, 1d, Double.MAX_VALUE, profileValues);
        }

        private ForgeConfigSpec.DoubleValue defineRange(String comment, String property, Double min, Double max, Double... profileValues) {
            return builder.comment(comment).defineInRange(property, get(profileValues), min, max);
        }

        private ForgeConfigSpec.IntValue defineTime(String comment, String property, Integer... seconds) {
            return builder.comment(comment).defineInRange(property, get(seconds) * 20, 20, MAX_VALUE);
        }

        private ForgeConfigSpec.IntValue defineTimeRange(String comment, String property, Integer min, Integer max, Integer... seconds) {
            return builder.comment(comment).defineInRange(property, get(seconds) * 20, min * 20, max * 20);
        }

        private boolean get(boolean... profileValues) {
            return profileValues.length <= profile.ordinal() ? profileValues[1] : profileValues[profile.ordinal()];
        }

        @SafeVarargs
        private <T> T get(T... profileValues) {
            return profileValues.length <= profile.ordinal() ? profileValues[1] : profileValues[profile.ordinal()];
        }
    }
}