package ccr4ft3r.appetite.registry;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class ModTabs {

    public static final Consumer<CreativeModeTab.Builder> MOD_TAB_BUILDER_CONSUMER =
        (b) -> b
            .title(MutableComponent.create(new TranslatableContents("itemGroup.appetite")))
            .icon(() -> new ItemStack(ModItems.APPETITE.get()))
            .displayItems((p_260072_, p_259069_, p_259064_) -> {
                p_259069_.accept(ModItems.FROZEN_COD.get());
                p_259069_.accept(ModItems.FROZEN_SALMON.get());
                p_259069_.accept(ModItems.FROZEN_TROPICAL_FISH.get());
            });
}