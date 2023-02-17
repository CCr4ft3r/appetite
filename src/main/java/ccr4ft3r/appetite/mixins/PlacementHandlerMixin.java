package ccr4ft3r.appetite.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;

import static ccr4ft3r.appetite.data.ServerData.*;

@Mixin(targets = "tschipp.carryon.common.carry.PlacementHandler")
public class PlacementHandlerMixin {

    @Inject(method = "tryPlaceBlock", at = @At("RETURN"), remap = false)
    private static void tryPlaceBlockInjected(ServerPlayer player, BlockPos pos, Direction facing, BiFunction<BlockPos, BlockState, Boolean> placementCallback, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue())
            getPlayerData(player).setCarrying(false);
    }

    @Inject(method = "tryPlaceEntity", at = @At("RETURN"), remap = false)
    private static void tryPlaceEntityInjected(ServerPlayer player, BlockPos pos, Direction facing, BiFunction<Vec3, Entity, Boolean> placementCallback, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue())
            getPlayerData(player).setCarrying(false);
    }
}