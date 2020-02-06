package fr.idarkay.minetasia.normes.anontation;



import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.lang.annotation.*;



/**
 * File <b>MinetasiaGuiNoCallEvent</b> located on fr.idarkay.minetasia.normes.anontation
 * MinetasiaGuiNoCallEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 28/01/2020 at 12:15
 *
 *
 *
 * set this aotation on :
 * @see fr.idarkay.minetasia.normes.MinetasiaGUI#open(Player, InventoryOpenEvent)
 * @see fr.idarkay.minetasia.normes.MinetasiaGUI#close(Player, InventoryCloseEvent)
 * @see fr.idarkay.minetasia.normes.MinetasiaGUI#click(Player, InventoryClickEvent)
 * @see fr.idarkay.minetasia.normes.MinetasiaGUI#close(Player, InventoryCloseEvent)
 * @see fr.idarkay.minetasia.normes.MinetasiaGUI#onOtherEvent(Player, InventoryEvent)
 *
 * register your {@link fr.idarkay.minetasia.normes.MinetasiaGUI} clazz with MinetasiaCoreApi#registerGui
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MinetasiaGuiNoCallEvent
{
}
