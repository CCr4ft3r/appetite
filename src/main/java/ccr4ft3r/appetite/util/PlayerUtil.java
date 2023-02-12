package ccr4ft3r.appetite.util;

import ccr4ft3r.appetite.data.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;

import static ccr4ft3r.appetite.config.MainConfig.*;
import static ccr4ft3r.appetite.config.ProfileConfig.*;

public class PlayerUtil {

    public static boolean isPlayerServerside(Entity entity) {
        if (entity instanceof PlayerEntity)
            return !entity.level.isClientSide;
        return false;
    }

    public static void exhaust(PlayerEntity player, ForgeConfigSpec.BooleanValue optionEnabled, boolean onlyIf, ForgeConfigSpec.IntValue exhaustionAfter, long multiplier,
                               float vanillaExhaustion) {
        if (cannotBeExhausted(player))
            return;
        if (optionEnabled.get() && onlyIf) {
            float appetiteExhaustion = 8f * multiplier * getExhaustionMultiplier(player) / (float) exhaustionAfter.get();
            float exhaustion = Math.max(appetiteExhaustion - vanillaExhaustion, 0);
            if (CONFIG_DATA.enableExtendedLogging.get())
                LogUtils.getLogger().info("Adding {} exhaustion to player '{}' due to rule '{}'", exhaustion, player.getScoreboardName(),
                    String.join(".", exhaustionAfter.getPath()));
            player.causeFoodExhaustion(exhaustion);
        }
    }

    private static float getExhaustionMultiplier(PlayerEntity player) {
        float multiplier = 1;
        if (BiomeUtil.isHot(player))
            multiplier *= getProfile().hotBiomeMultiplier.get().floatValue();
        if (BiomeUtil.isCold(player))
            multiplier *= getProfile().coldBiomeMultiplier.get().floatValue();
        if (ServerData.getPlayerData(player).isCarrying() && getProfile().enableCarry.get())
            multiplier *= getProfile().carryMultiplier.get().floatValue();
        if (getProfile().enableArmorImpactOnExhaustion.get()) {
            float logBase = getProfile().armorLogarithmicImpact.get().floatValue();
            if (logBase > 0) {
                double logDivisor = Math.log(logBase);
                int armorValue = player.getArmorValue();
                if (logDivisor != 0 && armorValue >= 0) {
                    multiplier *= Math.log(armorValue == 0 ? 7 : armorValue) / logDivisor;
                }
            }
        }
        return multiplier == 0 ? 1 : multiplier;
    }

    public static boolean cannotBeExhausted(PlayerEntity player) {
        return player instanceof FakePlayer || player.level.isClientSide() || !player.isAddedToWorld() ||
            player.isCreative() || player.isSpectator() || player.isSleeping() || !player.isAlive() ||
            CONFIG_DATA.dimensionBlacklist.get().contains(player.level.dimension().location().toString());
    }
}