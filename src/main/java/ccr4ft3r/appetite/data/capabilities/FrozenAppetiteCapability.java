package ccr4ft3r.appetite.data.capabilities;

import ccr4ft3r.appetite.config.MainConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

public class FrozenAppetiteCapability implements INBTSerializable<CompoundTag> {

    private int dayToCheck;

    private int amountAddedToday;

    private boolean shouldEffectBeRemoved;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("dayToCheck", IntTag.valueOf(dayToCheck));
        tag.put("amountAddedToday", IntTag.valueOf(amountAddedToday));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.dayToCheck = tag.getInt("dayToCheck");
        this.amountAddedToday = tag.getInt("amountAddedToday");
    }

    public void effectUsed(Level level) {
        if (amountAddedToday >= MainConfig.CONFIG_DATA.frozenAppetitePerDay.get() || !isCheckingForToday(level)) {
            this.amountAddedToday = 0;
            this.dayToCheck = (int) (level.getDayTime() / 24000);
        }
        this.amountAddedToday++;
    }

    private boolean isCheckingForToday(Level level) {
        return level.getDayTime() / 24000 == dayToCheck;
    }

    public boolean canUse(Level level) {
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