package win.huangyu.cntiertagger;

import com.google.gson.Gson;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Nullables;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

public class TierManager {
    private static final String API_URL = "https://cntier.win/api/ranking/overall";
    private static final File CACHE_FILE = new File("./config/tier_cache.json");
    private static final long CACHE_DURATION = TimeUnit.MINUTES.toSeconds(4);
    public final Map<String, PlayerData> playerMap = new ConcurrentHashMap<>();
    private static final Gson GSON = new Gson();
    private Instant lastCacheTime = Instant.MIN;

    // 模式配置
    public static final Map<String, TierMode> MODES = new HashMap<>();
    public static final Map<String, Integer> TIER_COLORS = new HashMap<>();
    static {
        MODES.put("Mace", new TierMode("\uE702", "§8"));
        MODES.put("Axe", new TierMode("\uE701", "§b"));
        MODES.put("Sword", new TierMode("\uE706", "§9"));
        MODES.put("BUHC", new TierMode("\uE707", "§c"));
        MODES.put("NPOT", new TierMode("\uE703", "§5"));
        MODES.put("Vanilla", new TierMode("\uE708", "§d"));
        MODES.put("Potion", new TierMode("\uE704", "§d"));
        MODES.put("SMP", new TierMode("\uE705", "§8"));

        TIER_COLORS.put("HT1", 0xe8ba3a);
        TIER_COLORS.put("LT1", 0xd5b355);
        TIER_COLORS.put("HT2", 0xc4d3e7);
        TIER_COLORS.put("LT2", 0xa0a7b2);
        TIER_COLORS.put("HT3", 0xf89f5a);
        TIER_COLORS.put("LT3", 0xc67b42);
        TIER_COLORS.put("HT4", 0x81749a);
        TIER_COLORS.put("LT4", 0x655b79);
        TIER_COLORS.put("HT5", 0x8f82a8);
        TIER_COLORS.put("LT5", 0x655b79);
        TIER_COLORS.put("R", 0xa2d6ff);
    }

    public TierManager(){
        refreshCache();
        loadCache();
    }

    private static class TierResult {
        public TierMode mode;
        public String tier;
        public TierResult(TierMode mode, String tier){
            this.mode = mode;
            this.tier = tier;
        }
    }

    public Text appendTier(PlayerEntity player, Text text) {
        var tierResult = getPlayerTier(player.getGameProfile().getName());
        if(tierResult == null) return text;
        var mode = tierResult.mode;
        var tier = tierResult.tier;
        var modeText = Text.of(mode.emoji).copy();
        var tierText = Text.literal(tier).styled(s -> s.withColor(tier.startsWith("R") ?
                TIER_COLORS.get("R") : TIER_COLORS.getOrDefault(tier,  0x655b79)));
        return ConfigManager.getRenderLocation() == RenderLocation.LEFT ?
                modeText.formatted(Formatting.WHITE).append(" ").append(tierText).append(Text.literal(" | ").formatted(Formatting.GRAY)).append(text) //LEFT
                : text.copy().append(Text.literal(" | ").formatted(Formatting.GRAY)).append(tierText).append(" ").append(modeText.styled(s -> s.withColor(Formatting.WHITE)));
    }

    private static class CacheData {
        long timestamp;
        PlayerData[] players;
    }

