package fr.idarkay.minetasia.normes.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * File <b>PlayerNpcEvent</b> located on fr.idarkay.minetasia.normes.event
 * PlayerNpcEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 04/04/2020 at 02:21
 */
public abstract class PlayerNpcEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();

    @NotNull
    private final UUID player;
    @NotNull
    private final UUID npc;

    /**
     * async event
     * @param player the uuid of who interact
     * @param npc the uuid of the target npc
     */
    public PlayerNpcEvent(@NotNull final UUID player, @NotNull final UUID npc)
    {
        super(true);
        this.player = Objects.requireNonNull(player);
        this.npc = Objects.requireNonNull(npc);
    }

    /**
     * @return the uuid v2 of the npc
     */
    @NotNull
    public UUID getNpc()
    {
        return npc;
    }

    /**
     * @return uuid of the player who clicked
     */
    @NotNull
    public UUID getPlayer()
    {
        return player;
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
