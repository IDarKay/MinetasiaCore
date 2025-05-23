package fr.idarkay.minetasia.core.spigot.utils;

import fr.idarkay.minetasia.core.api.MinetasiaCoreApi;
import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.IMinetasiaLang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import fr.idarkay.minetasia.normes.Tuple;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>Lang</b> located on fr.idarkay.mintasia.core.common.utils
 * Lang is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 13/11/2019 at 15:44
 */
public enum Lang implements IMinetasiaLang {

    WELCOME                                     ( "&6Welcome" + Argument.PLAYER + "to the server :)"),

    //help
    USER_HELP(
            "default msg"
    ),
    STAFF_HELP_BORDER("&7----&8----&6---- &2Minetasia Help &c" + Argument.PAGE + " / " + Argument.MAX_PAGE + " &6----&8----&7----"),
    STAFF_HELP_FORMAT(
      "&6" + Argument.COMMAND + " &2- &7" + Argument.DESCRIPTION
    ),

    COMMAND_HELP_BORDER("&7----&8----&6---- &2" + Argument.COMMAND + " &6----&8----&7----"),
    COMMAND_HELP_FORMAT("&6" + Argument.COMMAND + " &2- &7" + Argument.DESCRIPTION),

    // /lang
    SET_LANG                                    ( "&aYour new lang is English"),
    GET_LANG                                    ( "&aThe lang of &6 " + Argument.PLAYER + " is &9" + Argument.LANG),

    // /friends
    SELF_ADD_FRIEND                             ( "&cYou really must feel alone to want to add as a friend"),
    ALREADY_FRIEND                              ( "&cYour are friend with " + Argument.PLAYER + " !"),
    REQUEST_SEND_FRIENDS                        ( "&aYour friends request have been send to " + Argument.PLAYER + " !"),
    REQUEST_FRIEND                              ("&a" + Argument.PLAYER + " send to you a friend request use &6/friends accept&a for accept this else ignore the request but it's very sad"),
    NEW_FRIEND                                  ( "&aYou are now friend with " + Argument.PLAYER + " !"),
    REMOVE_FRIEND                               ( "&a" + Argument.PLAYER + " remove to your friend"),
    NOT_FRIEND                                  ( "&cYou are not friend with " + Argument.PLAYER + " !"),
    MAX_FRIENDS_REACH                           ("&cYour have reach the maximum of friends that you can have !"),
        //gui
    FRIENDS_GUI_NAME("Your's friends"),
    FRIENDS_GUI_FRIEND_LORE("&cLeft-Click &7Invite to party@@&cRight-Click &7Remove friends"),
    ITEM_PREVIOUS_PAGE("&7Previous page"),
    ITEM_NEXT_PAGE("&7Next page"),

    //misc

    PLAYER_NOT_ONLINE                           ( "&cSorry this player isn't only !"),
    PLAYER_NOT_EXIST                            ( "&cThe player don't exist"),
    ONLINE                                      ( "online"),
    OFFLINE                                     ( "offline"),
    NEED_BE_PLAYER                              ( "&cYour not player"),
    NO_PERMISSION                               ( "&cYou don't have permission"),
    ILLEGAL_NUMBER_VALUE                        ("&cPlease set valid number"),
    ILLEGAL_TIME_UNITS                          ("&cPlease set valid time units"),
    ILLEGAL_BOOLEAN_VALUE                       ("&cPlease set valid boolean (true or false)"),
    MSG_FORMAT                                  ("&6" + Argument.PLAYER_SENDER + " &c-> " + Argument.PLAYER_RECEIVER  + " : " + Argument.MESSAGE),
    MSG_FORMAT_SOCIAL_SPY                       ("&c[SS] " + Argument.MESSAGE),
    SOCIAL_SPU_ON                               ("&6SocialSpy &2on"),
    SOCIAL_SPU_OFF                              ("&6SocialSpy &4off"),
    SERVER_NOT_FOUND                            ("&cServer not found"),
    PROXY_NOT_FOUND                             ("&cProxy not found"),
    WORLD_NOT_FOUND                             ("&cWorld not found"),
    INCOMPATIBLE_CMD_TP                         ("&cYou can't tp @a to another server"),
    PLAYER_BOOST                                ("&6 " + Argument.PLAYER + " &aboost " + Argument.MONEY_TYPE + " &afor the party (" + Argument.ACTUAL_BOOST + " /" + Argument.MAX_BOOST + "%)"),
    GAME_REWARDS                                ("&c==========================================@@" +
                                                            "&6End game of : &a" + Argument.SERVER_TYPE +
                                                            "@@&6Rewards: " + Argument.REWARDS +
                                                            "&c=========================================="),
    SETTINGS_COMMANDS_FORMAT                    ("&c/settingseditor <key> <value>"),
    SETTINGS_COMMANDS_INVALID_KEY               ("&cinvalid key"),
    SETTINGS_COMMANDS_END                       ("&a" + Argument.SETTINGS + " set to " + Argument.VALUE),
    HOVER_CLICK_FOR_TELEPORT                    ("&8&lClick to teleport"),


