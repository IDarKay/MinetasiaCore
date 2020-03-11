package fr.idarkay.minetasia.core.spigot.listener;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * File <b>ChatMessageEventListener</b> located on fr.idarkay.minetasia.core.spigot.listener
 * ChatMessageEventListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 08/12/2019 at 20:35
 */
public class AsyncPlayerChatListener implements Listener {

    private final MinetasiaCore plugin;
    private final String chatChar;

    public AsyncPlayerChatListener(MinetasiaCore plugin)
    {
        this.plugin = plugin;
        chatChar = plugin.getConfig().getString("chat_char");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e)
    {
        Player p = e.getPlayer();
        StringBuilder format = new StringBuilder(plugin.getPlayerManager().getCorePlayer(p.getUniqueId()).getPrefix());
        format.append(" %1$s");

        if(p.hasPermission(CommandPermission.UTILS_CHAT_WHITE.getPermission())) format.append(ChatColor.WHITE);
        else format.append(ChatColor.GRAY);
        format.append(chatChar).append("%2$s");
        String msg = e.getMessage();
        if(p.hasPermission(CommandPermission.UTILS_CHAT_COLOR.getPermission())) msg = translateColorCodes(msg, true);
        if(p.hasPermission(CommandPermission.UTILS_CHAT_MAGIC.getPermission())) msg = translateColorCodes(msg, false);
        e.setFormat(format.toString());
        e.setMessage(msg);
    }


    private String translateColorCodes(String textToTranslate, boolean color)
    {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && (color ? "0123456789AaBbCcDdEeFf" : "KkLlMmNnOoRr").indexOf(b[i+1]) > -1) {
                b[i] = ChatColor.COLOR_CHAR;
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }

}
