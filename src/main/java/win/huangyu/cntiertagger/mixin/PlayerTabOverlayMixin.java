package win.huangyu.cntiertagger.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import win.huangyu.cntiertagger.ConfigManager;
import win.huangyu.cntiertagger.client.CntiertaggerClient;

@Mixin(value={PlayerTabOverlay.class})
public class PlayerTabOverlayMixin {
    @ModifyReturnValue(method={"getNameForDisplay"}, at={@At(value="RETURN")})
    public Component prependTier(Component original, PlayerInfo entry) {
        if (!ConfigManager.getConfig().enableMod) {
            return original;
        }
        if (!ConfigManager.getConfig().showOnPlayerList) {
            return original;
        }
        return CntiertaggerClient.tierManager.appendTier(entry.getProfile(), original);
    }
}

