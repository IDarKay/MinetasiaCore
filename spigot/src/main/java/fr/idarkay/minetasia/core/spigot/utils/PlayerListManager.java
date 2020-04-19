package fr.idarkay.minetasia.core.spigot.utils;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.utils.ReflectionVar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;


/**
 * File <b>PlayerListRunnable</b> located on fr.idarkay.minetasia.hub
 * PlayerListRunnable is a part of minetasiahub.
 * <p>
 * Copyright (c) 2020 minetasiahub.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 06/03/2020 at 17:53
 */
public class PlayerListManager
{

    private final MinetasiaCore core;

    public PlayerListManager(MinetasiaCore core)
    {
        this.core = core;
        core.registerIpConsumer(ip -> Bukkit.getOnlinePlayers().forEach(player -> updatePlayer(player, ip)));
    }

    public void updatePlayer(Player player)
    {
        updatePlayer(player, " ");
    }

    public void updatePlayer(Player player, String fullIp)
    {
        final String lang = core.getPlayerLang(player.getUniqueId());
        final int ping = getPlayerPing(player);
        sendPlayerList(player,
                Lang.LIST_PLAYER_HEADER.getWithSplit(lang,
                        Lang.Argument.PLAYER.match(player.getName()),
                        Lang.Argument.SERVER_TYPE.match(core.getThisServer().getType()),
                        Lang.Argument.MS.match(ping),
                        Lang.Argument.IP.match(fullIp)
                        ),
                Lang.LIST_PLAYER_FOOTER.getWithSplit(lang,
                        Lang.Argument.PLAYER.match(player.getName()),
                        Lang.Argument.SERVER_TYPE.match(core.getThisServer().getType()),
                        Lang.Argument.MS.match(ping),
                        Lang.Argument.IP.match(fullIp)
                )
        );
    }

    private static final Class<?> PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER_CLASS = Reflection.getNMSClass("PacketPlayOutPlayerListHeaderFooter");
    private static final Constructor PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER_CONSTRUCTOR = Reflection.getConstructor(PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER_CLASS, false);
    private static final Field HEADER = Reflection.getDeclaredField(PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER_CLASS, "header", false);
    private static final Field FOOTER = Reflection.getDeclaredField(PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER_CLASS, "footer", false);

    public void sendPlayerList(Player p, String[] header, String[] footer)
    {
        try
        {
            final Object headerCon = Reflection.getIChatBaseComponent(appends(Arrays.asList(header)));
            final Object footerCon = Reflection.getIChatBaseComponent(appends(Arrays.asList(footer)));

            final Object list = PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER_CONSTRUCTOR.newInstance();
            HEADER.set(list, headerCon);
            FOOTER.set(list, footerCon);

            Reflection.sendPacket(p, list);

        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }
    }

    private static final Field ENTITY_PLAYER_PING_FIELD = Reflection.getDeclaredField(ReflectionVar.ENTITY_PLAYER_CLASS, "ping", false);

    private int getPlayerPing(Player p)
    {
        try
        {
            return (int) ENTITY_PLAYER_PING_FIELD.get(ReflectionVar.CRAFT_PLAYER_GET_HANDLE.invoke(p));
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
            return 1;
        }
    }

    private String appends(List<String> strings)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ; i < strings.size() ; i++)
        {
            if(i != 0)
                stringBuilder.append("\n");
            stringBuilder.append(strings.get(i));
        }
        return stringBuilder.toString();
    }

    private enum IPPhase
    {
        WRITE,
        LIGHT,
        DELETE
    }

}
