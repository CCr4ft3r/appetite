package ccr4ft3r.appetite.config;

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
        public static final String AFTER_TIME = "Decrease saturation or food bar value by 1 after %s X ticks";

        public ForgeConfigSpec.BooleanValue enableForShovelMineables;
        public ForgeConfigSpec.BooleanValue enableForAxeMineables;
        public ForgeConfigSpec.BooleanValue enableForPickaxeMineables;
        public ForgeConfigSpec.BooleanValue enableForUsingHoe;
        public ForgeConfigSpec.BooleanValue enableForFishing;
        public ForgeConfigSpec.BooleanValue enableForAttacking;
        public ForgeConfigSpec.BooleanValue enableForBlocking;

        public ForgeConfigSpec.IntValue afterBreakingShovelMineables;
        public ForgeConfigSpec.IntValue afterBreakingAxeMineables;
        public ForgeConfigSpec.IntValue afterBreakingPickaxeMineables;
        public ForgeConfigSpec.IntValue afterUsingHoe;
        public ForgeConfigSpec.IntValue afterFishing;
        public ForgeConfigSpec.IntValue afterAttacking;
        public ForgeConfigSpec.IntValue afterBlocking;

        public ForgeConfigSpec.BooleanValue enableResting;
        public ForgeConfigSpec.BooleanValue enableSneaking;
        public ForgeConfigSpec.BooleanValue enableWalking;
        public ForgeConfigSpec.BooleanValue enableSwimming;
        public ForgeConfigSpec.BooleanValue enableSprinting;
        public ForgeConfigSpec.BooleanValue enablePaddling;
        public ForgeConfigSpec.BooleanValue enableClimbing;
        public ForgeConfigSpec.BooleanValue enableJumping;

        public ForgeConfigSpec.IntValue afterResting;
        public ForgeConfigSpec.IntValue afterSwimming;
        public ForgeConfigSpec.IntValue afterSprinting;
        public ForgeConfigSpec.IntValue afterWalking;
        public ForgeConfigSpec.IntValue afterPaddling;
        public ForgeConfigSpec.IntValue afterSneaking;
        public ForgeConfigSpec.IntValue afterClimbing;
        public ForgeConfigSpec.IntValue afterJumping;

        public ForgeConfigSpec.BooleanValue enableFreezing;

        public ForgeConfigSpec.IntValue whileFreezing;

        private final AppetiteProfile profile;
        private final ForgeConfigSpec.Builder builder;

        public Data(ForgeConfigSpec.Builder builder, AppetiteProfile profile) {
            this.builder = builder;
            this.profile = profile;
            builder.comment("When setting the values, keep in mind that 20 ticks last one second (in the best case)." +
                "\nSo if you want to drop the hunger bar by 1 after 10 seconds of walking, you have to specify 200 (10 seconds * 20 ticks/second = 200 ticks)");

            builder.push("Interaction exhaustion");
            enableForShovelMineables = define(ENABLE_AT + "breaking blocks tagged with mineable/shovel", "enableForShovelMineables", true, true, true);
            enableForAxeMineables = define(ENABLE_AT + "breaking blocks tagged with mineable/axe", "enableForAxeMineables", true, true, true);
            enableForPickaxeMineables = define(ENABLE_AT + "breaking blocks tagged with mineable/pickaxe", "enableForPickaxeMineables", true, true, true);
            enableForUsingHoe = define(ENABLE_AT + "using a hoe on tillable blocks", "enableForUsingHoe", true, true, true);
            enableForFishing = define(ENABLE_AT + "fishing items", "enableForFishing", true, true, true);
            enableForAttacking = define(ENABLE_AT + "attacking entities", "enableForAttacking", false, true, true);
            enableForBlocking = define(ENABLE_AT + "blocking an attack with a shield", "enableForBlocking", false, true, true);

            afterBreakingShovelMineables = defineRange(AFTER_ACTION + "breaking X blocks tagged with mineable/shovel", "afterBreakingShovelMineables", 1, 1600, 120, 80, 60);
            afterBreakingAxeMineables = defineRange(AFTER_ACTION + "breaking X blocks tagged with mineable/axe", "afterBreakingAxeMineables", 1, 1600, 100, 70, 50);
            afterBreakingPickaxeMineables = defineRange(AFTER_ACTION + "breaking X blocks tagged with mineable/pickaxe", "afterBreakingPickaxeMineables", 1, 1600, 80, 60, 40);
            afterUsingHoe = define(AFTER_ACTION + "using hoe on X blocks", "afterUsingHoe", 240, 180, 120);
            afterFishing = define(AFTER_ACTION + "fishing X items", "afterFishing", 60, 40, 20);
            afterAttacking = defineRange(AFTER_ACTION + "landing X attacks on entities", "afterAttacking", 1, 80, 70, 50, 30);
            afterBlocking = define(AFTER_ACTION + "blocking X attacks with shield", "afterBlocking", 70, 50, 35);
            builder.pop();

            builder.push("Movement exhaustion");
            enableResting = define(ENABLE_WHILE + "resting (standing still, sitting, ...)", "enableWhileResting", false, false, true);
            enableSneaking = define(ENABLE_WHILE + "sneaking", "enableWhileSneaking", true, true, true);
            enableWalking = define(ENABLE_WHILE + "walking", "enableWhileWalking", false, true, true);
            enableSwimming = define(ENABLE_WHILE + "swimming", "enableWhileSwimming", true, true, true);
            enableSprinting = define(ENABLE_WHILE + "sprinting", "enableWhileSprinting", true, true, true);
            enablePaddling = define(ENABLE_WHILE + "paddling", "enableWhilePaddling", false, true, true);
            enableClimbing = define(ENABLE_WHILE + "climbing", "enableWhileClimbing", true, true, true);
            enableJumping = define(ENABLE_WHILE + "jumping", "enableForJumping", true, true, true);

            afterResting = defineTime(AFTER_TIME.formatted("resting"), "afterResting", 2400, 1200, 600);
            afterSneaking = defineTime(AFTER_TIME.formatted("sneaking"), "afterSneaking", 480, 240, 120);
            afterWalking = defineTime(AFTER_TIME.formatted("walking"), "afterWalking", 360, 160, 90);
            afterSwimming = defineTime(AFTER_TIME.formatted("swimming"), "afterSwimming", 240, 100, 60);
            afterSprinting = defineTimeRange(AFTER_TIME.formatted("sprinting"), "afterSprinting", 1, 14, 14, 12, 10);
            afterPaddling = defineTime(AFTER_TIME.formatted("paddling"), "afterPaddling", 180, 120, 80);
            afterClimbing = defineTime(AFTER_TIME.formatted("climbing"), "afterClimbing", 120, 80, 40);
            afterJumping = defineRange(AFTER_ACTION + "jumping X times", "afterJumping", 1, 160, 160, 80, 40);
            builder.pop();

            builder.push("State exhaustion");
            enableFreezing = define(ENABLE_WHILE + "freezing", "enableWhileFreezing", false, false, true);

            whileFreezing = defineTime(AFTER_TIME.formatted("freezing"), "afterFreezing", 60, 30, 15);
        }

        private ForgeConfigSpec.BooleanValue define(String comment, String property, boolean... profileValues) {
            return builder.comment(comment).define(property, get(profileValues));
        }

        private ForgeConfigSpec.IntValue defineRange(String comment, String property, int min, int max, int... profileValues) {
            return builder.comment(comment).defineInRange(property, get(profileValues), min, max);
        }

        private ForgeConfigSpec.IntValue define(String comment, String property, int... profileValues) {
            return defineRange(comment, property, 1, MAX_VALUE, profileValues);
        }

        private ForgeConfigSpec.IntValue defineTime(String comment, String property, int... seconds) {
            return builder.comment(comment).defineInRange(property, get(seconds) * 20, 20, MAX_VALUE);
        }

        private ForgeConfigSpec.IntValue defineTimeRange(String comment, String property, int min, int max, int... seconds) {
            return builder.comment(comment).defineInRange(property, get(seconds) * 20, min * 20, max * 20);
        }

        private boolean get(boolean... profileValues) {
            return profileValues.length <= profile.ordinal() ? profileValues[1] : profileValues[profile.ordinal()];
        }

        private int get(int... profileValues) {
            return profileValues.length <= profile.ordinal() ? profileValues[1] : profileValues[profile.ordinal()];
        }
    }
}