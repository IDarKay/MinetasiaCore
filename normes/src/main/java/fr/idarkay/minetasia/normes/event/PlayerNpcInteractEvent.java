package fr.idarkay.minetasia.normes.event;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>PleyrNpcInteractEvent</b> located on fr.idarkay.minetasia.normes.event
 * PleyrNpcInteractEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 02/04/2020 at 20:02
 * async event
 */
public class PlayerNpcInteractEvent extends PlayerNpcEvent
{
    private static HandlerList handlerList = new HandlerList();

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
        super(player, npc);
        this.hand = hand;
    }
    /**
     * @return the hand use
     */
    @NotNull
    public EquipmentSlot getHand()
    {
        return hand;
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
