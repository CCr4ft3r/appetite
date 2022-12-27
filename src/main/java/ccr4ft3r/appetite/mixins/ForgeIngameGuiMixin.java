package ccr4ft3r.appetite.mixins;

import ccr4ft3r.appetite.events.ClientHandler;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ForgeIngameGui.class)
public class ForgeIngameGuiMixin {

    @ModifyConstant(method = "renderFood", constant = @Constant(intValue = 10, ordinal = 1), remap = false)
    public int getNewFoodLevel(int i) {
        return ClientHandler.PLAYER_DATA.getHungerbarMaximum();
    }
}