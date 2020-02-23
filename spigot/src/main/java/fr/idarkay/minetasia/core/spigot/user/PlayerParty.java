package fr.idarkay.minetasia.core.spigot.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.idarkay.minetasia.core.api.utils.Party;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * File <b>PlayerParty</b> located on fr.idarkay.minetasia.core.spigot.user
 * PlayerParty is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 22/02/2020 at 14:59
 */
public class PlayerParty implements Party
{

    private final UUID id;
    private final String ownerName;
    private final Map<UUID, String> members;
    private final UUID owner;
    private final int maxSize;


    public PlayerParty(@NotNull Player p)
    {
        Validate.notNull(p);

        this.id = UUID.randomUUID();
        this.ownerName = p.getName();
        this.owner = p.getUniqueId();
        this.maxSize = 10; //todo change
        this.members = new HashMap<>(maxSize);
        this.members.put(owner, ownerName);

    }

    public PlayerParty(@NotNull Document document)
    {
        Validate.notNull(document);

        this.id = UUID.fromString(document.getString("_id"));
        this.owner = UUID.fromString(document.getString("owner"));
        this.ownerName = document.getString("owner_name");
        this.maxSize = document.getInteger("max_size");
        this.members = document.getList("members", Document.class).stream().collect(Collectors.toMap(e -> UUID.fromString(e.getString("uuid")), e -> e.getString("name")));

    }

    public void update(@NotNull Document document)
    {
        members.clear();
        members.putAll(document.getList("members", Document.class).stream().collect(Collectors.toMap(e -> UUID.fromString(e.getString("uuid")), e -> e.getString("name"))));
    }

    public PlayerParty(@NotNull String json)
    {
        this(Document.parse(json));
    }

    public UUID getId()
    {
        return id;
    }

    @Override
    public UUID getOwner()
    {
        return owner;
    }

    @Override
    public String getOwnerName()
    {
        return ownerName;
    }

    @Override
    public Map<UUID, String> getPlayers()
    {
        return members;
    }

    public void removePlayer(UUID uuid)
    {
        members.remove(uuid);
    }

    public void addPlayers(UUID uuid, String name)
    {
        members.put(uuid, name);
    }

    @Override
    public int limitSize()
    {
        return maxSize;
    }

    public Document toDocument()
    {
        return new Document("_id", id.toString())
                .append("owner", owner.toString())
                .append("owner_name", ownerName)
                .append("max_size", maxSize)
                .append("members", members.entrySet().stream().map(e -> new Document("uuid", e.getKey().toString()).append("name", e.getValue())).collect(Collectors.toList()));
    }

    public String toJson()
    {
        return toDocument().toJson();
    }

    @Override
    public boolean equals(Object obj)
    {
        return this == obj || (obj instanceof PlayerParty && owner.equals(((PlayerParty) obj).getOwner()));
    }
}
