package fr.idarkay.minetasia.normes.hologram;

import fr.idarkay.minetasia.normes.component.TextComponent;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * File <b>StringHologram</b> located on fr.idarkay.minetasia.normes.hologram
 * StringHologram is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 06/04/2020 at 00:57
 */
public class StringHologram extends Hologram
{

    private final String[] text;
    private final FullEntityArmorStand[] armorStands;

    public StringHologram(@NotNull String[] text)
    {
        this.text = Objects.requireNonNull(text);
        armorStands = new FullEntityArmorStand[text.length];
    }


    @Override
    public void spawn(Location location)
    {
        for (int i = 0; i < this.text.length; i++)
        {
            final EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld)location.getWorld()).getHandle(), location.getX(), location.getY() + ((this.text.length - i) * spacing), location.getZ());
            armorStand.setCustomName(new TextComponent(text[i]).toChatBaseComponent());
            armorStands[i] = new FullEntityArmorStand(armorStand);
        }
        super.spawn(location);
    }

    protected void updateLocation(@NotNull Location location)
    {
        for (int i = 0; i < armorStands.length; i++)
        {
            armorStands[i].armorStand.setLocation(location.getX(), location.getY() + ((this.text.length - i) * spacing), location.getZ(), 0, 0);
        }
    }

    @Override
    protected FullEntityArmorStand[] getArmorStands(Player player)
    {
        return armorStands;
    }
}