    //party
    PARTY_NOT_IN_PARTY("&cSorry you're not in party"),
    PARTY_NOT_OWNER("&cSorry you're not the party owner"),
    PARTY_DELETE("&cSorry this party was disband"),
    PARTY_FULL("&cThis party is full"),
    PARTY_NOT_REQUEST("&cSorry you haven't join party request"),
    PARTY_MAX_SIZE("&cSorry you reach max party size"),
    PARTY_REQUEST_COMING("&aYou received party join request from &6" + Argument.PLAYER),
    PARTY_LEAVE("&aYou leave the party"),
    PARTY_DISBAND("&aYou disband your's party"),
    PARTY_JOIN("&aYou join party of &6" + Argument.PLAYER),
    PARTY_ALREADY_INVITE("&cYou have already invite this player"),
    WAIT_BETWEEN_USE("&cYou must wait &e" + Argument.TIME + "s&c between uses!"),
    YOUR_PARTY("&aYou're in &6" + Argument.PLAYER + "'s&a party@@&aMembers : &6" + Argument.MEMBERS),
    PARTY_INVITE_SEND("&aYou send invitation to &6" + Argument.PLAYER),
    PARTY_INVITE_SELF("&cSorry you can't invite your self"),
    PARTY_ALREADY_IN_PARTY("&cSorry, this player is on your's party"),
    PARTY_PLAYER_NOT_IN_PARTY("&cSorry this player isn't in your's party"),
    PARTY_PLAYER_KICK("&aYou have kick &6" + Argument.PLAYER),
    PARTY_MAKE_LEADER("&aNew Leaders is &6" + Argument.PLAYER),
    PARTY_YOU_ARE_LEADER("&cYou're the leaders"),
    PARTY_GUI_NAME("Your's party"),
    PARTY_GUI_INVITE_NAME("&aInvite player"),
    PARTY_GUI_INVITE_LORE("&7Click for invite player to your's party"),
    PARTY_GUI_DISBAND_NAME("&cDisband party"),
    PARTY_GUI_DISBAND_LORE("&7Click for disband your's party"),
    PARTY_GUI_LEAVE_NAME("&cLeave party"),
    PARTY_GUI_LEAVE_LORE("&7Click for leave the party"),
    PARTY_GUI_OWNER_VIEW_MEMBER_LORE("&aMember@@&cLeft-Click &7Promote to Owner@@&cRight-Click &7Kick player"),
    PARTY_GUI_MEMBER_VIEW_MEMBER_LORE("&aMember"),
    PARTY_GUI_OWNER_LORE("&cOwner"),


