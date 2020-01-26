package fr.idarkay.minetasia.core.api;

import java.util.HashMap;
import java.util.Map;

/**
 * File <b>GeneralPermission</b> located on fr.idarkay.minetasia.core.api
 * GeneralPermission is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/01/2020 at 10:57
 */
public enum GeneralPermission
{
    ALL("*"),

    ADMIN_ALL("admin", ALL),

    /**
     * a player with this permission can join full server in {@link ServerPhase#GAME} phase
     */
    ADMIN_SPECTATOR("admin.spectator", ADMIN_ALL, "a player with this permission can join full server in GAME phase"),

    ;
    public static final String ROOT = "general.";

    private final String node;
    private final String description;
    private final GeneralPermission parent;

    GeneralPermission(String node)
    {
        this.node = ROOT + node;
        this.parent = null;
        this.description = null;
    }

    GeneralPermission(String node, String description)
    {
        this.node = ROOT + node;
        this.parent = null;
        this.description = description;
    }

    GeneralPermission(String node, GeneralPermission parent)
    {
        this.node = ROOT + node;
        this.parent = parent;
        this.description = null;
    }

    GeneralPermission(String node, GeneralPermission parent, String description)
    {
        this.node = ROOT + node;
        this.parent = parent;
        this.description = description;
    }

    public String getPermission()
    {
        return node;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public Map<String, Boolean> getALLChild()
    {
        HashMap<String, Boolean> p = new HashMap<>();
        for(GeneralPermission c : values())
        {
            if(c.parent != null && c.parent.equals(this))
                p.put(c.node, true);
        }
        if(p.isEmpty()) return null;
        return p;
    }
}
