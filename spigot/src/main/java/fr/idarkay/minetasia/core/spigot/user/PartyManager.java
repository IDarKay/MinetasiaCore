package fr.idarkay.minetasia.core.spigot.user;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.PartyMessage;
import fr.idarkay.minetasia.core.spigot.server.MineServer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * File <b>PartyManager</b> located on fr.idarkay.minetasia.core.spigot.user
 * PartyManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 22/02/2020 at 15:19
 */
public class PartyManager
{
    private final MinetasiaCore core;
    private final Map<UUID, PlayerParty> partyMap = new HashMap<>();
    private final Map<UUID, PlayerParty> playersParty = new HashMap<>();

    public PartyManager(MinetasiaCore core)
    {
       this.core = core;
    }

    public PlayerParty load(@NotNull Document document)
    {
        PlayerParty p = getPartyMap().get(UUID.fromString(document.getString("_id")));
        if(p  == null)
        {
            p = new PlayerParty(document);
        }
        else
            p.update(document);

        load(p);
        return p;
    }

    public synchronized Map<UUID, PlayerParty> getPartyMap()
    {
        return partyMap;
    }

    public synchronized Map<UUID, PlayerParty> getPlayersParty()
    {
        return playersParty;
    }

    public PlayerParty createParty(Player player)
    {
        final PlayerParty p = new PlayerParty(player, core.getPlayerData(player.getUniqueId(), "head_texture", String.class));
        load(p);
        core.getMongoDbManager().getCollection(MongoCollections.ONLINE_USERS).updateMany(
                Filters.eq(player.getUniqueId().toString()), Updates.set("party_id", p.getId().toString())
        );

        core.getMongoDbManager().insert(MongoCollections.PARTY, p.toDocument());
        return p;
    }

    @Nullable
    public PlayerParty getParty(UUID pUUID)
    {
        PlayerParty p = getPartyMap().get(pUUID);
        if(p  == null)
        {
            try
            {
                p = new PlayerParty(core.getMongoDbManager().getByKey(MongoCollections.PARTY, pUUID.toString()));
            } catch (NullPointerException ignore) { }
        }
        return p;
    }

    public void load(PlayerParty p)
    {
        getPartyMap().put(p.getId(), p);
        boolean add = false;
        for (UUID k : p.getPlayers().keySet())
        {
            if(Bukkit.getPlayer(k) != null)
            {
                getPlayersParty().put(k, p);
                add = true;
            }
        }
        if(!add)
        {
            getPartyMap().remove(p.getId());
        }
    }

    @Nullable
    public PlayerParty getByPlayer(UUID uuid)
    {
        return getPlayersParty().get(uuid);
    }

    public void deleteAndUpdate(UUID id)
    {
        final PlayerParty p = getPartyMap().get(id);
        if(p != null)
        {
            if(delete(id))
            {
                final Set<String> past = new HashSet<>();
                past.add(core.getThisServer().getName());
                core.getMongoDbManager().getWithReferenceAndMatch(MongoCollections.ONLINE_USERS, "party_id", id.toString(),"servers", "server_id", "_id", "server").forEach(d -> {
                    if(!past.contains(d.getString("server_id")))
                    {
                        past.add(d.getString("server_id"));

                        final MineServer s = MineServer.getServerFromDocument(d.getList("server", Document.class).get(0));
                        core.publishTarget(CoreMessage.CHANNEL, PartyMessage.getRemove(id), s, false, true);
                    }
                });
            }


            core.getMongoDbManager().getCollection(MongoCollections.ONLINE_USERS).updateMany(
                    Filters.eq("party_id", id.toString()), Updates.unset("party_id")
            );

            core.getMongoDbManager().delete(MongoCollections.PARTY, id.toString());
        }
    }

    public boolean delete(UUID id)
    {
        final PlayerParty p = getPartyMap().remove(id);
        boolean notAll = false;
        if(p != null)
            for (UUID uuid : p.getPlayers().keySet())
            {
                notAll = getPlayersParty().remove(uuid) == null || notAll;
            }
        return notAll;
    }



    private boolean isNotAllPartyPlayerIsInThisServer(PlayerParty p)
    {
        boolean notAll = false;
        if(p != null)
            for (UUID uuid : p.getPlayers().keySet())
            {
                notAll = getPlayersParty().containsKey(uuid) || notAll;
            }
        return notAll;
    }

    private boolean isNoOnePlayerPartyInThisServer(PlayerParty p)
    {
        if(p != null)
        {
            for (UUID uuid : p.getPlayers().keySet())
            {
                if(Bukkit.getPlayer(uuid) != null) return false;
            }
        }
        return true;
    }

