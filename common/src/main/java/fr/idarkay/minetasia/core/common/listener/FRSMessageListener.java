package fr.idarkay.minetasia.core.common.listener;

import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.event.FRSMessageEvent;
import fr.idarkay.minetasia.core.common.MinetasiaCore;
import fr.idarkay.minetasia.core.common.user.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

/**
 * File <b>FRSMessageListener</b> located on fr.idarkay.minetasia.core.common.listener
 * FRSMessageListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 20/11/2019 at 13:46
 */
public final class FRSMessageListener implements Listener {

    private final MinetasiaCore plugin;

    public FRSMessageListener(MinetasiaCore plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFRSMessageEvent(FRSMessageEvent e)
    {
        if(e.getChanel().equals("data"))
        {
            String[] msg = e.getValue().split(";");
            if(msg.length > 2)
            {
                try
                {
                    Player p;
                    if((p =  plugin.getPlayerManagement().getOnlyInCache(UUID.fromString(msg[1]))) != null)
                    {
                        switch (msg[0]) {
                            case "data":
                                p.setData(msg[1], concat(msg, ";", 3));
                                break;
                            case "username":
                                p.setUsername(concat(msg, ";", 3));
                                break;
                            case "money":
                                p.setMoney(Economy.valueOf(msg[2]), Float.parseFloat(msg[3]));
                                break;
                            case "fremove":
                                p.removeFriends(UUID.fromString(msg[2]));
                                break;
                            case "fadd":
                                p.addFriends(UUID.fromString(msg[2]));
                        }
                    }
                } catch (IllegalArgumentException ignore) { }
            }
        }
    }

    private String concat(String[] array, String split, int indexFrom)
    {
        StringBuilder result = new StringBuilder();
        for(int i = indexFrom; i < array.length; i++) result.append(i != indexFrom && split != null ? split : "").append(array[i]);
        return result.toString();
    }

}
