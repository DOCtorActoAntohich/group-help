package com.doc.grouphelp;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class.
 */
public class GroupHelp extends JavaPlugin {
    /**
     * Does initialization.
     */
    public GroupHelp() {
        this.configReader = new ConfigReader(this);
    }
    public final ConfigReader configReader;


    /**
     * Enables all initialized components.
     */
    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        HelpCommandExecutor helpCommandExecutor = new HelpCommandExecutor(this);
        this.getCommand("help").setExecutor(helpCommandExecutor);

        ReloadHelpCommandExecutor reloadHelpCommandExecutor = new ReloadHelpCommandExecutor(this);
        this.getCommand("reloadhelp").setExecutor(reloadHelpCommandExecutor);

        configReader.reload();

        this.getLogger().info("Plugin initialized successfully!");
    }



    /**
     * Disables all components and saves data.
     */
    @Override
    public void onDisable() {
        this.getLogger().info("Plugin terminated.");
    }



    /**
     * Reloads config and config reader.
     */
    @Override
    public void reloadConfig() {
        super.reloadConfig();

        configReader.reload();
    }
}
