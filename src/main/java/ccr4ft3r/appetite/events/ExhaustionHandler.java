package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.ServerPlayerData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import static ccr4ft3r.appetite.config.ProfileConfig.*;
import static ccr4ft3r.appetite.data.ServerData.*;
import static ccr4ft3r.appetite.util.BlockUtil.*;
import static ccr4ft3r.appetite.util.PlayerUtil.*;
import static net.minecraft.tags.BlockTags.*;
import static net.minecraftforge.common.Tags.Blocks.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public class ExhaustionHandler {

    public static final Map<Class<?>, BooleanSupplier> INCLUDE_EVENT_PER_CLASS = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
        if (cannotBeExhausted(event.getPlayer()) || event.isCanceled()
            || !INCLUDE_EVENT_PER_CLASS.getOrDefault(event.getClass(), () -> true).getAsBoolean())
            return;

        BlockState state = event.getState();
        exhaust(event.getPlayer(), getProfile().enableForShovelMineables, isShovelMineable(state), getProfile().afterBreakingShovelMineables, 1L, 0.005f);
        exhaust(event.getPlayer(), getProfile().enableForAxeMineables, isAxeMineable(state), getProfile().afterBreakingAxeMineables, 1L, 0.005f);
        exhaust(event.getPlayer(), getProfile().enableForPickaxeMineables, isPickaxeMineable(state), getProfile().afterBreakingPickaxeMineables, 1L, 0.005f);
    }

    @SubscribeEvent
    public static void onArrowShooted(ArrowLooseEvent event) {
        exhaust(event.getPlayer(), getProfile().enableForShootingArrows, true, getProfile().afterShootingArrows, 1L, 0);
    }

    @SubscribeEvent
    public static void onPlayerModifiyBlock(BlockEvent.BlockToolInteractEvent event) {
        if (cannotBeExhausted(event.getPlayer()) || event.isCanceled())
            return;
        exhaust(event.getPlayer(), getProfile().enableForTillingDirt, event.getHeldItemStack().getItem() instanceof HoeItem
            && event.getState().is(DIRT), getProfile().afterTillingDirt, 1L, 0);
        exhaust(event.getPlayer(), getProfile().enableForPathingDirt, event.getHeldItemStack().getItem() instanceof ShovelItem
            && event.getState().is(DIRT), getProfile().afterPathingDirt, 1L, 0);
        exhaust(event.getPlayer(), getProfile().enableForStrippingLogs, event.getHeldItemStack().getItem() instanceof AxeItem
            && event.getState().is(LOGS), getProfile().afterStrippingLogs, 1L, 0);
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (!isPlayerServerside(event.getEntity()) || event.isCanceled())
            return;
        PlayerEntity player = (PlayerEntity) event.getEntity();
        ServerPlayerData playerData = ServerData.getPlayerData(player);
        boolean isMoving = !player.position().equals(playerData.getLastPosition());
        exhaust(player, getProfile().enableJumping, isMoving && !player.isInWater() && !player.onClimbable(), getProfile().afterJumping, 1, 0.05f);
    }

    @SubscribeEvent
    public static void onPlayerFishedItem(ItemFishedEvent event) {
        if (event.isCanceled())
            return;
        exhaust(event.getPlayer(), getProfile().enableForFishing, !event.getDrops().isEmpty(), getProfile().afterFishing, 1, 0);
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (event.isCanceled())
            return;
        exhaust(event.getPlayer(), getProfile().enableForAttacking, true, getProfile().afterAttacking, 1, 0.1f);
    }

    @SubscribeEvent
    public static void onPlayerPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (!isPlayerServerside(event.getEntity()) || event.isCanceled())
            return;
        PlayerEntity player = (PlayerEntity) event.getEntity();
        if (player.getMainHandItem().getItem() instanceof ToolItem)
            return;
        exhaust(player, getProfile().enableForPlacingBlocks, true, getProfile().afterPlacingBlocks, 1, 0);
    }

    @SubscribeEvent
    public static void onPlayerHurted(LivingHurtEvent event) {
        if (!isPlayerServerside(event.getEntity()) || event.getAmount() == 0 || event.isCanceled())
            return;
        exhaust((PlayerEntity) event.getEntity(), getProfile().enableForTakingDamage, true, getProfile().afterTakingDamage, 1, 0.1f);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.tickCount % 20 != 0 || event.phase != TickEvent.Phase.END)
            return;
        if (cannotBeExhausted(player))
            return;

        ServerPlayerData playerData = getPlayerData(player);
        boolean isMoving = playerData.isMoving() && !player.position().equals(playerData.getLastPosition());
        boolean isUpward = playerData.getLastPosition() != null && player.position().y > playerData.getLastPosition().y;
        playerData.setLastPosition(player.position());

        exhaust(player, getProfile().enableResting, !isMoving, getProfile().afterResting, 20, 0);

        if (!isMoving || playerData.isCrawling() || playerData.isParagliding() || playerData.isPullingUp())
            return;

        boolean isInVehicle = player.getVehicle() != null;
        boolean isClimbing = player.onClimbable();
        boolean isPaddling = isInVehicle && player.getVehicle() instanceof BoatEntity;
        boolean isSwimming = player.isInWater() && !isInVehicle && !isClimbing;
        boolean isSneaking = player.isCrouching() && !isClimbing;
        boolean isSprinting = player.isSprinting() && !isSwimming && !isInVehicle && !isSneaking && !isClimbing;
        boolean isWalking = !isSwimming && !isInVehicle && !isSneaking && !isSprinting && !isClimbing;
        boolean isWalkingUp = isWalking && isUpward;

        exhaust(player, getProfile().enableSneaking, isSneaking, getProfile().afterSneaking, 20, 0);
        exhaust(player, getProfile().enableWalking, isWalking && !isWalkingUp, getProfile().afterWalking, 20, 0);
        exhaust(player, getProfile().enableWalkingUp, isWalkingUp, getProfile().afterWalkingUp, 20, 0);
        exhaust(player, getProfile().enableSwimming, isSwimming, getProfile().afterSwimming, 20, 0.01f);
        exhaust(player, getProfile().enableSprinting, isSprinting, getProfile().afterSprinting, 20, 4 / 7f);
        exhaust(player, getProfile().enablePaddling, isPaddling, getProfile().afterPaddling, 20, 0);
        exhaust(player, getProfile().enableClimbing, isClimbing, getProfile().afterClimbing, 20, 0);
    }
}