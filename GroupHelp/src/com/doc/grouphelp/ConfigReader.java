package com.doc.grouphelp;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides smooth access to plugin config.
 */
public class ConfigReader {
    private final GroupHelp plugin;
    private final YamlConfiguration defaultConfig;
    public ConfigReader(GroupHelp plugin) {
        this.plugin = plugin;

        defaultConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(plugin.getResource("config.yml"))
        );

        currentChapters = new HashSet<>();
    }



    private Set<Chapter> currentChapters;

    /**
     * Reloads cache of help chapters.
     */
    public void reload() {
        currentChapters = new HashSet<>();

        GroupParser parser = new GroupParser(plugin);
        try {
            currentChapters = parser.readGroups();
        }
        catch (Exception ex) {
            plugin.getLogger().warning("Unable to parse help for groups. Is the config file malformed?");
        }
    }


    /**
     * Finds a chapter with specified name.
     * @param chapterName A name of a chapter.
     * @return A chapter if it was found, null otherwise.
     */
    public Chapter findChapter(String chapterName) {
        for (Chapter chapter : currentChapters) {
            if (chapter.getName().equals(chapterName)) {
                return chapter;
            }
        }
        return null;
    }



    private String extractString(String path) {
        String result = plugin.getConfig().getString(path);
        if (result != null) {
            return result;
        }
        String warning = String.format("Unable to load '%s' from config.yml. Probably it's missing.", path);
        plugin.getLogger().warning(warning);

        return defaultConfig.getString(path);
    }



    public String commandOnlyForPlayers() {
        return this.extractString("command-only-for-players");
    }

    public String helpHeader() {
        return this.extractString("help-header");
    }

    public String noHelpProvided() {
        return this.extractString("no-help-provided");
    }

    public String noPermission() {
        return this.extractString("no-permission");
    }
}



class GroupParser {
    private final GroupHelp plugin;
    public GroupParser(GroupHelp plugin) {
        this.plugin = plugin;
    }

    /**
     * Reads "groups" section.
     * @return A set of all found chapters.
     */
    public Set<Chapter> readGroups() {
        Set<Chapter> allChapters = new HashSet<>();

        MemorySection groupsSection = getGroupsSection();
        Set<String> allGroups = getAllKeys(groupsSection);
        for (String group : allGroups) {
            MemorySection currentGroupSection = getCurrentGroupSection(groupsSection, group);
            Set<Chapter> readChapters = readGroupSection(currentGroupSection, group);
            mergeChapters(readChapters, allChapters);
        }
        return allChapters;
    }

    private MemorySection getGroupsSection() {
        final String groups = "groups";
        return (MemorySection)plugin.getConfig().get(groups);
    }

    private Set<String> getAllKeys(MemorySection section) {
        return section.getKeys(false);
    }

    private MemorySection getCurrentGroupSection(MemorySection groupsSection, String group) {
        return (MemorySection)groupsSection.get(group);
    }

    private Set<Chapter> readGroupSection(MemorySection currentGroup, String groupName) {
        final String defaultPriority = "default-priority";
        int priority = 0;

        Map<String, List<String>> chapterTextMap = new HashMap<>();

        for (String chapter : getAllKeys(currentGroup)) {
            if (chapter.equals(defaultPriority)) {
                priority = currentGroup.getInt(defaultPriority);
                continue;
            }
            List<String> lines = currentGroup.getStringList(chapter);
            chapterTextMap.put(chapter, lines);
        }

        Set<Chapter> chapters = new HashSet<>();
        for (String chapterName : chapterTextMap.keySet()) {
            List<String> text = chapterTextMap.get(chapterName);
            Chapter chapter = new Chapter(chapterName);
            chapter.addGroup(groupName, priority, text);
            chapters.add(chapter);
        }

        return chapters;
    }

    private void mergeChapters(Set<Chapter> from, Set<Chapter> to) {
        for (Chapter read : from) {
            boolean found = false;
            for (Chapter existing : to) {
                if (read.getName().equals(existing.getName())) {
                    existing.addDataFrom(read);
                    found = true;
                    break;
                }
            }

            if (!found) {
                to.add(read);
            }
        }
    }
}
