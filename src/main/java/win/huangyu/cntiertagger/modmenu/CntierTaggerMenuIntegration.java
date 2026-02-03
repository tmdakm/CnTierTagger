package win.huangyu.cntiertagger.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import win.huangyu.cntiertagger.ModConfig;

@Environment(EnvType.CLIENT)
public class CntierTaggerMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("text.autoconfig.cntiertagger.title"))
                .build();
    }
}
