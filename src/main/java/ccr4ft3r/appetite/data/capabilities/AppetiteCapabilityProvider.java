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

public class AppetiteCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(FrozenAppetiteCapability.class)
    public static Capability<FrozenAppetiteCapability> FROZEN_APPETITE_CAP = null;
    private FrozenAppetiteCapability frozenAppetiteCapability = null;
    private final LazyOptional<FrozenAppetiteCapability> opt = LazyOptional.of(this::createCapability);

    private FrozenAppetiteCapability createCapability() {
        if (frozenAppetiteCapability == null) {
            frozenAppetiteCapability = new FrozenAppetiteCapability();
        }
        return frozenAppetiteCapability;
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == FROZEN_APPETITE_CAP)
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