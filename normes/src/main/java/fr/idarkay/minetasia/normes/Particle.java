package fr.idarkay.minetasia.normes;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * File <b>Particle</b> located on fr.idarkay.minetasia.normes
 * Particle is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/01/2020 at 21:32
 */
public class Particle
{
    private final static Class<?> PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS = Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutWorldParticles"));
    private final static Class<?>  PARTICLE_PARAM_CLASS = Reflection.getNMSClass("ParticleParam");
    private final static Constructor<?> PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = Reflection.getConstructor(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, true, PARTICLE_PARAM_CLASS, boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class);

    private final Particles particles;
    private final double x,y,z;
    private final float offsetX, offsetY , offsetZ, speed;
    private final int amount;

    /**
     * create new Particle
     * @param particles particles
     * @param location location
     * @see Particle#getParticlePacket(Particles, double, double, double, float, float, float, float, int)
     */
    public Particle(@NotNull final Particles particles, @NotNull final Location location)
    {
        this(particles, location, 0f, 0f, 0f, 0f, 1);
    }

    /**
     * create new Particle
     * @param particles particles
     * @param location location
     * @param offsetX offsetX
     * @param offsetY offsetY
     * @param offsetZ offsetZ
     * @see Particle#getParticlePacket(Particles, double, double, double, float, float, float, float, int)
     */
    public Particle(@NotNull final Particles particles, @NotNull final Location location, final float offsetX, final float offsetY, final float offsetZ)
    {
        this(particles, location, offsetX, offsetZ, offsetY, 0f, 1);
    }

    /**
     * create new Particle
     * @param particles particles
     * @param location location
     * @param speed speed
     * @param amount amount
     * @see Particle#getParticlePacket(Particles, double, double, double, float, float, float, float, int)
     */
    public Particle(@NotNull final Particles particles, @NotNull final Location location , final float speed, final int amount)
    {
        this(particles, location, 0f, 0f, 0f, speed, amount);
    }

    /**
     * create new Particle
     * @param particles particles
     * @param location location
     * @param offsetX offsetX
     * @param offsetY offsetY
     * @param offsetZ offsetZ
     * @param speed speed
     * @param amount amount
     * @see Particle#getParticlePacket(Particles, double, double, double, float, float, float, float, int)
     */
    public Particle(@NotNull final Particles particles, @NotNull final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount)
    {
        Validate.notNull(location);
        this.particles = Objects.requireNonNull(particles);
        x = location.getX();
        y = location.getY();
        z = location.getZ();
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.amount = amount;
    }

    private Object packet;

    /**
     * get the equivalent packet of the particle
     * @return packet
     */
    public Object toPacket()
    {
        if(packet == null)
            packet = getParticlePacket(particles, x, y, z, offsetX, offsetY, offsetZ, speed, amount);
        return packet;
    }

    /**
     * send the particle to player
     * @param player player
     */
    public void sendToPlayer(Player player)
    {
        Reflection.sendPacket(player, toPacket());
    }

    /**
     * send the particle to all player
     */
    public void sendToAllPlayers()
    {
        Bukkit.getOnlinePlayers().forEach(this::sendToPlayer);
    }

    /**
     * Creates a new PacketPlayOutWorldParticles
     * object with the given data.
     *
     * @param param     the ParticleParam of the
     *                  packet.
     * @param locationX the x coordinate of
     *                  the location the particle
     *                  should be displayed at.
     * @param locationY the y coordinate of
     *                  the location the particle
     *                  should be displayed at.
     * @param locationZ the z coordinate of
     *                  the location the particle
     *                  should be displayed at.
     * @param offsetX   the offset x value of the
     *                  packet.
     * @param offsetY   the offset y value of the
     *                  packet.
     * @param offsetZ   the offset z value of the
     *                  packet.
     * @param speed     the speed of the particle.
     * @param amount    the amount of particles.
     * @return A PacketPlayOutWorldParticles instance with the given data or {@code null} if an error occurs.
     */
    public static Object getParticlePacket(Particles param, double locationX, double locationY, double locationZ, float offsetX, float offsetY, float offsetZ, float speed, int amount)
    {
        Validate.notNull(param);
        try
        {
            return PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR.newInstance( Objects.requireNonNull(param.getNMSObject()), true, locationX, locationY, locationZ, offsetX, offsetY, offsetZ, speed, amount);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
