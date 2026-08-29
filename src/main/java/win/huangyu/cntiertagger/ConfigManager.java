package win.huangyu.cntiertagger;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class ConfigManager {
    public static void register(){
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }

    public static ModConfig getConfig(){
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }


    public static DisplayRule getDisplayRule() {
        return getConfig().displayRule;
    }

    public static Mode getSelectedMode() {
        return getConfig().selectedMode;
    }

    public static RenderLocation getRenderLocation(){ return getConfig().renderLocation; }
}

enum DisplayRule {
    SELECTED_ONLY, HIGHEST_ONLY, MIXED
}

enum Mode {
    AXE, BUILDUHC, NPOT, VANILLA, SMP, MACE, SWORD, POTION
}

