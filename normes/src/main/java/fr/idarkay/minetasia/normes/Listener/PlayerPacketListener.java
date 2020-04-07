package fr.idarkay.minetasia.normes.Listener;

import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.event.PlayerNpcHitEvent;
import fr.idarkay.minetasia.normes.event.PlayerNpcInteractEvent;
import fr.idarkay.minetasia.normes.event.PlayerPacketComingEvent;
import fr.idarkay.minetasia.normes.npc.MinetasiaNpc;
import fr.idarkay.minetasia.normes.sign.MinetasiaSignGui;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.PacketPlayInUpdateSign;
import net.minecraft.server.v1_15_R1.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * File <b>PlayerPacketListener</b> located on fr.idarkay.minetasia.normes.sign
 * PlayerPacketListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 24/02/2020 at 22:22
 */
public class PlayerPacketListener implements Listener
{
    //for get entity id
    private static final Field PACKET_PLAY_IN_USE_ENTITY_ID = Reflection.getDeclaredField(PacketPlayInUseEntity.class, "a", true);

    public PlayerPacketListener(JavaPlugin javaPlugin)
    {
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
    }

    @EventHandler
    public void onPlayerPacket(PlayerPacketComingEvent e)
    {
        //for sign
        if(e.getPacket() instanceof PacketPlayInUpdateSign)
        {
            final MinetasiaSignGui minetasiaSignGui = MinetasiaSignGui.guiMap.get(e.getPlayer());
            if(minetasiaSignGui != null)
            {
                minetasiaSignGui.getCompleteHandler().accept(Bukkit.getPlayer(e.getPlayer()), ((PacketPlayInUpdateSign) e.getPacket()).c());
                MinetasiaSignGui.guiMap.remove(e.getPlayer());
            }
        }
        //for npx
        if(e.getPacket() instanceof PacketPlayInUseEntity)
        {
            final PacketPlayInUseEntity packet = (PacketPlayInUseEntity) e.getPacket();
            try
            {
                //get a id in private field can't get from another place (fake entity)
                final MinetasiaNpc npc = MinetasiaNpc.getNpcFromId((Integer) PACKET_PLAY_IN_USE_ENTITY_ID.get(packet));
                if(npc != null)
                {
                    if(packet.c() == null)
                    {
                        Bukkit.getPluginManager().callEvent(new PlayerNpcHitEvent(e.getPlayer(), npc.getUuid()));
                    }
                    else
                    {
                        Bukkit.getPluginManager().callEvent(new PlayerNpcInteractEvent(e.getPlayer(), npc.getUuid(), getBukkitHand(packet.c())));
                    }
                }
            } catch (IllegalAccessException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @NotNull
    private EquipmentSlot getBukkitHand(EnumHand nmsHand)
    {
        if(nmsHand == EnumHand.MAIN_HAND) return EquipmentSlot.HAND;
        else return EquipmentSlot.OFF_HAND;
    }

}
