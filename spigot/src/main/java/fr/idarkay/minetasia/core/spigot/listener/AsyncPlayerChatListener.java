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
 * @author Alois. B. (IDarKay),
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e)
    {
        Player p = e.getPlayer();
        StringBuilder m = new StringBuilder(plugin.getGroupDisplay(p.getUniqueId()));
        m.append(" ");
        m.append(p.getDisplayName());
        m.append(chatChar);
        if(p.hasPermission(CommandPermission.UTILS_CHAT_WHITE.getPermission())) m.append(ChatColor.WHITE);
        else m.append(ChatColor.GRAY);
        String msg = e.getMessage();
        if(p.hasPermission(CommandPermission.UTILS_CHAT_COLOR.getPermission())) msg = translateColorCodes(msg, true);
        if(p.hasPermission(CommandPermission.UTILS_CHAT_MAGIC.getPermission())) msg = translateColorCodes(msg, false);
        m.append(msg);
        e.setMessage(m.toString());
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
