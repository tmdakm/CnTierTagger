package win.huangyu.cntiertagger;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import win.huangyu.cntiertagger.DisplayRule;
import win.huangyu.cntiertagger.ModConfig;
import win.huangyu.cntiertagger.Mode;
import win.huangyu.cntiertagger.RenderLocation;

public class ConfigManager {
    public static void register() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }

    public static ModConfig getConfig() {
        return (ModConfig)AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static DisplayRule getDisplayRule() {
        return ConfigManager.getConfig().displayRule;
    }

    public static Mode getSelectedMode() {
        return ConfigManager.getConfig().selectedMode;
    }

    public static RenderLocation getRenderLocation() {
        return ConfigManager.getConfig().renderLocation;
    }
}

