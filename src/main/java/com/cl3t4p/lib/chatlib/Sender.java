package com.cl3t4p.lib.chatlib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface Sender {

    default void send(Player player, String msg) {
        if (player == null) {
            Bukkit.getServer().getOnlinePlayers().forEach(p -> sendTo(p, msg));
        } else {
            sendTo(player, msg);
        }
    }

    void sendTo(Player player, String msg);
}
