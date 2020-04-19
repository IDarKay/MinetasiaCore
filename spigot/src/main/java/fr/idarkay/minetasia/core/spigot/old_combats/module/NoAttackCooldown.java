package fr.idarkay.minetasia.core.spigot.old_combats.module;

import fr.idarkay.minetasia.core.spigot.old_combats.OldCombatsModule;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * File <b>NoAttackCooldown</b> located on fr.idarkay.minetasia.core.spigot.old_combats.module
 * NoAttackCooldown is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 14/04/2020 at 16:46
 */
public class NoAttackCooldown extends OldCombatsModule
{
    public NoAttackCooldown()
    {
        super("no_cooldown");
    }

    @Override
    public void onWorldChange(Player player, PlayerChangedWorldEvent event)
    {
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
        player.saveData();
    }

    @Override
    public void onPlayerJoinEvent(Player player, PlayerJoinEvent event)
    {
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
        player.saveData();
    }
}
