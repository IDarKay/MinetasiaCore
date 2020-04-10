package fr.idarkay.minetasia.core.spigot.gui;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;

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
    public final SanctionHistoryGUI sanctionHistoryGUI;

    public GUI(MinetasiaCore plugin)
    {
        this.langGui = new LangGui(plugin);
        this.partyGui = new PartyGui(plugin);
        this.friendsGui = new FriendsGui(plugin);
        this.sanctionHistoryGUI = new SanctionHistoryGUI(plugin);
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
