package ccr4ft3r.appetite.data;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.phys.Vec3;

public class ServerPlayerData {

    private Vec3 lastPosition;
    private boolean isMoving;
    private boolean startedMoving;
    private boolean isParagliding;
    private boolean isCrawling;

    private Integer freezedFoodLevel = null;
    private Float freezedSaturationLevel = null;
    private Float freezedExhaustionLevel = null;

    ServerPlayerData(Player player) {
        setFoodData(player.getFoodData());
    }

    public Vec3 getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Vec3 lastPosition) {
        this.lastPosition = lastPosition;
    }

    public boolean isMoving() {
        if (startedMoving) {
            this.startedMoving = false;
            return true;
        }
        return isMoving;
    }

    public void setMoving(boolean moving) {
        if (moving && !isMoving)
            startedMoving = true;
        isMoving = moving;
    }

    public boolean isParagliding() {
        return isParagliding;
    }

    public void setParagliding(boolean paragliding) {
        isParagliding = paragliding;
    }

    public boolean isCrawling() {
        return isCrawling;
    }

    public void setCrawling(boolean crawling) {
        isCrawling = crawling;
    }

    public void setFoodData(FoodData foodData) {
        this.freezedFoodLevel = foodData.getFoodLevel();
        this.freezedExhaustionLevel = foodData.getExhaustionLevel();
        this.freezedSaturationLevel = foodData.getSaturationLevel();
    }

    public void updateFoodData(FoodData foodData) {
        if (freezedFoodLevel == null) {
            setFoodData(foodData);
        }
    }

    public Float getFreezedSaturationLevel() {
        return freezedSaturationLevel;
    }

    public Float getFreezedExhaustionLevel() {
        return freezedExhaustionLevel;
    }

    public Integer getFreezedFoodLevel() {
        return freezedFoodLevel;
    }
}