package com.cl3t4p.lib.chatlib;

import com.cl3t4p.lib.chatlib.factory.ActionBarFactory;
import com.cl3t4p.lib.chatlib.factory.ChatFactory;
import com.cl3t4p.lib.chatlib.factory.TitleFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class SenderFactory implements Sender {

    static HashMap<Character, SenderFactory> map;
    private final Character character;

    public SenderFactory(Character character) {
        this.character = character;
    }

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

    protected abstract Sender getWrapper(String msg);


}
