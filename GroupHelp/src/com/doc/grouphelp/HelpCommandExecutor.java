package com.doc.grouphelp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

/**
 * Executes /help command.
 */
public class HelpCommandExecutor implements CommandExecutor {
    private final GroupHelp plugin;
    public HelpCommandExecutor(GroupHelp plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            this.handlePlayer((Player) sender, args);
        }
        else {
            this.handleNonPlayer(sender);
        }
        return true;
    }



    private void handlePlayer(Player player, String[] args) {
        final String helpPermission = "grouphelp.help";
        if (!player.hasPermission(helpPermission)) {
            player.sendMessage(plugin.configReader.noPermission());
            return;
        }

        final String defaultMessage = "default-message";
        String chapterName = defaultMessage;
        if (args.length > 0) {
            chapterName = args[0].toLowerCase();
        }

        Chapter chapter = plugin.configReader.findChapter(chapterName);
        if (chapter == null && !chapterName.equals(defaultMessage)) {
            chapter = plugin.configReader.findChapter(defaultMessage);
        }

        if (chapter != null) {
            sendHelp(player, chapter);
        }
        else {
            player.sendMessage(plugin.configReader.noHelpProvided());
        }
    }

    private void sendHelp(Player player, Chapter chapter) {
        for (Chapter.DataTuple data : chapter.getSortedData()) {
            String group = data.getGroup();
            if (player.hasPermission("grouphelp." + group)) {
                String header = String.format(plugin.configReader.helpHeader(), chapter.getName());
                player.sendMessage(header);

                List<String> text = data.getText();
                for (String line : text) {
                    player.sendMessage(line);
                }
                return;
            }
        }
        player.sendMessage(plugin.configReader.noHelpProvided());
    }



    private void handleNonPlayer(CommandSender sender) {
        sender.sendMessage(plugin.configReader.commandOnlyForPlayers());
    }
}
