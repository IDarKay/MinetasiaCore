package fr.idarkay.minetasia.core.api.utils;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

/**
 * File <b>InventorySyncPlayer</b> located on fr.idarkay.minetasia.core.api.utils
 * InventorySyncPlayer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/04/2020 at 19:51
 */
public interface InventorySyncPlayer
{

    void read(NBTTagCompound nbtTagCompound);

    void write(NBTTagCompound nbtTagCompound);

}
