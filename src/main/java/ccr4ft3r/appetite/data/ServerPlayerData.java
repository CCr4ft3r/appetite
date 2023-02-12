package ccr4ft3r.appetite.data;

import ccr4ft3r.appetite.IFoodData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.vector.Vector3d;

public class ServerPlayerData {

    private Vector3d lastPosition;
    private boolean isMoving;
    private boolean startedMoving;
    private boolean isParagliding;
    private boolean isCrawling;
    private boolean isPullingUp;
    private boolean isCarrying;

    private Integer freezedFoodLevel = null;
    private Float freezedSaturationLevel = null;
    private Float freezedExhaustionLevel = null;

    ServerPlayerData(PlayerEntity player) {
        setFoodData(player.getFoodData());
    }

    public Vector3d getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Vector3d lastPosition) {
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

    public boolean isCarrying() {
        return isCarrying;
    }

    public void setCarrying(boolean isCarrying) {
        this.isCarrying = isCarrying;
    }

    public boolean isCrawling() {
        return isCrawling;
    }

    public void setCrawling(boolean crawling) {
        isCrawling = crawling;
    }

    public void setFoodData(FoodStats foodData) {
        this.freezedFoodLevel = foodData.getFoodLevel();
        this.freezedExhaustionLevel = ((IFoodData) foodData).getExhaustionLevel();
        this.freezedSaturationLevel = foodData.getSaturationLevel();
    }

    public void updateFoodData(FoodStats foodData) {
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

    public boolean isPullingUp() {
        return isPullingUp;
    }

    public void setPullingUp(boolean pullingUp) {
        isPullingUp = pullingUp;
    }
}