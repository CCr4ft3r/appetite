package ccr4ft3r.appetite.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface PlayerAccessor {

    @Accessor("foodData")
    FoodData getFoodData();

    @Accessor("foodData")
    void setFoodData(FoodData foodData);
}