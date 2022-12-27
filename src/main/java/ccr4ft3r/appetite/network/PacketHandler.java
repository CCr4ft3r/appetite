package ccr4ft3r.appetite.network;

import ccr4ft3r.appetite.IFoodData;
import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.capabilities.HungerLevelingCapability;
import ccr4ft3r.appetite.events.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Supplier;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1.0.0";
    private static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry
        .newSimpleChannel(new ResourceLocation(ModConstants.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerMessages() {
        SIMPLE_CHANNEL.registerMessage(0, ServerboundPacket.class, ServerboundPacket::encodeOnClientSide, ServerboundPacket::new, PacketHandler::handle);
        SIMPLE_CHANNEL.registerMessage(1, ClientboundCapabilityPacket.class, ClientboundCapabilityPacket::encode, ClientboundCapabilityPacket::new, PacketHandler::handle,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendToServer(ServerboundPacket packet) {
        SIMPLE_CHANNEL.sendToServer(packet);
    }

    public static void sendToPlayer(ClientboundCapabilityPacket packet, ServerPlayer player) {
        SIMPLE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    private static void handle(ServerboundPacket packet, Supplier<NetworkEvent.Context> ctx) {
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

    public static void handle(ClientboundCapabilityPacket packet, Supplier<NetworkEvent.Context> ctx) {
        final NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            HungerLevelingCapability cap = new HungerLevelingCapability();
            cap.deserializeNBT(packet.getCapData());
            ClientHandler.PLAYER_DATA.setHungerbarMaximum(cap.getCurrentFoodMaximum());
            FoodData foodData = Minecraft.getInstance().player.getFoodData();
            if (foodData instanceof IFoodData iFoodData)
                iFoodData.setFoodbarMax(cap.getCurrentFoodMaximum());
            context.setPacketHandled(true);
        });
    }
}