package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.ServerPlayerData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
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
import static ccr4ft3r.appetite.util.PlayerUtil.*;
import static net.minecraft.tags.BlockTags.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public class ExhaustionHandler {

    public static final Map<Class<?>, BooleanSupplier> INCLUDE_EVENT_PER_CLASS = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
        if (cannotBeExhausted(event.getPlayer()) || event.isCanceled()
            || !INCLUDE_EVENT_PER_CLASS.getOrDefault(event.getClass(), () -> true).getAsBoolean())
            return;

        BlockState blockState = event.getState();
        exhaust(event.getPlayer(), getProfile().enableForShovelMineables, blockState.is(MINEABLE_WITH_SHOVEL), getProfile().afterBreakingShovelMineables, 1L, 0.005f);
        exhaust(event.getPlayer(), getProfile().enableForAxeMineables, blockState.is(MINEABLE_WITH_AXE), getProfile().afterBreakingAxeMineables, 1L, 0.005f);
        exhaust(event.getPlayer(), getProfile().enableForPickaxeMineables, blockState.is(MINEABLE_WITH_PICKAXE), getProfile().afterBreakingPickaxeMineables, 1L, 0.005f);
    }

    @SubscribeEvent
    public static void onArrowShooted(ArrowLooseEvent event) {
        exhaust(event.getPlayer(), getProfile().enableForShootingArrows, true, getProfile().afterShootingArrows, 1L, 0);
    }

    @SubscribeEvent
    public static void onCrawling(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (shouldSkipTick(event, player))
            return;

        boolean hasCrawlPos = !player.isInLava() && !player.isInWater() && player.getPose() == Pose.SWIMMING;
        ServerPlayerData playerData = getPlayerData(player);
        boolean isCrawling = hasCrawlPos && playerData.isMoving() && !player.position().equals(playerData.getLastPosition());
        ServerData.getPlayerData(player).setCrawling(isCrawling);
        exhaust(event.player, getProfile().enableCrawling, isCrawling, getProfile().afterCrawling, 20, 0);
    }

    @SubscribeEvent
    public static void onPlayerModifiyBlock(BlockEvent.BlockToolModificationEvent event) {
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
        if (!(event.getEntity() instanceof Player player) || event.isCanceled())
            return;
        exhaust(player, getProfile().enableJumping, !player.isInWater() && !player.onClimbable(), getProfile().afterJumping, 1, 0.05f);
    }

    @SubscribeEvent
    public static void onPlayerFishedItem(ItemFishedEvent event) {
        if (event.isCanceled())
            return;
        exhaust(event.getPlayer(), getProfile().enableForFishing, !event.getDrops().isEmpty(), getProfile().afterFishing, 1, 0);
    }

    @SubscribeEvent
    public static void onPlayerBlocks(ShieldBlockEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.isCanceled())
            return;
        exhaust(player, getProfile().enableForBlocking, true, getProfile().afterBlocking, 1, 0);
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (event.isCanceled())
            return;
        exhaust(event.getPlayer(), getProfile().enableForAttacking, true, getProfile().afterAttacking, 1, 0.1f);
    }

    @SubscribeEvent
    public static void onPlayerPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.isCanceled())
            return;
        if (player.getMainHandItem().getItem() instanceof DiggerItem)
            return;
        exhaust(player, getProfile().enableForPlacingBlocks, true, getProfile().afterPlacingBlocks, 1, 0);
    }

    @SubscribeEvent
    public static void onPlayerHurted(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.getAmount() == 0 || event.isCanceled())
            return;
        exhaust(player, getProfile().enableForTakingDamage, true, getProfile().afterTakingDamage, 1, 0.1f);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.tickCount % 20 != 0 || event.phase != TickEvent.Phase.END)
            return;
        if (cannotBeExhausted(player))
            return;

        ServerPlayerData playerData = getPlayerData(player);
        boolean isMoving = playerData.isMoving() && !player.position().equals(playerData.getLastPosition());
        boolean isUpward = playerData.getLastPosition() != null && player.position().y > playerData.getLastPosition().y;
        playerData.setLastPosition(player.position());

        if (player.isFreezing())
            exhaust(player, getProfile().enableFreezing, true, getProfile().afterFreezing, 20, 0);
        else
            exhaust(player, getProfile().enableResting, !isMoving, getProfile().afterResting, 20, 0);

        if (!isMoving || playerData.isCrawling() || playerData.isParagliding() || playerData.isPullingUp())
            return;

        boolean isInVehicle = player.getVehicle() != null;
        boolean isClimbing = player.onClimbable();
        boolean isPaddling = isInVehicle && player.getVehicle() instanceof Boat;
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