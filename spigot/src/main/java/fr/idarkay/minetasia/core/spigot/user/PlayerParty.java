package fr.idarkay.minetasia.core.spigot.user;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
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
    private String ownerName;
    private final Map<UUID, String> members;
    private UUID owner;
    private int maxSize;


    public PlayerParty(@NotNull Player p)
    {
        Validate.notNull(p);

        this.id = UUID.randomUUID();
        setOwner(p);


        setMaxSize(p);
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
        this.owner = UUID.fromString(document.getString("owner"));
        this.ownerName = document.getString("owner_name");
        this.maxSize = document.getInteger("max_size");
        members.putAll(document.getList("members", Document.class).stream().collect(Collectors.toMap(e -> UUID.fromString(e.getString("uuid")), e -> e.getString("name"))));
    }


    public void setOwner(Player owner)
    {
        this.owner = owner.getUniqueId();
        this.ownerName = owner.getName();
    }

    public void setMaxSize(Player player)
    {
        int size = 2;

        final String perm = CommandPermission.PARTY_SIZE.getPermission() + ".";

        for (PermissionAttachmentInfo effectivePermission : player.getEffectivePermissions())
        {
            if(effectivePermission.getPermission().startsWith(perm))
            {
                try
                {
                    size = Math.max(size, Integer.parseInt(effectivePermission.getPermission().substring(perm.length())));
                }
                catch (NumberFormatException ignore) {}
            }
        }
        this.maxSize = size;
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
