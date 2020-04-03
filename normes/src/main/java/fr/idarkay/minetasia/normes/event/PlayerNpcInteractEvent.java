package fr.idarkay.minetasia.normes.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * File <b>PleyrNpcInteractEvent</b> located on fr.idarkay.minetasia.normes.event
 * PleyrNpcInteractEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 02/04/2020 at 20:02
 * async event
 */
public class PlayerNpcInteractEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();

    @NotNull
    private final UUID player;
    @NotNull
    private final UUID npc;
    @NotNull
    private final EquipmentSlot hand;

    /**
     * async event
     * @param player the uuid of who interact
     * @param npc the uuid of the target npc
     * @param hand the hand
     */
    public PlayerNpcInteractEvent(@NotNull final UUID player, @NotNull final UUID npc, @NotNull final EquipmentSlot hand)
    {
        super(true);
        this.player = Objects.requireNonNull(player);
        this.npc = Objects.requireNonNull(npc);
        this.hand = hand;
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
     * @return the hand use
     */
    @NotNull
    public EquipmentSlot getHand()
    {
        return hand;
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
