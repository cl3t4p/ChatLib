package com.cl3t4p.lib.chatlib.factory;

import com.cl3t4p.lib.chatlib.Sender;
import com.cl3t4p.lib.chatlib.SenderFactory;
import org.bukkit.entity.Player;

public class ChatFactory extends SenderFactory {
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