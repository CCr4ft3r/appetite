package ccr4ft3r.appetite.registry;

import ccr4ft3r.appetite.ModConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static final TagKey<Item> IS_FROZEN_FOOD = ItemTags.create(new ResourceLocation(ModConstants.MOD_ID, "is_frozen_food"));
}