package win.huangyu.cntiertagger.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import win.huangyu.cntiertagger.ConfigManager;
import win.huangyu.cntiertagger.TierManager;
import win.huangyu.cntiertagger.command.CntierCommand;

public class CntiertaggerClient implements ClientModInitializer {
    private static KeyBinding showLegendKey;
    public static final TierManager tierManager = new TierManager();

    @Override
    public void onInitializeClient() {
        ConfigManager.register();
        CntierCommand.register();


        // 定时更新
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {

                // 定期更新缓存
                tierManager.checkCacheExpiration();
            }
        });
    }

}
