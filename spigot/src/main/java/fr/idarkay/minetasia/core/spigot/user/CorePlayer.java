package fr.idarkay.minetasia.core.spigot.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * File <b>CorePlayer</b> located on fr.idarkay.minetasia.core.spigot.user
 * CorePlayer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 23/02/2020 at 14:48
 */
public class CorePlayer
{

    @NotNull
    private final UUID uuid;
    @NotNull
    private final String name;

    private UUID lastPartyRequest;
    //all player invite request with time
    @NotNull
    private final Map<String, Long> invitedPlayerParty = new HashMap<>();

    @NotNull
    private final Map<CountdownType, Long> countdown = new EnumMap<>(CountdownType.class);

    private String prefix;
    private long joinTime = System.currentTimeMillis();

    public CorePlayer(@NotNull UUID uuid, @NotNull String name)
    {
        this.uuid = Objects.requireNonNull(uuid);
        this.name = Objects.requireNonNull(name);
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @NotNull
    public UUID getUuid()
    {
        return uuid;
    }

    @Nullable
    public UUID getLastPartyRequest()
    {
        return lastPartyRequest;
    }

    public void setLastPartyRequest(@Nullable UUID lastPartyRequest)
    {
        this.lastPartyRequest = lastPartyRequest;
    }

    public boolean isEndCountDown(CountdownType type)
    {
        return System.currentTimeMillis() - countdown.getOrDefault(type, 0L) > type.getTime();
    }

    public void startCountDown(CountdownType type)
    {
        countdown.put(type, System.currentTimeMillis());
    }

    public long getCountDown(CountdownType type)
    {
        return countdown.getOrDefault(type, System.currentTimeMillis()) + type.getTime() - System.currentTimeMillis();
    }

    @NotNull
    public Map<String, Long> getInvitedPlayerParty()
    {
        return invitedPlayerParty;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public long getPlayTime()
    {
        return joinTime - System.currentTimeMillis();
    }

    public void resetJoinTime()
    {
        joinTime = System.currentTimeMillis();
    }


    public enum CountdownType
    {
        INVITE_PARTY(10L, TimeUnit.SECONDS), INVITE_SAME_PLAYER_PARTY(1L, TimeUnit.MINUTES);;

        private final long time;

        CountdownType(long time, TimeUnit timeUnit)
        {
            this.time = timeUnit.toMillis(time);
        }

        public long getTime()
        {
            return time;
        }


    }
}
