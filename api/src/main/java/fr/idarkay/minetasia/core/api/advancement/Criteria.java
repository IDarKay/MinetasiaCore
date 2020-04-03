package fr.idarkay.minetasia.core.api.advancement;

import com.google.gson.JsonObject;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * File <b>Criteria</b> located on fr.idarkay.minetasia.core.api.advancement
 * Criteria is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 23/03/2020 at 11:35
 */
public class Criteria
{

    public static final Criteria IMPOSSIBLE = new Criteria("impossible", "minecraft:impossible", null);

    @NotNull private final String trigger, name;
    @Nullable private final JsonObject jsonCondition;
    @Nullable private final Document documentCondition;

    public Criteria(@NotNull String name, @NotNull String trigger, @Nullable Map<String, String> condition)
    {
        this.name = name;
        this.trigger = Objects.requireNonNull(trigger);
        if(condition == null)
        {
            jsonCondition = null;
            documentCondition = null;
        }
        else
        {
            jsonCondition = new JsonObject();
            documentCondition = new Document();
            condition.forEach((k, v) -> {
                jsonCondition.addProperty(k, v);
                documentCondition.append(k, v);
            });
        }
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @NotNull
    public JsonObject toJson()
    {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("trigger", trigger);
        if(jsonCondition != null)
        {
            jsonObject.add("condition", jsonCondition);
        }
        return jsonObject;
    }

    @NotNull
    public Document toDocument()
    {
        final Document document = new Document();
        document.append("trigger", trigger);
        if(documentCondition != null)
        {
            document.append("conditon", documentCondition);
        }
        return document;
    }


    public static Criteria fromDocument(String name, Document document)
    {
        final Document preCondition = document.get("conditon", Document.class);
        return new Criteria( name,
                Objects.requireNonNull(document.getString("trigger"), "no trigger in document  : " + document.toJson()),
                preCondition == null ? null : preCondition.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()))
        );
    }
}
