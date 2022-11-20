package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.data.AdditionalPlayerData;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static ccr4ft3r.appetite.config.MainConfig.*;
import static ccr4ft3r.appetite.config.ProfileConfig.*;
import static ccr4ft3r.appetite.data.ServerData.*;
import static net.minecraft.tags.BlockTags.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public class ExhaustionHandler {

    @SubscribeEvent
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
        if (cannotBeExhausted(event.getPlayer()))
            return;

        BlockState blockState = event.getState();
        exhaust(event.getPlayer(), getProfile().enableForShovelMineables, blockState.is(MINEABLE_WITH_SHOVEL), getProfile().afterBreakingShovelMineables, 1L, 0.005f);
        exhaust(event.getPlayer(), getProfile().enableForAxeMineables, blockState.is(MINEABLE_WITH_AXE), getProfile().afterBreakingAxeMineables, 1L, 0.005f);
        exhaust(event.getPlayer(), getProfile().enableForPickaxeMineables, blockState.is(MINEABLE_WITH_PICKAXE), getProfile().afterBreakingPickaxeMineables, 1L, 0.005f);
        exhaust(event.getPlayer(), getProfile().enableForPickaxeMineables, blockState.is(MINEABLE_WITH_PICKAXE), getProfile().afterBreakingPickaxeMineables, 1L, 0.005f);
    }

    @SubscribeEvent
    public static void onPlayerUseHoe(BlockEvent.BlockToolModificationEvent event) {
        if (cannotBeExhausted(event.getPlayer()))
            return;
        exhaust(event.getPlayer(), getProfile().enableForUsingHoe, event.getHeldItemStack().getItem() instanceof HoeItem
            && event.getState().is(DIRT), getProfile().afterUsingHoe, 1L, 0);
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        exhaust(player, getProfile().enableJumping, !player.isInWater() && !player.onClimbable(), getProfile().afterJumping, 1, 0.05f);
    }

    @SubscribeEvent
    public static void onPlayerFishedItem(ItemFishedEvent event) {
        exhaust(event.getPlayer(), getProfile().enableForFishing, !event.getDrops().isEmpty(), getProfile().afterFishing, 1, 0);
    }

    @SubscribeEvent
    public static void onPlayerBlocks(ShieldBlockEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        exhaust(player, getProfile().enableForBlocking, true, getProfile().afterBlocking, 1, 0);
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        exhaust(event.getPlayer(), getProfile().enableForAttacking, true, getProfile().afterAttacking, 1, 0.1f);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.tickCount % 20 != 0 || event.phase != TickEvent.Phase.END)
            return;
        if (cannotBeExhausted(player))
            return;

        AdditionalPlayerData playerData = getPlayerData(player);
        boolean isMoving = playerData.isMoving() && !player.position().equals(playerData.getLastPosition());
        playerData.setLastPosition(player.position());

        if (player.isFreezing())
            exhaust(player, getProfile().enableFreezing, true, getProfile().whileFreezing, 20, 0);
        else
            exhaust(player, getProfile().enableResting, !isMoving, getProfile().afterResting, 20, 0);

        if (!playerData.isMoving())
            return;

        boolean isInVehicle = player.getVehicle() != null;
        boolean isClimbing = player.onClimbable() && isMoving;
        boolean isPaddling = isMoving && isInVehicle && player.getVehicle() instanceof Boat;
        boolean isSwimming = isMoving && player.isInWater() && !isInVehicle && !isClimbing;
        boolean isSneaking = isMoving && player.isCrouching() && !isClimbing;
        boolean isSprinting = isMoving && player.isSprinting() && !isSwimming && !isInVehicle && !isSneaking && !isClimbing;
        boolean isWalking = isMoving && !isSwimming && !isInVehicle && !isSneaking && !isSprinting && !isClimbing;

        exhaust(player, getProfile().enableSneaking, isSneaking, getProfile().afterSneaking, 20, 0);
        exhaust(player, getProfile().enableWalking, isWalking, getProfile().afterWalking, 20, 0);
        exhaust(player, getProfile().enableSwimming, isSwimming, getProfile().afterSwimming, 20, 0.01f);
        exhaust(player, getProfile().enableSprinting, isSprinting, getProfile().afterSprinting, 20, 4 / 7f);
        exhaust(player, getProfile().enablePaddling, isPaddling, getProfile().afterPaddling, 20, 0);
        exhaust(player, getProfile().enableClimbing, isClimbing, getProfile().afterClimbing, 20, 0);
    }

    private static void exhaust(Player player, ForgeConfigSpec.BooleanValue optionEnabled, boolean onlyIf, ForgeConfigSpec.IntValue exhaustionAfter, long multiplier,
                                float diff) {
        if (cannotBeExhausted(player))
            return;

        if (optionEnabled.get() && onlyIf) {
            float exhaustion = Math.max(8f * multiplier / (float) exhaustionAfter.get() - diff, 0);
            if (CONFIG_DATA.enableExtendedLogging.get())
                LogUtils.getLogger().info("Add {} exhaustion to player '{}' due to rule '{}'", exhaustion, player.getScoreboardName(),
                    String.join(".", exhaustionAfter.getPath()));
            player.causeFoodExhaustion(exhaustion);
        }
    }

    private static boolean cannotBeExhausted(Player player) {
        return player instanceof FakePlayer || player.getLevel().isClientSide() || !player.isAddedToWorld() ||
            player.isCreative() || player.isSpectator() || player.isSleeping() || !player.isAlive();
    }
}