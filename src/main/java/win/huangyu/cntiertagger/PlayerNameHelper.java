package win.huangyu.cntiertagger;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;

public class PlayerNameHelper {
    public static String getOriginalName(Player player) {
        GameProfile profile = player.getGameProfile();
        return profile.name();
    }
}

