package fr.idarkay.minetasia.core.spigot.old_combats;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.old_combats.module.NoAttackCooldown;
import fr.idarkay.minetasia.core.spigot.utils.EventListener;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * File <b>OldCombatsManger</b> located on fr.idarkay.minetasia.core.spigot.old_combats
 * OldCombatsManger is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 14/04/2020 at 15:31
 */
public class OldCombatsManger implements EventListener
{


    private final List<OldCombatsModule> loadModules = new ArrayList<>();

    public OldCombatsManger(MinetasiaCore core)
    {

        register(new NoAttackCooldown());

//        try
//        {
//            for (Class<?> aClass : core.getClasses("fr/idarkay/minetasia/core/spigot/old_combats/module"))
//            {
//                try
//                {
//                    if(OldCombatsModule.class.isAssignableFrom(aClass))
//                    {
//                        System.out.println("1-1");
//                        final OldCombatsModule o = (OldCombatsModule) aClass.getConstructor().newInstance();
//                        System.out.println("1-" + o.isEnable());
//                        if(o.isEnable())
//                            loadModules.add(o);
//                    }
//                    else if(aClass.isAssignableFrom(OldCombatsModule.class))
//                    {
//                        System.out.println("1-1");
//                        final OldCombatsModule o = (OldCombatsModule) aClass.getConstructor().newInstance();
//                        System.out.println("2-" + o.isEnable());
//                        if(o.isEnable())
//                            loadModules.add(o);
//                    }
//                }
//                catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignore)
//                {
//                    // not constructor in class
//                }
//            }
//
//        }
//        catch (ClassNotFoundException | IOException e)
//        {
//            e.printStackTrace();
//        }

    }

    public void register(OldCombatsModule oldCombatsModule)
    {
        if(oldCombatsModule.isEnable())
            loadModules.add(oldCombatsModule);
    }

    @Override
    public void onPlayerJoinEvent(Player player, PlayerJoinEvent event)
    {
        loadModules.forEach(oldCombatsModule -> oldCombatsModule.onPlayerJoinEvent(player, event));
    }

    @Override
    public void onPlayerQuitEvent(Player player, PlayerQuitEvent event)
    {
        loadModules.forEach(oldCombatsModule -> oldCombatsModule.onPlayerQuitEvent(player, event));
    }

    @Override
    public void onWorldChange(Player player, PlayerChangedWorldEvent event)
    {
        loadModules.forEach(oldCombatsModule -> oldCombatsModule.onWorldChange(player, event));
    }
}
