package fr.idarkay.minetasia.normes.hologram;

import fr.idarkay.minetasia.normes.MinetasiaPlugin;
import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.utils.NMSUtils;
import net.minecraft.server.v1_15_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File <b>Holograme</b> located on fr.idarkay.minetasia.normes.holograme
 * Holograme is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 06/04/2020 at 00:56
 */
public abstract class Hologram
{
    protected static final List<Hologram> loadHolograms = new ArrayList<>();

    public static final float spacing = 0.3f;

    private static MinetasiaPlugin plugin;
    private static final BukkitRunnable runnable = new HologramRunnable();
    private volatile static boolean isRunnable = false;

    public static void setPlugin(MinetasiaPlugin plugin)
    {
        Hologram.plugin = plugin;
    }

    protected final List<UUID> loadPlayer = new ArrayList<>();

    private Location currentLocation;

    public void spawn(Location location)
    {
        Validate.isTrue(location.getWorld() != null, "can't spawn hologram no world set");
        loadHolograms.add(this);
        this.currentLocation = location.clone();
        if(!isRunnable)
        {
            runnable.runTaskTimerAsynchronously(plugin, 10L, 20L);
            isRunnable = true;
        }
    }

    public void teleport(Location location)
    {
        Validate.isTrue(location.getWorld() != null && location.getWorld().equals(currentLocation.getWorld()), "both location need have same world !");
        currentLocation = location;
        updateLocation(currentLocation);
        for (UUID uuid : loadPlayer)
        {
            final Player player = Bukkit.getPlayer(uuid);
            if(player != null)
            {
                for (FullEntityArmorStand armorStand : getArmorStands(player))
                {
                    Reflection.sendPacket(player, new PacketPlayOutEntityTeleport(armorStand.armorStand));
                }
            }
        }
    }
    protected abstract void updateLocation(@NotNull Location location);

    public void move(Vector vector)
    {
        currentLocation.add(vector);
        updateLocation(currentLocation);
        for (UUID uuid : loadPlayer)
        {
            final Player player = Bukkit.getPlayer(uuid);
            if(player != null)
            {
                for (FullEntityArmorStand armorStand : getArmorStands(player))
                {
                    Reflection.sendPacket(player, new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                            armorStand.armorStand.getId(),
                            NMSUtils.toDelta(getCurrentLocation().getX(), getCurrentLocation().getX() + vector.getX()),
                            NMSUtils.toDelta(getCurrentLocation().getY(), getCurrentLocation().getY() + vector.getY()),
                            NMSUtils.toDelta(getCurrentLocation().getZ(), getCurrentLocation().getZ() + vector.getZ()),
                            true
                    ));
                }
            }
        }
    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    public void unShowToPlayer(Player player)
    {
        loadPlayer.remove(player.getUniqueId());
        final FullEntityArmorStand[] armorStands = getArmorStands(player);
        final int[] value = new int[armorStands.length];
        for (int i = 0; i < armorStands.length; i++)
        {
//            Reflection.sendPacket(player, new PacketPlayOutEntityDestroy(armorStands[i].armorStand.getId()));
            value[i] = armorStands[i].armorStand.getId();
        }
        Reflection.sendPacket(player, new PacketPlayOutEntityDestroy(value));
    }

    protected void unShowWithoutRemove(Player player)
    {
        final FullEntityArmorStand[] armorStands = getArmorStands(player);
        final int[] value = new int[armorStands.length];
        for (int i = 0; i < armorStands.length; i++)
        {
//            Reflection.sendPacket(player, new PacketPlayOutEntityDestroy(armorStands[i].armorStand.getId()));
            value[i] = armorStands[i].armorStand.getId();
        }
        Reflection.sendPacket(player, new PacketPlayOutEntityDestroy(value));
    }

    public void showToPlayer(Player player)
    {
        Validate.isTrue(getCurrentLocation() != null, "cant show to player on non load hologram");
        loadPlayer.add(player.getUniqueId());
        for (FullEntityArmorStand armorStand : getArmorStands(player))
        {
            Validate.isTrue(armorStand != null);
            Reflection.sendPacket(player, new PacketPlayOutSpawnEntityLiving(armorStand.armorStand));
            Reflection.sendPacket(player, armorStand.packetPlayOutEntityMetadata);
        }
    }

    public static void disconnectPlayerForAll(Player player)
    {
        for (Hologram minetasiaNpc : loadHolograms)
        {
            minetasiaNpc.disconnectPlayer(player);
        }
    }

    protected void disconnectPlayer(Player player)
    {
        loadPlayer.remove(player.getUniqueId());
    }

    protected void updates(Player player)
    {
        for (FullEntityArmorStand armorStand : getArmorStands(player))
        {
            Reflection.sendPacket(player, armorStand.packetPlayOutEntityMetadata);
        }
    }

    protected abstract FullEntityArmorStand[] getArmorStands(Player player);

    protected static class FullEntityArmorStand
    {
        protected final EntityArmorStand armorStand;
        protected PacketPlayOutEntityMetadata packetPlayOutEntityMetadata;

        protected FullEntityArmorStand(EntityArmorStand armorStand)
        {
            this.armorStand = armorStand;
            armorStand.setMarker(true);
            armorStand.setCustomNameVisible(true);
            armorStand.setInvulnerable(true);
            armorStand.setInvisible(true);
            updatePacket();
        }

        protected void updatePacket()
        {
            this.packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);
        }
    }

}
