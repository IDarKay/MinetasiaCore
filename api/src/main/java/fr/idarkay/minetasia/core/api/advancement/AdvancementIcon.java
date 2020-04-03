package fr.idarkay.minetasia.core.api.advancement;


import com.google.gson.JsonObject;
import fr.idarkay.minetasia.normes.utils.BukkitUtils;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * File <b>AdvancementIcon</b> located on fr.idarkay.minetasia.core.spigot.advancement
 * AdvancementIcon is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 20/03/2020 at 23:28
 */
public class AdvancementIcon
{

    @NotNull private final NamespacedKey material;
    @Nullable private final String nbt;

    public AdvancementIcon(@NotNull Material material)
    {
        this.material = Objects.requireNonNull(material).getKey();
        this.nbt = null;
    }

    public AdvancementIcon(@NotNull Material material, @Nullable JsonObject nbt)
    {
        this.material = Objects.requireNonNull(material).getKey();
        this.nbt = nbt == null ? null : nbt.toString();
    }

    public AdvancementIcon(@NotNull Material material, @Nullable String nbt)
    {
        this.material = Objects.requireNonNull(material).getKey();
        this.nbt = nbt;
    }

    public AdvancementIcon(@NotNull NamespacedKey material, @Nullable String nbt)
    {
        this.material = material;
        this.nbt = nbt == null ? null : nbt.toString();
    }

    public static AdvancementIcon fromDocument(@NotNull Document document)
    {
        Validate.notNull(document);
        return new AdvancementIcon(
                BukkitUtils.namespaceKeyFromSting(document.getString("item")),
                document.getString("nbt")
        );
    }

    public Document toDocument()
    {
        final Document document =  new Document();
        document.append("item", material.toString());
        if(nbt != null)
        {
            document.append("nbt", nbt);
        }
        return document;
    }

    public JsonObject toJson()
    {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", material.toString());
        if(nbt != null)
        {
            jsonObject.addProperty("nbt", nbt);
        }
        return jsonObject;
    }
}
