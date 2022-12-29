package ccr4ft3r.appetite.data;

import ccr4ft3r.appetite.ModConstants;

public class ClientPlayerData {

    private boolean isMoving;

    private int hungerbarMaximum = ModConstants.VANILLA_MAX_FOOD_LEVEL / 2;

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public int getHungerbarMaximum() {
        return hungerbarMaximum;
    }

    public void setHungerbarMaximum(int hungerbarMaximum) {
        this.hungerbarMaximum = hungerbarMaximum;
    }
}