package fr.idarkay.minetasia.normes;

import org.bukkit.Location;

/**
 * File <b>Direction</b> located on fr.idarkay.minetasia.normes
 * Direction is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 24/01/2020 at 15:04
 */
public enum Direction
{
    NORTH(true, true, "north", 0),
    EAST(false, true, "east", 1),
    SOUTH(false, false, "south", 2),
    WEST(true, false, "west", 3),
    ;

    private static Direction[] values = values();

    private boolean x0,z0;
    private int rank;
    private String name;


    /**
     *
     * @param x true for positive
     * @param z true for positive
     */
    Direction(boolean x, boolean z, String name, int rank)
    {
        this.x0 = x;
        this.z0 = z;
        this.name = name;
        this.rank = rank;
    }


    public String rotateData(String data)
    {
        String[] split = data.split("facing=");
        Direction old =  Direction.valueOf(split[1].substring(0, indexOf(split[1])).toUpperCase());
        return data.replace(old.name, values[(old.rank + rank) % 4].name);
    }

    private int indexOf(String s)
    {
        int i = s.indexOf(',');
        return i == -1 ? s.indexOf(']') : i;
    }

    public int x(int x, int z, int mx, int mz)
    {
        return x0 ? (!z0 ? z : x) : (-(z0 ? z : x) + mx);
    }

    public int z(int x, int z, int mx, int mz)
    {
        return z0 ? (!x0 ? x : z) : (-(x0 ? x : z) + mz);
    }

    public Location loc(Location loc, int mx, int mz)
    {
        loc.setX(x(loc.getBlockX(), loc.getBlockZ(), mx, mz));
        loc.setX(z(loc.getBlockX(), loc.getBlockZ(), mx, mz));
        return loc;
    }

}

