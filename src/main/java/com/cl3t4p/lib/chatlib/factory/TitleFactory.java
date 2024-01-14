package com.cl3t4p.lib.chatlib.factory;

import com.cl3t4p.lib.chatlib.Sender;
import com.cl3t4p.lib.chatlib.SenderFactory;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleFactory extends SenderFactory {

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

            set.add(new AbstractMap.SimpleEntry<>(key, value));
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