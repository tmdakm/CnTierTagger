package win.huangyu.cntiertagger.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import win.huangyu.cntiertagger.ModConfig;

@Environment(value=EnvType.CLIENT)
public class CntierTaggerMenuIntegration
implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> (Screen)AutoConfigClient.getConfigScreen(ModConfig.class, (Screen)parent).get();
    }
}