    //money
    MONEY_WRONG_TYPE                            ("&cInvalid money type"),
    MONEY_GET                                   ("&a" + Argument.PLAYER + " has "+ Argument.AMOUNT + " " + Argument.MONEY_TYPE),
    MONEY_ADD                                   ("&a" + Argument.AMOUNT + " " + Argument.MONEY_TYPE + " add to" + Argument.PLAYER),
    MONEY_REMOVE                                ("&a" + Argument.AMOUNT + " " + Argument.MONEY_TYPE + " remove to" + Argument.PLAYER),
    MONEY_SET                                   ("&a" + Argument.AMOUNT + " " + Argument.MONEY_TYPE + " set to" + Argument.PLAYER),
    NO_AMOUNT_MONEY                             ("&a" + Argument.PLAYER + " dont have enough money"),
    NO_PREVIOUS_MSG                             ("&cYou don't have current discussion"),

    // /permission
        //group
    GROUP_CREATE                                ("&aThe group " + Argument.GROUP_NAME + " was created !"),
    GROUP_ALREADY_EXIST                         ("&cThe group " + Argument.GROUP_NAME + " already exist"),
    GROUP_NOT_EXIST                             ("&cThe group don't exist"),
    GROUP_DISPLAY_CHANGE                        ("&a group " + Argument.GROUP_NAME + " has now display" + Argument.DISPLAY),
    GROUP_PRIORITY_CHANGE                       ("&a group " + Argument.GROUP_NAME + " has now priority" + Argument.VALUE),
    GROUP_SAVE                                  ("&aGroup " + Argument.GROUP_NAME + " saved !"),
    GROUP_DELETE                                ("&aGroup " + Argument.GROUP_NAME + " delete !"),
    GROUP_NO_ENOUGH_CHAR                        ("&c name to short please set more than " + Argument.NUMBER + " char!"),
    GROUP_PERMISSION_ADD                        ("&aPermission " + Argument.PERMISSION_NAME + " add to" + Argument.PLAYER),
    GROUP_PERMISSION_REMOVE                     ("&aPermission " + Argument.PERMISSION_NAME + " remove to" + Argument.PLAYER),
    GROUP_PERMISSION_CANT_REMOVE                ("&cGroup " + Argument.GROUP_NAME + "  don't have permission " + Argument.PERMISSION_NAME),
    GROUP_PARENT_ADD                            ("&aParent " + Argument.GROUP_PARENT + " add to " + Argument.GROUP_NAME ),
    GROUP_PARENT_CANT_ADD                       ("&ccan't add Parent " + Argument.GROUP_PARENT + "  to " + Argument.GROUP_NAME + "  because " + Argument.GROUP_NAME + " have ealready " + Argument.GROUP_PARENT + " for parent"),
    GROUP_PARENT_REMOVE                         ("&aParent " + Argument.GROUP_PARENT + " remove to " + Argument.GROUP_NAME),
    GROUP_PARENT_CANT_REMOVE                    ("&cGroup " + Argument.GROUP_NAME + " don't have parent " + Argument.GROUP_PARENT),
    GROUP_BOOST_INVALID_TYPE                    ("&ctype of the boost is invalid"),
    GROUP_BOOST_ADD                             ("&b" + Argument.BOOST_VALUE + " &a% &b" + Argument.BOOST_TYPE + " &aboost for " + Argument.MONEY_TYPE + " &aadded to " + Argument.GROUP_NAME),

        //user
    USER_PERMISSION_ADD                         ("&aPermission " + Argument.PERMISSION_NAME + " add to " + Argument.PLAYER),
    USER_PERMISSION_REMOVE                      ("&aPermission " + Argument.PERMISSION_NAME + " remove to " + Argument.PLAYER),
    USER_GROUP_ADD                              ("&aGroup " + Argument.GROUP_NAME + " add to " + Argument.PLAYER),
    USER_GROUP_REMOVE                           ("&aGroup " + Argument.GROUP_NAME + " remove to " + Argument.PLAYER),

