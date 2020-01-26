package fr.idarkay.minetasia.normes;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * File <b>BossBar</b> located on fr.idarkay.minetasia.skyblockbattle.utils
 * BossBar is a part of MinetasiaSkyBlockBattle.
 * <p>
 * Copyright (c) 2019 MinetasiaSkyBlockBattle.
 * <p>
 *
 * @author alice. B. (IDarKay),
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

    /**
     * create a new boos bar, the boss bar is unique object and contain a UUID generate automatically, them for change a player bossBar
     * you need edit this object and send to player with {@link Action#UPDATE_NAME} or {@link Action#UPDATE_PCT} or
     * {@link Action#UPDATE_PROPERTIES} or {@link Action#UPDATE_STYLE}. Or remove the older bar with {@link Action#REMOVE}
     * and add this with {@link Action#ADD}
     * <br>
     * @param title the name show on top of the bare can contain {@link org.bukkit.ChatColor}
     * @param heal heal of the bar 1 = full 0 = empty
     * @param color the color of the bar see {@link Color}
     * @param division the division type  see {@link Division}
     * @param flags optional flag see {@link Flags}
     */
    public BossBar(@NotNull String title, float heal, @NotNull  Color color, @NotNull Division division, Flags... flags)
    {
        this(UUID.randomUUID(), title, heal, color, division, flags);
    }

    /**
     * create a new boos bar, the boss bar is unique object and contain a UUID, them for change a player bossBar
     * you need edit this object and send to player with {@link Action#UPDATE_NAME} or {@link Action#UPDATE_PCT} or
     * {@link Action#UPDATE_PROPERTIES} or {@link Action#UPDATE_STYLE}. Or remove the older bar with {@link Action#REMOVE}
     * and add this with {@link Action#ADD}
     * <br>
     * @param uuid set your custom uuid
     * @param title the name show on top of the bare can contain {@link org.bukkit.ChatColor}
     * @param heal heal of the bar 1 = full 0 = empty
     * @param color the color of the bar see {@link Color}
     * @param division the division type  see {@link Division}
     * @param flags optional flag see {@link Flags}
     */
    public BossBar(@NotNull UUID uuid, @NotNull String title, float heal, @NotNull  Color color, @NotNull Division division, Flags... flags)
    {
        this.uuid = Objects.requireNonNull(uuid);
        this.title = Objects.requireNonNull(title);
        this.heal = heal;
        this.color = Objects.requireNonNull(color);
        this.division = Objects.requireNonNull(division);
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

    /**
     * return the UUID of the BossBar, this can't be change
     * @return uuid of the bar
     */
    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    /**
     * get the color of the bossBar will be show to player select in {@link Color}
     * @return {@link Color} of the bare
     */
    @NotNull
    public Color getColor() {
        return color;
    }

    /**
     * get the division = the design of the bar
     * @return {@link Division} of the bar
     */
    @NotNull
    public Division getDivision() {
        return division;
    }

    /**
     * return all flag of the list
     * @return all flags of the bar
     */
    @Nullable
    public Flags[] getFlags() {
        return flags;
    }

    /**
     * get the heal of value in [0; 1] ; 0 = empty life 1 = full life
     * if you set more than 1 second bar will spawn
     * @return heal
     */
    public float getHeal() {
        return heal;
    }

    /**
     * get text show on top of the bar accept ChatColor
     * @return title
     */
    @NotNull
    public String getTitle() {
        return title;
    }

    /**
     * check if the bar have a flags
     * @param f flag to check
     * @return true if have this flag
     */
    public boolean isFlag(@NotNull Flags f) {
        return f.equals(Flags.PLAY_BOSS_MUSIC) ? pbm : f.equals(Flags.DARKEN_SKY) ? ds : cf;
    }

    /**
     * change the color of the bossBar will be show to player select in {@link Color}
     * @param color new {@link Color}
     */
    public void setColor(@NotNull Color color) {
        this.color = Objects.requireNonNull(color);
    }

    /**
     * set the division = the design of the bar
     * @param division for the bar see {@link Division}
     */
    public void setDivision(@NotNull Division division) {
        this.division = Objects.requireNonNull(division);
    }

    /**
     * set flags of the bar
     * warn : this is a set not a add this methods remove older flags for this new
     * @param flags new flag see {@link Flags}
     */
    public void setFlags(Flags... flags) {
        this.flags = flags;
        upBool();
    }

    /**
     * set the heal of value in [0; 1] ; 0 = empty life 1 = full life
     * if you set more than 1 second bar will spawn
     * @param heal to set need be in [0 ; 1]
     */
    public void setHeal(float heal) {
        this.heal = heal;
    }

    /**
     * set the new text to show at top of the bar accept {@link org.bukkit.ChatColor}
     * @param title new title to set
     */
    public void setTitle(@NotNull String title) {
        this.title = Objects.requireNonNull(title);
    }

    /**
     * clone the bar with the uuid !
     * @return clone bar
     */
    public BossBar clone() {
        return new BossBar(uuid, title, heal, color, division, flags);
    }

    /**
     * all action to set on {@link Reflection#getWorldBorderPacket(WorldBorder.EnumWorldBorderAction, WorldBorder)}
     */
    public enum Action{
        /**
         * add new bar to the player
         */
        ADD,
        /**
         * remove the bar with same uuid to player
         */
        REMOVE,
        /**
         * change the heal point of the bar
         */
        UPDATE_PCT,
        /**
         * change the name of the bar (text at top of the bar)
         */
        UPDATE_NAME,
        /**
         * change the style of the bar
         */
        UPDATE_STYLE,
        /**
         * change flags of teh bar
         */
        UPDATE_PROPERTIES
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

    /**
     * design of the bar
     */
    public enum Division {
        /**
         * smooth bar with no separation
         */
        PROGRESS,
        /**
         * 6 separation bar
         */
        NOTCHED_6,
        /**
         * 10 separation bar
         */
        NOTCHED_10,
        /**
         * 12 separation bar
         */
        NOTCHED_12,
        /**
         * 20 separation bar
         */
        NOTCHED_20
    }

    public enum Flags{
        DARKEN_SKY,
        PLAY_BOSS_MUSIC,
        CREATE_FOG;
    }

}
