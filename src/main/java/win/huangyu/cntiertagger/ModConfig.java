package win.huangyu.cntiertagger;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;

@Config(name = "cntiertagger")
public class ModConfig implements ConfigData {
    @Tooltip()
    public Mode selectedMode = Mode.AXE;

    @Tooltip()
    public DisplayRule displayRule = DisplayRule.HIGHEST_ONLY;

    @Tooltip
    public RenderLocation renderLocation = RenderLocation.LEFT;

    public static String modeToString(Mode mode){
        return switch (mode){
            case AXE -> "Axe";
            case SMP -> "SMP";
            case MACE -> "Mace";
            case NPOT -> "NPOT";
            case SWORD -> "Sword";
            case POTION -> "Potion";
            case VANILLA -> "Vanilla";
            case BUILDUHC -> "BUHC";
        };
    }
}

enum RenderLocation{
    LEFT, RIGHT
}
