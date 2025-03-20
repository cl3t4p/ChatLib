package com.cl3t4p.lib.chatlib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface Sender {


    /**
     * Send a message
     * @param player null to send to all players
     * @param msg the message
     */
    default void send(Player player, String msg) {
        if (player == null) {
            // Send to all players
            Bukkit.getServer().getOnlinePlayers().forEach(p -> sendTo(p, msg));
        } else {
            sendTo(player, msg);
        }
    }

    /**
     * Send a message to a player
     * @param player
     * @param msg
     */
    void sendTo(Player player, String msg);
}
