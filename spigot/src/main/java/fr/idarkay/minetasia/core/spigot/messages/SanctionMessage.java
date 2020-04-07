package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.moderation.PlayerSanction;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>SanctionMessage</b> located on fr.idarkay.minetasia.core.spigot.messages
 * SanctionMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 03/04/2020 at 19:25
 */
public class SanctionMessage extends CoreMessage
{

    public SanctionMessage()
    {
        super(getIdentifier());
    }

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        final UUID target = UUID.fromString(args[1]);
        final String concat = concat(args, ";", 2);
        final Document sanction = Document.parse(concat);
        final Player player = Bukkit.getPlayer(target);
        // if player change server between send and receive
        if(player == null)
        {
            // get new server
            final PlayerStatueFix playerStatue = plugin.getPlayerStatue(target);
            //if not disconnect
            if(playerStatue != null)
            {
                plugin.publishTarget(CHANNEL, CoreMessage.getMessage(getIdentifier(), args[1], concat), playerStatue.getServer(), false, false);
            }

        }
        else
        {
            //send hud
            plugin.applySanctionToPlayerClientBound(player, PlayerSanction.fromDocument(sanction));
        }

    }

    public static @NotNull String getMessage(UUID target, PlayerSanction sanction)
    {
        return CoreMessage.getMessage(getIdentifier(), target, sanction.toDocument().toJson());
    }

    public static  @NotNull String getIdentifier()
    {
        return "core-sanction";
    }

}
