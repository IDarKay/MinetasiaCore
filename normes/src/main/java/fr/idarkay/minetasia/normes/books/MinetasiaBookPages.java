package fr.idarkay.minetasia.normes.books;

import fr.idarkay.minetasia.normes.component.BaseComponent;
import fr.idarkay.minetasia.normes.component.TextComponent;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.NBTBase;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * File <b>MinetasiaBookPages</b> located on fr.idarkay.minetasia.normes.books
 * MinetasiaBookPages is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/04/2020 at 15:07
 */
public class MinetasiaBookPages
{

    @NotNull
    final BaseComponent text;

    public MinetasiaBookPages(Collection<BaseComponent> components)
    {
        this.text = new TextComponent(" ").withExtra(components.toArray(new BaseComponent[0]));
    }

    public MinetasiaBookPages(BaseComponent... components)
    {
        this.text = new TextComponent(" ").withExtra(components);
    }

    @NotNull
    public NBTBase toNBT()
    {
        return text.toNbtTagCompound();
    }

    public IChatBaseComponent toIChatBaseComponent()
    {
        return text.toChatBaseComponent();
    }
}
