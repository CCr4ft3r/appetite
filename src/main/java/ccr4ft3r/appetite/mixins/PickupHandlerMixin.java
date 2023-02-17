package ccr4ft3r.appetite.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;
import java.util.function.Function;

import static ccr4ft3r.appetite.data.ServerData.*;

@Mixin(targets = "tschipp.carryon.common.carry.PickupHandler")
public class PickupHandlerMixin {

    @Inject(method = "tryPickUpBlock", at = @At("RETURN"), remap = false)
    private static void tryPickUpBlockInjected(ServerPlayer player, BlockPos pos, Level level, BiFunction<BlockState, BlockPos, Boolean> pickupCallback, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue())
            getPlayerData(player).setCarrying(true);
    }

    @Inject(method = "tryPickupEntity", at = @At("RETURN"), remap = false)
    private static void tryPickupEntityInjected(ServerPlayer player, Entity entity, Function<Entity, Boolean> pickupCallback, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue())
            getPlayerData(player).setCarrying(true);
    }
}