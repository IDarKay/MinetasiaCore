package fr.idarkay.minetasia.core.spigot.utils;

import fr.idarkay.minetasia.normes.IMinetasiaLang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    // /permission
        //group
    GROUP_CREATE                                ("&aThe group %1$s was created !"),
    GROUP_ALREADY_EXIST                         ("&cThe group %1$s already exist"),
    GROUP_NOT_EXIST                             ("&cThe group don't exist"),
    GROUP_DISPLAY_CHANGE                        ("&a group %1$s has now display %2$s"),
    GROUP_PRIORITY_CHANGE                       ("&a group %1$s has now priority %2$s"),
    GROUP_SAVE                                  ("&aGroup %1$s saved !"),
    GROUP_NO_ENOUGH_CHAR                        ("&c name to short please set more than %1$s char!"),
    GROUP_PERMISSION_ADD                        ("&aPermission %1$s add to %2$s"),
    GROUP_PERMISSION_REMOVE                     ("&aPermission %1$s remove to %2$s"),
    GROUP_PERMISSION_CANT_REMOVE                ("&cGroup %1$s don't have permission %2$s"),
    GROUP_PARENT_ADD                            ("&aParent %1$s add to %2$s"),
    GROUP_PARENT_REMOVE                         ("&aParent %1$s remove to %2$s"),
    GROUP_PARENT_CANT_REMOVE                    ("&cGroup %1$s don't have parent %2$s"),

        //user
    USER_PERMISSION_ADD                         ("&aPermission %1$s add to %2$s"),
    USER_PERMISSION_REMOVE                      ("&aPermission %1$s remove to %2$s"),
    USER_GROUP_ADD                              ("&aGroup %1$s add to £2$s"),
    USER_GROUP_REMOVE                           ("&aGroup %1$s remove to £2$s"),

    //command description
    DESC_PERMISSION                             ("first step for all commands for edit permission"),
    DESC_PERMISSION_HELP                        ("get description of all command"),
    DESC_PERMISSION_LIST                        ("get list all permission of the plugin"),
    DESC_PERMISSION_GROUP                       ("first step for all commands for use group permission"),
    DESC_PERMISSION_GROUP_CREATE                ("<name> / for create new group "),
    DESC_PERMISSION_GROUP_CREATE_NAME           ("for create new group"),
    DESC_PERMISSION_GROUP_NAME                  ("for mange a group (enter valid group name for more information)"),
    DESC_PERMISSION_GROUP_DISPLAY               ("<name> / for change the display of the group (you can set space and color)"),
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
