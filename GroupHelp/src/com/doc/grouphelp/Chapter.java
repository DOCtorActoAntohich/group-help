package com.doc.grouphelp;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Provides information about help chapter from config file.
 */
public class Chapter {
    /**
     * Represents a simple 3-tuple to store all data of Chapter.
     * Allows sorting by priority value.
     */
    public static class DataTuple implements Comparable<DataTuple> {
        private final String group;
        private final Integer priority;
        private final List<String> text;
        public DataTuple(String group, Integer priority, List<String> text) {
            this.group = group;
            this.priority = priority;
            this.text = text;
        }

        public String getGroup() {
            return group;
        }

        public Integer getPriority() {
            return priority;
        }

        public List<String> getText() {
            return text;
        }

        @Override
        public int compareTo(DataTuple other) {
            return this.priority.compareTo(other.priority);
        }
    }

    private final String name;
    private final HashMap<String, AbstractMap.SimpleEntry<Integer, List<String>>> groupsPriority;

    /**
     * Creates character identified by name.
     * @param name The name of the chapter.
     */
    public Chapter(String name) {
        this.name = name;

        this.groupsPriority = new HashMap<>();
    }

    /**
     * @return Unique name of the chapter.
     */
    public String getName() {
        return name;
    }

    /**
     * Add information about the chapter if it was found in another group.
     * @param group Group name
     * @param defaultPriority The priority of the group.
     * @param text The text lines of the chapter.
     */
    public void addGroup(String group, Integer defaultPriority, List<String> text) {
        AbstractMap.SimpleEntry<Integer, List<String>> data = new AbstractMap.SimpleEntry<>(defaultPriority, text);
        groupsPriority.put(group, data);
    }

    /**
     * @return All groups in which this chapter was found.
     */
    public Set<String> getGroups() {
        return groupsPriority.keySet();
    }

    /**
     * @param group Group in which the chapter was found.
     * @return The priority of the groups in which this chapter was found.
     */
    public int getGroupPriority(String group) {
        return groupsPriority.get(group).getKey();
    }

    /**
     * @param group Group in which the chapter was found.
     * @return The text of the chapter for a specified group.
     */
    public List<String> getText(String group) {
        return groupsPriority.get(group).getValue();
    }

    /**
     * Merges two chapters data to the current one.
     * @param other Another chapter with the same name (exception is thrown otherwise).
     */
    public void addDataFrom(Chapter other) {
        if (!this.name.equals(other.name)) {
            throw new RuntimeException("Tried to merge chapters with different names.");
        }

        for (String group : other.groupsPriority.keySet()) {
            Integer priority = other.getGroupPriority(group);
            List<String> text = other.getText(group);
            this.addGroup(group, priority, text);
        }
    }

    /**
     * Retrieves all groups, priorities and texts for the chapter.
     * @return A list of all occurrences in descending order.
     */
    public ArrayList<DataTuple> getSortedData() {
        ArrayList<DataTuple> data = new ArrayList<>();

        for (String group : groupsPriority.keySet()) {
            Integer priority = this.getGroupPriority(group);
            List<String> text = this.getText(group);

            DataTuple tuple = new DataTuple(group, priority, text);
            data.add(tuple);
        }

        Collections.sort(data);
        Collections.reverse(data);

        return data;
    }
}
