package ccr4ft3r.appetite.network;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.data.ServerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1.0.0";
    private static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry
        .newSimpleChannel(new ResourceLocation(ModConstants.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerMessages() {
        SIMPLE_CHANNEL.registerMessage(0, ServerboundPacket.class, ServerboundPacket::encodeOnClientSide, ServerboundPacket::new, PacketHandler::handleOnServerSide);
    }

    public static void sendToServer(ServerboundPacket packet) {
        SIMPLE_CHANNEL.sendToServer(packet);
    }

    public static void handleOnServerSide(ServerboundPacket packet, Supplier<NetworkEvent.Context> ctx) {
        final NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            final ServerPlayer sender = context.getSender();
            if (sender == null) {
                return;
            }
            switch (packet.getAction()) {
                case PLAYER_MOVING -> ServerData.getPlayerData(sender).setMoving(true);
                case PLAYER_STOP_MOVING -> ServerData.getPlayerData(sender).setMoving(false);
            }
            context.setPacketHandled(true);
        });
    }
}