package fr.idarkay.minetasia.normes;


import java.util.UUID;

/**
 * File <b>BossBar</b> located on fr.idarkay.minetasia.skyblockbattle.utils
 * BossBar is a part of MinetasiaSkyBlockBattle.
 * <p>
 * Copyright (c) 2019 MinetasiaSkyBlockBattle.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 19/12/2019 at 18:38
 */
public class BossBar
{

    private final UUID uuid;
    private String title;
    private float heal;
    private Color color;
    private Division division;
    private Flags[] flags;
    private boolean ds = false;
    private boolean pbm = false;
    private boolean cf = false;

    public BossBar(String title, float heal, Color color, Division division, Flags... flags)
    {
        this(UUID.randomUUID(), title, heal, color, division, flags);
    }

    public BossBar(UUID u, String title, float heal, Color color, Division division, Flags... flags)
    {
        this.uuid = u;
        this.title = title;
        this.heal = heal;
        this.color = color;
        this.division = division;
        this.flags = flags;
        upBool();
    }

    private void upBool()
    {
         cf = contain(flags, Flags.CREATE_FOG);
        pbm = contain(flags, Flags.PLAY_BOSS_MUSIC);
        ds = contain(flags, Flags.DARKEN_SKY);
    }

    private <T> boolean contain(T[] table,  T obj)
    {
        for(T a : table)
        {
            if(a.equals(obj)) return true;
        }
        return false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Color getColor() {
        return color;
    }

    public Division getDivision() {
        return division;
    }

    public Flags[] getFlags() {
        return flags;
    }

    public float getHeal() {
        return heal;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFlag(Flags f) {
        return f.equals(Flags.PLAY_BOSS_MUSIC) ? pbm : f.equals(Flags.DARKEN_SKY) ? ds : cf;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public void setFlags(Flags... flags) {
        this.flags = flags;
        upBool();
    }

    public void setHeal(float heal) {
        this.heal = heal;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BossBar clone() {
        return new BossBar(uuid, title, heal, color, division, flags);
    }

    public enum Action{
        ADD,
        REMOVE,
        UPDATE_PCT,
        UPDATE_NAME,
        UPDATE_STYLE,
        UPDATE_PROPERTIES;
    }

    public enum Color{
        PINK,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        PURPLE,
        WHITE;
    }

    public enum Division {
        PROGRESS,
        NOTCHED_6,
        NOTCHED_10,
        NOTCHED_12,
        NOTCHED_20;
    }

    public enum Flags{
        DARKEN_SKY,
        PLAY_BOSS_MUSIC,
        CREATE_FOG;
    }

}
