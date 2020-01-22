package fr.idarkay.minetasia.normes;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


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

    private final Player player;
    private HashMap<Integer, String> lines = new HashMap<>();

    public PlayerScoreBoard(Player player, String display)
    {
        this.player = player;

        try
        {
            Reflection.sendPacket(player, getEditDisplayPacket(0, display));
            Reflection.sendPacket(player, getShowPacket());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private static final Class<?> packetPlayOutScoreboardScoreClass = Reflection.getNMSClass("PacketPlayOutScoreboardScore");
    private static final Class<?> action = Reflection.getNMSClass("ScoreboardServer$Action");
    private static final Constructor<?> pPOSSConstructor = Reflection.getConstructor(packetPlayOutScoreboardScoreClass, false, action, String.class, String.class, int.class);

    public void setLine(String text, int line)
    {
        setLine(text, line, false);
    }

    public void setLine(String text, int line, boolean force)
    {
        if(lines.containsKey(line))
        {
            if(lines.get(line).equals(text) && !force) return;
            removeLine(line);
        }

        try
        {
            Reflection.sendPacket(player, pPOSSConstructor.newInstance(Action.CHANGE.asNMS, player.getName(), text, line));
            lines.put(line, text);
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    public void removeLine(int line)
    {
        try
        {
            Reflection.sendPacket(player, pPOSSConstructor.newInstance(Action.REMOVE.asNMS, player.getName(), lines.get(line), 0));
            lines.remove(line);
        }
        catch(Exception e) { e.printStackTrace(); }
    }


    public void setDisplayName(String display)
    {
        try
        {
            Reflection.sendPacket(player, getEditDisplayPacket(2, display));
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    public void destroy()
    {
        try
        {
            Reflection.sendPacket(player, getEditDisplayPacket(1, null));
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    private static final Class<?> packetPlayOutScoreboardObjectiveClass = Reflection.getNMSClass("PacketPlayOutScoreboardObjective");
    private static final Constructor<?> packetPlayOutScoreboardObjectiveClassConstructor = Reflection.getConstructor(packetPlayOutScoreboardObjectiveClass, false);
    private static final Field pPOSOFAName = Reflection.getField(packetPlayOutScoreboardObjectiveClass, "a", true);
    private static final Field pPOSOFBDisplay = Reflection.getField(packetPlayOutScoreboardObjectiveClass, "b", true);
    private static final Field pPOSOFDMode = Reflection.getField(packetPlayOutScoreboardObjectiveClass, "d", true);
    private static final Field pPOSOFCType = Reflection.getField(packetPlayOutScoreboardObjectiveClass, "c", true);
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

        pPOSOFAName.set(packet, player.getName());
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
    private static final Field pPOSDOFAPosition = Reflection.getField(packetPlayOutScoreboardDisplayObjectiveClass, "a", true);
    private static final Field pPOSDOFBName = Reflection.getField(packetPlayOutScoreboardDisplayObjectiveClass, "b", true);


    private Object getShowPacket() throws InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Object packet = packetPlayOutScoreboardDisplayObjectiveConstructor.newInstance();

        pPOSDOFAPosition.set(packet, 1);
        pPOSDOFBName.set(packet, player.getName());
        return packet;
    }

    private enum Action
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
