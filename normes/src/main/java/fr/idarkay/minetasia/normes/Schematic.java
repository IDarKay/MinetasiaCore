package fr.idarkay.minetasia.normes;

import org.bukkit.Material;

import java.io.Serializable;

/**
 * File <b>Schematic</b> located on fr.idarkay.minetasia.normes
 * Schematic is a part of Normes.
 * <p>
 * Copyright (c) 2019 Normes.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 15/12/2019 at 12:46
 */
public final class Schematic implements Serializable
{

    private static final long serialVersionUID = 1577258786157412358L;

    private final Material[] blocks;
    private final String[] data;
    private final short length, width, height;

    public Schematic(Material[] blocks, String[] data, short length, short width, short height)
    {
        this.blocks = blocks;
        this.length = length;
        this.width = width;
        this.height = height;
        this.data = data;
    }


    public Material[] getBlocks() {
        return blocks;
    }

    public short getHeight() {
        return height;
    }

    public short getLength() {
        return length;
    }

    public short getWidth() {
        return width;
    }

    public String[] getData() {
        return data;
    }
}
