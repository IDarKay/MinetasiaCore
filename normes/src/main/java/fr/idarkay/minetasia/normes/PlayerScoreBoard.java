package fr.idarkay.minetasia.normes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;


/**
 * File <b>SBBPlayerScoreBoard</b> located on fr.idarkay.minetasia.skyblockbattle.utils
 * SBBPlayerScoreBoard is a part of MinetasiaSkyBlockBattle.
 * <p>
 * Copyright (c) 2019 MinetasiaSkyBlockBattle.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 23/12/2019 at 11:57
 */
public class PlayerScoreBoard
{

    private final UUID player;
    private HashMap<Integer, String> lines = new HashMap<>();

    public PlayerScoreBoard(Player player, String display)
    {
        this.player = player.getUniqueId();

        try
        {
            Reflection.sendPacket(player, getEditDisplayPacket(0, display));
            Reflection.sendPacket(player, getShowPacket());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private Player getPlayer()
    {
        return Bukkit.getPlayer(player);
    }

    private static final Class<?> packetPlayOutScoreboardScoreClass = Reflection.getNMSClass("PacketPlayOutScoreboardScore");
    private static final Class<?> action = Reflection.getNMSClass("ScoreboardServer$Action");
    private static final Constructor<?> pPOSSConstructor = Reflection.getConstructor(packetPlayOutScoreboardScoreClass, false, action, String.class, String.class, int.class);

    /**
     * set new line to list
     * @param text test (display)
     * @param line number of the line
     * force = false
     */
    public void setLine(String text, int line)
    {
        setLine(text, line, false);
    }

    /**
     * set new line to list
     * @param text test (display)
     * @param line number of the line
     * @param force if true if scoreboard have same line force set else no set
     */
    public void setLine(String text, int line, boolean force)
    {
        if(lines.containsKey(line))
        {
            if(lines.get(line).equals(text) && !force) return;
            removeLine(line);
        }

        try
        {
            final Player player = getPlayer();
            Reflection.sendPacket(player, pPOSSConstructor.newInstance(Action.CHANGE.asNMS, player.getName(), text, line));
            lines.put(line, text);
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    public void resendAllLine(Action action)
    {
        final Player player = getPlayer();
        lines.forEach((line, text) ->
        {
            try
            {
                Reflection.sendPacket(player, pPOSSConstructor.newInstance(action.asNMS, player.getName(), text, line));
            }
            catch(Exception e) { e.printStackTrace(); }
        });

    }

    /**
     * remove a line of scoreBoard
     * @param line number of remove ligne
     */
    public void removeLine(int line)
    {
        try
        {
            Player player = getPlayer();
            Reflection.sendPacket(player, pPOSSConstructor.newInstance(Action.REMOVE.asNMS, player.getName(), lines.get(line), 0));
            lines.remove(line);
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    /**
     * change the name of the ScoreBoard
     * @param display name accept ChatColor
     */
    public void setDisplayName(String display)
    {
        try
        {
            Reflection.sendPacket(getPlayer(), getEditDisplayPacket(2, display));
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    /**
     * delete the ScoreBoard
     */
    public void destroy()
    {
        try
        {
            Reflection.sendPacket(getPlayer(), getEditDisplayPacket(1, null));
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    private static final Class<?> packetPlayOutScoreboardObjectiveClass = Reflection.getNMSClass("PacketPlayOutScoreboardObjective");
    private static final Constructor<?> packetPlayOutScoreboardObjectiveClassConstructor = Reflection.getConstructor(packetPlayOutScoreboardObjectiveClass, false);
    private static final Field pPOSOFAName = Reflection.getDeclaredField(packetPlayOutScoreboardObjectiveClass, "a", true);
    private static final Field pPOSOFBDisplay = Reflection.getDeclaredField(packetPlayOutScoreboardObjectiveClass, "b", true);
    private static final Field pPOSOFDMode = Reflection.getDeclaredField(packetPlayOutScoreboardObjectiveClass, "d", true);
    private static final Field pPOSOFCType = Reflection.getDeclaredField(packetPlayOutScoreboardObjectiveClass, "c", true);
    private static final Object integerTypeEnum;

    static {
        try {
            integerTypeEnum = Reflection.getNMSClass("IScoreboardCriteria$EnumScoreboardHealthDisplay").getField("INTEGER").get(null);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Object getEditDisplayPacket(int mode, String name) throws InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Object packet = packetPlayOutScoreboardObjectiveClassConstructor.newInstance();

        pPOSOFAName.set(packet, getPlayer().getName());
        pPOSOFDMode.set(packet, mode);

        if(mode == 0 || mode == 2)
        {
            pPOSOFBDisplay.set(packet, Reflection.getIChatBaseComponent(name));
            pPOSOFCType.set(packet, integerTypeEnum);
        }

        return packet;
    }


    private static final Class<?> packetPlayOutScoreboardDisplayObjectiveClass = Reflection.getNMSClass("PacketPlayOutScoreboardDisplayObjective");
    private static final Constructor<?> packetPlayOutScoreboardDisplayObjectiveConstructor = Reflection.getConstructor(packetPlayOutScoreboardDisplayObjectiveClass, false);
    private static final Field pPOSDOFAPosition = Reflection.getDeclaredField(packetPlayOutScoreboardDisplayObjectiveClass, "a", true);
    private static final Field pPOSDOFBName = Reflection.getDeclaredField(packetPlayOutScoreboardDisplayObjectiveClass, "b", true);

    private Object getShowPacket() throws InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Object packet = packetPlayOutScoreboardDisplayObjectiveConstructor.newInstance();

        pPOSDOFAPosition.set(packet, 1);
        pPOSDOFBName.set(packet, getPlayer().getName());
        return packet;
    }

    public enum Action
    {
        CHANGE,
        REMOVE,
        ;

        public Object asNMS;

        Action()
        {
            try {
                asNMS = action.getField(name()).get(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