    //command description
    DESC_PERMISSION                             ("first step for all commands for edit permission"),
    DESC_PERMISSION_HELP                        ("get description of all command"),
    DESC_PERMISSION_LIST                        ("get list all permission of the plugin"),
    DESC_PERMISSION_GROUP                       ("first step for all commands for use group permission"),
    DESC_PERMISSION_GROUP_CREATE                ("<name> / for create new group "),
    DESC_PERMISSION_GROUP_CREATE_NAME           ("for create new group"),
    DESC_PERMISSION_GROUP_NAME                  ("for mange a group (enter valid group name for more information)"),
    DESC_PERMISSION_GROUP_DISPLAY               ("<name> / for change the display of the group (you can set space and color)"),
    DESC_PERMISSION_GROUP_DELETE                ("for delete th group"),
    DESC_PERMISSION_GROUP_DISPLAY_NAME          ("for change the display of the group (you can set space and color)"),
    DESC_PERMISSION_GROUP_PRIORITY              ("<priority> / for change the priority of the group"),
    DESC_PERMISSION_GROUP_PRIORITY_VALUE        ("for change the priority of the group"),
    DESC_PERMISSION_GROUP_SAVE                  ("Save and update the group for all server use when you finish all modification"),
    DESC_PERMISSION_GROUP_DEFAULT               ("change if the group is a default group or not"),
    DESC_PERMISSION_GROUP_INFO                  ("get all information of a group"),
    DESC_PERMISSION_GROUP_PERMISSION            ("edit permissions of the group"),
    DESC_PERMISSION_GROUP_PERMISSION_ADD        ("<permission> / add permission of the group"),
    DESC_PERMISSION_GROUP_PERMISSION_ADD_VALUE  ("add permission of the group"),
    DESC_PERMISSION_GROUP_PERMISSION_REMOVE     ("<permission> / remove permission of the group"),
    DESC_PERMISSION_GROUP_PERMISSION_REMOVE_VALUE("remove permission of the group"),
    DESC_PERMISSION_GROUP_PARENT                ("edit parents of group"),
    DESC_PERMISSION_GROUP_PARENT_ADD            ("<permission> / add parent of the group"),
    DESC_PERMISSION_GROUP_PARENT_ADD_VALUE      ("add parent of the group"),
    DESC_PERMISSION_GROUP_PARENT_REMOVE         ("<permission> / remove parent of the group"),
    DESC_PERMISSION_GROUP_PARENT_REMOVE_VALUE   ("remove parent of the group"),
    DESC_PERMISSION_GROUP_BOOST                 ("&c<type> <money type> <value> &a for add boost to group (0 to remove)"),
    DESC_PERMISSION_GROUP_BOOST_TYPE0           ("&c<money type> <value> &a for add boost to group (0 to remove)"),
    DESC_PERMISSION_GROUP_BOOST_TYPE            ("&c<value> &a for add boost to group (0 to remove)"),
    DESC_PERMISSION_GROUP_BOOST_VALID           ("&a for add boost to group (0 to remove)"),
    DESC_PERMISSION_USER                        ("first step for all commands for use user permission"),
    DESC_PERMISSION_USER_NAME                   ("for mange a user (enter valid user name for more information)"),
    DESC_PERMISSION_USER_INFO                   ("get all information of a user"),
    DESC_PERMISSION_USER_PERMISSION             ("edit permissions of the user"),
    DESC_PERMISSION_USER_PERMISSION_ADD         ("<permission> / add permission to a user"),
    DESC_PERMISSION_USER_PERMISSION_ADD_VALUE   ("add permission to a user"),
    DESC_PERMISSION_USER_PERMISSION_ADD_VALUE_TEMP("add temp permission to a user"),
    DESC_PERMISSION_USER_PERMISSION_REMOVE      ("<permission> / remove permission to a user"),
    DESC_PERMISSION_USER_PERMISSION_REMOVE_VALUE("remove permission to a user"),
    DESC_PERMISSION_USER_GROUP                  ("edit group of user"),
    DESC_PERMISSION_USER_GROUP_ADD              ("<permission> / add group to a user"),
    DESC_PERMISSION_USER_GROUP_ADD_VALUE        ("add group to a user"),
    DESC_PERMISSION_USER_GROUP_ADD_VALUE_TEMP   ("add temp group to a user"),
    DESC_PERMISSION_USER_GROUP_REMOVE           ("<permission> / remove group to a user"),
    DESC_PERMISSION_USER_GROUP_REMOVE_VALUE     ("remove group to a user"),
    DEC_BAN                                     ("<speudo> open gui"),
    DEC_BAN_USERNAME                            ("<speudo> open gui"),


