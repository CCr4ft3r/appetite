package ccr4ft3r.appetite.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.AxeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.spongepowered.asm.util.ObfuscationUtil;

import java.util.Set;

public class BlockUtil {

    private static final Set<Block> SHOVEL_DIGGABLES = ObfuscationReflectionHelper.getPrivateValue(ShovelItem.class, null, "field_150916_c");
    private static final Set<Material> AXE_MATERIALS = ObfuscationReflectionHelper.getPrivateValue(AxeItem.class, null, "field_234662_c_");
    private static final Set<Block> AXE_DIGGABLES = ObfuscationReflectionHelper.getPrivateValue(AxeItem.class, null, "field_150917_d_");
    private static final Set<Block> PICKAXE_DIGGABLES = ObfuscationReflectionHelper.getPrivateValue(PickaxeItem.class, null, "field_150915_c");

    public static boolean isShovelMineable(BlockState state) {
        return state.is(BlockTags.SAND) || state.is(Tags.Blocks.DIRT) || state.is(Tags.Blocks.GRAVEL) || SHOVEL_DIGGABLES.contains(state.getBlock());
    }

    public static boolean isAxeMineable(BlockState state) {
        return state.is(BlockTags.LOGS) || AXE_MATERIALS.contains(state.getMaterial()) || AXE_DIGGABLES.contains(state.getBlock());
    }

    public static boolean isPickaxeMineable(BlockState state) {
        return state.is(Tags.Blocks.STONE) || state.is(Tags.Blocks.ORES) || PICKAXE_DIGGABLES.contains(state.getBlock());
    }
}