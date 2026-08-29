package win.huangyu.cntiertagger.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import win.huangyu.cntiertagger.ConfigManager;
import win.huangyu.cntiertagger.PlayerData;
import win.huangyu.cntiertagger.TierManager;
import win.huangyu.cntiertagger.client.CntiertaggerClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class CntierCommand {
    public static void register(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, ra) -> {
            dispatcher.register(
                    literal("cntier")
                            .then(argument("player", StringArgumentType.string())
                                    .executes((context) -> {
                                        var player = context.getArgument("player", String.class);
                                        var key = CntiertaggerClient.tierManager.playerMap.keySet().stream().filter(str -> str.equalsIgnoreCase(player)).findFirst();
                                        PlayerData pd = null;
                                        if(key.isPresent()) pd = CntiertaggerClient.tierManager.playerMap.get(key.get());
                                        if(pd == null){
                                            context.getSource().sendError(Component.literal("该玩家没有Tier！"));
                                            return 1;
                                        }

                                        MutableComponent res = Component.literal(String.format("§e========%s's Tier========\n", key.get()));
                                        var config = ConfigManager.getConfig();
                                        for(var modeStr : pd.modeTiers.keySet()){
                                            if (pd.modeTiers.getOrDefault(modeStr, null) == null) continue;
                                            var mode = TierManager.MODES.get(modeStr);
                                            var modeText = Component.literal(mode.emoji + " " + modeStr + " §7: ");
                                            var tierString = TierManager.processTierString(pd.modeTiers.get(modeStr));
                                            var retired = tierString.startsWith("R");
                                            var tierText = Component.literal(tierString.replace("R", ""));
                                            if(!retired){
                                                var tierColor = config.getColorForTier(tierString);
                                                tierText = tierText.withStyle(s -> s.withColor(tierColor));
                                            }else{
                                                var tierColor = config.getColorForTier(tierString.replace("R",""));
                                                var retiredText = Component.literal("R").withStyle(s -> s.withColor(config.getColorForTier("R")));
                                                tierText = retiredText.append(tierText.withStyle(s -> s.withColor(tierColor))).copy();
                                            }
                                            res.append(modeText).append(tierText).append("\n");
                                        }

                                        context.getSource().sendFeedback(res);
                                        return 1;
                                    }))
            );
        });
    }
}
