package xphp.xphp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Xphp extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            double startingHealth = 1.0;
            player.setMaxHealth(startingHealth);
            player.setHealth(startingHealth);
            savePlayerHealth(player, startingHealth);
        }
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();
        int newLevel = event.getNewLevel();

        double maxHealth = calculateMaxHealth(player, newLevel);

        player.setMaxHealth(maxHealth);
        player.setHealth(Math.min(maxHealth, player.getHealth()));
        savePlayerHealth(player, maxHealth);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        double startingHealth = 1.0;

        player.setMaxHealth(startingHealth);
        player.setHealth(startingHealth);
        savePlayerHealth(player, startingHealth);
    }

    private void savePlayerHealth(Player player, double health) {
        getConfig().set("players." + player.getUniqueId().toString(), health);
        saveConfig();
    }

    private double calculateMaxHealth(Player player, int newLevel) {
        double maxHealth;

        if (newLevel <= 10) {
            // 레벨 10까지는 체력을 2포인트씩 증가
            maxHealth = Math.min(1024.0, (newLevel * 2.0));
        } else {
            // 레벨 10 이후로는 체력을 1포인트씩 증가
            maxHealth = Math.min(1024.0, (10 * 2.0) + (newLevel - 10));
        }

        return maxHealth;
    }
}