    DESC_MONEY                                  ("first step for all commands for edit money of user"),
    DESC_MONEY_TYPE                             ("type of money to edit"),
    DESC_MONEY_PLAYER                           ("target player"),
    DESC_MONEY_AMOUNT                           ("amount of money to add / remove / set"),
    DESC_MONEY_ADD                              ("add money to user"),
    DESC_MONEY_REMOVE                           ("remove money to user"),
    DESC_MONEY_SET                              ("set money to user"),
    DESC_MONEY_GET                              ("get money og user"),

    DESC_FRIENDS                                 ("all commands to manage your friends"),
    DESC_FRIENDS_LIST                            ("view all your friends"),
    DESC_FRIENDS_ADD                             ("<username> add friends "),
    DESC_FRIENDS_ADD_SUB                         (" add friends "),
    DESC_FRIENDS_REMOVE_SUB                      (" remove friends "),
    DESC_FRIENDS_REMOVE                          ("<username> remove friends "),
    DESC_FRIENDS_ACCEPT                          ("accept the last invitation"),

    DESC_MSG                                    ("&c/msg <player> <msg>"),
    DESC_R                                      ("&c/r <msg>"),

    DESC_TP                                     ("first step for all commands for tp user"),
    DESC_TP_USER                                ("if no second argument tp the executor to the player, else tp player to second argument"),
    DESC_TP_A                                   ("tp all player of the server (no all proxy) to second argument"),

    BROADCAST_MESSAGE_SEND("&aMessage send"),

    DESC_BROADCAST_ALL("<message> send a specific message to all server"),
    DESC_BROADCAST_SERVER("<server> <message> send a specific message to specific server"),
    DESC_BROADCAST_SERVER_MESSAGE("<message> send a specific message to specific server"),
    DESC_BROADCAST_SERVER_TYPE("<server> <message> send a specific message to specific group of server"),
    DESC_BROADCAST_SERVER_TYPE_MESSAGE("<message> send a specific message to specific group of server"),
    DESC_BROADCAST_PROXY("<proxy> <message> send a specific message to specific proxy"),
    DESC_BROADCAST_PROXY_MESSAGE("<message> send a specific message to specific proxy"),

    DESC_WHITELIST_LIST("get all player in maintenance whitelist "),
    DESC_WHITELIST_ADD("add player in maintenance whitelist"),
    DESC_WHITELIST_REMOVE("remove player in maintenance whitelist"),

    WHITELIST_LIST("&aPlayer in whitelist: " + Argument.PLAYERS),
    WHITELIST_ADD("&aYou have add " + Argument.PLAYERS + " from the whitelist"),
    WHITELIST_REMOVE_GOOD("&aYou have remove " + Argument.PLAYERS + " from the whitelist"),
    WHITELIST_REMOVE_FAIL("&a" + Argument.PLAYERS + " is not in the whitelist"),

    DESC_MAINTENANCE_ALL("set on / off maintenance for all servers"),
    DESC_MAINTENANCE_ALL_OFF("turn off maintenance for all servers"),
    DESC_MAINTENANCE_ALL_ON("<disconnect> turn on maintenance for all servers with disconnect option"),
    DESC_MAINTENANCE_SERVER_TYPE("turn on / off maintenance for specific type of servers"),

    MAINTENANCE_ENABLE("&aEnable maintenance for " + Argument.SERVER_TYPE + " servers"),
    MAINTENANCE_DISABLE("&aDisable maintenance for " + Argument.SERVER_TYPE + " servers"),
    MAINTENANCE("&aMinetasia@@ &cSorry the server is on maintenance !@@retry later !"),


