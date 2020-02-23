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
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 17:56
 */
public enum CommandPermission {


    // general permission
    NO_PERMISSION(""),
    ALL("*", "all permission for the core (not for other plugin)"),


    ALL_MONEY("money.*", ALL),

    MONEY("money", ALL_MONEY),
    MONEY_ADD("money.add", ALL_MONEY),
    MONEY_SET("money.set", ALL_MONEY),
    MONEY_REMOVE("money.remove", ALL_MONEY),
    MONEY_GET("money.get", ALL_MONEY),

    FRIEND("friend", ALL),

    LANG("lang", ALL),
    LANG_ADMIN("lang.admin", ALL),

    ALL_PERMISSION("permission.*", ALL, "all permission for /permission"),
    ALL_PERMISSION_GROUP("permission.group.*", ALL_PERMISSION, "all permission for /permission group"),
    ALL_PERMISSION_USER("permission.user.*", ALL_PERMISSION, "all permission for /permission user"),

    PERMISSION("permission", ALL_PERMISSION),
    PERMISSION_HELP("permission.help", ALL_PERMISSION),
    PERMISSION_PERMISSION_LIST("permission.permissionlist", ALL_PERMISSION),

    PERMISSION_USER("permission.user", ALL_PERMISSION),
    PERMISSION_USER_PERMISSION("permission.user.permission", ALL_PERMISSION_USER),
    PERMISSION_USER_GROUP("permission.user.group", ALL_PERMISSION_USER),
    PERMISSION_USER_INFO("permission.user.info", ALL_PERMISSION_USER),


    PERMISSION_GROUP("permission.group", ALL_PERMISSION),
    PERMISSION_GROUP_CREATE("permission.group.create", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_DELETE("permission.group.delete", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_PERMISSION("permission.group.permission", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_DISPLAY("permission.group.display", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_PRIORITY("permission.group.priority", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_PARENT("permission.group.parent", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_INFO("permission.group.info", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_BOOST("permission.group.boost", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_SAVE("permission.group.save", ALL_PERMISSION_GROUP),
    PERMISSION_GROUP_DEFAULT("permission.group.default", ALL_PERMISSION_GROUP),


    UTILS_CHAT_COLOR("utils.chat.color", ALL),
    UTILS_CHAT_MAGIC("utils.chat.magic", ALL),
    UTILS_CHAT_WHITE("utils.chat.white", ALL),
    UTILS_CHAT_MSG("utils.chat.msg", ALL),
    UTILS_CHAT_SOCIALSPY("utils.chat.socialspy", ALL), // register her but use in bungee

    TP("tp", ALL),
    HUB("hub", ALL),
    PARTY("party", ALL);

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
