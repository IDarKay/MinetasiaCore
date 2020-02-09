package fr.idarkay.minetasia.core.spigot.frs;

import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * File <b>PlayerFrsMessage</b> located on fr.idarkay.minetasia.core.spigot.frs
 * PlayerFrsMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 05/02/2020 at 21:25
 */
public class PlayerFrsMessage implements CoreFRSMessage
{


    // message architecture : (core-player) {uuid} {type} {data...}


    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        if(Bukkit.getPlayer(UUID.fromString(args[1])) != null)
        {
            ActionType.valueOf(args[2]).run(plugin, args);
        }
    }

    public static @NotNull String getMessage(@NotNull UUID uuid, @NotNull ActionType actionType, Object... args )
    {
        if(!goodObject(args)) throw new IllegalArgumentException();
        if(uuid == null || actionType == null) throw new NullPointerException();
        return CoreFRSMessage.getMessage(getIdentifier(), uuid, actionType, args);
    }

    public static boolean goodObject(Object... args)
    {
        return args.length > 0;
    }

    public static  @NotNull String getIdentifier()
    {
        return "core-player";
    }


    public enum ActionType
    {
        ADD_MONEY((plugin, args) -> plugin.addPlayerMoney(UUID.fromString(args[1]), Economy.valueOf(args[3]), Float.parseFloat(args[4]), true)),
        SET_MONEY((plugin, args) -> plugin.setPlayerMoney(UUID.fromString(args[1]), Economy.valueOf(args[3]), Float.parseFloat(args[4]), true)),
        REMOVE_MONEY((plugin, args) -> plugin.removePlayerMoney(UUID.fromString(args[1]), Economy.valueOf(args[3]), Float.parseFloat(args[4]), true)),

        ADD_FRIENDS((plugin, args) -> plugin.addFriend(UUID.fromString(args[1]), UUID.fromString(args[3]))),
        REMOVE_FRIENDS((plugin, args) -> plugin.removeFriend(UUID.fromString(args[1]), UUID.fromString(args[3]))),

        PUT_GENERAL_DATA((plugin, args) -> plugin.getPlayer(UUID.fromString(args[1])).putGeneralData(args[3], args[4])),
        PUT_CUSTOM_DATA((plugin, args) -> plugin.getPlayer(UUID.fromString(args[1])).putData(args[3], args[4])),

        UPDATE_STATS((plugin, args) -> Objects.requireNonNull(plugin.getPlayerManager().get(UUID.fromString(args[1]))).reloadStats(CoreFRSMessage.concat(args, ";", 3))),

        ;

        private final BiConsumer<MinetasiaCore, String[]> action;

        ActionType(BiConsumer<MinetasiaCore, String[]> action)
        {
            this.action = action;
        }

        public void run(MinetasiaCore plugin, String[] args)
        {
            action.accept(plugin, args);
        }

    }

}
