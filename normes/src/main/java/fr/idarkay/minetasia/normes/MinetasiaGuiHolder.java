package fr.idarkay.minetasia.normes;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author alice B. (IDarKay),
 * <p>
 *     Copyright (c) 2019 Normes, Minetasia.
 * </p>
 * @since 1.0
 */
@SuppressWarnings("unused")
public class MinetasiaGuiHolder implements InventoryHolder {

    private final String id;
    private Inventory inventory;
    private final GUIFlags[] flags;
    private final MinetasiaGUI minetasiaGUI;

    public MinetasiaGuiHolder(@NotNull String id, @Nullable MinetasiaGUI minetasiaGUI, @NotNull GUIFlags... flags)
    {
        this.id = Objects.requireNonNull(id);
        this.flags = Objects.requireNonNull(flags);
        this.minetasiaGUI = minetasiaGUI;
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
    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public GUIFlags[] getFlags()
    {
        return flags;
    }

    @Nullable
    public MinetasiaGUI getMinetasiaGUI()
    {
        return minetasiaGUI;
    }
}
