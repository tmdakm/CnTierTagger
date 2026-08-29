package win.huangyu.cntiertagger.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Optional;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import win.huangyu.cntiertagger.ConfigManager;
import win.huangyu.cntiertagger.ModConfig;
import win.huangyu.cntiertagger.PlayerData;
import win.huangyu.cntiertagger.TierManager;
import win.huangyu.cntiertagger.TierMode;
import win.huangyu.cntiertagger.client.CntiertaggerClient;

public class CntierCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, ra) -> dispatcher.register((LiteralArgumentBuilder)ClientCommands.literal((String)"cntier").then(ClientCommands.argument((String)"player", (ArgumentType)StringArgumentType.string()).executes(context -> {
            String player = (String)context.getArgument("player", String.class);
            Optional<String> key = CntiertaggerClient.tierManager.playerMap.keySet().stream().filter(str -> str.equalsIgnoreCase(player)).findFirst();
            PlayerData pd = null;
            if (key.isPresent()) {
                pd = CntiertaggerClient.tierManager.playerMap.get(key.get());
            }
            if (pd == null) {
                ((FabricClientCommandSource)context.getSource()).sendError((Component)Component.literal((String)"\u8be5\u73a9\u5bb6\u6ca1\u6709Tier\uff01"));
                return 1;
            }
            MutableComponent res = Component.literal((String)String.format("\u00a7e========%s's Tier========\n", key.get()));
            ModConfig config = ConfigManager.getConfig();
            for (String modeStr : pd.modeTiers.keySet()) {
                if (pd.modeTiers.getOrDefault(modeStr, null) == null) continue;
                TierMode mode = TierManager.MODES.get(modeStr);
                MutableComponent modeText = Component.literal((String)(mode.emoji + " " + modeStr + " \u00a77: "));
                String tierString = TierManager.processTierString(pd.modeTiers.get(modeStr));
                boolean retired = tierString.startsWith("R");
                MutableComponent tierText = Component.literal((String)tierString.replace("R", ""));
                if (!retired) {
                    tierColor = config.getColorForTier(tierString);
                    tierText = tierText.withStyle(s -> s.withColor(tierColor));
                } else {
                    tierColor = config.getColorForTier(tierString.replace("R", ""));
                    MutableComponent retiredText = Component.literal((String)"R").withStyle(s -> s.withColor(config.getColorForTier("R")));
                    tierText = retiredText.append((Component)tierText.withStyle(s -> s.withColor(tierColor))).copy();
                }
                res.append((Component)modeText).append((Component)tierText).append("\n");
            }
            ((FabricClientCommandSource)context.getSource()).sendFeedback((Component)res);
            return 1;
        }))));
    }
}

