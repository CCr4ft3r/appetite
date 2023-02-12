package ccr4ft3r.appetite.data.capabilities;

import ccr4ft3r.appetite.config.MainConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class FrozenAppetiteCapability implements INBTSerializable<CompoundNBT> {

    private int dayToCheck;

    private int amountAddedToday;

    private boolean shouldEffectBeRemoved;

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.put("dayToCheck", IntNBT.valueOf(dayToCheck));
        tag.put("amountAddedToday", IntNBT.valueOf(amountAddedToday));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        this.dayToCheck = tag.getInt("dayToCheck");
        this.amountAddedToday = tag.getInt("amountAddedToday");
    }

    public void effectUsed(World level) {
        if (amountAddedToday >= MainConfig.CONFIG_DATA.frozenAppetitePerDay.get() || !isCheckingForToday(level)) {
            this.amountAddedToday = 0;
            this.dayToCheck = (int) (level.getDayTime() / 24000);
        }
        this.amountAddedToday++;
    }

    private boolean isCheckingForToday(World level) {
        return level.getDayTime() / 24000 == dayToCheck;
    }

    public boolean canUse(World level) {
        return (this.amountAddedToday < MainConfig.CONFIG_DATA.frozenAppetitePerDay.get() || !isCheckingForToday(level))
            && MainConfig.CONFIG_DATA.allowFrozenAppetite.get();
    }

    public boolean shouldEffectBeRemoved() {
        return shouldEffectBeRemoved;
    }

    public void setShouldEffectBeRemoved(boolean shouldEffectBeRemoved) {
        this.shouldEffectBeRemoved = shouldEffectBeRemoved;
    }
}