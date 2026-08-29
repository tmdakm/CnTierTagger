package win.huangyu.cntiertagger.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import win.huangyu.cntiertagger.ConfigManager;
import win.huangyu.cntiertagger.client.CntiertaggerClient;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    public Component prependTier(Component original) {
        if(!ConfigManager.getConfig().enableMod) return original;
        Player self = (Player) (Object) this;
        return CntiertaggerClient.tierManager.appendTier(self, original);
    }
}
