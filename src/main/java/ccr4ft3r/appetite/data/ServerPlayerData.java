package ccr4ft3r.appetite.data;

import net.minecraft.world.phys.Vec3;

public class ServerPlayerData {

    private Vec3 lastPosition;
    private boolean isMoving;
    private boolean startedMoving;

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

}