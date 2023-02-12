package ccr4ft3r.appetite.data.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HungerLevelingProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(HungerLevelingCapability.class)
    public static Capability<HungerLevelingCapability> HUNGER_LEVELING_CAP = null;
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
            return opt.cast();
        else
            return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return createCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        createCapability().deserializeNBT(nbt);
    }
}