package ccr4ft3r.appetite.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.INBTSerializable;

public class ClientboundCapabilityPacket {

    public CompoundTag getCapData() {
        return capData;
    }

    private final CompoundTag capData;

    public ClientboundCapabilityPacket(INBTSerializable<CompoundTag> serializable) {
        this.capData = serializable.serializeNBT();
    }

    public ClientboundCapabilityPacket(FriendlyByteBuf friendlyByteBuf) {
        this.capData = friendlyByteBuf.readNbt();
    }

    public static void encode(final ClientboundCapabilityPacket msg, final FriendlyByteBuf packetBuffer) {
         packetBuffer.writeNbt(msg.capData);
    }
}