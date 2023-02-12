package ccr4ft3r.appetite.data.capabilities;

import ccr4ft3r.appetite.IFoodData;
import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.network.ClientboundCapabilityPacket;
import ccr4ft3r.appetite.network.PacketHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import static ccr4ft3r.appetite.config.ProfileConfig.*;

public class HungerLevelingCapability implements INBTSerializable<CompoundNBT> {

    private int currentFoodMaximum = getProfile().initialHungerbarMaximum.get();

    private int lastLevelOfIncrease;

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.put("currentFoodMaximum", IntNBT.valueOf(currentFoodMaximum));
        tag.put("lastLevelOfIncrease", IntNBT.valueOf(lastLevelOfIncrease));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        this.currentFoodMaximum = tag.getInt("currentFoodMaximum");
        this.lastLevelOfIncrease = tag.getInt("lastLevelOfIncrease");
    }

    public void updateFoodMax(ServerPlayerEntity player, int newLevels) {
        try {
            if (!getProfile().enableHungerLeveling.get())
                return;

            if (player.experienceLevel + newLevels >= lastLevelOfIncrease + getProfile().raisingHungerbarAfter.get()
                && getCurrentFoodMaximum() < 10) {
                currentFoodMaximum++;
                lastLevelOfIncrease = player.experienceLevel + newLevels;
                PlayerAdvancements advancements = player.getAdvancements();
                Advancement levelUpAdvancement = player.createCommandSourceStack().getServer().getAdvancements().getAdvancement(
                    new ResourceLocation(ModConstants.MOD_ID, "level_up"));
                advancements.getOrStartProgress(levelUpAdvancement).revokeProgress("requirement");
                advancements.award(levelUpAdvancement, "requirement");
            }
        } finally {
            if (player.getFoodData() instanceof IFoodData) {
                ((IFoodData) player.getFoodData()).setFoodbarMax(getCurrentFoodMaximum());
                PacketHandler.sendToPlayer(new ClientboundCapabilityPacket(this), player);
            }
        }
    }

    public int getCurrentFoodMaximum() {
        return getProfile().enableHungerLeveling.get() ? currentFoodMaximum : getProfile().getInitalHungerbarMaximum();
    }
}