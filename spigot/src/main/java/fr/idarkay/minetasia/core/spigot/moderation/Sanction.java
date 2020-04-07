package fr.idarkay.minetasia.core.spigot.moderation;

import fr.idarkay.minetasia.normes.utils.Message;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * File <b>Sanction</b> located on fr.idarkay.minetasia.core.spigot.moderation
 * Sanction is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 03/04/2020 at 16:46
 */
public class Sanction
{

    @NotNull
    private final String genericName;
    @NotNull
    private final Message message;
    @NotNull
    private final RepetitionSanction[] sanctions;

    public Sanction(@NotNull final Document document)
    {
        // name to enter in /sanction and show in /history
        this.genericName = Objects.requireNonNull(document.getString("generic_name"), "no generic name in on sanction");
        final Document msg = Objects.requireNonNull(document.get("message", Document.class), "no message set in sanction : " + genericName);
        this.message = new Message(msg.getString("key"), msg.getString("default"));
        System.out.println(document.toJson());
        this.sanctions = document.getList("sanction", Document.class).stream().map(RepetitionSanction::new).sorted().toArray(RepetitionSanction[]::new);
    }

    public RepetitionSanction getSanctions(int repetition)
    {
        Validate.isTrue(repetition >= 0, "a player can't sanction for negative or null number of repetition !");
        return repetition >= sanctions.length ? sanctions[sanctions.length - 1] : sanctions[repetition];
    }

    public Message getMessage()
    {
        return message;
    }

    @NotNull
    public String getGenericName()
    {
        return genericName;
    }
}
