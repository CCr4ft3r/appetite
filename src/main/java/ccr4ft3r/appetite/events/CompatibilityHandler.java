package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.ServerPlayerData;
import com.yyon.grapplinghook.server.ServerControllerManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tictim.paraglider.capabilities.Caps;
import tictim.paraglider.capabilities.PlayerMovement;
import tschipp.carryon.common.handler.PickupHandler;

import static ccr4ft3r.appetite.config.ProfileConfig.*;
import static ccr4ft3r.appetite.data.ServerData.*;
import static ccr4ft3r.appetite.util.PlayerUtil.*;

public class CompatibilityHandler {
    @SubscribeEvent
    public static void onParagliding(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (shouldSkipTick(event, player))
            return;

        LazyOptional<PlayerMovement> movement = player.getCapability(Caps.playerMovement);
        boolean isParagliding = movement.map(PlayerMovement::isParagliding).orElse(false);
        ServerData.getPlayerData(player).setParagliding(isParagliding);
        exhaust(event.player, getProfile().enableParagliding, isParagliding, getProfile().afterParagliding, 20, 0);
    }

    @SubscribeEvent
    public static void onPullingUp(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (shouldSkipTick(event, player))
            return;

        ServerPlayerData playerData = getPlayerData(player);
        boolean isUpward = playerData.isMoving() && playerData.getLastPosition() != null && player.position().y > playerData.getLastPosition().y;
        boolean isPullingUp = isUpward && ServerControllerManager.attached.contains(player.getId());
        ServerData.getPlayerData(player).setPullingUp(isPullingUp);
        exhaust(player, getProfile().enablePullingUp, isPullingUp, getProfile().afterPullingUp, 20, 0);
    }

    @SubscribeEvent
    public static void onPickingUp(PickupHandler.PickUpBlockEvent event) {
        if (event.getPlayer().getLevel().isClientSide())
            return;
        getPlayerData(event.getPlayer()).setCarrying(true);
    }

    @SubscribeEvent
    public static void onPickingUpEntity(PickupHandler.PickUpEntityEvent event) {
        if (event.player.getLevel().isClientSide())
            return;
        getPlayerData(event.player).setCarrying(true);
    }

    @SubscribeEvent
    public static void onPlacingEntity(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player) || player.getLevel().isClientSide())
            return;
        getPlayerData((Player) event.getEntity()).setCarrying(false);
    }
}