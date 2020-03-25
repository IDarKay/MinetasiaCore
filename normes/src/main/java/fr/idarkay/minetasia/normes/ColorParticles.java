package fr.idarkay.minetasia.normes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * File <b>ColorParticles</b> located on fr.idarkay.minetasia.normes
 * ColorParticles is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 27/02/2020 at 18:14
 */
public class ColorParticles implements Particles
{

    private static final Class<?> PARTICLE_PARAM_REDSTONE = Objects.requireNonNull(Reflection.getNMSClass("ParticleParamRedstone"));
    private static final Constructor PARTICLE_PARAM_REDSTONE_CONSTRUCTOR = Reflection.getConstructor(PARTICLE_PARAM_REDSTONE, false, float.class, float.class, float.class, float.class );
    private final float red, green, blue;

    public ColorParticles(int red, int green, int blue)
    {
        this.red = verifyAndRand(red) / 255f;
        this.green = verifyAndRand(green) / 255f;
        this.blue = verifyAndRand(blue) / 255f;
    }

    private static int verifyAndRand(int c)
    {
        return Math.max(0, Math.min(c, 255));
    }

    public float getGreen()
    {
        return green;
    }

    public float getBlue()
    {
        return blue;
    }

    public float getRed()
    {
        return red;
    }

    private Object cache = null;

    public static Object getNMSObject(int red, int green, int blue)
    {
        try
        {
            return PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(verifyAndRand(red) / 255f, verifyAndRand(green) / 255f, verifyAndRand(blue) / 255f, 1F);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            return null;
        }
    }

    @Override
    public Object getNMSObject()
    {
        if(cache == null)
        {
            try
            {
                cache = PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(getRed(), getGreen(), getBlue(), 1F);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException ignore)
            {

            }
        }
        return cache;
    }
}
