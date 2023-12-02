package ccr4ft3r.appetite.items;

import ccr4ft3r.appetite.registry.ModTabs;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class FrozenFoodItem extends Item {

    private final Item unfrozenItem;

    public FrozenFoodItem(Item unfrozenItem) {
        super(new Item.Properties().tab(ModTabs.MOD_TAB).food(new FoodProperties.Builder().alwaysEat().build()));
        this.unfrozenItem = unfrozenItem;
    }

    public Item getUnfrozenItem() {
        return unfrozenItem;
    }
}