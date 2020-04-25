package fr.idarkay.minetasia.core.spigot.utils;

import fr.idarkay.minetasia.normes.Tuple;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * File <b>InventorySyncMapper</b> located on fr.idarkay.minetasia.core.spigot.utils
 * InventorySyncMapper is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 25/04/2020 at 19:15
 */
public class InventorySyncTools
{

    public static final Map<UUID, Tuple<InventorySyncType, String>> pendingSync = new HashMap<>();

    public static Tuple<InventorySyncType, String> map(@NotNull Player player, @NotNull String serverTypeIn, @NotNull String serverTypeOut)
    {
        if (serverTypeOut.equals("skyblock-island") || serverTypeOut.equals("skyblock-hub"))
        {
            if (serverTypeIn.equals("skyblock-island") || serverTypeIn.equals("skyblock-hub"))
            {
                return new Tuple<>(InventorySyncType.DAT, ((CraftPlayer) player).getHandle().save(new NBTTagCompound()).toString());
            }
            else
                return new Tuple<>(InventorySyncType.BDD, "skyblock.dat");

        }
        return new Tuple<>(InventorySyncType.NONE, "");
    }

//    public static void write(NBTTagCompound nbtTagCompound, Out)

}
