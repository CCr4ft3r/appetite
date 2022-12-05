package ccr4ft3r.appetite.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegisterEvent event) {
        if (event.getVanillaRegistry() != null && !event.getVanillaRegistry().key().registry().equals(
            new ResourceLocation("minecraft:item")))
            return;
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