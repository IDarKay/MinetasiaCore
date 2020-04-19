package fr.idarkay.minetasia.normes.component;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ClientOption</b> located on fr.idarkay.minetasia.normes.component
 * ClientOption is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/04/2020 at 17:21
 */
public enum ClientOption
{
    ATTACK("key.attack"),
    USE("key.use"),
    FORWARD("key.forward"),
    LEFT("key.left"),
    BACK("keu.back"),
    RIGHT("key.right"),
    JUMP("key.jump"),
    SPRINT("key.sprint"),
    DROP("key.drop"),
    INVENTORY("key.inventory"),
    CHAT("key.chat"),
    PLAYER_LIST("key.playerlist"),
    PICK_ITEM("key.pickItem"),
    COMMAND("key.command"),
    SCREENSHOT("key.screenshot"),
    TOGGLE_PERSPECTIVE("key.togglePerspective"),
    SMOOTH_CAMERA("key.smoothCamera"),
    FULLSCREEN("key.fullscreen"),
    SPECTATOR_OUTLINES("key.spectatorOutlines"),
    SWAMP_HANDS("key.swapHands"),
    SAVE_TOOLBAR_ACTIVATOR("key.saveToolbarActivator"),
    LOAD_TOOLBAR_ACTIVATOR("key.loadToolbarActivator"),
    ADVANCEMENTS("key.advancements"),
    KEY_HOT_BAR_1("key.hotbar.1"),
    KEY_HOT_BAR_2("key.hotbar.2"),
    KEY_HOT_BAR_3("key.hotbar.3"),
    KEY_HOT_BAR_4("key.hotbar.4"),
    KEY_HOT_BAR_5("key.hotbar.5"),
    KEY_HOT_BAR_6("key.hotbar.6"),
    KEY_HOT_BAR_7("key.hotbar.7"),
    KEY_HOT_BAR_8("key.hotbar.8"),
    KEY_HOT_BAR_9("key.hotbar.9"),
    ;

    @NotNull
    private final String key;

    ClientOption(@NotNull String key)
    {
        this.key = key;
    }

    @NotNull
    public String getKey()
    {
        return key;
    }

    @NotNull
    public static ClientOption fromKey(@NotNull String key)
    {
        Validate.notNull(key);
        for (ClientOption value : values())
        {
            if(value.getKey().equals(key)) return value;
        }
        throw new IllegalArgumentException();
    }

}