package fr.idarkay.minetasia.normes;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * @author AloIs B. (IDarKay),
 * <p>
 *     Copyright (c) 2019 Normes, Minetasia.
 * </p>
 * @since 1.0
 */
@SuppressWarnings("unused")
public class MinetasiaGuiHolder implements InventoryHolder {

    private String id;
    private Inventory inventory;

    public MinetasiaGuiHolder(String id){
        this.id = id;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * @return String id of the gui
     * @since 1.0
     */
    public String getId() {
        return id;
    }
}
