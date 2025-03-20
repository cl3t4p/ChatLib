package com.cl3t4p.lib.chatlib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Messenger {

    // Hex color pattern for chat colors
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private static final char COLOR_CHAR = ChatColor.COLOR_CHAR;
    HashMap<String, String> message_map = new HashMap<>();

    public Messenger() {
    }

    public Messenger(File file) {
        ConfigurationSection config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(true)) {
            message_map.put(key, config.getString(key));
        }
    }

    public static String color(String string) {
        // Translate the color codes
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(string));
    }

    /**
     * Translate the hex color codes to the chat color codes
     * @param message the message to translate
     * @return the translated message
     */
    public static String translateHexColorCodes(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    public HashMap<String, String> getMessage_map() {
        return message_map;
    }

    public void addMessage(String key, String value) {
        message_map.put(key, value);
    }

    public boolean containsKey(String key) {
        return message_map.containsKey(key);
    }

    public String getMessage(String key) {
        return message_map.get(key);
    }

    public void broadcast(String key, Map<String, Object> data) {
        sendMsg(key, null, data);
    }

    public void broadcast(String key) {
        sendMsg(key, null, null);
    }

    public void sendMsg(String key, Player player) {
        sendMsg(key, player, null);
    }

    /**
     * Send a message to a player
     * @param key the key of the message
     * @param player the player to send the message if null the message will be broadcasted
     * @param data the data to replace in the message (key, value) like ("level", "13")
     * 
     * %player% will be replaced by the player name
     * 
     */
    public void sendMsg(String key, Player player, Map<String, Object> data) {
        String string = message_map.get(key);
        if (string == null || string.isEmpty()) {
            Bukkit.getLogger().warning("String key " + key + " does not exists!!");
            Bukkit.getLogger().warning("Please check the configs");
            return;
        }

        // Get sender factory default chat
        Sender sender = SenderFactory.getSender(string);
        if (sender == null) {
            sender = SenderFactory.getSender("#C");
        } else {
            string = string.substring(2);
        }

        // Replace the data in the string
        string = replaceString(data, string);
        if (string.contains("%player%"))
            string = string.replaceAll("%player%", player.getName());

        sender.send(player, color(string));
    }

    private String replaceString(Map<String, Object> data, String string) {
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String symbol = "%" + entry.getKey() + "%";
                if (string.contains(symbol)) {
                    string = string.replaceAll(symbol, entry.getValue().toString());
                }
            }
        }
        return string;
    }


    public void sendRaw(String key, CommandSender reciver) {
        sendRaw(key, reciver, null);
    }

    public void sendRaw(String key, CommandSender reciver, Map<String, Object> data) {
        String string = message_map.get(key);
        if (string == null || string.isEmpty()) {
            Bukkit.getLogger().warning("String key " + key + " does not exists!!");
            Bukkit.getLogger().warning("Please check the configs");
            return;
        }

        if (SenderFactory.getSender(string) != null) {
            string = string.substring(2);
        }

        string = replaceString(data, string);
        reciver.sendMessage(color(string));
    }


    /**
     * Get a colored string of the message
     * @param key the key of the message
     */
    public String getColorMessage(String key) {
        return getColorMessage(key, null);
    }

    /**
     * Get a colored string of the message with data
     * @param key the key of the message
     * @param data the data to replace in the message (key, value) like ("level", "13")
     * 
     * %player% will be replaced by the player name
     */
    public String getColorMessage(String key, Map<String, Object> data) {
        String string = message_map.get(key);
        if (string == null || string.isEmpty()) {
            Bukkit.getLogger().warning("String key " + key + " does not exists!!");
            Bukkit.getLogger().warning("Please check the configs");
            return "Missing key";
        }
        if (SenderFactory.getSender(string) != null) {
            string = string.substring(2);
        }
        string = replaceString(data, string);
        return color(string);
    }


}
