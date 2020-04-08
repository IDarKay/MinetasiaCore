package fr.idarkay.minetasia.normes.hologram;

import fr.idarkay.minetasia.normes.IMinetasiaLang;
import fr.idarkay.minetasia.normes.component.TextComponent;
import fr.idarkay.minetasia.normes.utils.dynamical.DynamicalLang;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

/**
 * File <b>LangHologram</b> located on fr.idarkay.minetasia.normes.hologram
 * LangHologram is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 06/04/2020 at 01:01
 */
public class LangHologram extends Hologram
{

    private final IMinetasiaLang[] lang;
    private final Function<Player, String> getLangFunction;
    private boolean isPlayerDynamicArgs = false;
    private final HashMap<UUID, FullEntityArmorStand[]> playerArmorStand;

    public LangHologram(IMinetasiaLang[] lang, Function<Player, String> getLangFunction)
    {
        this.lang = lang;
        this.getLangFunction = getLangFunction;

        for (IMinetasiaLang l : lang)
        {
            if(l instanceof DynamicalLang)
            {
                if(!((DynamicalLang) l).isOnlyServer())
                {
                    isPlayerDynamicArgs = true;
                }
            }
        }

        playerArmorStand = isPlayerDynamicArgs ? new HashMap<>() : null;
    }

    private final HashMap<String, FullEntityArmorStand[]> langArmorStand = new HashMap<>();

    protected void updateLocation(@NotNull Location location)
    {
        for (FullEntityArmorStand[] value : langArmorStand.values())
        {
            for (int i = 0; i < value.length; i++)
            {
                value[i].armorStand.setLocation(location.getX(), location.getY() + ((this.lang.length - i) * spacing), location.getZ(), 0, 0);
            }
        }
    }

    @Override
    public void unShowToPlayer(Player player)
    {
        if(isPlayerDynamicArgs)
            playerArmorStand.remove(player.getUniqueId());
        super.unShowToPlayer(player);
    }

    public void updateArgs()
    {
        langArmorStand.forEach((lang, fullEntityArmorStands) -> {
            for (FullEntityArmorStand armorStand :  fullEntityArmorStands)
            {
                if(armorStand == null) continue;
                final LangFullEntityArmorStand stand = (LangFullEntityArmorStand) armorStand;
                if(stand.isDynamicArgs)
                {
                    stand.update(lang, null);
                }
            }
        });
        if(isPlayerDynamicArgs)
        {
            playerArmorStand.forEach((uuid, fullEntityArmorStands) -> {
                final Player player = Bukkit.getPlayer(uuid);
                if(player == null) return;
                final String lang = getLangFunction.apply(player);
                for (FullEntityArmorStand armorStand :  fullEntityArmorStands)
                {
                    final LangFullEntityArmorStand stand = (LangFullEntityArmorStand) armorStand;
                    if(stand.isDynamicArgs)
                    {
                        stand.update(lang, player);
                    }
                }
            });
        }
        for (UUID uuid : loadPlayer)
        {
            final Player player = Bukkit.getPlayer(uuid);
            if(player == null) continue;
            updates(player);
        }
    }

    @Override
    protected void disconnectPlayer(Player player)
    {
        super.disconnectPlayer(player);
        if(isPlayerDynamicArgs)
            playerArmorStand.remove(player.getUniqueId());
    }

    protected FullEntityArmorStand[] getArmorStands(Player player)
    {
        final FullEntityArmorStand[] armorStands = getDefaultArmorStands(player);
        if(isPlayerDynamicArgs)
        {
            FullEntityArmorStand[] playStand = playerArmorStand.get(player.getUniqueId());
            if(playStand == null)
            {
                playStand = new FullEntityArmorStand[armorStands.length];
                final Location currentLocation = getCurrentLocation();
                final String lang = getLangFunction.apply(player);
                for (int i = 0; i < armorStands.length; i++)
                {
                    if(armorStands[i] == null)
                    {
                        final EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld)currentLocation.getWorld()).getHandle(), currentLocation.getX(), currentLocation.getY() + ((this.lang.length - i) * spacing), currentLocation.getZ());
                        armorStand.setCustomName(new TextComponent(LangHologram.this.lang[i].getWithoutPrefixPlayer(lang, player)).toChatBaseComponent());
                        playStand[i] = new LangFullEntityArmorStand(armorStand, this.lang[i]);
                    }
                    else
                        playStand[i] = armorStands[i];
                }
                playerArmorStand.put(player.getUniqueId(), playStand);
            }
            return playStand;
        }
        else return armorStands;
    }

    private FullEntityArmorStand[] getDefaultArmorStands(Player player)
    {
        FullEntityArmorStand[] armorStands = langArmorStand.get(lang);
        if(armorStands == null)
        {
            final String lang = getLangFunction.apply(player);
            armorStands = new FullEntityArmorStand[this.lang.length];
            final Location currentLocation = getCurrentLocation();
            for (int i = 0; i < this.lang.length; i++)
            {
                if(this.lang[i] instanceof DynamicalLang && ((DynamicalLang) this.lang[i]).isOnlyServer() )
                {
                    final EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld)currentLocation.getWorld()).getHandle(), currentLocation.getX(), currentLocation.getY() + ((this.lang.length - i) * spacing), currentLocation.getZ());
                    armorStand.setCustomName(new TextComponent(LangHologram.this.lang[i].getWithoutPrefixPlayer(lang, player)).toChatBaseComponent());
                    armorStands[i] = new LangFullEntityArmorStand(armorStand, this.lang[i]);
                }
                else
                    armorStands[i] = null;
            }
            langArmorStand.put(lang, armorStands);
        }
        return armorStands;
    }

    private static class LangFullEntityArmorStand extends FullEntityArmorStand
    {
        private final boolean isDynamicArgs;
        private final boolean isPlayerDynamicArgs;
        private final IMinetasiaLang lang;

        protected LangFullEntityArmorStand(EntityArmorStand armorStand, IMinetasiaLang lang)
        {
            super(armorStand);
            this.lang = lang;
            if(lang instanceof DynamicalLang)
            {
                this.isDynamicArgs = true;
                isPlayerDynamicArgs = !((DynamicalLang) lang).isOnlyServer();
            }
            else
            {
                this.isDynamicArgs = isPlayerDynamicArgs = false;
            }
        }

        private void update(String lang, Player player)
        {
            armorStand.setCustomName(new TextComponent(this.lang.getWithoutPrefixPlayer(lang, player)).toChatBaseComponent());
            updatePacket();
        }
    }
}
