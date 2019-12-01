package fr.idarkay.minetasia.core.spigot.command;

import java.util.HashMap;
import java.util.Map;

/**
 * File <b>CommandPermission</b> located on fr.idarkay.minetasia.core.spigot.command
 * CommandPermission is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 30/11/2019 at 17:56
 */
public enum CommandPermission {


    // general permission
    NO_PERMISSION(""),
    ALL("*", "all permission for the core (not for other plugin)"),
    ALL_PERMISSION("permission.*", ALL, "all permission for /permission"),
    ALL_PERMISSION_GROUP("permission.group.*", ALL_PERMISSION, "all permission for /permission group"),
    ALL_PERMISSION_USER("permission.user.*", ALL_PERMISSION, "all permission for /permission user"),

    FRIEND("friend", ALL),

    LANG("lang", ALL),
    LANG_ADMIN("lang.admin", ALL),

    PERMISSION("permission", ALL_PERMISSION),
    PERMISSION_HELP("permission.help", ALL_PERMISSION),
    PERMISSION_PERMISSION_LIST("permission.permissionlist", ALL_PERMISSION),

    PERMISSION_USER("permission.user", ALL_PERMISSION),
    PERMISSION_USER_PERMISSION("permission.user.permission", ALL_PERMISSION_USER),
    PERMISSION_USER_GROUP("permission.user.group", ALL_PERMISSION_USER),
    PERMISSION_USER_INFO("permission.user.info", ALL_PERMISSION_USER),


    PERMISSION_GROUP("permission.group", ALL_PERMISSION),
    PERMISSION_GROUP_CREATE("permission.create", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_DELETE("permission.delete", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_PERMISSION("permission.create.permission", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_DISPLAY("permission.create.display", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_PRIORITY("permission.create.priority", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_PARENT("permission.create.parent", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_INFO("permission.create.info", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_SAVE("permission.create.save", ALL_PERMISSION_GROUP),



    ;


    public static final String ROOT = "core.";

    private final String node;
    private final String description;
    private final CommandPermission parent;

    CommandPermission(String node)
    {
        this.node = ROOT + node;
        this.parent = null;
        this.description = null;
    }

    CommandPermission(String node, String description)
    {
        this.node = ROOT + node;
        this.parent = null;
        this.description = description;
    }

    CommandPermission(String node, CommandPermission parent)
    {
        this.node = ROOT + node;
        this.parent = parent;
        this.description = null;
    }

    CommandPermission(String node, CommandPermission parent, String description)
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
        for(CommandPermission c : values())
        {
            if(c.parent != null && c.parent.equals(this))
                p.put(c.node, true);
        }
        if(p.isEmpty()) return null;
        return p;
    }

}
