package fr.idarkay.minetasia.core.bungee.utils.user;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * File <b>PlayerSanction</b> located on fr.idarkay.minetasia.core.spigot.moderation
 * PlayerSanction is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 13/03/2020 at 23:15
 */
public class PlayerSanction
{

    public final long startTime, during;
    public final UUID author;
    public final String authorName, reason;
    public final SanctionType sanctionType;
    public final TimeUnit baseTimeUnit;

    public PlayerSanction(@NotNull SanctionType sanctionType, long startTime, long during, @NotNull UUID author, @NotNull String authorName, @NotNull String reason, @NotNull TimeUnit timeUnit)
    {
        this.sanctionType = Objects.requireNonNull(sanctionType);
        this.startTime = startTime;
        this.during = during;
        this.author = Objects.requireNonNull(author);
        this.authorName = Objects.requireNonNull(authorName);
        this.reason = Objects.requireNonNull(reason);
        this.baseTimeUnit = Objects.requireNonNull(timeUnit);
    }

    public PlayerSanction(@NotNull SanctionType sanctionType, long during, @NotNull UUID author, @NotNull String authorName, @NotNull String reason, @NotNull TimeUnit timeUnit)
    {
        this(sanctionType, System.currentTimeMillis(), during, author, authorName, reason, timeUnit);
    }

    public static PlayerSanction fromDocument(@NotNull Document document)
    {
        return new PlayerSanction(
                SanctionType.valueOf(document.getString("type")),
                document.getLong("start"),
                document.getLong("during"),
                UUID.fromString(document.getString("author")),
                document.getString("author_username"),
                document.getString("reason"),
                TimeUnit.valueOf(document.getString("base_time_unite"))
        );
    }

    private Document document;

    @NotNull
    public SanctionType getType()
    {
        return sanctionType;
    }

    @NotNull
    public Document toDocument()
    {
        if(document == null)
        {
            document = new Document()
                    .append("start", startTime)
                    .append("during", during)
                    .append("author", author.toString())
                    .append("author_username", authorName)
                    .append("type", sanctionType.name())
                    .append("base_time_unite", baseTimeUnit.name())
                    .append("reason", reason);
        }
        return document;
    }

    public boolean isEnd()
    {
        return getReamingTime() <= 0L;
    }

    public long getReamingTime()
    {
        return startTime + during - System.currentTimeMillis();
    }

}
