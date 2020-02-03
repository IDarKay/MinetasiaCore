package fr.idarkay.minetasia.core.spigot.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.api.ServerPhase;
import fr.idarkay.minetasia.core.api.utils.SQLManager;
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
    private final String ip, type, serverConfig;
    private final int port;
    private int playerCount = 0, maxPlayerCount = -1;
    private ServerPhase serverPhase = ServerPhase.LOAD;

    public Server(@NotNull String ip, int port, String type, String serverConfig)
    {
        creatTime = System.currentTimeMillis();
        uuid = UUID.randomUUID();
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.serverConfig = serverConfig;
    }

    private Server(@NotNull String ip, int port, String type, String serverConfig, @NotNull UUID uuid, long creatTime, int serverPhase, int maxPlayerCount)
    {
        this.creatTime = creatTime;
        this.uuid = uuid;
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.serverConfig = serverConfig;
        this.serverPhase = ServerPhase.values()[serverPhase];
        this.maxPlayerCount = maxPlayerCount;
    }

    public static Server getServerFromJson(String json)
    {
        JsonObject server = new JsonParser().parse(json).getAsJsonObject();
        return new Server(server.get("ip").getAsString(), server.get("port").getAsInt(), server.get("type").getAsString()
                ,server.get("server_config").getAsString() , UUID.fromString(server.get("uuid").getAsString()), server.get("create_time").getAsLong(),
                server.get("phase").getAsInt(), server.get("max_player_count").getAsInt());
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
    public String getName()
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

    @Override
    public int getMaxPlayerCount()
    {
        return maxPlayerCount;
    }

    @Override
    public void setMaxPlayerCount(int maxPlayerCount)
    {
        this.maxPlayerCount = maxPlayerCount;
    }

    @Override
    public ServerPhase getServerPhase()
    {
        return serverPhase;
    }

    @Override
    public String getServerConfig()
    {
        return serverConfig;
    }

    @Override
    public void setPhase(ServerPhase phase)
    {
        this.serverPhase = phase;
    }

    @NotNull
    public String toJson()
    {
        JsonObject server = new JsonObject();
        server.addProperty("create_time", creatTime);
        server.addProperty("uuid", uuid.toString());
        server.addProperty("ip", ip);
        server.addProperty("port", port);
        server.addProperty("type", type);
        server.addProperty("server_config", serverConfig);
        server.addProperty("phase", serverPhase.ordinal());
        server.addProperty("max_player_count", maxPlayerCount);
        //todo: player count
        return server.toString();
    }

    @Override
    public int compareTo(@NotNull fr.idarkay.minetasia.core.api.utils.Server server)
    {
        return  server.getServerPhase().ordinal() - getServerPhase().ordinal();
    }
}
