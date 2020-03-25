package fr.idarkay.minetasia.core.spigot.gui;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.normes.InventoryFileType;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

/**
 * File <b>LangGui</b> located on fr.idarkay.minetasia.core.common.gui
 * LangGui is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 25/11/2019 at 19:14
 */
public final class GUI {



    public final LangGui langGui;
    public final PartyGui partyGui;
    public final FriendsGui friendsGui;

    public GUI(MinetasiaCore plugin)
    {
        this.langGui = new LangGui(plugin);
        this.partyGui = new PartyGui(plugin);
        friendsGui = new FriendsGui(plugin);
    }


    public LangGui getLangGui()
    {
        return langGui;
    }

    public PartyGui getPartyGui()
    {
        return partyGui;
    }
}
