package win.huangyu.cntiertagger;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import win.huangyu.cntiertagger.DisplayRule;
import win.huangyu.cntiertagger.Mode;
import win.huangyu.cntiertagger.RenderLocation;

@Config(name="cntiertagger")
public class ModConfig
implements ConfigData {
    public boolean enableMod = true;
    public boolean showOnPlayerList = true;
    @ConfigEntry.Gui.Tooltip
    public Mode selectedMode = Mode.AXE;
    @ConfigEntry.Gui.Tooltip
    public DisplayRule displayRule = DisplayRule.HIGHEST_ONLY;
    @ConfigEntry.Gui.Tooltip
    public RenderLocation renderLocation = RenderLocation.LEFT;
    @ConfigEntry.ColorPicker
    public int HT1 = 15252026;
    @ConfigEntry.ColorPicker
    public int LT1 = 14005077;
    @ConfigEntry.ColorPicker
    public int HT2 = 12899303;
    @ConfigEntry.ColorPicker
    public int LT2 = 10528690;
    @ConfigEntry.ColorPicker
    public int HT3 = 16293722;
    @ConfigEntry.ColorPicker
    public int LT3 = 13007682;
    @ConfigEntry.ColorPicker
    public int HT4 = 8483994;
    @ConfigEntry.ColorPicker
    public int LT4 = 6642553;
    @ConfigEntry.ColorPicker
    public int HT5 = 9405096;
    @ConfigEntry.ColorPicker
    public int LT5 = 6642553;
    @ConfigEntry.ColorPicker
    public int Retired = 10671871;

    public int getColorForTier(String tier) {
        return switch (tier) {
            case "HT1" -> this.HT1;
            case "HT2" -> this.HT2;
            case "HT3" -> this.HT3;
            case "HT4" -> this.HT4;
            case "LT1" -> this.LT1;
            case "LT2" -> this.LT2;
            case "LT3" -> this.LT3;
            case "LT4" -> this.LT4;
            case "HT5" -> this.HT5;
            case "LT5" -> this.LT5;
            default -> this.Retired;
        };
    }

    public static String modeToString(Mode mode) {
        return switch (mode) {
            default -> throw new MatchException(null, null);
            case Mode.AXE -> "Axe";
            case Mode.SMP -> "SMP";
            case Mode.MACE -> "Mace";
            case Mode.NPOT -> "NPOT";
            case Mode.SWORD -> "Sword";
            case Mode.POTION -> "Potion";
            case Mode.VANILLA -> "Vanilla";
            case Mode.BUILDUHC -> "BUHC";
        };
    }
}

