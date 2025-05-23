package fr.idarkay.minetasia.core.spigot.permission;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.api.event.PlayerPermissionLoadEndEvent;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * File <b>PermissionManager</b> located on fr.idarkay.minetasia.core.spigot.permission
 * PermissionManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 28/11/2019 at 20:22
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PermissionManager
{

    private final static JsonParser PARSER = new JsonParser();

    public final HashMap<UUID, HashMap<String, PermissionAttachment>> permissionAttachments = new HashMap<>();
    public final HashMap<String, Group> groups = new HashMap<>();
    private final List<Group> defaultGroups = new ArrayList<>();
    private final MinetasiaCore plugin;
    private boolean tabGroup = false;

    public PermissionManager(MinetasiaCore plugin)
    {
        this.plugin = plugin;
        for (Document d : plugin.getMongoDbManager().getAll(MongoCollections.GROUPS))
        {
            final Group g = new Group(d, this);
            groups.put(g.getName(), g);
            if (g.isDefault())
                defaultGroups.add(g);
        }
    }

    private Scoreboard mainScoreboard;

    public void loadTabGroup()
    {
        this.tabGroup = true;
        mainScoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
        groups.values().forEach(g ->
        {
            final String name = (g.getPriority() * -1) + "_" + g.getName();
            Team team = mainScoreboard.getTeam(name);
            if (team == null)
                team = mainScoreboard.registerNewTeam(name);

            team.setDisplayName(ChatColor.translateAlternateColorCodes('&', g.getDisplayName()));
            team.setPrefix(ChatColor.translateAlternateColorCodes('&', g.getDisplayName()) + " ");
            team.setColor(getLastChatColor(team.getPrefix()));
            g.setTeam(team);
        });
    }

    private ChatColor getLastChatColor(@NotNull String input)
    {
        Validate.notNull(input);
        String result = "";
        int length = input.length();

        for (int index = length - 1; index > -1; --index)
        {
            char section = input.charAt(index);
            if (section == 167 && index < length - 1)
            {
                char c = input.charAt(index + 1);
                ChatColor color = ChatColor.getByChar(c);
                if (color != null)
                {
                    result = color.toString() + result;
                    if (color.isColor() || color.equals(ChatColor.RESET))
                    {
                        return color;
                    }
                }
            }
        }
        return ChatColor.RESET;
    }

    public Group createGroup(String name)
    {
        Group g = new Group(this, name);
        groups.put(name, g);
        if(tabGroup)
        {
            final String tname = (g.getPriority() * -1) + "_" + g.getName();
            Team team = mainScoreboard.getTeam(tname);
            if(team == null)
                team = mainScoreboard.registerNewTeam(tname);
            team.setDisplayName(ChatColor.translateAlternateColorCodes('&', g.getDisplayName()));
            team.setPrefix(ChatColor.translateAlternateColorCodes('&', g.getDisplayName()) + " ");
            team.setColor(getLastChatColor(team.getPrefix()));
            g.setTeam(team);
        }
        plugin.getMongoDbManager().insert(MongoCollections.GROUPS, g.toDocument());
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
        plugin.getMongoDbManager().delete(MongoCollections.GROUPS, group);
    }

    public List<Group> getDefaultGroups()
    {
        return defaultGroups;
    }

    public void updateGroupToDB(String group)
    {
        Group g = groups.get(group);
        if(g != null)
        {
            updateGroupToDB(g);
        }
    }

    public void updateGroupToDB(@NotNull Group group)
    {
        plugin.getMongoDbManager().replace(MongoCollections.GROUPS, group.getName(), group.toDocument());
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

    private void addGroup(@NotNull UUID player, @NotNull String group, boolean update)
    {
        final Group g = groups.get(group);
        if(g != null)
        {
            add(player, group, "group", update);
        }
    }

    public void addGroup(@NotNull UUID player, @NotNull String group)
    {
        addGroup(player, group, true);
    }

    public void addPermission(@NotNull UUID player, @NotNull String permission)
    {
        add(player, permission, "permission");
    }

    private void addTemp(@NotNull UUID player, @NotNull String group, @NotNull String type, long during)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String data = plugin.getPlayerData(player, type, String.class);
            JsonObject p = data == null ? new JsonObject() : PARSER.parse(data).getAsJsonObject();
            JsonArray ja = new JsonArray();
            ja.add(System.currentTimeMillis());
            ja.add(during);
            p.add(group, ja);
            plugin.setPlayerData(player, type, p.toString());
            sayUpdate(player);
        });
    }

    public void addGroupWithoutUpdate(MinePlayer player, String group)
    {
        String data = (String) player.getData("group");
        JsonArray p = data == null ? new JsonArray() : PARSER.parse(data).getAsJsonArray();
        p.add(group);
        player.putData("group", p.toString());
    }

    private void add(@NotNull UUID player, @NotNull String args, @NotNull String type, boolean update)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final String data = plugin.getPlayerData(player, type, String.class);
            final JsonArray p = data == null ? new JsonArray() : PARSER.parse(data).getAsJsonArray();
            p.add(args);
            plugin.setPlayerData(player, type, p.toString());
            if(update) sayUpdate(player);
        });
    }

    private void add(@NotNull UUID player, @NotNull String args, @NotNull String type)
    {
        add(player, args, type, true);
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
            String data = plugin.getPlayerData(player, type, String.class);
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
            String data = plugin.getPlayerData(player, type, String.class);
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
        else Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> plugin.publishTargetPlayer("core-cmd", "permission;" + player.toString(), player, false, false), 40);
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
                String data = plugin.getPlayerData(uuid, think, String.class);
                JsonObject tempPermission = data == null ? null : PARSER.parse(data).getAsJsonObject();
                if(data != null)
                    for(Map.Entry<String, JsonElement> j : tempPermission.entrySet())
                    {
                        back.add(j.getKey());
                    }

            }
            else
            {
                String data = plugin.getPlayerData(uuid, think, String.class);
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

    public List<String> getGroupOfUser(@NotNull MinePlayer player)
    {
        return getThinkOfUSer(player, "group", false, false);
    }

    public List<String> getGroupsOfUser(@NotNull MinePlayer player)
    {
        List<String> s = getTempGroupOfUser(player);
        s.addAll(getGroupOfUser(player));
        return s;
    }

    public List<String> getGroupsOfUser(@NotNull UUID uuid)
    {
        List<String> s =  getTempGroupOfUser(uuid);
        s.addAll(getGroupOfUser(uuid));
        return s;
    }

    private List<String> getThinkOfUSer(@NotNull MinePlayer player, String think, boolean isTemp, boolean isPerm)
    {
        List<String> back = new ArrayList<>();
        if(permissionAttachments.containsKey(player.getUUID()))
        {
            for(Map.Entry<String, PermissionAttachment> e : permissionAttachments.get(player.getUUID()).entrySet())
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
                Object data = player.getData(think);
                JsonObject tempPermission = data == null ? null : PARSER.parse(data.toString()).getAsJsonObject();
                if(data != null)
                    for(Map.Entry<String, JsonElement> j : tempPermission.entrySet())
                    {
                        back.add(j.getKey());
                    }

            }
            else
            {
                Object data = player.getData(think);
                JsonArray permission = data == null ? null : PARSER.parse(data.toString()).getAsJsonArray();
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

    public List<String> getTempGroupOfUser(@NotNull MinePlayer player)
    {
        return getThinkOfUSer(player, "temp_group", true, false);
    }

    public List<String> getTempPermissionOfUser(@NotNull UUID uuid)
    {
        return getThinkOfUSer(uuid, "temp_permission", true, true);
    }


    public void loadUser(@NotNull UUID uuid, boolean reset)
    {
        Object data =  plugin.getPlayerData(uuid, "group");
        JsonArray group = data == null ? null : PARSER.parse(data.toString()).getAsJsonArray();
        data = plugin.getPlayerData(uuid, "permission");
        JsonArray permission = data == null ? null : PARSER.parse(data.toString()).getAsJsonArray();
        data = plugin.getPlayerData(uuid, "temp_group");
        JsonObject tempgroup = data == null ? null : PARSER.parse(data.toString()).getAsJsonObject();
        data = plugin.getPlayerData(uuid, "temp_permission");
        JsonObject tempPermission = data == null ? null : PARSER.parse(data.toString()).getAsJsonObject();

        org.bukkit.entity.Player p = plugin.getServer().getPlayer(uuid);
        if(p != null)
        {
            if(reset)
            {
                if(tabGroup)
                {
                    Group playerMasterGroup = (Group) plugin.getPlayerMasterGroup(uuid);
                    if(playerMasterGroup != null)
                    {
                        playerMasterGroup.getTeam().removeEntry(p.getName());
                        p.setPlayerListName(p.getName());
                    }
                }
                HashMap<String, PermissionAttachment> paa = permissionAttachments.get(uuid);
                if(paa != null) {
                    paa.values().forEach(p::removeAttachment);
                    paa.clear();
                }

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
                        for(String perm : g.getPermissions()) {
                            if(perm.equals("*")) Bukkit.getScheduler().runTask(plugin, () -> p.setOp(true));
                            pa.setPermission(perm, true);
                        }
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
                for(JsonElement j : permission) {
                    final String perm = j.getAsString();
                    if(perm.equals("*")) p.setOp(true);
                    pa.setPermission(perm, true);
                }
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
                            for(String perm : g.getPermissions()) {
                                if(perm.equals("*")) p.setOp(true);
                                Objects.requireNonNull(pa).setPermission(perm, true);
                            }
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
                        if(entry.getKey().equals("*")) p.setOp(true);
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

            plugin.getPlayerManager().getCorePlayer(uuid).setPrefix(plugin.getGroupDisplay(uuid));
            if(tabGroup)
            {
                Group playerMasterGroup = (Group) plugin.getPlayerMasterGroup(uuid);
                if(playerMasterGroup != null)
                {
                    playerMasterGroup.getTeam().addEntry(p.getName());
                    p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', playerMasterGroup.getDisplayName()) + p.getName());
                }
            }
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerPermissionLoadEndEvent(p)));
        }
        else Bukkit.getLogger().warning("Can't load permission of " + uuid.toString() + ", he isn't connected");
    }
}
