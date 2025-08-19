package win.huangyu.cntiertagger;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "cntiertagger")
public class ModConfig implements ConfigData {
    public String selectedMode = "Axe";
    public int renderLocation = 0; // 0:头顶 1:名字左侧
    public boolean showRegion = true;
    public DisplayRule displayRule;
}

enum RenderLocation{
    HEAD, NAMETAG
}
