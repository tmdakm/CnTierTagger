package win.huangyu.cntiertagger.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import win.huangyu.cntiertagger.PlayerData;
import win.huangyu.cntiertagger.TierManager;
import win.huangyu.cntiertagger.client.CntiertaggerClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CntierCommand {
    public static void register(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, ra) -> {
            dispatcher.register(
                    literal("cntier")
                            .then(argument("player", StringArgumentType.string())
                                    .executes((context) -> {
                                        var player = context.getArgument("player", String.class);
                                        PlayerData pd = CntiertaggerClient.tierManager.playerMap.getOrDefault(player, null);
                                        if(pd == null){
                                            context.getSource().sendError(Text.literal("该玩家没有Tier！"));
                                            return 1;
                                        }

                                        MutableText res = MutableText.of(Text.of(String.format("§e========%s's Tier========\n", player)).getContent());
                                        for(var modeStr : pd.modeTiers.keySet()){
                                            if (pd.modeTiers.getOrDefault(modeStr, null) == null) continue;
                                            var mode = TierManager.MODES.get(modeStr);
                                            var modeText = Text.of(mode.emoji + " " + modeStr + " §7: ").copy();
                                            var tierString = TierManager.processTierString(pd.modeTiers.get(modeStr));
                                            var tierColor = tierString.startsWith("R") ?
                                                    TierManager.TIER_COLORS.get("R") : TierManager.TIER_COLORS.getOrDefault(tierString, 0x655b79);
                                            var tierText = Text.of(tierString).copy().styled(s -> s.withColor(tierColor));
                                            res.append(modeText).append(tierText).append("\n");
                                        }

                                        context.getSource().sendFeedback(res);
                                        return 1;
                                    }))
            );
        });
    }
}
