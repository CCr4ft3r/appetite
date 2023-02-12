package ccr4ft3r.appetite.effect;

import ccr4ft3r.appetite.IFoodData;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.ServerPlayerData;
import ccr4ft3r.appetite.data.capabilities.AppetiteCapabilityProvider;
import ccr4ft3r.appetite.data.capabilities.FrozenAppetiteCapability;
import ccr4ft3r.appetite.registry.ModMobEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

import static ccr4ft3r.appetite.util.PlayerUtil.*;

public class FrozenAppetiteEffect extends Effect {

    public FrozenAppetiteEffect() {
        super(EffectType.BENEFICIAL, 2785727);
    }

    @Override
    public void applyEffectTick(@Nullable LivingEntity entity, int amplifier) {
        if (!isPlayerServerside(entity))
            return;

        PlayerEntity player = (PlayerEntity) entity;
        LazyOptional<FrozenAppetiteCapability> frozenAppetiteCap = player.getCapability(AppetiteCapabilityProvider.FROZEN_APPETITE_CAP, null);
        if (frozenAppetiteCap.map(FrozenAppetiteCapability::shouldEffectBeRemoved).orElse(false)) {
            frozenAppetiteCap.ifPresent(cap -> cap.setShouldEffectBeRemoved(false));
            player.removeEffect(ModMobEffects.FROZEN_APPETITE.get());
            player.addEffect(new EffectInstance(ModMobEffects.BRAIN_FREEZE.get(), 160));
            return;
        }

        ServerPlayerData playerData = ServerData.getPlayerData(player);
        playerData.updateFoodData(player.getFoodData());

        ((IFoodData) player.getFoodData()).setExhaustion(playerData.getFreezedExhaustionLevel());
        player.getFoodData().setFoodLevel(playerData.getFreezedFoodLevel());
        player.getFoodData().setSaturation(playerData.getFreezedSaturationLevel());
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}