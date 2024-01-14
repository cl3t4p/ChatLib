package com.cl3t4p.lib.chatlib.factory;

import com.cl3t4p.lib.chatlib.Sender;
import com.cl3t4p.lib.chatlib.SenderFactory;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionBarFactory extends SenderFactory implements Sender {
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