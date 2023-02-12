package ccr4ft3r.appetite.registry;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.items.FrozenFoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.MOD_ID);

    public static final Map<Item, RegistryObject<FrozenFoodItem>> FROZEN_FOOD_PER_FOOD_ITEM = new ConcurrentHashMap<>();

    public static final RegistryObject<FrozenFoodItem> FROZEN_COD = registerFrozenFoodItem(Items.COD, "frozen_cod");
    public static final RegistryObject<FrozenFoodItem> FROZEN_SALMON = registerFrozenFoodItem(Items.SALMON, "frozen_salmon");
    public static final RegistryObject<FrozenFoodItem> FROZEN_TROPICAL_FISH = registerFrozenFoodItem(Items.TROPICAL_FISH, "frozen_tropical_fish");
    public static final RegistryObject<Item> APPETITE = ITEMS.register("appetite", () -> new Item(new Item.Properties()));

    private static RegistryObject<FrozenFoodItem> registerFrozenFoodItem(Item unfrozenItem, String name) {
        RegistryObject<FrozenFoodItem> registryObject = ITEMS.register(name, () -> new FrozenFoodItem(unfrozenItem));
        FROZEN_FOOD_PER_FOOD_ITEM.put(unfrozenItem, registryObject);
        return registryObject;
    }

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}