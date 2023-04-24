package com.cl3t4p.lib.chatlib;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SenderFactory implements Sender {

    static HashMap<Character, SenderFactory> map;

    private static HashMap<Character, SenderFactory> getMap() {
        if (map == null) {
            map = new HashMap<>();
            addWrapper(new ActionBarFactory());
            addWrapper(new TitleFactory());
            addWrapper(new ChatFactory());
        }
        return map;
    }

    private static void addWrapper(SenderFactory wrapper) {
        map.put(wrapper.character, wrapper);
    }

    public static Sender getSender(String string) {
        for (Map.Entry<Character, SenderFactory> entry : getMap().entrySet()) {
            if (string.startsWith("#" + entry.getKey()))
                return entry.getValue().getWrapper(string);
        }
        return null;
    }

    private final Character character;

    public SenderFactory(Character character) {
        this.character = character;
    }

    protected abstract Sender getWrapper(String msg);

    private static class ActionBarFactory extends SenderFactory implements Sender {
        public ActionBarFactory() {
            super('A');
        }

        @Override
        public void sendTo(Player player, String msg) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        }

        @Override
        protected Sender getWrapper(String msg) {
            return this;
        }

    }

    private static class ChatFactory extends SenderFactory {
        public ChatFactory() {
            super('C');
        }

        @Override
        public void sendTo(Player player, String msg) {
            player.sendMessage(msg);
        }

        @Override
        protected Sender getWrapper(String msg) {
            return this;
        }
    }

    private static class TitleFactory extends SenderFactory {

        protected final Pattern pattern = Pattern.compile("#(\\w+)=(\\w+)");

        public TitleFactory() {
            super('T');
        }

        @Override
        public void sendTo(Player player, String msg) {
            player.sendTitle(msg, "", 0, 50, 20);
        }

        @Override
        protected Sender getWrapper(String msg) {
            Matcher matcher = pattern.matcher(msg);
            Set<Map.Entry<String, String>> set = new HashSet<>();
            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2);

                set.add(new AbstractMap.SimpleEntry<>(key,value));
            }
            if (!set.isEmpty()) {
                return new Title(set);
            } else
                return this;
        }

        private class Title implements Sender {
            private int fadeIn = 0;
            private int stay = 50;
            private int fadeOut = 20;

            public Title(Set<Map.Entry<String, String>> set) {
                for (Map.Entry<String, String> entry : set) {
                    fadeIn = getValue(entry, "fadeIn", fadeIn);
                    stay = getValue(entry, "stay", stay);
                    fadeOut = getValue(entry, "fadeOut", fadeOut);
                }
            }

            private Integer getValue(Map.Entry<String, String> entry, String name, int def) {
                if (name.equalsIgnoreCase(entry.getKey())) {
                    return Integer.valueOf(entry.getValue());
                } else {
                    return def;
                }
            }

            @Override
            public void sendTo(Player player, String msg) {
                player.sendTitle(msg.replaceAll(pattern.pattern(), ""), "", fadeIn, stay, fadeOut);
            }
        }

    }
}
