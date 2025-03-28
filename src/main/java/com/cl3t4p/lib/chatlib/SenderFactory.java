package com.cl3t4p.lib.chatlib;

import com.cl3t4p.lib.chatlib.factory.ActionBarFactory;
import com.cl3t4p.lib.chatlib.factory.ChatFactory;
import com.cl3t4p.lib.chatlib.factory.TitleFactory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

public abstract class SenderFactory implements Sender {

    static HashMap<Character, SenderFactory> map;

    private static HashMap<Character, SenderFactory> getMap() {
        if (map == null) {
            map = new HashMap<>();
            // Add basic wrappers
            addWrapper(new ActionBarFactory());
            addWrapper(new TitleFactory());
            addWrapper(new ChatFactory());
        }
        return map;
    }

    private static void addWrapper(SenderFactory wrapper) {
        if (map.put(wrapper.character, wrapper) != null)
            Bukkit.getLogger().warning("Wrapper with character " + wrapper.character + " already exists");
    }


    public static Sender getSender(String string) {
        for (Map.Entry<Character, SenderFactory> entry : getMap().entrySet()) {
            if (string.startsWith("#" + entry.getKey()))
                return entry.getValue().getWrapper(string);
        }
        return null;
    }




    private final Character character;

    /*
     * SenderFactory constructor
     * @param character the character that identifies the activation of the wrapper
     */
    public SenderFactory(Character character) {
        this.character = character;
    }

    protected abstract Sender getWrapper(String msg);

}
