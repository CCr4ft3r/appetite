package ccr4ft3r.appetite.data;

import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerData {

    private static final Map<UUID, ServerPlayerData> DATA_PER_PLAYER = new ConcurrentHashMap<>();

    public static ServerPlayerData getPlayerData(Player player) {
        ServerPlayerData serverPlayerData = DATA_PER_PLAYER.get(player.getUUID());
        if (serverPlayerData == null) {
            serverPlayerData = new ServerPlayerData();
            DATA_PER_PLAYER.put(player.getUUID(), serverPlayerData);
        }
        return serverPlayerData;
    }
}