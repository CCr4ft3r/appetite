package ccr4ft3r.appetite.registry;

import ccr4ft3r.appetite.ModConstants;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModTabs {

    public static final CreativeModeTab MOD_TAB = new CreativeModeTab(-1, ModConstants.MOD_ID) {
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.APPETITE.get());
        }
    };
}