package win.huangyu.cntiertagger.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.realms.dto.PlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import win.huangyu.cntiertagger.ConfigManager;
import win.huangyu.cntiertagger.client.CntiertaggerClient;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @ModifyReturnValue(method = "getPlayerName", at = @At("RETURN"))
    public Text prependTier(Text original, PlayerListEntry entry) {
        if(!ConfigManager.getConfig().enableMod) return original;
        if(!ConfigManager.getConfig().showOnPlayerList) return original;
        return CntiertaggerClient.tierManager.appendTier(entry.getProfile(), original);
    }
}
