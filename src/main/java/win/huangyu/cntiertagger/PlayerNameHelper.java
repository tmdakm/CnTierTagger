package win.huangyu.cntiertagger;
import net.minecraft.world.entity.player.Player;
import com.mojang.authlib.GameProfile;

public class PlayerNameHelper {
    public static String getOriginalName(Player player) {
        GameProfile profile = player.getGameProfile();
        return profile.name();
    }
}
