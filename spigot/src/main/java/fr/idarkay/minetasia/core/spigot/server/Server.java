package fr.idarkay.minetasia.core.spigot.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>Server</b> located on fr.idarkay.minetasia.core.spigot.server
 * Server is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/11/2019 at 20:07
 */
public final class Server implements fr.idarkay.minetasia.core.api.utils.Server {

    private final long creatTime;
    private final UUID uuid;
    private final String ip, type;
    private final int port;
    private int playerCount = 0;

    public Server(@NotNull String ip, int port, String type)
    {
        creatTime = System.currentTimeMillis();
        uuid = UUID.randomUUID();
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    private Server(@NotNull String ip, int port, String type, @NotNull UUID uuid, long creatTime)
    {
        this.creatTime = creatTime;
        this.uuid = uuid;
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    public static Server getServerFromJson(String json)
    {
        JsonObject server = new JsonParser().parse(json).getAsJsonObject();
        return new Server(server.get("ip").getAsString(), server.get("port").getAsInt(), server.get("type").getAsString()
                , UUID.fromString(server.get("uuid").getAsString()), server.get("createTime").getAsLong());
    }

    @Override
    public long getCreatTime()
    {
        return creatTime;
    }

    @Override
    @NotNull
    public String getIp()
    {
        return ip;
    }

    @Override
    public int getPort()
    {
        return port;
    }

    @Override
    @NotNull
    public UUID getUuid()
    {
        return uuid;
    }

    @Override
    @NotNull
    public String getType()
    {
        return type;
    }

    @Override
    @NotNull
    public String  getName()
    {
        return type + "#" + uuid.toString();
    }

    @Override
    public int getPlayerCount()
    {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    @NotNull
    public String toJson()
    {
        JsonObject proxy = new JsonObject();
        proxy.addProperty("createTime", creatTime);
        proxy.addProperty("uuid", uuid.toString());
        proxy.addProperty("ip", ip);
        proxy.addProperty("port", port);
        proxy.addProperty("type", type);
        return proxy.toString();
    }

}
