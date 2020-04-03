package fr.idarkay.minetasia.normes.sign;

import fr.idarkay.minetasia.normes.Reflection;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static fr.idarkay.minetasia.normes.utils.ReflectionVar.*;

/**
 * File <b>MinetasiaSignGui</b> located on fr.idarkay.minetasia.normes.sign
 * MinetasiaSignGui is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 24/02/2020 at 20:00
 */
public class MinetasiaSignGui
{
    public static final Map<UUID, MinetasiaSignGui> guiMap = new HashMap<>();

    private BiConsumer<Player, String[]> completeHandler;
    private String[] lines;

    public MinetasiaSignGui(BiConsumer<Player, String[]>  completeHandler)
    {
        this.completeHandler = completeHandler;
        this.lines = new String[4];
    }

    public MinetasiaSignGui setLines(String... lines)
    {
        if (lines.length != 4) throw new IllegalArgumentException("Must have at least 4 lines");

        this.lines = lines;
        return this;
    }


    public void open(Player player)
    {
        try
        {
            final Object blockPosition = BLOCK_POSITION_CONSTRUCTOR.newInstance( player.getLocation().getBlockX(), 1, player.getLocation().getBlockZ());

            final Object packet = PACKET_PLAYER_OUT_BLOCK_CHANGE_CONSTRUCTOR.newInstance(WORLD_GET_HANDLE.invoke(player.getWorld()), blockPosition);
            BLOCK_FIELD.set(packet, CRAFT_MAGIC_NUMBERS_GET_BLOCK.invoke(null, Material.OAK_SIGN, (byte) 0));
            Reflection.sendPacket(player, packet);

            final Object components = CRAFT_SIGN_SAINTLINESS.invoke(null, (Object) lines);

            final Object sign =  TITLE_ENTITY_SIGN_CONSTRUCTOR.newInstance();
            SIGN_SET_POSITION.invoke(sign, blockPosition);
            SIGN_LINES.set(sign, components);
            Reflection.sendPacket(player, SIGN_GET_UPDATE_PACKET.invoke(sign));

            Reflection.sendPacket(player, PACKET_PLAY_OUT_OPEN_SIGN_EDITOR_CONSTRUCTOR.newInstance(blockPosition));

            guiMap.put(player.getUniqueId(), this);


        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
        {
            e.printStackTrace();
        }

    }

    public BiConsumer<Player, String[]> getCompleteHandler()
    {
        return completeHandler;
    }
}
