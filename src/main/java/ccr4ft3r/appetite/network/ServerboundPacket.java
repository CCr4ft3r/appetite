package ccr4ft3r.appetite.network;

import net.minecraft.network.PacketBuffer;

public class ServerboundPacket {

    private final Action action;

    public ServerboundPacket(Action action) {
        this.action = action;
    }

    public ServerboundPacket(PacketBuffer packetBuffer) {
        this.action = packetBuffer.readEnum(Action.class);
    }

    public void encodeOnClientSide(PacketBuffer packetBuffer) {
        packetBuffer.writeEnum(this.action);
    }

    public Action getAction() {
        return this.action;
    }

    public enum Action {
        PLAYER_MOVING,
        PLAYER_STOP_MOVING
    }
}