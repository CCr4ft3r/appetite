package ccr4ft3r.appetite.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class BrainFreezeEffect extends MobEffect {

    public BrainFreezeEffect() {
        super(MobEffectCategory.HARMFUL, 2785727);
    }

    @Override
    public void applyEffectTick(@Nullable LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player) || player.getLevel().isClientSide())
            return;
        player.hurt(DamageSource.FREEZE, 0.4f);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}