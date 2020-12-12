package com.doc.grouphelp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Executed /reloadhelp command.
 */
public class ReloadHelpCommandExecutor implements CommandExecutor {
    private GroupHelp plugin;
    public ReloadHelpCommandExecutor(GroupHelp plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String permission = "grouphelp.reloadhelp";
        if (sender.hasPermission(permission)) {
            try {
                plugin.reloadConfig();
            }
            catch (Exception ex) {
                final String errorMessage = "Failed to reload config. Is structure malformed?";
                sender.sendMessage(errorMessage);
                plugin.getLogger().warning(errorMessage);
                return true;
            }

            final String message = "Config successfully reloaded!";
            sender.sendMessage(message);
            plugin.getLogger().info(message);
        }
        return true;
    }
}