    private void loadCache() {
        try {
            if (CACHE_FILE.exists()) {
                CacheData cache = GSON.fromJson(new FileReader(CACHE_FILE), CacheData.class);
                if (cache != null && System.currentTimeMillis() / 1000 - cache.timestamp < CACHE_DURATION) {
                    for (PlayerData player : cache.players) {
                        playerMap.put(player.name, player);
                    }
                    lastCacheTime = Instant.ofEpochSecond(cache.timestamp);
                    return;
                }
            }
            refreshCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkCacheExpiration() {
        if (lastCacheTime.plusSeconds(CACHE_DURATION).isBefore(Instant.now())) {
            refreshCache();
        }
    }
    private volatile boolean isRefreshing = false;
    private static final long RETRY_DELAY = TimeUnit.MINUTES.toSeconds(1); // 重试间隔1分钟
    private Instant lastRefreshAttempt = Instant.MIN;

    public void refreshCache() {
        if (isRefreshing) return;

        if (lastRefreshAttempt.plusSeconds(RETRY_DELAY).isAfter(Instant.now())) {
            return;
        }

        isRefreshing = true;
        lastRefreshAttempt = Instant.now();

        Executors.newSingleThreadExecutor().submit(() -> {
            try (HttpClient client = HttpClient.newHttpClient()) {

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                PlayerData[] players = GSON.fromJson(response.body(), PlayerData[].class);
                playerMap.clear();
                for (PlayerData player : players) {
                    playerMap.put(player.name, player);
                }

                saveCache(players);
                lastCacheTime = Instant.now();
            } catch (Exception e) {
                throw new RuntimeException("Failed to refresh cache", e);
            } finally {
                isRefreshing = false;
            }
        });
    }
    private void saveCache(PlayerData[] players) {
        try (FileWriter writer = new FileWriter(CACHE_FILE)) {
            CacheData cache = new CacheData();
            cache.timestamp = System.currentTimeMillis() / 1000;
            cache.players = players;
            GSON.toJson(cache, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TierResult getPlayerTier(String name) {
        PlayerData data = playerMap.get(name);
        if (data == null) return null;

        // 应用显示规则
        DisplayRule rule = ConfigManager.getDisplayRule();
        String selectedMode = ModConfig.modeToString(ConfigManager.getSelectedMode());

        String finalTier = null;

        // Rule 1: 仅选定的模式
        if (rule == DisplayRule.SELECTED_ONLY || rule == DisplayRule.MIXED) {
            finalTier = data.modeTiers.getOrDefault(selectedMode, null);
            if(rule == DisplayRule.SELECTED_ONLY){
                if(finalTier == null) return null;
                return new TierResult(MODES.get(selectedMode), processTierString(finalTier));
            } else {
                if(finalTier != null) return new TierResult(MODES.get(selectedMode), processTierString(finalTier));
                rule = DisplayRule.HIGHEST_ONLY;
            }
        }

        // Rule 2: 选择最高分等级
        if (finalTier == null || rule == DisplayRule.HIGHEST_ONLY) {
            String highestTier = null;
            String highestMode = null;
            for (String mode : data.modeTiers.keySet()) {
                if (data.modeTiers.getOrDefault(mode, null) == null) continue;
                var tier = data.modeTiers.get(mode);
                if (highestTier == null || compareTiers(processTierString(tier).replace("R",""), processTierString(highestTier).replace("R","")) > 0) {
                    highestTier = tier;
                    highestMode = mode;
                }
            }
            if (highestTier != null) return new TierResult(MODES.get(highestMode), processTierString(highestTier));
        }

        return null;
    }

    public static String processTierString(String tier) {
        // 处理退休状态
        if (tier.startsWith("Retired")) {
            return "R" + tier.replace("Retired", "").trim();
        }
        // 处理峰值状态
        if (tier.contains("|")) {
            return tier.split("\\|")[1].trim();
        }
        return tier;
    }

    private int compareTiers(String a, String b) {
        // 排序规则: HT1 > LT1 > HT2 > ... > HT5 > LT5
        String[] levels = {"HT1", "LT1", "HT2", "LT2", "HT3", "LT3", "HT4", "LT4", "HT5", "LT5"};
        int indexA = findIndex(levels, a);
        int indexB = findIndex(levels, b);
        return Integer.compare(indexB, indexA); // 越小表示越高
    }

    private int findIndex(String[] arr, String key) {
        for (int i = 0; i < arr.length; i++) {
            if (key.contains(arr[i])) return i;
        }
        return arr.length;
    }


}
