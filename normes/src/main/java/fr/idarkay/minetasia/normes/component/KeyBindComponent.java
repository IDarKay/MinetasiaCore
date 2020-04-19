package fr.idarkay.minetasia.normes.component;

import net.minecraft.server.v1_15_R1.ChatBaseComponent;
import net.minecraft.server.v1_15_R1.ChatComponentKeybind;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>KeyBindComponent</b> located on fr.idarkay.minetasia.normes.component
 * KeyBindComponent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/04/2020 at 17:20
 */
public class KeyBindComponent extends BaseComponent
{

    @NotNull
    private final ClientOption clientOption;

    public KeyBindComponent(@NotNull ClientOption clientOption)
    {
        super();
        this.clientOption = clientOption;
    }

    public KeyBindComponent(@NotNull ChatComponentKeybind chatBaseComponents)
    {
        super(chatBaseComponents);
        this.clientOption = ClientOption.fromKey(chatBaseComponents.j());
    }

    @Override
    protected @NotNull NBTTagCompound getAddon()
    {
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("keybind", clientOption.getKey());
        return nbtTagCompound;
    }

    @Override
    protected @NotNull ChatBaseComponent getBaseChatComponent()
    {
        return new ChatComponentKeybind(clientOption.getKey());
    }

}
