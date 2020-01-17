package fr.idarkay.minetasia.core.spigot.permission;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.api.BoostType;
import fr.idarkay.minetasia.core.api.utils.Boost;
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
    private final List<String> permissions = new ArrayList<>();
    private final List<String> parents = new ArrayList<>();
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
    }

    public Group(String jsonS, PermissionManager pm)
    {
        this.pm = pm;
        JsonObject json = PARSER.parse(jsonS).getAsJsonObject();
        name = json.get("name").getAsString();
        displayName = json.get("displayName").getAsString();
        priority = json.get("priority").getAsByte();

        JsonArray a = json.getAsJsonArray("permission");
        if(a != null)
        {
            for (JsonElement jsonElement : a)
            {
                permissions.add(jsonElement.getAsString());
            }
        }

        JsonArray b = json.getAsJsonArray("parents");
        if(b != null)
        {
            for (JsonElement jsonElement : b)
            {
                parents.add(jsonElement.getAsString());
            }
        }

        JsonObject b0 = json.getAsJsonObject("personalboosts");
        if(b0 != null)
        {
            Map<BoostType, Float> map = b0.entrySet().stream().collect(Collectors.toMap(e -> BoostType.valueOf(e.getKey()), e -> e.getValue().getAsFloat()));
            personalBoost = () -> map;
        }
        else personalBoost = HashMap::new;


        JsonObject pb0 = json.getAsJsonObject("partyboosts");
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

    public String toJson()
    {
        JsonObject p = new JsonObject();
        p.addProperty("name", name);
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
        p.add("personalboosts", bo);

        JsonObject pbo = new JsonObject();
        partyBoost.getBoost().forEach((k, v) -> pbo.addProperty(k.name(), v));
        p.add("partyboosts", bo);

        return p.toString();
    }

}
