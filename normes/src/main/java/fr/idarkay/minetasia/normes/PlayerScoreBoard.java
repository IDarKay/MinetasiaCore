package fr.idarkay.minetasia.normes;

import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.IScoreboardCriteria;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_15_R1.ScoreboardServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
 * @author alice. B. (IDarKay),
 * Created the 23/12/2019 at 11:57
 */
public class PlayerScoreBoard
{

    protected final UUID player;
    private final HashMap<Integer, String> lines = new HashMap<>();
    private String display;
    private boolean load = true;

    public PlayerScoreBoard(Player player, String display)
    {
        this.player = player.getUniqueId();
        this.display = display;
    }

    public void show()
    {
        if(load) return;
        load = true;
        final Player player = getPlayer();

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

//    private static final Class<?> packetPlayOutScoreboardScoreClass = Reflection.getNMSClass("PacketPlayOutScoreboardScore");
//    private static final Class<?> action = Reflection.getNMSClass("ScoreboardServer$Action");
//    private static final Constructor<?> pPOSSConstructor = Reflection.getConstructor(packetPlayOutScoreboardScoreClass, false, action, String.class, String.class, int.class);

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
            if (load)
            {
                final Player player = getPlayer();
                Reflection.sendPacket(player, new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, player.getName(), text, line));
            }
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
                Reflection.sendPacket(player, new PacketPlayOutScoreboardScore(action.asNMS, player.getName(), text, line));
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
            if (load)
            {
                Player player = getPlayer();
                Reflection.sendPacket(player, new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, player.getName(), lines.get(line), 0));
            }
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
        this.display = display;
        if(load)
        {
            try
            {
                Reflection.sendPacket(getPlayer(), getEditDisplayPacket(2, display));
            }
            catch(Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * delete the ScoreBoard
     */
    public void destroy()
    {
        load = false;
        try
        {
            Reflection.sendPacket(getPlayer(), getEditDisplayPacket(1, null));
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    private static final Class<?> packetPlayOutScoreboardObjectiveClass = Reflection.getNMSClass("PacketPlayOutScoreboardObjective");
    private static final Field pPOSOFAName = Reflection.getDeclaredField(packetPlayOutScoreboardObjectiveClass, "a", true);
    private static final Field pPOSOFBDisplay = Reflection.getDeclaredField(packetPlayOutScoreboardObjectiveClass, "b", true);
    private static final Field pPOSOFDMode = Reflection.getDeclaredField(packetPlayOutScoreboardObjectiveClass, "d", true);
    private static final Field pPOSOFCType = Reflection.getDeclaredField(packetPlayOutScoreboardObjectiveClass, "c", true);


    private Object getEditDisplayPacket(int mode, String name) throws InstantiationException, IllegalAccessException, InvocationTargetException
    {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();

        pPOSOFAName.set(packet, getPlayer().getName());
        pPOSOFDMode.set(packet, mode);

        if(mode == 0 || mode == 2)
        {
            pPOSOFBDisplay.set(packet, new ChatComponentText(name));

            pPOSOFCType.set(packet, IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        }

        return packet;
    }


    private static final Class<?> packetPlayOutScoreboardDisplayObjectiveClass = Reflection.getNMSClass("PacketPlayOutScoreboardDisplayObjective");
    private static final Field pPOSDOFAPosition = Reflection.getDeclaredField(packetPlayOutScoreboardDisplayObjectiveClass, "a", true);
    private static final Field pPOSDOFBName = Reflection.getDeclaredField(packetPlayOutScoreboardDisplayObjectiveClass, "b", true);

    private Object getShowPacket() throws InstantiationException, IllegalAccessException, InvocationTargetException
    {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();

        pPOSDOFAPosition.set(packet, 1);
        pPOSDOFBName.set(packet, getPlayer().getName());
        return packet;
    }

    public enum Action
    {
        CHANGE(ScoreboardServer.Action.CHANGE),
        REMOVE(ScoreboardServer.Action.REMOVE),
        ;

        public ScoreboardServer.Action asNMS;

        Action(ScoreboardServer.Action asNMS)
        {
            this.asNMS = asNMS;
        }

    }

}
