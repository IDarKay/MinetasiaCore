package fr.idarkay.minetasia.core.spigot.moderation;

import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * File <b>Sanction</b> located on fr.idarkay.minetasia.core.spigot.moderation
 * Sanction is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/03/2020 at 23:15
 */
public class Sanction
{

    public final long startTime, during;
    public final UUID author;
    public final String authorName, reason;
    public final SanctionType sanctionType;

    public Sanction(@NotNull SanctionType sanctionType, long startTime, long during, @NotNull UUID author, @NotNull String authorName, @NotNull String reason)
    {
        this.sanctionType = Objects.requireNonNull(sanctionType);
        this.startTime = startTime;
        this.during = during;
        this.author = Objects.requireNonNull(author);
        this.authorName = Objects.requireNonNull(authorName);
        this.reason = Objects.requireNonNull(reason);
    }

    public Sanction(@NotNull SanctionType sanctionType, long during, @NotNull UUID author, @NotNull String authorName, @NotNull String reason)
    {
        this(sanctionType, System.currentTimeMillis(), during, author, authorName, reason);
    }

    public static Sanction fromDocument(@NotNull SanctionType sanctionType, @NotNull Document document)
    {
        Validate.notNull(document);
        return new Sanction(
                Objects.requireNonNull(sanctionType),
                document.getLong("start"),
                document.getLong("during"),
                UUID.fromString(document.getString("author")),
                document.getString("author_username"),
                document.getString("reason")
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
