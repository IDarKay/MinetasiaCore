package fr.idarkay.minetasia.core.api.utils;

import java.util.List;

/**
 * File <b>Group</b> located on fr.idarkay.minetasia.core.api.utils
 * Group is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 28/11/2019 at 18:54
 * @since 1.0
 * a group is a pack of permission and other utils
 */
public interface Group {

    /**
     * get all permissions of a group
     * @return {@code List<String>}
     */
    List<String> getPermissions();

    /**
     * @return the name of the group (basic char)
     */
    String getName();

    /**
     * @return the display name of the group (can contain special char and color char)
     */
    String getDisplayName();

    /**
     * get the priority of the permission
     * the priority is use when you get the display of the group of a player,
     * if the player have deferments group it's the group with the begets priority that will be chose
     * @return byte priority
     */
    byte getPriority();

    /**
     * get all name of parent group
     * a group with a parent group inherited all permission of parent
     * @return {@code List<String>}
     */
    List<String> getParents();

}
