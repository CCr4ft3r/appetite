package ccr4ft3r.appetite.registry;

import ccr4ft3r.appetite.data.capabilities.FrozenAppetiteCapability;
import ccr4ft3r.appetite.data.capabilities.HungerLevelingCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;

public class ModCaps {

    public static void register() {
        CapabilityManager.INSTANCE.register(FrozenAppetiteCapability.class, createStorage(), FrozenAppetiteCapability::new);
        CapabilityManager.INSTANCE.register(HungerLevelingCapability.class, createStorage(), HungerLevelingCapability::new);
    }

    private static <T extends INBTSerializable<CompoundNBT>> Capability.IStorage<T> createStorage() {
        return new Capability.IStorage<T>() {
            public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
                return instance.serializeNBT();
            }

            public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        };
    }
}