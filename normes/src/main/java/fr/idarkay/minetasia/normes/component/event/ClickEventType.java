package fr.idarkay.minetasia.normes.component.event;

import net.minecraft.server.v1_15_R1.ChatClickable;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ClickEvent</b> located on fr.idarkay.minetasia.normes.component
 * ClickEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 08/04/2020 at 16:02
 */
public enum  ClickEventType
{
    OPEN_URL("open_url", ChatClickable.EnumClickAction.OPEN_URL),
    RUN_COMMAND("run_command", ChatClickable.EnumClickAction.RUN_COMMAND),
    SUGGEST_COMMAND("suggest_command", ChatClickable.EnumClickAction.SUGGEST_COMMAND),
    CHANGE_PAGE("change_page", ChatClickable.EnumClickAction.CHANGE_PAGE),
    COPY_TO_CLIPBOARD("copy_to_clipboard", ChatClickable.EnumClickAction.COPY_TO_CLIPBOARD)
    ;

    private final String key;
    private final ChatClickable.EnumClickAction nms;

    ClickEventType(String key, ChatClickable.EnumClickAction nms)
    {
        this.key = key;
        this.nms = nms;
    }

    public ChatClickable.EnumClickAction getNms()
    {
        return nms;
    }

    public String getKey()
    {
        return key;
    }

    @NotNull
    public static ClickEventType fromNms(@NotNull ChatClickable.EnumClickAction key)
    {
        Validate.notNull(key);
        for (ClickEventType value : values())
        {
            if(value.nms.equals(key)) return value;
        }
        throw new IllegalArgumentException();
    }

}
