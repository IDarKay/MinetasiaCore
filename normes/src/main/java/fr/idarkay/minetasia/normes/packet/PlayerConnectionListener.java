package fr.idarkay.minetasia.normes.packet;

import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.Utils.BukkitUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * File <b>PlayerConnectionListener</b> located on fr.idarkay.minetasia.normes.packet
 * PlayerConnectionListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 24/02/2020 at 21:57
 */
public class PlayerConnectionListener implements Listener
{

    private final JavaPlugin javaPlugin;

    public PlayerConnectionListener(JavaPlugin javaPlugin)
    {
        this.javaPlugin = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
    }

    private static final Class<?> ENTITY_PLAYER_CLASS = Objects.requireNonNull(Reflection.getNMSClass("EntityPlayer"));
    private static final Field PLAYER_CONNECTION = Reflection.getField(ENTITY_PLAYER_CLASS, "playerConnection", false);
    private static final Field NETWORK_MANAGER = Reflection.getField(PLAYER_CONNECTION.getType(), "networkManager", false);
    private static final Field CHANNEL = Reflection.getField(NETWORK_MANAGER.getType(), "channel", false);

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e)
    {
        final Player player = e.getPlayer();
        final ChannelDuplexHandler channelDuplexHandler =  new ChannelDuplexHandler()
        {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
            {
                final PlayerPacketComingEvent e = new PlayerPacketComingEvent(player, msg);
                javaPlugin.getServer().getPluginManager().callEvent(e);
                super.channelRead(ctx, msg);
            }
        };
        try
        {
            ((Channel) CHANNEL.get(NETWORK_MANAGER.get(PLAYER_CONNECTION.get(BukkitUtils.CRAFT_PLAYER_GET_HANDLE.invoke(player))))).pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        }
        catch (IllegalAccessException | InvocationTargetException ex)
        {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        try
        {
            final Channel channel = ((Channel) CHANNEL.get(NETWORK_MANAGER.get(PLAYER_CONNECTION.get(BukkitUtils.CRAFT_PLAYER_GET_HANDLE.invoke(e.getPlayer())))));
            channel.eventLoop().submit(() -> channel.pipeline().remove(e.getPlayer().getName()));
        }
        catch (IllegalAccessException | InvocationTargetException ex)
        {
            ex.printStackTrace();
        }
    }

}
