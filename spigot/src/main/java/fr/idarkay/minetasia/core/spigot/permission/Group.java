package fr.idarkay.minetasia.core.spigot.permission;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * File <b>Group</b> located on fr.idarkay.minetasia.core.spigot.permission
 * Group is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
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

    public Group(PermissionManager pm, String name)
    {
        this.name = name;
        this.displayName = name;
        this.pm = pm;
    }

    public Group(String jsonS, PermissionManager pm)
    {
        this.pm = pm;
        JsonObject json = PARSER.parse(jsonS).getAsJsonObject();
        name = json.get("name").getAsString();
        displayName = json.get("displayName").getAsString();
        priority = json.get("priority").getAsByte();

        JsonArray a = json.getAsJsonArray("permission");
        for (JsonElement jsonElement : a)
        {
            permissions.add(jsonElement.getAsString());
        }

        JsonArray b = json.getAsJsonArray("parents");
        for (JsonElement jsonElement : b)
        {
            parents.add(jsonElement.getAsString());
        }

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

        return p.toString();
    }

}