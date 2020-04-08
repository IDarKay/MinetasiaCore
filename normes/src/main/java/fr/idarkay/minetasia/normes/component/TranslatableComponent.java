package fr.idarkay.minetasia.normes.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * File <b>TranslatableComponent</b> located on fr.idarkay.minetasia.normes.component
 * TranslatableComponent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 05/04/2020 at 04:07
 */
public class TranslatableComponent extends BaseComponent
{

    private final String src;
    private final List<BaseComponent> compounds = new ArrayList<>();

    /**
     * create Translatable component
     * @param src of the translate
     */
    public TranslatableComponent(@NotNull String src)
    {
        this.src = src;
    }

    /**
     * get translatableComponent with the name of item
     * @param material the material for th item
     * @return the component
     */
    public static TranslatableComponent getTranslatableComponentForItem(Material material)
    {
        return new TranslatableComponent((material.isBlock() ? "block" : "item") + ".minecraft." + material.name().toLowerCase());
    }

    /**
     * add argument
     * @param component argument
     */
    public void with(BaseComponent component)
    {
        compounds.add(component);
    }

    /**
     * add argument
     * @param component argument
     */
    public void with(String component)
    {
        compounds.add(new TextComponent(component));
    }

    @Override
    public JsonElement toJsonElement()
    {
        final JsonObject component = new JsonObject();
        component.addProperty("translate", src);
        if(!compounds.isEmpty())
        {
            final JsonArray array = new JsonArray();
            compounds.forEach(c -> array.add(c.toJsonElement()));
            component.add("with", array);
        }
        return component;
    }
}
