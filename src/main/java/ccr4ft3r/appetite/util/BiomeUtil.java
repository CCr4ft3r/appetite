package ccr4ft3r.appetite.util;

import net.minecraft.entity.Entity;

public class BiomeUtil {

    public static boolean isCold(Entity entity) {
        return entity.level.getBiome(entity.blockPosition()).getBaseTemperature() <= 0.05;
    }

    public static boolean isHot(Entity entity) {
        return entity.level.getBiome(entity.blockPosition()).getBaseTemperature() >= 2;
    }
}