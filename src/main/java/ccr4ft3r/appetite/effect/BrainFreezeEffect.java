package ccr4ft3r.appetite.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

import static ccr4ft3r.appetite.util.PlayerUtil.*;

public class BrainFreezeEffect extends Effect {

    public BrainFreezeEffect() {
        super(EffectType.HARMFUL, 2785727);
    }

    @Override
    public void applyEffectTick(@Nullable LivingEntity entity, int amplifier) {
        if (!isPlayerServerside(entity))
            return;
        entity.hurt(DamageSource.GENERIC, 0.4f);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}