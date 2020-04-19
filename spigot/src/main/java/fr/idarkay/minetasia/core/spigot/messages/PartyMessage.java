package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.user.CorePlayer;
import fr.idarkay.minetasia.core.spigot.user.PlayerParty;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * File <b>PartyMessage</b> located on fr.idarkay.minetasia.core.spigot.messages
 * PartyMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
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

    @NotNull
    public static String getRemove(UUID partyUUID)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.REMOVE.name(), partyUUID);
    }

    @NotNull
    public static String getRemovePlayer(UUID partyUUID, UUID playerUUID)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.REMOVE_PLAYER, partyUUID, playerUUID);
    }

    @NotNull
    public static String getAddPlayer(PlayerParty p)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.ADD_PLAYER, p.toJson());
    }

    @NotNull
    public static String getInvitePlayerMessage(Party p, UUID target, String senderName)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.INVITE, target, p.getId().toString(), senderName);
    }

    @NotNull
    public static String getPartyMakeLeaderFirst(Party p, UUID target)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.MAKE_LEADER_1, p.getId(), target);
    }

    @NotNull
    public static String getPartyMakeLeaderSecond(Party p, Document doc)
    {
        return CoreMessage.getMessage(getIdentifier(), ActionType.MAKE_LEADER_2, p.getId(), doc.toJson());
    }

    public static @NotNull String getIdentifier()
    {
        return "core-party";
    }

    public enum ActionType
    {
        MAKE_LEADER_1((core, strings) -> {
            try
            {
                core.getPartyManager().setLeaderAndUpdate(UUID.fromString(strings[2]), Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(strings[3]))));
            }
            catch (NullPointerException ignore)
            {
                //player disconnect
            }
        }),
        MAKE_LEADER_2((core, strings) -> core.getPartyManager().setLeader(UUID.fromString(strings[2]), Document.parse(GeneralUtils.concat(strings, ";", 2)))),
        INVITE((core, strings) -> {
            final CorePlayer c = core.getPlayerManager().getCorePlayer(UUID.fromString(strings[2]));
            c.setLastPartyRequest(UUID.fromString(strings[3]));

            final TextComponent textComponent = new TextComponent(Lang.PARTY_REQUEST_COMING.get(core.getPlayerLang(c.getUuid()), Lang.Argument.PLAYER.match(strings[4])));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join"));

            Bukkit.getPlayer(c.getUuid()).spigot().sendMessage(textComponent);
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
