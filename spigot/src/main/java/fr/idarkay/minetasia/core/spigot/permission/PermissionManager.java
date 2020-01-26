package fr.idarkay.minetasia.core.spigot.permission;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.api.event.PlayerPermissionLoadEndEvent;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.user.Player;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * File <b>PermissionManager</b> located on fr.idarkay.minetasia.core.spigot.permission
 * PermissionManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 28/11/2019 at 20:22
 */
@SuppressWarnings("unused")
public class PermissionManager {

    private final static JsonParser PARSER = new JsonParser();

    public final HashMap<UUID, HashMap<String, PermissionAttachment>>  permissionAttachments = new HashMap<>();
    public final HashMap<String, Group> groups = new HashMap<>();
    private final MinetasiaCore plugin;

    public PermissionManager(MinetasiaCore plugin)
    {
        this.plugin = plugin;
        for(String f : plugin.getFields("group"))
        {
            System.out.println(f);
            try
            {
                Group g = new Group(plugin.getValue("group", f), this);
                groups.put(g.getName(), g);
            } catch (Exception ignore) { ignore.printStackTrace();}

        }
    }

    public Group createGroup(String name)
    {
        Group g = new Group(this, name);
        groups.put(name, g);
        return g;
    }


    public void removeGroup(String group)
    {
        permissionAttachments.forEach((k, v) ->{
            PermissionAttachment pa = v.get("group_" + group);
            if(pa != null) pa.remove(); v.remove("group_" + group);
            pa = v.get("temp_group_" + group);
            if(pa != null) pa.remove(); v.remove("temp_group_" + group);
        });
        groups.remove(group);
        plugin.setValue("group", group, null);
        plugin.publish("core-group", "group;" + group + ";null");
    }

    public void updateGroupToFRS(String group)
    {
        Group g = groups.get(group);
        if(g != null)
        {
            updateGroupToFRS(g);
        }
    }

    public void updateGroupToFRS(@NotNull Group group)
    {
        String j = group.toJson();
        plugin.setValue("group", group.getName(), j);
        plugin.publish("core-group", "group;" + group.getName() + ";" + j);
    }

    public void updateGroupToPlayer(String group)
    {
        if(groups.containsKey(group))
        {
            permissionAttachments.forEach((k, v) ->{
                PermissionAttachment pa = v.get("group_" + group);
                if(pa == null) pa = v.get("temp_group_" + group);
                if(pa != null)
                {
                    for(Map.Entry<String, Boolean> e : pa.getPermissions().entrySet()) pa.unsetPermission(e.getKey());
                    Group g = groups.get(group);
                    for(String perm : g.getPermissions()) pa.setPermission(perm, true);
                }
            });
        }
    }

    public void removePlayer(UUID u)
    {
        permissionAttachments.remove(u);
    }

    public void addTempGroup(@NotNull UUID player, @NotNull String group, long during)
    {
        Group g = groups.get(group);
        if(g != null)
        {
            addTemp(player, group, "temp_group", during);
        }
    }

    public void addTempPermission(@NotNull UUID player, @NotNull String permission, long during)
    {
        addTemp(player, permission, "temp_permission" ,during);
    }


    public void addGroup(@NotNull UUID player, @NotNull String group)
    {
        Group g = groups.get(group);
        if(g != null)
        {
            add(player, group, "group");
        }
    }

    public void addPermission(@NotNull UUID player, @NotNull String permission)
    {
        add(player, permission, "permission");
    }

