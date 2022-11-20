package ccr4ft3r.appetite.data;

import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerData {

    private static final Map<UUID, AdditionalPlayerData> DATA_PER_PLAYER = new ConcurrentHashMap<>();

    public static AdditionalPlayerData getPlayerData(Player player) {
        AdditionalPlayerData additionalPlayerData = DATA_PER_PLAYER.get(player.getUUID());
        if (additionalPlayerData == null) {
            additionalPlayerData = new AdditionalPlayerData();
            DATA_PER_PLAYER.put(player.getUUID(), additionalPlayerData);
        }
        return additionalPlayerData;
    }
}