package win.huangyu.cntiertagger;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;

@Config(name = "cntiertagger")
public class ModConfig implements ConfigData {
    public boolean enableMod = true;
    public boolean showOnPlayerList = true;

    @Tooltip()
    public Mode selectedMode = Mode.AXE;

    @Tooltip()
    public DisplayRule displayRule = DisplayRule.HIGHEST_ONLY;

    @Tooltip
    public RenderLocation renderLocation = RenderLocation.LEFT;

    @ConfigEntry.ColorPicker
    public int HT1 = 0xe8ba3a;
    @ConfigEntry.ColorPicker
    public int LT1 = 0xd5b355;
    @ConfigEntry.ColorPicker
    public int HT2 = 0xc4d3e7;
    @ConfigEntry.ColorPicker
    public int LT2 = 0xa0a7b2;
    @ConfigEntry.ColorPicker
    public int HT3 = 0xf89f5a;
    @ConfigEntry.ColorPicker
    public int LT3 = 0xc67b42;
    @ConfigEntry.ColorPicker
    public int HT4 = 0x81749a;
    @ConfigEntry.ColorPicker
    public int LT4 = 0x655b79;
    @ConfigEntry.ColorPicker
    public int HT5 = 0x8f82a8;
    @ConfigEntry.ColorPicker
    public int LT5 = 0x655b79;

    @ConfigEntry.ColorPicker
    public int Retired = 0xa2d6ff;

    public int getColorForTier(String tier){
        return switch (tier) {
            case "HT1" -> HT1;
            case "HT2" -> HT2;
            case "HT3" -> HT3;
            case "HT4" -> HT4;
            case "LT1" -> LT1;
            case "LT2" -> LT2;
            case "LT3" -> LT3;
            case "LT4" -> LT4;
            case "HT5" -> HT5;
            case "LT5" -> LT5;
            default -> Retired;
        };
    }

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