    private void addTemp(@NotNull UUID player, @NotNull String group, @NotNull String type, long during)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String data = plugin.getPlayerData(player, type);
            JsonObject p = data == null ? new JsonObject() : PARSER.parse(data).getAsJsonObject();
            JsonArray ja = new JsonArray();
            ja.add(System.currentTimeMillis());
            ja.add(during);
            p.add(group, ja);
            plugin.setPlayerData(player, type, p.toString());
            sayUpdate(player);
        });
    }

    private void add(@NotNull UUID player, @NotNull String args, @NotNull String type)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String data = plugin.getPlayerData(player, type);
            JsonArray p = data == null ? new JsonArray() : PARSER.parse(data).getAsJsonArray();
            p.add(args);
            plugin.setPlayerData(player, type, p.toString());
            sayUpdate(player);
        });
    }

    public void removeTempPermission(@NotNull UUID player, @NotNull String group)
    {
        removeTemp(player, group, "temp_permission");
    }

    public void removeTempGroup(@NotNull UUID player, @NotNull String group)
    {
        Group g = groups.get(group);
        if(g != null)
        {
            removeTemp(player, group, "temp_group");
        }
    }

    public void removeGroup(@NotNull UUID player, @NotNull String group)
    {
        remove(player, group, "group");
    }

    public void removePermission(@NotNull UUID player, @NotNull String permission)
    {
        remove(player, permission, "permission");
    }

    private void removeTemp(@NotNull UUID player, @NotNull String args, @NotNull String type)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String data = plugin.getPlayerData(player, type);
            JsonObject p = data == null ? null : PARSER.parse(data).getAsJsonObject();
            if(p == null) return;
            boolean u = (p.remove(args) != null);
            if(u)
            {
                plugin.setPlayerData(player, type, p.toString());
                sayUpdate(player);
            }
        });
    }

    private void remove(@NotNull UUID player, @NotNull String args, @NotNull String type)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String data = plugin.getPlayerData(player, type);
            JsonArray p = data == null ? null : PARSER.parse(data).getAsJsonArray();
            if(p == null) return;
            JsonArray p2 = new JsonArray();
            boolean u = false;
            for(JsonElement j : p) if(!j.getAsString().equals(args)) p2.add(j); else u = true;
            if(u)
            {
                plugin.setPlayerData(player, type, p2.toString());
                sayUpdate(player);
            }
        });
    }

    private void sayUpdate(@NotNull UUID player)
    {
        if(Bukkit.getPlayer(player) != null) loadUser(player, true);
        else Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> plugin.publish("core-cmd", "permission;" + player.toString()), 40);
    }

    private List<String> getThinkOfUSer(@NotNull UUID uuid, String think, boolean isTemp, boolean isPerm)
    {
        List<String> back = new ArrayList<>();
        if(permissionAttachments.containsKey(uuid))
        {
            for(Map.Entry<String, PermissionAttachment> e : permissionAttachments.get(uuid).entrySet())
            {
                if(e.getKey().startsWith(think))
                {
                    if(isPerm)
                        back.addAll(e.getValue().getPermissions().keySet());
                    else
                        back.add(e.getKey().replace(think + "_", ""));

                }
            }
        }
        else
        {
            if (isTemp)
            {
                String data = plugin.getPlayerData(uuid, think);
                JsonObject tempPermission = data == null ? null : PARSER.parse(data).getAsJsonObject();
                if(data != null)
                    for(Map.Entry<String, JsonElement> j : tempPermission.entrySet())
                    {
                        back.add(j.getKey());
                    }

            }
            else
            {
                String data = plugin.getPlayerData(uuid, think);
                JsonArray permission = data == null ? null : PARSER.parse(data).getAsJsonArray();
                if(data != null)
                    for (JsonElement j : permission) back.add(j.getAsString());
            }
        }
        return back;
    }

    public List<String> getGroupOfUser(@NotNull UUID uuid)
    {
        return getThinkOfUSer(uuid, "group", false, false);
    }

    public List<String> getGroupOfUser(@NotNull Player player)
    {
        return getThinkOfUSer(player, "group", false, false);
    }

    public List<String> getGroupsOfUser(@NotNull Player player)
    {
        List<String> s =  getTempGroupOfUser(player);
        s.addAll(getGroupOfUser(player));
        return s;
    }

    public List<String> getGroupsOfUser(@NotNull UUID uuid)
    {
        List<String> s =  getTempGroupOfUser(uuid);
        s.addAll(getGroupOfUser(uuid));
        return s;
    }

    private List<String> getThinkOfUSer(@NotNull Player player, String think, boolean isTemp, boolean isPerm)
    {
        List<String> back = new ArrayList<>();
        if(permissionAttachments.containsKey(player.getUuid()))
        {
            for(Map.Entry<String, PermissionAttachment> e : permissionAttachments.get(player.getUuid()).entrySet())
            {
                if(e.getKey().startsWith(think))
                {
                    if(isPerm)
                        back.addAll(e.getValue().getPermissions().keySet());
                    else
                        back.add(e.getKey().replace(think + "_", ""));

                }
            }
        }
        else
        {
            if (isTemp)
            {
                String data = player.getData(think);
                JsonObject tempPermission = data == null ? null : PARSER.parse(data).getAsJsonObject();
                if(data != null)
                    for(Map.Entry<String, JsonElement> j : tempPermission.entrySet())
                    {
                        back.add(j.getKey());
                    }

            }
            else
            {
                String data = player.getData(think);
                JsonArray permission = data == null ? null : PARSER.parse(data).getAsJsonArray();
                if(data != null)
                    for (JsonElement j : permission) back.add(j.getAsString());
            }
        }
        return back;
    }

    public List<fr.idarkay.minetasia.core.api.utils.Group> getGroupFromName(List<String> g)
    {
        List<fr.idarkay.minetasia.core.api.utils.Group> l = new ArrayList<>();
        for(String s : g)
            l.add(groups.get(s));
        return l;
    }

    public List<String> getPermissionOfUser(@NotNull UUID uuid)
    {
        return getThinkOfUSer(uuid, "permission", false, true);
    }

    public List<String> getTempGroupOfUser(@NotNull UUID uuid)
    {
        return getThinkOfUSer(uuid, "temp_group", true, false);
    }

    public List<String> getTempGroupOfUser(@NotNull Player player)
    {
        return getThinkOfUSer(player, "temp_group", true, false);
    }

    public List<String> getTempPermissionOfUser(@NotNull UUID uuid)
    {
        return getThinkOfUSer(uuid, "temp_permission", true, true);
    }


    public void loadUser(@NotNull UUID uuid, boolean reset)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{

            String data =  plugin.getPlayerData(uuid, "group");
            JsonArray group = data == null ? null : PARSER.parse(data).getAsJsonArray();
            data = plugin.getPlayerData(uuid, "permission");
            JsonArray permission = data == null ? null : PARSER.parse(data).getAsJsonArray();
            data = plugin.getPlayerData(uuid, "temp_group");
            JsonObject tempgroup = data == null ? null : PARSER.parse(data).getAsJsonObject();
            data = plugin.getPlayerData(uuid, "temp_permission");
            JsonObject tempPermission = data == null ? null : PARSER.parse(data).getAsJsonObject();

            org.bukkit.entity.Player p = plugin.getServer().getPlayer(uuid);
            if(p != null)
            {
                if(reset)
                {
                    HashMap<String, PermissionAttachment> paa = permissionAttachments.get(uuid);
                    if(paa != null) paa.values().forEach(p::removeAttachment);
                }
                byte u =0;
                final HashMap<String, PermissionAttachment> map = new HashMap<>();
                final ArrayList<String> groupL = new ArrayList<>();
                final HashMap<String, Long[]> tempgroupH = new HashMap<>();
                final HashMap<String, Long[]> tempPermH = new HashMap<>();
                if(group != null)
                {
                    for(JsonElement j : group)
                    {
                        Group g = groups.get(j.getAsString());
                        if(g != null)
                        {
                            PermissionAttachment pa = p.addAttachment(plugin);
                            for(String perm : g.getPermissions()) pa.setPermission(perm, true);
                            map.put("group_" + g.getName(), pa);
                            groupL.add(g.getName());

                        } else {
                            u |= 1;
                        }
                    }
                }
                if(permission != null)
                {
                    PermissionAttachment pa = p.addAttachment(plugin);
                    for(JsonElement j : permission) pa.setPermission(j.getAsString(), true);
                    map.put("permission", pa);
                }
                if(tempgroup != null)
                {
                    for (Map.Entry<String, JsonElement> entry : tempgroup.entrySet())
                    {
                        Group g = groups.get(entry.getKey());
                        if(g != null)
                        {
                            JsonArray ja = entry.getValue().getAsJsonArray();
                            long t1 = ja.get(0).getAsLong();
                            long t2 =  ja.get(1).getAsLong();
                            long tLeft =  t1 + t2  - System.currentTimeMillis();
                            if(tLeft > 0)
                            {
                                PermissionAttachment pa = p.addAttachment(plugin, ((int) (tLeft / 50)));
                                for(String perm : g.getPermissions()) Objects.requireNonNull(pa).setPermission(perm, true);
                                map.put("temp_group_" + g.getName(), pa);
                                tempgroupH.put(g.getName(), new Long[] {t1, t2});
                            }
                            else u |= 1 << 1;
                        }
                        else  u |= 1 << 1;
                    }
                }
                if(tempPermission != null)
                {
                    for (Map.Entry<String, JsonElement> entry : tempPermission.entrySet())
                    {
                        JsonArray ja = entry.getValue().getAsJsonArray();
                        long t1 = ja.get(0).getAsLong();
                        long t2 =  ja.get(1).getAsLong();
                        long tLeft =  t1 + t2  - System.currentTimeMillis();
                        if(tLeft > 0)
                        {
                            PermissionAttachment pa = p.addAttachment(plugin, ((int) (tLeft / 50)));
                            Objects.requireNonNull(pa).setPermission(entry.getKey(), true);
                            map.put("temp_permission_" + entry.getKey(), pa);
                            tempPermH.put(entry.getKey(), new Long[] {t1, t2});
                        }
                        else u |= 1 << 2;
                    }
                }
                if((u & 0x1) == 1)
                {
                    JsonArray ja = new JsonArray();
                    groupL.forEach(ja::add);
                    plugin.setPlayerData(uuid, "group", ja.toString());
                }
                if ((u >> 1 & 0x1) == 1)
                {
                    JsonObject jo = new JsonObject();
                    tempgroupH.forEach((k, v) -> {
                        JsonArray ja = new JsonArray();
                        ja.add(v[0]);
                        ja.add(v[1]);
                        jo.add(k, ja);
                    });
                    plugin.setPlayerData(uuid, "temp_group", jo.toString());
                }
                if(((u >> 2 & 0x1) == 1))
                {
                    JsonObject jo = new JsonObject();
                    tempPermH.forEach((k, v) -> {
                        JsonArray ja = new JsonArray();
                        ja.add(v[0]);
                        ja.add(v[1]);
                        jo.add(k, ja);
                    });
                    plugin.setPlayerData(uuid, "temp_permission", jo.toString());
                }
                permissionAttachments.put(uuid, map);
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerPermissionLoadEndEvent(p)));
            }
            else Bukkit.getLogger().warning("Can't load permission of " + uuid.toString() + ", he isn't connected");
        });
    }
}
