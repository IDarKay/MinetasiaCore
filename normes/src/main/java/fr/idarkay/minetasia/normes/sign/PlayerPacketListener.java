package fr.idarkay.minetasia.normes.sign;

import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.packet.PlayerPacketComingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * File <b>PlayerPacketListener</b> located on fr.idarkay.minetasia.normes.sign
 * PlayerPacketListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 24/02/2020 at 22:22
 */
public class PlayerPacketListener implements Listener
{

    private static final Class<?> PACKET_PLAY_IN_UPDATE_SIGN = Objects.requireNonNull(Reflection.getNMSClass("PacketPlayInUpdateSign"));
    private static final Field PACKET_PLAY_IN_UPDATE_SIGN_FIELD_B = Reflection.getDeclaredField(PACKET_PLAY_IN_UPDATE_SIGN, "b", true);

    public PlayerPacketListener(JavaPlugin javaPlugin)
    {
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
    }

    @EventHandler
    public void onPlayerPacket(PlayerPacketComingEvent e)
    {
        if(PACKET_PLAY_IN_UPDATE_SIGN.isInstance(e.getPacket()))
        {
            final MinetasiaSignGui minetasiaSignGui = MinetasiaSignGui.guiMap.get(e.getPlayer().getUniqueId());
            if(minetasiaSignGui != null)
            {
                try
                {
                    minetasiaSignGui.getCompleteHandler().accept(e.getPlayer(), (String[]) PACKET_PLAY_IN_UPDATE_SIGN_FIELD_B.get(e.getPacket()));
                } catch (IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
                MinetasiaSignGui.guiMap.remove(e.getPlayer().getUniqueId());
            }
        }
    }

}
