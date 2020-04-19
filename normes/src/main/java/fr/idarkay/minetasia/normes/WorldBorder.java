package fr.idarkay.minetasia.normes;

/**
 * File <b>WorldBorder</b> located on fr.idarkay.minetasia.skyblockbattle.utils
 * WorldBorder is a part of MinetasiaSkyBlockBattle.
 * <p>
 * Copyright (c) 2019 MinetasiaSkyBlockBattle.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 20/12/2019 at 13:11
 */
public class WorldBorder
{

    private double x;
    private double z;
    private double r;
    private int waringBlock = 5;
    private int waringTime = 15;

    public WorldBorder(double x, double z, double r)
    {
        this.x = x;
        this.z = z;
        this.r = r;
    }

    public double x() {
        return x;
    }

    public double z() {
        return z;
    }

    public double r() {
        return r;
    }

    public int getWaringBlock() {
        return waringBlock;
    }

    public int getWaringTime() {
        return waringTime;
    }

    public void setWaringBlock(int waringBlock) {
        this.waringBlock = waringBlock;
    }

    public void setWaringTime(int waringTime) {
        this.waringTime = waringTime;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public enum EnumWorldBorderAction {
        SET_SIZE,
        LERP_SIZE,
        SET_CENTER,
        INITIALIZE,
        SET_WARNING_TIME,
        SET_WARNING_BLOCKS;
    }

}