    public void setLeaderAndUpdate(@NotNull UUID pUUID, Player player)
    {
        final PlayerParty party = getPartyMap().get(pUUID);

        party.setMaxSize(player);
        party.setOwner(player);

        final String id = pUUID.toString();
        final Document doc = party.toDocument();
        if(setLeader(pUUID, doc))
        {
            final Set<String> past = new HashSet<>();
            past.add(core.getThisServer().getName());
            core.getMongoDbManager().getWithReferenceAndMatch(MongoCollections.ONLINE_USERS, "party_id", id,"servers", "server_id", "_id", "server").forEach(d -> {
                if(!past.contains(d.getString("server_id")))
                {
                    past.add(d.getString("server_id"));

                    final MineServer s = MineServer.getServerFromDocument(d.getList("server", Document.class).get(0));
                    core.publishTarget(CoreMessage.CHANNEL, PartyMessage.getPartyMakeLeaderSecond(party, doc), s, false, true);
                }
            });
        }

        core.getMongoDbManager().replace(MongoCollections.PARTY, pUUID.toString(), doc);
    }

    public boolean setLeader(UUID pUUID, Document doc)
    {
        final PlayerParty party = getPartyMap().get(pUUID);
        party.update(doc);
        load(party);
        return isNotAllPartyPlayerIsInThisServer(party);
    }

    public void removePlayerAdnUpdate(@NotNull UUID pUUID, UUID player)
    {
        final PlayerParty party = getPartyMap().get(pUUID);
        if(party.getPlayers().size() == 2) deleteAndUpdate(party.getId());
        else
        {
            final String id = pUUID.toString();
            if(removePlayer(pUUID, player))
            {
                final Set<String> past = new HashSet<>();
                past.add(core.getThisServer().getName());
                core.getMongoDbManager().getWithReferenceAndMatch(MongoCollections.ONLINE_USERS, "party_id", id,"servers", "server_id", "_id", "server").forEach(d -> {
                    if(!past.contains(d.getString("server_id")))
                    {
                        past.add(d.getString("server_id"));

                        final MineServer s = MineServer.getServerFromDocument(d.getList("server", Document.class).get(0));
                        core.publishTarget(CoreMessage.CHANNEL, PartyMessage.getRemovePlayer(pUUID, player), s, false, true);
                    }
                });
            }


            core.getMongoDbManager().getCollection(MongoCollections.ONLINE_USERS).updateOne(
                    Filters.eq(player.toString()), Updates.unset("party_id")
            );

            core.getMongoDbManager().replace(MongoCollections.PARTY, pUUID.toString(), party.toDocument());
        }
    }

    public boolean removePlayer(@NotNull UUID pUUID, UUID player)
    {
        final PlayerParty party = getPartyMap().get(pUUID);

        if(party.getPlayers().size() == 2)
        {
            delete(party.getId());
            return false;
        }

        party.removePlayer(player);
        getPlayersParty().remove(player);

        if(isNoOnePlayerPartyInThisServer(party)) getPartyMap().remove(party.getId());
        else load(party);

        return isNotAllPartyPlayerIsInThisServer(party);
    }

    public void disconnectPlayer(@NotNull UUID player)
    {
        final PlayerParty party = getPlayersParty().remove(player);
        if(party != null)
        {
            if(isNoOnePlayerPartyInThisServer(party)) {
                getPartyMap().remove(party.getId());
            }
        }

    }

    public void addPlayerAndUpdate(@NotNull PlayerParty p, @NotNull  UUID player, @NotNull  String name, @Nullable String texture)
    {
        final String id = p.getId().toString();
        p.addPlayers(player, name, texture);
        load(p);
        if(isNotAllPartyPlayerIsInThisServer(p))
        {
            final Set<String> past = new HashSet<>();
            past.add(core.getThisServer().getName());
            core.getMongoDbManager().getWithReferenceAndMatch(MongoCollections.ONLINE_USERS, "party_id", id,"servers", "server_id", "_id", "server").forEach(d -> {
                if(!past.contains(d.getString("server_id")))
                {
                    try
                    {
                        final MineServer s = MineServer.getServerFromDocument(d.getList("server", Document.class).get(0));
                        core.publishTarget(CoreMessage.CHANNEL, PartyMessage.getAddPlayer( p), s, false, true);
                        past.add(d.getString("server_id"));
                    } catch (ArrayIndexOutOfBoundsException e)
                    {
                        // some time when disconnect
                    }

                }
            });
        }

        core.getMongoDbManager().getCollection(MongoCollections.ONLINE_USERS).updateOne(
                Filters.eq(player.toString()), Updates.set("party_id", id)
        );

        core.getMongoDbManager().replace(MongoCollections.PARTY, id, p.toDocument());
    }
}
