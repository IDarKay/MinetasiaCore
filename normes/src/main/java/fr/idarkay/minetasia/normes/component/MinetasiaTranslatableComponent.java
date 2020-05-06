package fr.idarkay.minetasia.normes.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import fr.idarkay.minetasia.normes.Tuple;
import fr.idarkay.minetasia.normes.utils.IMComponentLang;
import fr.idarkay.minetasia.normes.utils.IMComponentLangArgsCore;
import net.minecraft.server.v1_15_R1.ChatBaseComponent;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * File <b>MinetasiaTranslatableComponent</b> located on fr.idarkay.minetasia.normes.component
 * MinetasiaTranslatableComponent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 15/04/2020 at 19:27
 *
 * Skyblock ONLY !
 *
 */
public class MinetasiaTranslatableComponent extends BaseComponent
{

    @Nullable
    private static MinetasiaLang minetasiaLang = null;
    public static final String identifier = "minetasia_translatable_component";

    public static void setMinetasiaLang(@NotNull MinetasiaLang minetasiaLang)
    {
        if(MinetasiaTranslatableComponent.minetasiaLang == null)
            MinetasiaTranslatableComponent.minetasiaLang = minetasiaLang;
        else
            throw new IllegalArgumentException("only skyblock-core can use this!");
    }

    private final boolean isArgs;
    private final String key;
    private final String defaultValue;
    private final JsonObject compact;

    private static final JsonParser JSON_PARSER = new JsonParser();

    public MinetasiaTranslatableComponent(final String json)
    {
        super();
        JsonElement parse = JSON_PARSER.parse(json);
        if(!parse.isJsonObject())
        {
            throw new IllegalArgumentException(" json need be a json object");
        }
        JsonObject jsonObject = parse.getAsJsonObject();
        Validate.isTrue(jsonObject.has("key"), "json object must have has elements : 'key'");
        this.compact = jsonObject;
        this.key = jsonObject.get("key").getAsString();
        isArgs = !jsonObject.has("defaultValue");
        if(!isArgs)
            this.defaultValue = jsonObject.get("defaultValue").getAsString();
        else
            defaultValue = null;
        TextComponent textComponent = new TextComponent((ChatComponentText) IChatBaseComponent.ChatSerializer.a(jsonObject.get("classic").getAsString()));
        copyDisplay(textComponent);
    }

    public MinetasiaTranslatableComponent(final IMComponentLang imComponentLang)
    {
        super();
        this.key = imComponentLang.getKey();
        compact = new JsonObject();
        compact.addProperty("key", key);
        if(imComponentLang instanceof IMComponentLangArgsCore)
        {
            isArgs = true;
            this.defaultValue = null;
        }
        else
        {
            isArgs = false;
            this.defaultValue = imComponentLang.getDefault();
            compact.addProperty("defaultValue", defaultValue);
        }
        compact.addProperty("classic", IChatBaseComponent.ChatSerializer.a(new TextComponent("", this).toChatBaseComponent()));

    }


    @Override
    protected @NotNull NBTTagCompound getAddon()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("key", key);
        if(isArgs)
            nbtTagCompound.setString("defaultValue", defaultValue);
        return nbtTagCompound;
    }

    @Override
    protected @NotNull ChatBaseComponent getBaseChatComponent()
    {
        return new ChatComponentText("");
    }

    @Override
    public @NotNull <R> IChatBaseComponent toChatBaseComponent(String lang, @Nullable R argsObject)
    {
        if (isArgs)
        {
            IMComponentLangArgsCore<R> registered = IMComponentLangArgsCore.<R>getRegistered(key);
            @NotNull Tuple<Args, Function<R, Object>>[] registeredArgs = registered.getArgs();
            if (registeredArgs.length > 0)
            {
                Validate.notNull(argsObject);
                //noinspection unchecked
                Tuple<Args, Object>[] args = new Tuple[registeredArgs.length];
                for (int i = 0; i < registeredArgs.length; i++)
                {
                    args[i] = new Tuple<>(registeredArgs[i].a(), registeredArgs[i].b().apply(argsObject));
                }

               return new TextComponent(Objects.requireNonNull(minetasiaLang, "Can't use this function elsewhere skyblock").get(key, registered.getDefault(), lang, args), this).toChatBaseComponent(lang, argsObject);
            }
            else
                return new TextComponent(Objects.requireNonNull(minetasiaLang, "Can't use this function elsewhere skyblock").get(key, registered.getDefault(), lang), this).toChatBaseComponent(lang, argsObject);
        }
        else
            return new TextComponent(Objects.requireNonNull(minetasiaLang, "Can't use this function elsewhere skyblock").get(key, defaultValue, lang), this).toChatBaseComponent(lang, argsObject);
    }

    @Override
    public <R> @NotNull IChatBaseComponent[] toChatBaseComponentWithSplit(String lang, R argsObject, int maxChar)
    {
        if (isArgs)
        {
            IMComponentLangArgsCore<R> registered = IMComponentLangArgsCore.<R>getRegistered(key);
            @NotNull Tuple<Args, Function<R, Object>>[] registeredArgs = registered.getArgs();
            if (registeredArgs.length > 0)
            {
                Validate.notNull(argsObject);
                //noinspection unchecked
                Tuple<Args, Object>[] args = new Tuple[registeredArgs.length];
                for (int i = 0; i < registeredArgs.length; i++)
                {
                    args[i] = new Tuple<>(registeredArgs[i].a(), registeredArgs[i].b().apply(argsObject));
                }

                return split(argsObject, maxChar, key, registered.getDefault(), lang, args);
            }
            else
                return split(argsObject, maxChar, key, registered.getDefault(), lang);
        }
        else
            return split(argsObject, maxChar, key, defaultValue, lang);

    }

    @SafeVarargs
    private final <R> @NotNull IChatBaseComponent[] split(R argsObject, int maxChar, @NotNull String path, @NotNull String defaultMsg, @NotNull String lang, Tuple<Args, Object>... args)
    {
        final List<IChatBaseComponent> back = new ArrayList<>();
        String[] spl = Objects.requireNonNull(minetasiaLang, "Can't use this function elsewhere skyblock").get(path, defaultMsg, lang, args).split("@@");

        for (String s1 : spl)
        {
            String[] split = s1.replaceAll(".{" +maxChar+ ",}?[ ]", "$0\n").split("\n");
            for (int i = 1; i < split.length; i++)
            {
                split[i] = ChatColor.getLastColors(split[i-1]) + split[i];
            }

            for (String s : split)
            {
                back.add(new TextComponent(s, this).toChatBaseComponent(lang, argsObject));
            }
        }
        return back.toArray(new IChatBaseComponent[0]);
    }

    public String toString()
    {
        return identifier + ";" + compact;
    }

}
