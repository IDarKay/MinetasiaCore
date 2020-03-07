package fr.idarkay.minetasia.normes;


import java.util.HashMap;
import java.util.Objects;

/**
 * File <b>ParticlesType</b> located on fr.idarkay.minetasia.normes
 * ParticlesType is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/01/2020 at 21:28
 */
public enum ParticlesType implements Particles
{
    AMBIENT_ENTITY_EFFECT,
    ANGRY_VILLAGER,
    BARRIER,
    BLOCK,
    BUBBLE,
    CLOUD,
    CRIT,
    DAMAGE_INDICATOR,
    DRAGON_BREATH,
    DRIPPING_LAVA,
    FALLING_LAVA,
    LANDING_LAVA,
    DRIPPING_WATER,
    FALLING_WATER,
    DUST,
    EFFECT,
    ELDER_GUARDIAN,
    ENCHANTED_HIT,
    ENCHANT,
    END_ROD,
    ENTITY_EFFECT,
    EXPLOSION_EMITTER,
    EXPLOSION,
    FALLING_DUST,
    FIREWORK,
    FISHING,
    FLAME,
    FLASH,
    HAPPY_VILLAGER,
    COMPOSTER,
    HEART,
    INSTANT_EFFECT,
    ITEM,
    ITEM_SLIME,
    ITEM_SNOWBALL,
    LARGE_SMOKE,
    LAVA,
    MYCELIUM,
    NOTE,
    POOF,
    PORTAL,
    RAIN,
    SMOKE,
    SNEEZE,
    SPIT,
    SQUID_INK,
    SWEEP_ATTACK,
    TOTEM_OF_UNDYING,
    UNDERWATER,
    SPLASH,
    WITCH,
    BUBBLE_POP,
    CURRENT_DOWN,
    BUBBLE_COLUMN_UP,
    NAUTILUS,
    DOLPHIN,
    CAMPFIRE_COSY_SMOKE,
    CAMPFIRE_SIGNAL_SMOKE,
    DRIPPING_HONEY,
    FALLING_HONEY,
    LANDING_HONEY,
    FALLING_NECTAR,
    ;

    private final static Class<?> PARTICLES = Objects.requireNonNull(Reflection.getNMSClass("Particles"));
    private final static HashMap<ParticlesType, Object> load = new HashMap<>();

    @Override
    public Object getNMSObject()
    {
        Object p = load.get(this);
        if(p != null) return p;
        else
        {
            try
            {
                Object o = Reflection.getDeclaredField(PARTICLES, name(), true).get(null);
                load.put(this, o);
                return o;
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

}