    BAN_FORMAT("&c&lYou are banned by " + Argument.PLAYER + "@@&c for the reason : " + Argument.REASON + "@@&c for " + Argument.TIME),
    MUTE_FORMAT("&c&lYou are muted by " + Argument.PLAYER + "&c for the reason : " + Argument.REASON + "&c for " + Argument.TIME),
    WARN_FORMAT("&c&lYou are warned by " + Argument.PLAYER + "&c for the reason : " + Argument.REASON + "&c for " + Argument.TIME),

    REASON_FORMAT( "&c" + Argument.REASON + "&c for the " + Argument.REPETITION + "&c times"),

    SANCTION_COMMAND_END("&aYou have &6" + Argument.SANCTION_TYPE + " &b" + Argument.PLAYER + "&a for " + Argument.TIME + "&a for reason " + Argument.REASON),
    UN_SANCTION_NOT_SANCTION("&cThis player isn't " + Argument.SANCTION_TYPE),
    UN_SANCTION_END("&aYou have &6un" + Argument.SANCTION_TYPE + " &b" + Argument.PLAYER),

    DESC_PARTY(""),
    DESC_PARTY_LIST("show all player in your team"),
    DESC_PARTY_JOIN("join the last party invitation"),
    DESC_PARTY_INVITE("<player> invite a given player to your party"),
    DESC_PARTY_LEAVE("leave your party or disband if you are leader"),
    DESC_PARTY_KICK("<player> kick a player from your party"),
    DESC_MAKER_LEADER("<player> set the new owner of the group"),
    DESC_HELP("get information about access command "),

    DESC_PLAYER_DATA_PLAYER("manage player"),
    DESC_PLAYER_DATA_PLAYER_GET("get a player data"),
    DESC_PLAYER_DATA_PLAYER_SET("<value> set a player data"),
    DESC_PLAYER_DATA_PLAYER_REMOVE("remove a player data"),

    LIST_PLAYER_HEADER("&3>>>&a&lMine&6&lTasia&r&3<<<@@" +
            "@@" +
            "&7Welcome " + Argument.PLAYER  + " on &a&lMine&6&lTasia&r &7 enjoy !@@" +
            "&7&lPING :&a " + Argument.MS + " &r&7&l| Connected on : &a" + Argument.SERVER_TYPE +
            "@@@@"),
    LIST_PLAYER_FOOTER("@@" +
            "&7&lDiscord: &r&3https://discord.minetasia.com@@" +
            "&7&lSites: &r&3https://www.minetasia.com@@" +
            "@@" +
            Argument.IP),

    //moderation
    WRONG_SANCTION("&cInvalid sanction !"),

    CHALLENGE_COMPLETE("&8________&7_______&e_______&6_______&e_______&7_______&8@@  @@&5&kl&6Challenge complete:&5&kl@@       " + Argument.CHALLENGE + "@@  @@ &8______&7_____&e_____&6_____&e_____&7_____&8"),

    REPORT("Reporting " + Argument.PLAYER),
    REPORT_SELF("&cYou can't report your self..."),
    REPORT_TYPE_FORMAT("   => " + Argument.REPORT_TYPE),
    REPORT_HOVER("&7Click for report " + Argument.PLAYER + " for " + Argument.REPORT_TYPE),
    REPORT_HOVER_ARGS("&7Click for select argument"),
    REPORT_VALIDATE("&a&lClick for report &a&l" + Argument.PLAYER),
    REPORT_CANCEL("&c&lClick for cancel &c&lreport "),
    REPORT_NO_ARGS("&7You must be select at least one argument"),
    REPORT_END("&aThanks you for reporting"),

    REPORT_WAR_MESSAGE("&d&l" + Argument.PLAYER + " &a&l warn &d&l" + Argument.TARGET + " &a&lfor &d&l" + Argument.REASON + " " + Argument.ARGUMENT + " &8&l[CLICK TO TP]"),

