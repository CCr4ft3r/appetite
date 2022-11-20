package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.network.PacketHandler;
import ccr4ft3r.appetite.network.ServerboundPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;

import static ccr4ft3r.appetite.config.MainConfig.*;
import static ccr4ft3r.appetite.network.ServerboundPacket.Action.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, value = Dist.CLIENT)
public class MovementHandler {

    private static final AtomicBoolean IS_PLAYER_MOVING = new AtomicBoolean();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        boolean isPressed = event.getAction() == GLFW.GLFW_PRESS;
        if (isPressed || event.getAction() == GLFW.GLFW_RELEASE) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null)
                return;

            Options options = Minecraft.getInstance().options;
            int key = event.getKey();
            boolean isMovingKey = key == options.keyUp.getKey().getValue()
                || key == options.keyDown.getKey().getValue()
                || key == options.keyRight.getKey().getValue()
                || key == options.keyLeft.getKey().getValue()
                || key == options.keyJump.getKey().getValue() && (player.isInWater() || player.onClimbable() || IS_PLAYER_MOVING.get());

            if (!isMovingKey)
                return;

            boolean isActivelyMoving = options.keyUp.isDown() || options.keyDown.isDown() || options.keyLeft.isDown() || options.keyRight.isDown()
                || options.keyJump.isDown() && (player.isInWater() || player.onClimbable());

            if (isActivelyMoving != IS_PLAYER_MOVING.get()) {
                IS_PLAYER_MOVING.set(isActivelyMoving);
                if (CONFIG_DATA.enableExtendedLogging.get())
                    LogUtils.getLogger().info("Sending packet to server caused by " + (isPressed ? "pressing {}" : "releasing {}")
                        , GLFW.glfwGetKeyName(event.getKey(), event.getScanCode()));
                PacketHandler.sendToServer(new ServerboundPacket(isActivelyMoving ? PLAYER_MOVING : PLAYER_STOP_MOVING));
            }
        }
    }
}