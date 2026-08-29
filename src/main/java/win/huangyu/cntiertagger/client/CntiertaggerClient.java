package win.huangyu.cntiertagger.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import win.huangyu.cntiertagger.ConfigManager;
import win.huangyu.cntiertagger.TierManager;
import win.huangyu.cntiertagger.command.CntierCommand;

public class CntiertaggerClient
implements ClientModInitializer {
    public static final TierManager tierManager = new TierManager();

    public void onInitializeClient() {
        ConfigManager.register();
        CntierCommand.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                tierManager.checkCacheExpiration();
            }
        });
    }
}

