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
 * @author Alois. B. (IDarKay),
 * Created the 13/11/2019 at 15:44
 */
public enum Lang implements IMinetasiaLang {

    WELCOME                                     ( "&6Welcome" + Argument.PLAYER + "to the server :)"),



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

    //misc
    PLAYER_NOT_ONLINE                           ( "&cSorry this player isn't only !"),
    PLAYER_NOT_EXIST                            ( "&cThe player don't exist"),
    ONLINE                                      ( "online"),
    OFFLINE                                     ( "offline"),
    NEED_BE_PLAYER                              ( "&cYour not player"),
    NO_PERMISSION                               ( "&cYou don't have permission"),
    ILLEGAL_NUMBER_VALUE                        ("&cPlease set valid number"),
    MSG_FORMAT                                  ("&6" + Argument.PLAYER_SENDER + " &c-> " + Argument.PLAYER_RECEIVER  + " : " + Argument.MESSAGE),
    MSG_FORMAT_SOCIAL_SPY                       ("&c[SS] &6" + Argument.PLAYER_SENDER + " &c-> " + Argument.PLAYER_RECEIVER  + " : " + Argument.MESSAGE ),
    SOCIAL_SPU_ON                               ("&6SocialSpy &2on"),
    SOCIAL_SPU_OFF                              ("&6SocialSpy &4off"),
    SERVER_NOT_FOUND                            ("&cServer not found"),
    WORLD_NOT_FOUND                             ("&cWorld not found"),
    INCOMPATIBLE_CMD_TP                         ("&cYou can't tp @a to another server"),
    PLAYER_BOOST                                ("&6 " + Argument.PLAYER + " &aboost " + Argument.MONEY_TYPE + " &afor the party (" + Argument.ACTUAL_BOOST + " /" + Argument.MAX_BOOST + "%)"),
    GAME_REWARDS                                ("&c==========================================\n" +
                                                            "&6End game of : &a" + Argument.SERVER_TYPE +
                                                            "\n&6Rewards: " + Argument.REWARDS +
                                                            "&c=========================================="),

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

    DESC_MONEY                                  ("first step for all commands for edit money of user"),
    DESC_MONEY_TYPE                             ("type of money to edit"),
    DESC_MONEY_PLAYER                           ("target player"),
    DESC_MONEY_AMOUNT                           ("amount of money to add / remove / set"),
    DESC_MONEY_ADD                              ("add money to user"),
    DESC_MONEY_REMOVE                           ("remove money to user"),
    DESC_MONEY_SET                              ("set money to user"),
    DESC_MONEY_GET                              ("get money og user"),

    DESC_MSG                                    ("&c/msg <player> <msg>"),
    DESC_R                                      ("&c/r <msg>"),

    DESC_TP                                     ("first step for all commands for tp user"),
    DESC_TP_USER                                ("if no second argument tp the executor to the player, else tp player to second argument"),
    DESC_TP_A                                   ("tp all player of the server (no all proxy) to second argument"),

    ;

    public static String prefix;
    public static MinetasiaCoreApi api;

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

    @SafeVarargs
    public final <T> String getWithoutPrefix(String lang, Tuple<? extends Args, T>... args)
    {
        return MinetasiaLang.get(path, defaultMsg, lang, args);
    }

    @SafeVarargs
    @Override
    public final <T> String get(String lang, Tuple<? extends Args, T>... args)
    {
        return prefix + " " + MinetasiaLang.get(path, defaultMsg, lang, args);
    }

    @SafeVarargs
    public final <T>  void sendToSender(CommandSender sender, Tuple<? extends Args, T>... args)
    {
        sender.sendMessage(get(sender instanceof Player ? api.getPlayerLang(((Player) sender).getUniqueId()) : MinetasiaLang.BASE_LANG, args));
    }


    public enum Argument implements Args
    {
        PLAYER, PLAYER_SENDER, PLAYER_RECEIVER, MESSAGE, LANG, MONEY_TYPE, ACTUAL_BOOST, MAX_BOOST, SERVER_TYPE, REWARDS, AMOUNT, GROUP_NAME, PERMISSION_NAME,
        DISPLAY, VALUE, NUMBER, GROUP_PARENT, BOOST_VALUE, BOOST_TYPE
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
