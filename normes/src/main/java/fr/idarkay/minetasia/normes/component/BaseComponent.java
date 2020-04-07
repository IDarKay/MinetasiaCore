package fr.idarkay.minetasia.normes.component;

import com.google.gson.JsonElement;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;

/**
 * File <b>BaseComponent</b> located on fr.idarkay.minetasia.normes.component
 * BaseComponent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 05/04/2020 at 03:55
 */
public abstract class BaseComponent
{

    public abstract JsonElement toJsonElement();

    public IChatBaseComponent toChatBaseComponent()
    {
        return IChatBaseComponent.ChatSerializer.a(toJsonElement());
    }


}
