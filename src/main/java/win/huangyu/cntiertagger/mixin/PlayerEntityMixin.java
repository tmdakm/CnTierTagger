package win.huangyu.cntiertagger.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import win.huangyu.cntiertagger.client.CntiertaggerClient;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    public Text prependTier(Text original) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        return CntiertaggerClient.tierManager.appendTier(self, original);
    }
}