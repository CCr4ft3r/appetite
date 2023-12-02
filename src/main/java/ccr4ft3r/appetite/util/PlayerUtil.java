package ccr4ft3r.appetite.util;

import ccr4ft3r.appetite.IFoodData;
import ccr4ft3r.appetite.config.MainConfig;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.capabilities.AppetiteCapabilityProvider;
import ccr4ft3r.appetite.data.capabilities.FrozenAppetiteCapability;
import ccr4ft3r.appetite.registry.ModMobEffects;
import com.mojang.logging.LogUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;

import static ccr4ft3r.appetite.config.MainConfig.*;
import static ccr4ft3r.appetite.config.ProfileConfig.*;

public class PlayerUtil {

    public static void exhaust(Player player, boolean onlyIf, ForgeConfigSpec.IntValue exhaustionAfter, long multiplier,
                               float vanillaExhaustion, boolean canBeExhaustedAlreadyChecked) {
        if (!canBeExhaustedAlreadyChecked && cannotBeExhausted(player))
            return;
        if (onlyIf && canApplyExhaustion(player)) {
            float appetiteExhaustion = (8f * (((IFoodData) player.getFoodData()).getFoodbarMax() / 10f))
                * multiplier * getExhaustionMultiplier(player) / (float) exhaustionAfter.get();
            float exhaustion = Math.max(appetiteExhaustion - vanillaExhaustion, 0);
            if (CONFIG_DATA.enableExtendedLogging.get())
                LogUtils.getLogger().info("Adding {} exhaustion to player '{}' due to rule '{}'", exhaustion, player.getScoreboardName(),
                    String.join(".", exhaustionAfter.getPath()));
            player.causeFoodExhaustion(exhaustion);
        }
    }

    public static boolean canApplyExhaustion(Player player) {
        return !getFrozenAppetiteEffect(player).map(FrozenAppetiteCapability::shouldEffectBeRemoved).orElse(false);
    }

    public static LazyOptional<FrozenAppetiteCapability> getFrozenAppetiteEffect(Player player) {
        return player.getCapability(AppetiteCapabilityProvider.FROZEN_APPETITE_CAP, null);
    }

    public static void exhaust(Player player, ForgeConfigSpec.BooleanValue optionEnabled, boolean onlyIf, ForgeConfigSpec.IntValue exhaustionAfter, long multiplier,
                               float vanillaExhaustion) {
        if (!optionEnabled.get())
            return;
        exhaust(player, onlyIf, exhaustionAfter, multiplier, vanillaExhaustion, false);
    }

    private static float getExhaustionMultiplier(Player player) {
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

    public static boolean cannotBeExhaustedOverTime(Player player) {
        return player instanceof FakePlayer || player.getLevel().isClientSide() || !player.isAddedToWorld() ||
            player.isCreative() || player.isSpectator() || !player.isAlive() ||
            CONFIG_DATA.dimensionBlacklist.get().contains(player.getLevel().dimension().location().toString());
    }

    public static boolean cannotBeExhausted(Player player) {
        return player instanceof FakePlayer || player.getLevel().isClientSide() || !player.isAddedToWorld() ||
            player.isCreative() || player.isSpectator() || player.isSleeping() || !player.isAlive() ||
            CONFIG_DATA.dimensionBlacklist.get().contains(player.getLevel().dimension().location().toString())
            || !CONFIG_DATA.enablesRules.get();
    }

    public static void initiateFrozenFoodEffect(Player player) {
        boolean isFrozenAppetiteAllowed = MainConfig.CONFIG_DATA.allowFrozenAppetite.get();
        boolean isFrozenAppetiteAlreadyAdded = player.hasEffect(ModMobEffects.FROZEN_APPETITE.get());
        boolean canFrozenAppetiteBeUsed = player.getCapability(AppetiteCapabilityProvider.FROZEN_APPETITE_CAP).map(c -> c.canUse(player.getLevel())).orElse(false);
        boolean shouldBeBrainfreezed = isFrozenAppetiteAlreadyAdded || !canFrozenAppetiteBeUsed;

        if (shouldBeBrainfreezed) {
            player.addEffect(new MobEffectInstance(ModMobEffects.BRAIN_FREEZE.get(), 160));
        } else if (isFrozenAppetiteAllowed) {
            player.addEffect(new MobEffectInstance(ModMobEffects.FROZEN_APPETITE.get(), 1200));
        }
    }
}