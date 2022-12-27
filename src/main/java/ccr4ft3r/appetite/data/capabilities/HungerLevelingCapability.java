package ccr4ft3r.appetite.data.capabilities;

import ccr4ft3r.appetite.IFoodData;
import ccr4ft3r.appetite.ModConstants;
import net.minecraft.advancements.Advancement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
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

    public boolean increaseFoodMaximum(Player player, int newLevels) {
        try {
            if (player.experienceLevel + newLevels >= lastLevelOfIncrease + getProfile().raisingHungerbarAfter.get()
                && currentFoodMaximum < 10) {
                currentFoodMaximum++;
                lastLevelOfIncrease = player.experienceLevel + newLevels;
                PlayerAdvancements advancements = ((ServerPlayer) player).getAdvancements();
                Advancement levelUpAdvancement = player.createCommandSourceStack().getAdvancement(
                    new ResourceLocation(ModConstants.MOD_ID, "level_up"));
                advancements.getOrStartProgress(levelUpAdvancement).revokeProgress("requirement");
                advancements.award(levelUpAdvancement, "requirement");
                return true;
            }
            return false;
        } finally {
            if (player.getFoodData() instanceof IFoodData iFoodData)
                iFoodData.setFoodbarMax(currentFoodMaximum);
        }
    }

    public int getCurrentFoodMaximum() {
        return currentFoodMaximum;
    }
}