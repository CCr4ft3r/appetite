package ccr4ft3r.appetite.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        add(Potions.AWKWARD, new ItemStack(ModItems.FROZEN_COD.get()), ModPotions.FROZEN_APPETITE.get());
        add(Potions.AWKWARD, new ItemStack(ModItems.FROZEN_SALMON.get()), ModPotions.FROZEN_APPETITE.get());
        add(Potions.AWKWARD, new ItemStack(ModItems.FROZEN_TROPICAL_FISH.get()), ModPotions.FROZEN_APPETITE.get());
        addPotionTransforms(ModPotions.FROZEN_APPETITE.get());
    }

    private static void add(Potion input, ItemStack ingredient, Potion output) {
        add(new ItemStack(Items.POTION), input, ingredient, new ItemStack(Items.POTION), output);
        add(new ItemStack(Items.SPLASH_POTION), input, ingredient, new ItemStack(Items.SPLASH_POTION), output);
        add(new ItemStack(Items.LINGERING_POTION), input, ingredient, new ItemStack(Items.LINGERING_POTION), output);
    }

    private static void addPotionTransforms(Potion potion) {
        add(new ItemStack(Items.POTION), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);
        add(new ItemStack(Items.LINGERING_POTION), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);
        add(new ItemStack(Items.POTION), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);
        add(new ItemStack(Items.SPLASH_POTION), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);
    }

    private static void add(ItemStack inBottle, Potion inPotion, ItemStack ingredient, ItemStack outBottle, Potion outPotion) {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(inBottle, inPotion)), Ingredient.of(ingredient), PotionUtils.setPotion(outBottle, outPotion));
    }
}