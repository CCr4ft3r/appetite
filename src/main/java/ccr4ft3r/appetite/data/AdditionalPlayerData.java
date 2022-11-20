package ccr4ft3r.appetite.data;

import net.minecraft.world.phys.Vec3;

public class AdditionalPlayerData {

    private Vec3 lastPosition;
    private boolean isMoving;

    public Vec3 getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Vec3 lastPosition) {
        this.lastPosition = lastPosition;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

}