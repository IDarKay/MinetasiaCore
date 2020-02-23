package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.user.CorePlayer;
import fr.idarkay.minetasia.core.spigot.user.PlayerParty;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.Utils.GeneralUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * File <b>PartyMessage</b> located on fr.idarkay.minetasia.core.spigot.messages
 * PartyMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 22/02/2020 at 21:00
 */
public class PartyMessage extends CoreMessage
{
    public PartyMessage()
    {
        super(getIdentifier());
    }

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        ActionType.valueOf(args[1]).run(plugin, args);
    }

    public static String getRemove(UUID partyUUID)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.REMOVE.name(), partyUUID);
    }

    public static String getRemovePlayer(UUID partyUUID, UUID playerUUID)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.REMOVE_PLAYER, partyUUID, playerUUID);
    }

    public static String getAddPlayer(PlayerParty p)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.ADD_PLAYER, p.toJson());
    }

    public static String getInvitePlayerMessage(Party p, UUID target, String senderName)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.INVITE, target, p.getId().toString(), senderName);
    }

    public static  @NotNull String getIdentifier()
    {
        return "core-party";
    }

    public enum ActionType
    {
        INVITE((core, strings) -> {
            final CorePlayer c = core.getPlayerManager().getCorePlayer(UUID.fromString(strings[2]));
            c.setLastPartyRequest(UUID.fromString(strings[3]));
            Bukkit.getPlayer(c.getUuid()).sendMessage(Lang.PARTY_REQUEST_COMING.get(core.getPlayerLang(c.getUuid())
                    , Lang.Argument.PLAYER.match(strings[4])));
        }),
        ADD_PLAYER((core, strings) -> core.getPartyManager().load(Document.parse(GeneralUtils.concat(strings, ";", 2)))),
        REMOVE_PLAYER((core, strings) -> core.getPartyManager().removePlayer(UUID.fromString(strings[2]), UUID.fromString(strings[3]))),
        REMOVE((core, strings) -> core.getPartyManager().delete(UUID.fromString(strings[2])));

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
