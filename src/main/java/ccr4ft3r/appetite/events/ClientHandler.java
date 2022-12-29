package ccr4ft3r.appetite.events;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.data.ClientPlayerData;
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

import java.util.function.Predicate;

import static ccr4ft3r.appetite.config.MainConfig.*;
import static ccr4ft3r.appetite.network.ServerboundPacket.Action.*;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, value = Dist.CLIENT)
public class ClientHandler {

    public static final ClientPlayerData PLAYER_DATA = new ClientPlayerData();

    private static final Predicate<LocalPlayer> NOT_JUMPABLE = (player) -> player.isInWater() || player.onClimbable();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
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
                || key == options.keyJump.getKey().getValue() && (NOT_JUMPABLE.test(player) || PLAYER_DATA.isMoving());

            if (!isMovingKey)
                return;

            boolean isActivelyMoving = options.keyUp.isDown() || options.keyDown.isDown() || options.keyLeft.isDown() || options.keyRight.isDown()
                || options.keyJump.isDown() && NOT_JUMPABLE.test(player);

            if (isActivelyMoving != PLAYER_DATA.isMoving()) {
                PLAYER_DATA.setMoving(isActivelyMoving);
                if (CONFIG_DATA.enableExtendedLogging.get())
                    LogUtils.getLogger().info("Sending packet to server caused by {} {}", isPressed ? "pressing" : "releasing"
                        , GLFW.glfwGetKeyName(event.getKey(), event.getScanCode()));
                PacketHandler.sendToServer(new ServerboundPacket(isActivelyMoving ? PLAYER_MOVING : PLAYER_STOP_MOVING));
            }
        }
    }
}