package win.huangyu.cntiertagger;
import net.minecraft.entity.player.PlayerEntity;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerNameHelper {
    private static Method NAME_METHOD = null;
    private static boolean METHOD_CHECKED = false;

    public static String getOriginalName(PlayerEntity player) {
        GameProfile profile = player.getGameProfile();

        if (!METHOD_CHECKED) {
            try {
                NAME_METHOD = GameProfile.class.getMethod("getName");
            } catch (NoSuchMethodException e) {
            }
            METHOD_CHECKED = true;
        }

        try {
            if (NAME_METHOD != null) {
                // 1.21.9-
                return (String) NAME_METHOD.invoke(profile);
            } else {
                // 1.21.9+
                return profile.name();
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("获取玩家名称失败", e);
        }
    }
}
