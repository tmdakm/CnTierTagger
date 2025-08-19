package win.huangyu.cntiertagger.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import win.huangyu.cntiertagger.ConfigManager;
import win.huangyu.cntiertagger.TierManager;

import java.io.IOException;

public class CntiertaggerClient implements ClientModInitializer {
    private static KeyBinding showLegendKey;
    public static final TierManager tierManager = new TierManager();

    @Override
    public void onInitializeClient() {
        ConfigManager.register();
        tierManager.loadCache();

        // 按键绑定
        showLegendKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cntiertagger.showlegend",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.cntiertagger.general"
        ));

        // 定时更新
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                // 检查按键
                if (showLegendKey.wasPressed()) {
                    showModeLegend();
                }

                // 定期更新缓存
                tierManager.checkCacheExpiration();
            }
        });
    }

    private void showModeLegend() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        client.player.sendMessage(Text.of("§r======== 模式图例 ========"), false);
        client.player.sendMessage(Text.of("§7Mace: §f🔨 §8(灰色)"), false);
        client.player.sendMessage(Text.of("§bAxe: §f🪓 §b(浅蓝色)"), false);
        client.player.sendMessage(Text.of("§9Sword: §f🗡 §9(蓝色)"), false);
        client.player.sendMessage(Text.of("§cBUHC: §f❤ §c(红色)"), false);
        client.player.sendMessage(Text.of("§5NPOT: §f☠ §5(紫色)"), false);
        client.player.sendMessage(Text.of("§dVanilla: §f🔮 §b(青色)"), false);
        client.player.sendMessage(Text.of("§dPotion: §f⚗ §d(亮紫色)"), false);
        client.player.sendMessage(Text.of("§8SMP: §f⛨ §8(黑灰色)"), false);
    }
}
