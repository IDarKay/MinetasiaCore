package fr.idarkay.minetasia.core.spigot.permission;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.api.BoostType;
import fr.idarkay.minetasia.core.api.utils.Boost;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * File <b>Group</b> located on fr.idarkay.minetasia.core.spigot.permission
 * Group is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 28/11/2019 at 18:49
 */
public class Group implements fr.idarkay.minetasia.core.api.utils.Group {

    private final static JsonParser PARSER = new JsonParser();

    private final PermissionManager pm;
    private final List<String> permissions;
    private final List<String> parents;
    private final String name;
    private String displayName;
    private byte priority = 0;
    private Boost personalBoost, partyBoost;

    public Group(PermissionManager pm, String name)
    {
        this.name = name;
        this.displayName = name;
        this.pm = pm;
        this.personalBoost = HashMap::new;
        this.partyBoost = HashMap::new;
        this.permissions = new ArrayList<>();
        this.parents = new ArrayList<>();
    }

    public Group(Document d, PermissionManager pm)
    {
        this.pm = pm;
        name = d.getString("_id");
        displayName = d.getString("displayName");
        priority = (byte) d.getInteger("priority", 0);
        permissions = d.getList("permission", String.class);
        parents = d.getList("parents", String.class);

        final Map<BoostType, Float> personalBoosts = new HashMap<>();
        d.get("personal_boosts", Document.class).forEach((k, v) -> personalBoosts.put(BoostType.valueOf(k), (float) v));
        this.personalBoost = () -> personalBoosts;

        final Map<BoostType, Float> partyBoost = new HashMap<>();
        d.get("party_boosts", Document.class).forEach((k, v) -> partyBoost.put(BoostType.valueOf(k), (float) v));
        this.partyBoost = () -> partyBoost;
    }

    public Group(String jsonS, PermissionManager pm)
    {
        this.pm = pm;
        JsonObject json = PARSER.parse(jsonS).getAsJsonObject();
        name = json.get("_id").getAsString();
        displayName = json.get("displayName").getAsString();
        priority = json.get("priority").getAsByte();

        permissions = new ArrayList<>();
        JsonArray a = json.getAsJsonArray("permission");
        if(a != null)
        {
            for (JsonElement jsonElement : a)
            {
                permissions.add(jsonElement.getAsString());
            }
        }

        parents = new ArrayList<>();

        JsonArray b = json.getAsJsonArray("parents");
        if(b != null)
        {
            for (JsonElement jsonElement : b)
            {
                parents.add(jsonElement.getAsString());
            }
        }

        JsonObject b0 = json.getAsJsonObject("personal_boosts");
        if(b0 != null)
        {
            Map<BoostType, Float> map = b0.entrySet().stream().collect(Collectors.toMap(e -> BoostType.valueOf(e.getKey()), e -> e.getValue().getAsFloat()));
            personalBoost = () -> map;
        }
        else personalBoost = HashMap::new;


        JsonObject pb0 = json.getAsJsonObject("party_boosts");
        if(pb0 != null)
        {
            Map<BoostType, Float> map2 = pb0.entrySet().stream().collect(Collectors.toMap(e -> BoostType.valueOf(e.getKey()), e -> e.getValue().getAsFloat()));
            partyBoost = () -> map2;
        }
        else partyBoost = HashMap::new;

    }

    @Override
    public List<String> getPermissions()
    {
        List<String> pe = new ArrayList<>(permissions);
        for(String p : parents)
        {
            Group g = pm.groups.get(p);
            if(g != null)
            {
                pe.addAll(g.getPermissions());
            }
        }

        return pe;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public byte getPriority() {
        return priority;
    }

    @Override
    public List<String> getParents() {
        return parents;
    }

    @Override
    public Boost getPersonalBoost()
    {
        return personalBoost;
    }

    public void setPersonalBoost(@NotNull BoostType type, float amount)
    {
        Map<BoostType, Float> m = personalBoost.getBoost();
        if(amount == 0)
        {
            m.remove(type);
        }
        else
            m.put(Objects.requireNonNull(type), amount);
        personalBoost = () -> m;
    }

    @Override
    public Boost getPartyBoost()
    {
        return partyBoost;
    }

    public void setPartyBoost(@NotNull BoostType type, float amount)
    {
        Map<BoostType, Float> m = partyBoost.getBoost();
        m.put(Objects.requireNonNull(type), amount);
        partyBoost = () -> m;
    }

    public void addPermissions(String... permissions)
    {
        this.permissions.addAll(Arrays.asList(permissions));
    }

    public boolean removePermissions(String permission)
    {
        return this.permissions.remove(permission);
    }

    public void addParents(String... parents)
    {
        this.parents.addAll(Arrays.asList(parents));
    }

    public boolean removeParent(String permission)
    {
        return this.parents.remove(permission);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Document toDocument()
    {
        final Document d = new Document("_id", name);
        d.append("displayName", displayName);
        d.append("priority", priority);
        d.append("permission", permissions);
        d.append("parents", parents);

        final Document partyBoost = new Document();
        this.partyBoost.getBoost().forEach((k, v) -> partyBoost.append(k.name(), v));
        d.append("party_boosts", partyBoost);

        final Document personalBoost = new Document();
        this.personalBoost.getBoost().forEach((k, v) -> personalBoost.append(k.name(), v));
        d.append("personal_boosts", personalBoost);
        return d;
    }

    public String toJson()
    {
        JsonObject p = new JsonObject();
        p.addProperty("_id", name);
        p.addProperty("displayName", displayName);
        p.addProperty("priority", priority);

        JsonArray a = new JsonArray();
        permissions.forEach(a::add);
        p.add("permission", a);

        JsonArray b = new JsonArray();
        parents.forEach(b::add);
        p.add("parents", b);

        JsonObject bo = new JsonObject();
        personalBoost.getBoost().forEach((k, v) -> bo.addProperty(k.name(), v));
        p.add("personal_boosts", bo);

        JsonObject pbo = new JsonObject();
        partyBoost.getBoost().forEach((k, v) -> pbo.addProperty(k.name(), v));
        p.add("party_boosts", bo);

        return p.toString();
    }

}
