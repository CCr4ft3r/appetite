package ccr4ft3r.appetite.util;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;

import static ccr4ft3r.appetite.config.MainConfig.*;

public class PlayerUtil {

    public static void exhaust(Player player, ForgeConfigSpec.BooleanValue optionEnabled, boolean onlyIf, ForgeConfigSpec.IntValue exhaustionAfter, long multiplier,
                               float diff) {
        if (cannotBeExhausted(player))
            return;

        if (optionEnabled.get() && onlyIf) {
            float exhaustion = Math.max(8f * multiplier / (float) exhaustionAfter.get() - diff, 0);
            if (CONFIG_DATA.enableExtendedLogging.get())
                LogUtils.getLogger().info("Adding {} exhaustion to player '{}' due to rule '{}'", exhaustion, player.getScoreboardName(),
                    String.join(".", exhaustionAfter.getPath()));
            player.causeFoodExhaustion(exhaustion);
        }
    }

    public static boolean cannotBeExhausted(Player player) {
        return player instanceof FakePlayer || player.getLevel().isClientSide() || !player.isAddedToWorld() ||
            player.isCreative() || player.isSpectator() || player.isSleeping() || !player.isAlive();
    }
}