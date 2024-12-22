package org.windy.customblockcommands;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class Customblockcommands extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block != null && event.getAction().toString().contains("RIGHT_CLICK")) {
            String blockName = block.getType().toString().toUpperCase();
            List<String> commands = getConfig().getStringList(blockName);

            if (commands != null && !commands.isEmpty()) {
                // 以玩家身份执行命令
                for (String command : commands) {
                    if (command.startsWith("[player]")) {
                        String playerCommand = command.replace("[player]", "").trim();
                        // 处理 PlaceholderAPI 变量
                        playerCommand = PlaceholderAPI.setPlaceholders(player, playerCommand);
                        player.performCommand(playerCommand);
                    } else if (command.startsWith("[console]")) {
                        String consoleCommand = command.replace("[console]", "").trim();
                        consoleCommand = consoleCommand.replace("%player_name%", player.getName());
                        // 处理 PlaceholderAPI 变量
                        consoleCommand = PlaceholderAPI.setPlaceholders(player, consoleCommand);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
                    }
                }
            }
        }
    }
}
