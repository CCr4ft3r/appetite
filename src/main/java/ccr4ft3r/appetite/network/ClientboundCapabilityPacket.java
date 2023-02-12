package ccr4ft3r.appetite.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;

public class ClientboundCapabilityPacket {

    public CompoundNBT getCapData() {
        return capData;
    }

    private final CompoundNBT capData;

    public ClientboundCapabilityPacket(INBTSerializable<CompoundNBT> serializable) {
        this.capData = serializable.serializeNBT();
    }

    public ClientboundCapabilityPacket(PacketBuffer friendlyByteBuf) {
        this.capData = friendlyByteBuf.readNbt();
    }

    public static void encode(final ClientboundCapabilityPacket msg, final PacketBuffer packetBuffer) {
         packetBuffer.writeNbt(msg.capData);
    }
}