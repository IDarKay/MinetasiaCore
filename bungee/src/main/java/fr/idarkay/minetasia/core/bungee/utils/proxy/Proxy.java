package fr.idarkay.minetasia.core.bungee.utils.proxy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>Proxy</b> located on fr.idarkay.minetasia.core.bungee.utils.proxy
 * Proxy is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/11/2019 at 18:52
 */
public final class Proxy {

    private final long creatTime;
    private final UUID uuid;
    private final String ip;
    private final int port;

    public Proxy(@NotNull String ip, int port)
    {
        creatTime = System.currentTimeMillis();
        uuid = UUID.randomUUID();
        this.ip = ip;
        this.port = port;
    }

    private Proxy(@NotNull String ip, int port, @NotNull UUID uuid, long creatTime)
    {
        this.creatTime = creatTime;
        this.uuid = uuid;
        this.ip = ip;
        this.port = port;
    }

    public static Proxy getProxyFromJson(String json)
    {
        JsonObject proxy = new JsonParser().parse(json).getAsJsonObject();
        return new Proxy(proxy.get("ip").getAsString(), proxy.get("port").getAsInt()
            , UUID.fromString(proxy.get("uuid").getAsString()), proxy.get("createTime").getAsLong());
    }

    public long getCreatTime()
    {
        return creatTime;
    }

    @NotNull
    public String getIp()
    {
        return ip;
    }

    public int getPort()
    {
        return port;
    }

    @NotNull
    public UUID getUuid()
    {
        return uuid;
    }

    @NotNull
    public String toJson()
    {
        JsonObject proxy = new JsonObject();
        proxy.addProperty("createTime", creatTime);
        proxy.addProperty("uuid", uuid.toString());
        proxy.addProperty("ip", ip);
        proxy.addProperty("port", port);
        return proxy.toString();
    }

}