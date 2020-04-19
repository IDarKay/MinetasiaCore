package fr.idarkay.minetasia.core.spigot.executor;

import fr.idarkay.minetasia.core.api.SettingsKey;
import fr.idarkay.minetasia.core.api.utils.MinetasiaSettings;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>SettingsEditorExecutor</b> located on fr.idarkay.minetasia.core.spigot.executor
 * SettingsEditorExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 31/03/2020 at 17:17
 */
public class SettingsEditorExecutor implements TabExecutor
{

    private final MinetasiaCore plugin;

    public SettingsEditorExecutor(MinetasiaCore plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull  String[] args)
    {
        if(!commandSender.hasPermission(CommandPermission.SETTINGS_EDITOR.getPermission())) return true;
        final String lang = commandSender instanceof Player ? plugin.getPlayerLang(((Player) commandSender).getUniqueId()) : MinetasiaLang.BASE_LANG;
        if(args.length < 2)
        {
            commandSender.sendMessage(Lang.SETTINGS_COMMANDS_FORMAT.get(lang));
            return true;
        }
        final SettingsKey<?> key = SettingsKey.fromName(args[0]);
        if(key == null)
        {
            commandSender.sendMessage(Lang.SETTINGS_COMMANDS_INVALID_KEY.get(lang));
            return true;
        }
        final MinetasiaSettings set = plugin.getSettings(key);
        final String concat = GeneralUtils.concat(args, " ", 1);
        boolean isDonne = false;
        final Class<?> clazz = set.getClazz();
        if(List.class.isAssignableFrom(clazz))
        {
           set.setValue(Arrays.asList(ChatColor.translateAlternateColorCodes('&',  concat).split("@@")));
           isDonne = true;
        }
        else if(Boolean.class.isAssignableFrom(clazz))
        {
            if(concat.equalsIgnoreCase("TRUE") || concat.equalsIgnoreCase("FALSE"))
            {
                final boolean bool = concat.equalsIgnoreCase("true");
                set.setValue(bool);
                isDonne = true;
            }
            else commandSender.sendMessage(Lang.ILLEGAL_BOOLEAN_VALUE.get(lang));
        }
        else if(Number.class.isAssignableFrom(clazz))
        {
            try
            {
                if(Byte.class.isAssignableFrom(clazz))
                {
                    set.setValue(Byte.parseByte(concat));
                }
                else if(Short.class.isAssignableFrom(clazz))
                {
                    set.setValue(Short.parseShort(concat));
                }
                else if(Integer.class.isAssignableFrom(clazz))
                {
                    set.setValue(Integer.parseInt(concat));
                }
                else if(Long.class.isAssignableFrom(clazz))
                {
                    set.setValue(Long.parseLong(concat));
                }
                else if(Float.class.isAssignableFrom(clazz))
                {
                    set.setValue(Float.parseFloat(concat));
                }
                else if(Double.class.isAssignableFrom(clazz))
                {
                    set.setValue(Double.parseDouble(concat));
                }
                isDonne = true;
            } catch (NumberFormatException e)
            {
                commandSender.sendMessage(Lang.ILLEGAL_NUMBER_VALUE.get(lang));
            }

        }
        else if (String.class.isAssignableFrom(clazz))
        {
            if(set.equals(SettingsKey.MOTD))
            {
//                set.setValue(concat.replaceAll("[^%]%20", " ").replaceAll("[^%]%%[%20]", "%"));
                set.setValue(concat.replace("%20", " "));
            }
            else
            {
//                set.setValue(ChatColor.translateAlternateColorCodes('&',  concat.replaceAll("[^%]%20", " ").replaceAll("[^%]%%[%20]", "%")));
                set.setValue(ChatColor.translateAlternateColorCodes('&',  concat.replace("%20", " ")));
            }
            isDonne = true;
        }
        if(isDonne) commandSender.sendMessage(Lang.SETTINGS_COMMANDS_END.get(lang, Lang.Argument.SETTINGS.match(key.name()), Lang.Argument.VALUE.match(concat)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,  @NotNull String[] args)
    {
        if(!commandSender.hasPermission(CommandPermission.SETTINGS_EDITOR.getPermission())) return null;
        if(args.length == 0)
        {
            return Arrays.stream(SettingsKey.values()).map(SettingsKey::name).collect(Collectors.toList());
        }
        else if(args.length == 1)
        {
            return Arrays.stream(SettingsKey.values()).map(SettingsKey::name).filter(n -> n.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
        }
        else
        {
            final SettingsKey<?> key = SettingsKey.fromName(args[0]);
            if(key == null)
            {
                return Collections.singletonList(ChatColor.RED + "invalid key !");
            }
            final Class<?> clazz = key.getClazz();
            if(List.class.isAssignableFrom(clazz))
            {
                return Collections.singletonList(ChatColor.GREEN + "List : <l1 @@ l2 @@ l3...>");
            }
            else if(Boolean.class.isAssignableFrom(clazz))
            {
                if(args.length > 2 || ((!args[1].toLowerCase().startsWith("true") && !args[1].toLowerCase().startsWith("false"))))
                    return Collections.singletonList(ChatColor.RED + "invalid type set true or false");
                return Collections.singletonList(ChatColor.GREEN + "<true ; false>");
            }
            else if(Number.class.isAssignableFrom(clazz))
            {
                if(args.length > 2)
                {
                    return Collections.singletonList(ChatColor.RED + "invalid number format");
                }
                if(StringUtils.isNumeric(args[1]))
                {
                    return Collections.singletonList(ChatColor.RED + "invalid number format");
                }
                return Collections.singletonList(ChatColor.GREEN + "number <" + clazz.getSimpleName() + ">");
            }
            else if (String.class.isAssignableFrom(clazz))
            {
                return Collections.singletonList(ChatColor.GREEN + "<text>");
            }
            else
            {
                return Collections.singletonList(ChatColor.GREEN + "<" + clazz.getSimpleName() + ">");
            }
        }
    }
}
