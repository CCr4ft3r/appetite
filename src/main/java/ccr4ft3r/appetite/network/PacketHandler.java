package ccr4ft3r.appetite.network;

import ccr4ft3r.appetite.IFoodData;
import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.data.ServerData;
import ccr4ft3r.appetite.data.capabilities.HungerLevelingCapability;
import ccr4ft3r.appetite.events.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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

    public static void sendToPlayer(ClientboundCapabilityPacket packet, ServerPlayerEntity player) {
        SIMPLE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    private static void handle(ServerboundPacket packet, Supplier<NetworkEvent.Context> ctx) {
        final NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null) {
                return;
            }
            switch (packet.getAction()) {
                case PLAYER_MOVING:
                    ServerData.getPlayerData(sender).setMoving(true);
                    break;
                case PLAYER_STOP_MOVING:
                    ServerData.getPlayerData(sender).setMoving(false);
                    break;
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
            FoodStats foodData = Minecraft.getInstance().player.getFoodData();
            if (foodData instanceof IFoodData)
                ((IFoodData) foodData).setFoodbarMax(cap.getCurrentFoodMaximum());
            context.setPacketHandled(true);
        });
    }
}