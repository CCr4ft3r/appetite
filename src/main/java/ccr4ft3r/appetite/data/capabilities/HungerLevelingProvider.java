package ccr4ft3r.appetite.data.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HungerLevelingProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    public static Capability<HungerLevelingCapability> HUNGER_LEVELING_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    private HungerLevelingCapability hungerLevelingCapability = null;
    private final LazyOptional<HungerLevelingCapability> opt = LazyOptional.of(this::createCapability);

    private HungerLevelingCapability createCapability() {
        if (hungerLevelingCapability == null) {
            hungerLevelingCapability = new HungerLevelingCapability();
        }
        return hungerLevelingCapability;
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == HUNGER_LEVELING_CAP)
            return LazyOptional.of(() -> hungerLevelingCapability).cast();
        else
            return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return createCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createCapability().deserializeNBT(nbt);
    }
}