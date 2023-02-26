package ccr4ft3r.appetite.data.capabilities;

import ccr4ft3r.appetite.IFoodData;
import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.network.ClientboundCapabilityPacket;
import ccr4ft3r.appetite.network.PacketHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

import static ccr4ft3r.appetite.config.ProfileConfig.*;

public class HungerLevelingCapability implements INBTSerializable<CompoundTag> {

    private int currentFoodMaximum = getProfile().initialHungerbarMaximum.get();

    private int lastLevelOfIncrease;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("currentFoodMaximum", IntTag.valueOf(currentFoodMaximum));
        tag.put("lastLevelOfIncrease", IntTag.valueOf(lastLevelOfIncrease));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.currentFoodMaximum = tag.getInt("currentFoodMaximum");
        this.lastLevelOfIncrease = tag.getInt("lastLevelOfIncrease");
    }

    public void updateFoodMax(ServerPlayer player) {
        try {
            if (!getProfile().enableHungerLeveling.get())
                return;

            if (player.experienceLevel >= lastLevelOfIncrease + getProfile().raisingHungerbarAfter.get()
                && getCurrentFoodMaximum() < 10) {
                currentFoodMaximum++;
                lastLevelOfIncrease = player.experienceLevel;
                PlayerAdvancements advancements = player.getAdvancements();
                Advancement levelUpAdvancement = player.createCommandSourceStack().getAdvancement(
                    new ResourceLocation(ModConstants.MOD_ID, "level_up"));
                advancements.getOrStartProgress(levelUpAdvancement).revokeProgress("requirement");
                advancements.award(levelUpAdvancement, "requirement");
            }
        } finally {
            if (player.getFoodData() instanceof IFoodData iFoodData) {
                iFoodData.setFoodbarMax(getCurrentFoodMaximum());
                PacketHandler.sendToPlayer(new ClientboundCapabilityPacket(this), player);
            }
        }
    }

    public int getCurrentFoodMaximum() {
        return getProfile().enableHungerLeveling.get() ? currentFoodMaximum : getProfile().getInitalHungerbarMaximum();
    }
}