package fr.idarkay.minetasia.core.spigot.moderation;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * File <b>RepetitionSanction</b> located on fr.idarkay.minetasia.core.spigot.moderation
 * RepetitionSanction is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 03/04/2020 at 17:19
 */
public class RepetitionSanction implements Comparable<RepetitionSanction>
{
    private final int repetition;
    @NotNull
    private final SanctionType type;
    @NotNull
    private final TimeUnit timeUnit;
    private final long during;

    public RepetitionSanction(@NotNull final Document document)
    {
        this.repetition = Objects.requireNonNull(document.getInteger("repetition"), "no repetition value set in a sanction");
        this.during = Objects.requireNonNull(document.getLong("during"), "no during value set in a sanction");
        this.type = Objects.requireNonNull(SanctionType.valueOf(document.getString("type")), "no type or invalid type in a sanction");
        this.timeUnit = Objects.requireNonNull(TimeUnit.valueOf(document.getString("time_unit")), "no time_unit or invalid type in a sanction");
    }

    public int getRepetition()
    {
        return repetition;
    }

    public long getDuring()
    {
        return during;
    }

    public SanctionType getType()
    {
        return type;
    }

    public TimeUnit getTimeUnit()
    {
        return timeUnit;
    }

    @Override
    public int compareTo(@NotNull RepetitionSanction o)
    {
        return repetition - o.repetition;
    }
}
