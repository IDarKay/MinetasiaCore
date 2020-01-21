package fr.idarkay.minetasia.normes;

import java.io.Serializable;

/**
 * File <b>Schematic</b> located on fr.idarkay.minetasia.normes
 * Schematic is a part of Normes.
 * <p>
 * Copyright (c) 2019 Normes.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 15/12/2019 at 12:46
 */
public final class Schematic implements Serializable {

    private static final long serialVersionUID = 1577258786157412358L;

    private final String[] blocks;
    private final String[] data;
    private final int length, width, height;

    public Schematic(String[] blocks, String[] data, int length, int width, int height)
    {
        this.blocks = blocks;
        this.length = length;
        this.width = width;
        this.height = height;
        this.data = data;
    }


    public String[] getBlocks() {
        return blocks;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public String[] getData() {
        return data;
    }
}
