package ccr4ft3r.appetite.data.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AppetiteCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    public static Capability<FrozenAppetiteCapability> FROZEN_APPETITE_CAP = CapabilityManager.get(new CapabilityToken<>(){});
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
        if(cap == FROZEN_APPETITE_CAP)
            return opt.cast();
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