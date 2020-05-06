package fr.idarkay.minetasia.core.spigot.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.idarkay.minetasia.core.api.utils.InventorySyncPlayer;
import fr.idarkay.minetasia.normes.Tuple;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * File <b>InventorySyncMapper</b> located on fr.idarkay.minetasia.core.spigot.utils
 * InventorySyncMapper is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
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
                return new Tuple<>(InventorySyncType.DAT, getRawPlayerData(player));
            }
            else
                return new Tuple<>(InventorySyncType.BDD, "skyblock.dat");

        }
        return new Tuple<>(InventorySyncType.NONE, "");
    }

    @Nullable
    public static String getBddLinkType(String serverType)
    {
        if (serverType.equals("skyblock-island") || serverType.equals("skyblock-hub"))
            return "skyblock.dat";
        else
            return null;
    }

    public static Function<Player, InventorySyncPlayer> function = null;

    public static String getRawPlayerData(@NotNull Player player)
    {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        NBTTagCompound compound = new NBTTagCompound();
        compound.set("Inventory", entityPlayer.inventory.a(new NBTTagList()));
        entityPlayer.getFoodData().b(compound);
        compound.setFloat("Health", entityPlayer.getHealth());
        compound.setFloat("AbsorptionAmount", entityPlayer.getAbsorptionHearts());
        compound.setInt("SelectedItemSlot", entityPlayer.inventory.itemInHandIndex);

        if (!entityPlayer.effects.isEmpty()) {
            NBTTagList listNbt = new NBTTagList();

            for(MobEffect mobEffect : entityPlayer.effects.values()) {
                listNbt.add(mobEffect.a(new NBTTagCompound()));
            }

            compound.set("ActiveEffects", listNbt);
        }

        if(function != null)
        {
            InventorySyncPlayer apply = function.apply(player);
            if(apply != null)
                apply.write(compound);
        }
        return compound.toString();
//        return compound.toString();
    }

    public static void applied(@NotNull String nbt, @NotNull Player player) throws CommandSyntaxException
    {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        NBTTagCompound compound = MojangsonParser.parse(new String(nbt.getBytes()));
        entityPlayer.inventory.b(compound.getList("Inventory", 10));
        entityPlayer.getFoodData().a(compound);
        if(compound.hasKeyOfType("Health", 99))
        {
            float health = compound.getFloat("Health");
            entityPlayer.setHealth(health <= 0 ? 20 : health);
        }

        entityPlayer.setAbsorptionHearts(compound.getFloat("AbsorptionAmount"));
        entityPlayer.inventory.itemInHandIndex = compound.getInt("SelectedItemSlot");

        if (compound.hasKeyOfType("ActiveEffects", 9)) {
            NBTTagList listNbt = compound.getList("ActiveEffects", 10);

            for(int i = 0; i < listNbt.size(); ++i) {
                NBTTagCompound compoundNbt = listNbt.getCompound(i);
                MobEffect effectInstance = MobEffect.b(compoundNbt);
                if (effectInstance != null) {
                    entityPlayer.effects.put(effectInstance.getMobEffect(), effectInstance);
                }
            }
        }

        if(function != null)
        {
            InventorySyncPlayer apply = function.apply(player);
            if(apply != null)
                apply.read(compound);
        }
    }
}
