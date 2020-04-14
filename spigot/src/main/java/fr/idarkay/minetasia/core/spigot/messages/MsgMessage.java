package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import fr.idarkay.minetasia.normes.Tuple;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

/**
 * File <b>MsgMessage</b> located on fr.idarkay.minetasia.core.spigot.messages
 * MsgMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 14:57
 */
public class MsgMessage extends CoreMessage
{
    public MsgMessage()
    {
        super(getIdentifier());
    }

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        final Lang lang = Lang.valueOf(args[1]);
        final UUID uuid = UUID.fromString(args[2]);
        final boolean prefix = "true".equalsIgnoreCase(args[3]);
        final Tuple<String, String>[]  argument = args.length > 4 ? rawStringToArgument(Arrays.copyOfRange(args, 4, args.length)) : null;
        final Player player = Bukkit.getPlayer(uuid);
        if(player != null)
        {
            String msg = (prefix ? lang.get(plugin.getPlayerLang(uuid)) : lang.getWithoutPrefix(plugin.getPlayerLang(uuid)));
            if(argument != null)
            {
                for (Tuple<String, String> arg : argument)
                {
                    msg = msg.replace(arg.a(), arg.b());
                }
            }
            player.sendMessage(msg);

            //for /socialspy
            if(lang == Lang.MSG_FORMAT)
            {
                // socialspy
                final String ssMsg = Lang.MSG_FORMAT_SOCIAL_SPY.getWithoutPrefix(MinetasiaLang.BASE_LANG, Lang.Argument.MESSAGE.match(msg));
                for(Player pl : plugin.socialSpyPlayer) pl.sendMessage(ssMsg);
            }
        }
    }

    @SafeVarargs
    public static @NotNull String getMessage(@NotNull Lang message, @NotNull  UUID target, boolean prefix, @NotNull Tuple<? extends Args, Object>... argument)
    {
        if(argument.length == 0)
            return CoreMessage.getMessage(getIdentifier(), message.name(), target, prefix);
        else
            return CoreMessage.getMessage(getIdentifier(), GeneralUtils.concat(new Object[] { message.name(), target, prefix}, (Object[]) argumentToRawString(argument)));
    }

    @NotNull
    private static Tuple<String, String>[] rawStringToArgument(@NotNull String[] args)
    {
        //secure in data isn't a odd number
        final int max = args.length % 2 == 0 ? args.length : args.length -1;

        final Tuple<String, String>[] arguments = new Tuple[max >> 1];

        for (int i = 0; i < max; i += 2)
        {
            arguments[i >> 1] = new Tuple<>(args[i], args[i + 1]);
        }
        return arguments;
    }

    @SafeVarargs
    @NotNull
    private static String[] argumentToRawString(@NotNull Tuple<? extends Args, Object>... argument)
    {
        final String[] back = new String[argument.length << 2];

        for (int i = 0; i < argument.length; i++)
        {
            back[i << 1] = argument[i].a().getNode();
            back[(i << 1) + 1] = argument[i].b().toString().replace(';', ':');
        }
        return back;
    }

    public static @NotNull String getIdentifier()
    {
        return "core-msg";
    }

}
