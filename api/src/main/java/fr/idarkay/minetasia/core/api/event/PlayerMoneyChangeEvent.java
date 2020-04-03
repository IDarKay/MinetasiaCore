package fr.idarkay.minetasia.core.api.event;

import fr.idarkay.minetasia.core.api.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PlayerMoneyChangeEvent</b> located on fr.idarkay.minetasia.core.api.event
 * PlayerMoneyChangeEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 02/04/2020 at 16:20
 */
public class PlayerMoneyChangeEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();

    private final Player player;
    private final Economy economy;
    private final float newAmount;

    public PlayerMoneyChangeEvent(Player player, Economy economy, float newAmount)
    {
        this.player = player;
        this.economy = economy;
        this.newAmount = newAmount;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Economy getEconomy()
    {
        return economy;
    }

    public float getNewAmount()
    {
        return newAmount;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

}
