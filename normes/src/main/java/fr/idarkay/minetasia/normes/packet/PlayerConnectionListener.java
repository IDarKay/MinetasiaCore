package fr.idarkay.minetasia.normes.packet;

import fr.idarkay.minetasia.normes.event.PlayerPacketComingEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

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

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e)
    {
        final UUID player = e.getPlayer().getUniqueId();
        final ChannelDuplexHandler channelDuplexHandler =  new ChannelDuplexHandler()
        {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
            {
                final PlayerPacketComingEvent e = new PlayerPacketComingEvent(player, msg);
                javaPlugin.getServer().getPluginManager().callEvent(e);
                if(!e.isCancelled())
                    super.channelRead(ctx, msg);
            }
        };
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.networkManager.channel.pipeline()
                .addBefore("packet_handler", e.getPlayer().getName(), channelDuplexHandler);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        final Channel channel = ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> channel.pipeline().remove(e.getPlayer().getName()));
    }

}
