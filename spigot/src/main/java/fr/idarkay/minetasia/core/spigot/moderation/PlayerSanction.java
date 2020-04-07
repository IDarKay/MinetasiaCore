package fr.idarkay.minetasia.core.spigot.moderation;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.apache.commons.lang.Validate;
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
 * @author Alois. B. (IDarKay),
 * Created the 13/03/2020 at 23:15
 */
public class PlayerSanction
{

    public final long startTime, during;
    public final UUID author;
    public final String authorName, reason, genric_raison_name;
    public final SanctionType sanctionType;
    public final TimeUnit baseTimeUnit;

    public PlayerSanction(@NotNull SanctionType sanctionType, long startTime, long during, @NotNull UUID author, @NotNull String authorName, @NotNull String reason, @NotNull String genric_raison_name, @NotNull TimeUnit timeUnit)
    {
        this.sanctionType = Objects.requireNonNull(sanctionType);
        this.startTime = startTime;
        this.during = during;
        this.author = Objects.requireNonNull(author);
        this.authorName = Objects.requireNonNull(authorName);
        this.reason = Objects.requireNonNull(reason);
        this.genric_raison_name = genric_raison_name;
        this.baseTimeUnit = Objects.requireNonNull(timeUnit);
    }

    public PlayerSanction(@NotNull SanctionType sanctionType, long during, @NotNull UUID author, @NotNull String authorName, @NotNull String genric_raison_name, @NotNull String reason,  @NotNull TimeUnit timeUnit)
    {
        this(sanctionType, System.currentTimeMillis(), during, author, authorName, reason, genric_raison_name, timeUnit);
    }

    public PlayerSanction(@NotNull MinePlayer minePlayer, @NotNull Sanction sanction, @NotNull UUID author, @NotNull String authorName)
    {
        Validate.notNull(minePlayer);
        Validate.notNull(sanction);
        this.authorName = Objects.requireNonNull(authorName);
        this.author = Objects.requireNonNull(author);
        final int recurrenceOfASanction = minePlayer.getRecurrenceOfASanction(sanction);
        final RepetitionSanction repetitionSanction = sanction.getSanctions(recurrenceOfASanction);
        this.baseTimeUnit = repetitionSanction.getTimeUnit();
        this.sanctionType = repetitionSanction.getType();
        this.during = repetitionSanction.getTimeUnit().toMillis(repetitionSanction.getDuring());
        this.startTime = System.currentTimeMillis();
        this.genric_raison_name = sanction.getGenericName();
        this.reason = Lang.REASON_FORMAT.getWithoutPrefix(minePlayer.getLang()
                , Lang.Argument.REASON.match(MinetasiaCore.getCoreInstance().getMinetasiaLang().getFromMessage(minePlayer.getLang(), sanction.getMessage()))
                , Lang.Argument.REPETITION.match(recurrenceOfASanction + 1)
                        );
    }

    @Deprecated
    public static PlayerSanction fromDocument(@NotNull SanctionType sanctionType, @NotNull Document document)
    {
        Validate.notNull(document);
        return new PlayerSanction(
                Objects.requireNonNull(sanctionType),
                document.getLong("start"),
                document.getLong("during"),
                UUID.fromString(document.getString("author")),
                document.getString("author_username"),
                document.getString("reason"), document.getString("generic_name"), TimeUnit.valueOf(document.getString("base_time_unite"))
        );
    }

    public static PlayerSanction fromDocument(@NotNull Document document)
    {
        Validate.notNull(document);
        return new PlayerSanction(
                SanctionType.valueOf(document.getString("type")),
                document.getLong("start"),
                document.getLong("during"),
                UUID.fromString(document.getString("author")),
                document.getString("author_username"),
                document.getString("reason"),document.getString("generic_name"), TimeUnit.valueOf(document.getString("base_time_unite"))
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
                    .append("generic_name", genric_raison_name)
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
