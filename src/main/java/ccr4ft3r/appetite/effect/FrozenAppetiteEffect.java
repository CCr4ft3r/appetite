package ccr4ft3r.appetite.effect;

import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.ServerPlayerData;
import ccr4ft3r.appetite.data.capabilities.FrozenAppetiteCapability;
import ccr4ft3r.appetite.registry.ModMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

import static ccr4ft3r.appetite.util.PlayerUtil.*;

public class FrozenAppetiteEffect extends MobEffect {

    public FrozenAppetiteEffect() {
        super(MobEffectCategory.BENEFICIAL, 2785727);
    }

    @Override
    public void applyEffectTick(@Nullable LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player) || player.getLevel().isClientSide())
            return;

        LazyOptional<FrozenAppetiteCapability> frozenAppetiteCap = getFrozenAppetiteEffect(player);
        if (frozenAppetiteCap.map(FrozenAppetiteCapability::shouldEffectBeRemoved).orElse(false)) {
            frozenAppetiteCap.ifPresent(cap -> cap.setShouldEffectBeRemoved(false));
            player.removeEffect(ModMobEffects.FROZEN_APPETITE.get());
            player.addEffect(new MobEffectInstance(ModMobEffects.BRAIN_FREEZE.get(), 160));
            return;
        }

        ServerPlayerData playerData = ServerData.getPlayerData(player);
        playerData.updateFoodData(player.getFoodData());

        player.getFoodData().setExhaustion(playerData.getFreezedExhaustionLevel());
        player.getFoodData().setFoodLevel(playerData.getFreezedFoodLevel());
        player.getFoodData().setSaturation(playerData.getFreezedSaturationLevel());
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}