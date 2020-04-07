package fr.idarkay.minetasia.normes.event;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>PlayerNpcHitEvent</b> located on fr.idarkay.minetasia.normes.event
 * PlayerNpcHitEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 04/04/2020 at 02:20
 */
public class PlayerNpcHitEvent extends PlayerNpcEvent
{
    private static HandlerList handlerList = new HandlerList();

    /**
     * async event
     * @param player the uuid of who interact
     * @param npc the uuid of the target npc
     */
    public PlayerNpcHitEvent(@NotNull final UUID player, @NotNull final UUID npc)
    {
        super(player, npc);
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
