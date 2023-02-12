package ccr4ft3r.appetite.registry;

import ccr4ft3r.appetite.ModConstants;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.antlr.v4.runtime.misc.NotNull;

public class ModTabs {

    public static final ItemGroup MOD_TAB = new ItemGroup(-1, ModConstants.MOD_ID) {
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.APPETITE.get());
        }
    };
}