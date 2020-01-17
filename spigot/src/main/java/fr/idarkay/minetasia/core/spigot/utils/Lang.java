package fr.idarkay.minetasia.core.spigot.utils;

import fr.idarkay.minetasia.normes.IMinetasiaLang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
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

    WELCOME                                     ( "&6Welcome to the server :)"),



    // /lang
    SET_LANG                                    ( "&aYour new lang is English"),
    GET_LANG                                    ( "&aThe lang of &6%1$s is &9%2$s"),

    // /friends
    SELF_ADD_FRIEND                             ( "&cYou really must feel alone to want to add as a friend"),
    ALREADY_FRIEND                              ( "&cYour are friend with %1$s !"),
    REQUEST_SEND_FRIENDS                        ( "&aYour friends request have been send to %1$s !"),
    REQUEST_FRIEND                              ("&a%1$s send to you a friend request use &6/friends accept&a for accept this else ignore the request but it's very sad"),
    NEW_FRIEND                                  ( "&aYou are now friend with %1$s !"),
    REMOVE_FRIEND                               ( "&a%1$s remove to your friend"),
    NOT_FRIEND                                  ( "&cYou are not friend with %1$s !"),

    //misc
    PLAYER_NOT_ONLY                             ( "&cSorry this player isn't only !"),
    PLAYER_NOT_EXIST                            ( "&cThe player don't exist"),
    ONLINE                                      ( "online"),
    OFFLINE                                     ( "offline"),
    NEED_BE_PLAYER                              ( "&cYour not player"),
    NO_PERMISSION                               ( "&cYou don't have permission"),
    ILLEGAL_NUMBER_VALUE                        ("&cPlease set valid number"),
    MSG_FORMAT                                  ("&6%1$s &c-> &6%2$s : &r%3$s"),
    MSG_FORMAT_SOCIAL_SPY                       ("&c[SS] &6%1$s &c-> &6%2$s : &r%3$s"),
    SOCIAL_SPU_ON                               ("&6SocialSpy &2on"),
    SOCIAL_SPU_OFF                              ("&6SocialSpy &4off"),
    SERVER_NOT_FOUND                            ("&cServer not found"),
    WORLD_NOT_FOUND                             ("&cWorld not found"),
    INCOMPATIBLE_CMD_TP                         ("&cYou can't tp @a to another server"),
    PLAYER_BOOST                                ("&6%1$s &aboost %2$s &afor the party (%3$s/%4$s%%)"),
    GAME_REWARDS                                ("&c=================================\n" +
                                                            "&6End game of : &a%1$s\n" +
                                                            "&6Rewards: %2$s" +
                                                            "&c================================="),

    //money
    MONEY_WRONG_TYPE                            ("&cInvalid money type"),
    MONEY_GET                                   ("&a%1$s has %2$s %3$s"),
    MONEY_ADD                                   ("&a%1$s %2$s add to %3$s"),
    MONEY_REMOVE                                ("&a%1$s %2$s remove to %3$s"),
    MONEY_SET                                   ("&a%1$s %2$s set to %3$s"),
    NO_AMOUNT_MONEY                             ("&a%1$s dont have enough money"),
    NO_PREVIOUS_MSG                             ("&cYou don't have current discussion"),

    // /permission
        //group
    GROUP_CREATE                                ("&aThe group %1$s was created !"),
    GROUP_ALREADY_EXIST                         ("&cThe group %1$s already exist"),
    GROUP_NOT_EXIST                             ("&cThe group don't exist"),
    GROUP_DISPLAY_CHANGE                        ("&a group %1$s has now display %2$s"),
    GROUP_PRIORITY_CHANGE                       ("&a group %1$s has now priority %2$s"),
    GROUP_SAVE                                  ("&aGroup %1$s saved !"),
    GROUP_DELETE                                ("&aGroup %1$s delete !"),
    GROUP_NO_ENOUGH_CHAR                        ("&c name to short please set more than %1$s char!"),
    GROUP_PERMISSION_ADD                        ("&aPermission %1$s add to %2$s"),
    GROUP_PERMISSION_REMOVE                     ("&aPermission %1$s remove to %2$s"),
    GROUP_PERMISSION_CANT_REMOVE                ("&cGroup %1$s don't have permission %2$s"),
    GROUP_PARENT_ADD                            ("&aParent %1$s add to %2$s"),
    GROUP_PARENT_CANT_ADD                       ("&ccan't add Parent %1$s to %2$s because %2$s have ealready %1$s for parent"),
    GROUP_PARENT_REMOVE                         ("&aParent %1$s remove to %2$s"),
    GROUP_PARENT_CANT_REMOVE                    ("&cGroup %1$s don't have parent %2$s"),
    GROUP_BOOST_INVALID_TYPE                    ("&c type of the boost is invalid"),
    GROUP_BOOST_ADD                             ("&b%1$s &a% &b%2$s &aboost for &b%3$s &aadded to &b%4$s"),

        //user
    USER_PERMISSION_ADD                         ("&aPermission %1$s add to %2$s"),
    USER_PERMISSION_REMOVE                      ("&aPermission %1$s remove to %2$s"),
    USER_GROUP_ADD                              ("&aGroup %1$s add to %2$s"),
    USER_GROUP_REMOVE                           ("&aGroup %1$s remove to %2$s"),

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
    DESC_TP_2_USER                              ("tp first argument to selected user"),
    DESC_TP_2_CORD                              ("tp first argument to cord"),
    DESC_TP_2_SERVER                            ("tp first argument to server"),

    ;

    public static String prefix;

    final String path;
    final String defaultMsg;

    Lang(@NotNull String defaultMsg)
    {
        this.path = name().toLowerCase().replace('_', '-');
        this.defaultMsg = defaultMsg;
    }


    public String getWithoutPrefix(String lang, Object... args)
    {
        return MinetasiaLang.get(path, defaultMsg, lang, args);
    }

    @Override
    public String get(String lang, Object... args)
    {
        return prefix + " " + MinetasiaLang.get(path, defaultMsg, lang, args);
    }

}