    //sanction gui
    FILTER_NAME("&aFilter"),
    FILTER_LORE("&8&l[TYPE]@@" +
            "&f&lLeft-Click: &d&lBan@@" +
            "&f&lMiddle-Click: &d&lMute@@" +
            "&f&lRight-Click: &d&lWarn@@" +
            "&f&lDrop: &d&lReset@@" +
            ""),
    SANCTION_HEAD_NAME("&8&l[INFO]"),
    SANCTION_HEAD_LORE(
            "&f&lType: &a" + Argument.SANCTION_TYPE + "@@" +
            "&f&lGeneric reason: &a" + Argument.GENERIC_REASON + "@@" +
            "&f&lReason: &a" + Argument.REASON + "@@" +
            "&f&lBy: &a" + Argument.PLAYER + "@@" +
            "&f&lDate: &a" + Argument.DATE + "@@" +
            "&f&lFor: &a" + Argument.TIME + "@@" +
            "&f&lStatus: " + Argument.STATUS + "@@" +
            ""),
    SIGN_FILTER_NAME("&aFilter by reason"),
    SIGN_FILTER_LOR("&8&l[TYPE]@@" +
            "&f&lLeft-Click: &d&lOpen filter@@" +
            "&f&lRight-Click: &d&lReset@@"),

    SANCTION_SIGN_FILTER_CONTENT(">@@enter sanction@@name filter@@---------------"),

    PLAYER_DATA_INVALID_KEY("&cThe have'nt this data"),
    PLAYER_DATA_REMOVE("&aData removed !"),
    PLAYER_DATA_SET("&aData set !"),
    PLAYER_DATA_SET_NO_VALUE("&cPlease set value"),
    PLAYER_DATA_GET("&aThe value of the data is " + Argument.VALUE),

    ;

    public static String prefix;
    public static MinetasiaCoreApi api;
    public static MinetasiaLang minetasiaLang;

    final String path;
    final String defaultMsg;

    Lang(@NotNull String defaultMsg)
    {
        this.path = name().toLowerCase().replace('_', '-');
        this.defaultMsg = defaultMsg;
    }

    public static void setApi(MinetasiaCoreApi api)
    {
        Lang.api = api;
    }

    @Override
    @SafeVarargs
    public final <T> String getWithoutPrefix(String lang, Tuple<? extends Args, T>... args)
    {
        return minetasiaLang.get(path, defaultMsg, lang, args);
    }

    @SafeVarargs
    @Override
    public final <T> String get(String lang, Tuple<? extends Args, T>... args)
    {
        return prefix + " " + minetasiaLang.get(path, defaultMsg, lang, args);
    }

    @SafeVarargs
    public final <T>  void sendToSender(CommandSender sender, Tuple<? extends Args, T>... args)
    {
        sender.sendMessage(get(sender instanceof Player ? api.getPlayerLang(((Player) sender).getUniqueId()) : MinetasiaLang.BASE_LANG, args));
    }

    public static final String newlineRegex = "@@";

    @SafeVarargs
    public final <T> String[] getWithSplit(String lang, Tuple<? extends Args, T>... args)
    {
        return getWithoutPrefix(lang, args).split(newlineRegex);
    }

    public enum Argument implements Args
    {
        PLAYER, PLAYER_SENDER, PLAYER_RECEIVER, MESSAGE, LANG, MONEY_TYPE, ACTUAL_BOOST, MAX_BOOST, SERVER_TYPE, REWARDS, AMOUNT, GROUP_NAME, PERMISSION_NAME,
        DISPLAY, VALUE, NUMBER, GROUP_PARENT, BOOST_VALUE, BOOST_TYPE, MEMBERS, TIME, MS, IP, COMMAND, DESCRIPTION, PAGE, MAX_PAGE, SETTINGS, REASON, REPETITION,
        SANCTION_TYPE, CHALLENGE, REPORT_TYPE, DATE, STATUS, GENERIC_REASON, TARGET, ARGUMENT, PLAYERS
        ;

        String node;

        Argument()
        {
            node = getNode();
        }

        @Override
        public String toString()
        {
            return node;
        }
    }
}
