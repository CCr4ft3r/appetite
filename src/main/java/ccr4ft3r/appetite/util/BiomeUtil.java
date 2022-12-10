package ccr4ft3r.appetite.util;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.Tags;

public class BiomeUtil {

    public static boolean isCold(Entity entity) {
        return entity.getLevel().getBiome(entity.blockPosition()).is(Tags.Biomes.IS_COLD);
    }

    public static boolean isHot(Entity entity) {
        return entity.getLevel().getBiome(entity.blockPosition()).is(Tags.Biomes.IS_HOT);
    }
}