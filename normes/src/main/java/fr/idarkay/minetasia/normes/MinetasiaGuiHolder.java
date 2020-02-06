package fr.idarkay.minetasia.normes;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * @author AloIs B. (IDarKay),
 * <p>
 *     Copyright (c) 2019 Normes, Minetasia.
 * </p>
 * @since 1.0
 */
@SuppressWarnings("unused")
public class MinetasiaGuiHolder implements InventoryHolder {

    private final UUID uuid;
    private Inventory inventory;
    private final GUIFlags[] flags;
    private final MinetasiaGUI minetasiaGUI;
    private Object[] data = new Object[0];

    public MinetasiaGuiHolder(@NotNull UUID uuid, @Nullable MinetasiaGUI minetasiaGUI, @NotNull GUIFlags... flags)
    {
        this.uuid = Objects.requireNonNull(uuid);
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

    public UUID getUuid()
    {
        return uuid;
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

    public Object[] getData()
    {
        return data;
    }

    public void setData(Object... data)
    {
        this.data = data;
    }
}
