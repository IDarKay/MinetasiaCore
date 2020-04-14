package fr.idarkay.minetasia.normes.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.idarkay.minetasia.normes.MinetasiaPlugin;
import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.hologram.Hologram;
import fr.idarkay.minetasia.normes.utils.BukkitUtils;
import fr.idarkay.minetasia.normes.utils.NMSUtils;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File <b>MinetasiaNpc</b> located on fr.idarkay.minetasia.normes.npc
 * MinetasiaNpc is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 25/03/2020 at 16:19
 */
public class MinetasiaNpc
{
    protected static final List<MinetasiaNpc> loadNpc = new ArrayList<>();

    private static MinetasiaPlugin plugin;
    private static final BukkitRunnable runnable = new NPCRunnable();
    private volatile static boolean isRunnable = false;

    public static void setPlugin(MinetasiaPlugin plugin)
    {
        MinetasiaNpc.plugin = plugin;
    }

    protected final List<UUID> loadPlayer = new ArrayList<>();
    private final GameProfile gameProfile;
    private final UUID uuid = getUUIDV2();

    @Nullable
    private String sName = null;
    @Nullable
    private Hologram hologram;

    public MinetasiaNpc()
    {
        this.gameProfile = new GameProfile(uuid, "null");
//        GameProfile
    }

    /**
     * add a skin to the npc from player skin
     * @param player the player to take the skin
     */
    public void withTexture(Player player)
    {
        withTexture(BukkitUtils.getTextureFromPlayer(player));
    }

    /**
     * add s skin to the npc from string texture
     * @param string the texture
     */
    public void withTexture(String string)
    {
        Validate.notNull(string);
        final String[] data = string.split("_");
        if(data.length > 2) throw new IllegalArgumentException("invalid format");
        final Property property;
        if(data.length > 1) property = new Property("textures", data[0], data[1]);
        else property = new Property("textures", string);
        gameProfile.getProperties().put("textures", property);
    }

    public void withHologram(@Nullable Hologram hologram)
    {
        this.hologram = hologram;
    }

    private static final Field GAME_PROFILE_NAME = Reflection.getDeclaredField(GameProfile.class, "name", true);

    /**
     * set the name of the npc
     * @param name the name
     */
    public void witheDisplayName(String name)
    {
        this.sName = name;
        try
        {
            GAME_PROFILE_NAME.set(gameProfile, sName);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }



    //    private static final Constructor<?> PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR = Reflection.getConstructor(PACKET_PLAY_OUT_PLAYER_INFO_CLASS, false);
//    private static final Class<?> ENTITY_PLAYER_ARRAY = Objects.requireNonNull(Reflection.getArrayNMSClass("EntityPlayer"));
//    private static final Field ENUM_PLAYER_INFO_ACTION = Reflection.getDeclaredField(PACKET_PLAY_OUT_PLAYER_INFO_CLASS, "a", true);
//    private static final Field LIST_PLAYER_INFO = Reflection.getDeclaredField(PACKET_PLAY_OUT_PLAYER_INFO_CLASS, "b", true);
//    private static final Class<?> ENUM_GAMEMODE = Objects.requireNonNull(Reflection.getNMSClass("EnumGamemode"));
//    private static final Object CREATIVE = Reflection.getEnumField(ENUM_GAMEMODE, "CREATIVE");//    private static final Object REMOVE_PLAYER = Reflection.getEnumField(PLAYER_INFO_ACTION, "REMOVE_PLAYER");
//    private static final Class<?> PLAYER_INFO_DATA = Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutPlayerInfo$PlayerInfoData"));
//    private static final Constructor<?> PLAYER_INFO_CONSTRUCTOR = Reflection.getConstructor(PLAYER_INFO_DATA, false,PACKET_PLAY_OUT_PLAYER_INFO_CLASS,  GameProfile.class, int.class, ENUM_GAMEMODE, ReflectionVar.I_CHAT_BASE_COMPONENT);
//    private static final Field EID = Reflection.getDeclaredField(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN, "a", true);
//    private static final Field UUID = Reflection.getDeclaredField(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN, "b", true);
//    private static final Field POS_X = Reflection.getDeclaredField(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN, "c", true);
//    private static final Field POS_Y = Reflection.getDeclaredField(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN, "d", true);
//    private static final Field POS_Z = Reflection.getDeclaredField(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN, "e", true);
//    private static final Field YAW = Reflection.getDeclaredField(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN, "f", true);
//    private static final Field PITCH = Reflection.getDeclaredField(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN, "g", true);

//    private static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO_CLASS = Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutPlayerInfo"));
//    private static final Class<?> ENTITY_PLAYER = Objects.requireNonNull(Reflection.getNMSClass("EntityPlayer"));
//    private static final Class<?> MINECRAFT_SERVER = Objects.requireNonNull(Reflection.getNMSClass("MinecraftServer"));
//    private static final Class<?> WORLD_SERVER = Objects.requireNonNull(Reflection.getNMSClass("WorldServer"));
//    private static final Class<?> PLAYER_INTERACT_MANAGER = Objects.requireNonNull(Reflection.getNMSClass("PlayerInteractManager"));
//    private static final Class<?> PLAYER_INFO_ACTION = Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"));
//    private static final Class<?> PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN = Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutNamedEntitySpawn"));
//    private static final Class<?> ENTITY_HUMAN = Reflection.getNMSClass("EntityHuman");
//
//    private static final Constructor<?> PLAYER_INTERACT_MANAGER_CONSTRUCTOR = Reflection.getConstructor(PLAYER_INTERACT_MANAGER, false, WORLD_SERVER);
//    private static final Constructor<?> ENTITY_PLAYER_CONSTRUCTOR = Reflection.getConstructor(ENTITY_PLAYER, false, MINECRAFT_SERVER, WORLD_SERVER, GameProfile.class, PLAYER_INTERACT_MANAGER);
//    private static final Constructor<?> PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR_BIS = Reflection.getConstructor(PACKET_PLAY_OUT_PLAYER_INFO_CLASS, false, PLAYER_INFO_ACTION,  Iterable.class);
//    private static final Constructor<?> PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CONSTRUCTOR = Reflection.getConstructor(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN, false, ENTITY_HUMAN);
//
//    private static final Method ENTITY_PLAYER_SET_LOCATION = Reflection.getMethod(ENTITY_PLAYER, false, "setLocation", double.class, double.class, double.class, float.class, float.class);



//    private static final Object ADD_PLAYER = Reflection.getEnumField(PLAYER_INFO_ACTION, "ADD_PLAYER");

    private PacketPlayOutPlayerInfo packetInfoAdd, packetInfoRemove;
    private PacketPlayOutNamedEntitySpawn spawnPacket;
    private PacketPlayOutEntityDestroy packetDestroy;
    private Location lastLocation, currentLocation;

    private EntityPlayer npc;
    private boolean isLoad = false;

    /**
     * summon the npc can't use twice
     * @param location the location where spawn the npc
     */
    public void spawn(@NotNull Location location)
    {
        if(isLoad) throw new IllegalArgumentException("npc already load");
        Validate.isTrue(sName != null, "no display name set");
        final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        final WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        final PlayerInteractManager interactManager = new PlayerInteractManager(world);

        this.npc = new EntityPlayer(server, world, gameProfile, interactManager);
        npc.getDataWatcher().set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
//        npc.getDataWatcher().register();
        this.packetInfoAdd = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
        this.currentLocation = location;
        updateSpawnPacket();
        this.packetInfoRemove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc);
        this.packetDestroy = new PacketPlayOutEntityDestroy(npc.getId());
        isLoad = true;
        loadNpc.add(this);
        checkNeedRegister();

        if(hologram != null)
        {
            hologram.spawn(location.clone().add(0, 2, 0));
        }
    }

    @Nullable
    public static MinetasiaNpc getNpcFromId(int id)
    {
        for (MinetasiaNpc minetasiaNpc : loadNpc)
        {
            if(minetasiaNpc.npc != null && minetasiaNpc.npc.getId() == id) return minetasiaNpc;;
        }
        return null;
    }

    protected void showToPlayer(Player player, UUID uuid)
    {
        loadPlayer.add(uuid);
        updateSpawnPacket();
        Reflection.sendPacket(player, packetInfoAdd);
        Reflection.sendPacket(player, spawnPacket);
        Reflection.sendPacket(player, new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true));
        Bukkit.getScheduler().runTaskLater(plugin, () ->  Reflection.sendPacket(player, packetInfoRemove), 5L);
    }

    protected void unShowToPlayer(Player player, UUID uuid)
    {
        loadPlayer.remove(uuid);
        Reflection.sendPacket(player, packetDestroy);
    }

    public static void disconnectPlayerForAll(Player player)
    {
        for (MinetasiaNpc minetasiaNpc : loadNpc)
        {
            minetasiaNpc.disconnectPlayer(player);
        }
    }

    protected void disconnectPlayer(Player player)
    {
        for (MinetasiaNpc minetasiaNpc : loadNpc)
        {
            loadPlayer.remove(player.getUniqueId());
        }

    }

    private void updateSpawnPacket()
    {
        if(lastLocation != null && lastLocation.equals(currentLocation)) return;
        this.lastLocation = currentLocation;
        updatePlayerLocation(currentLocation);
        this.spawnPacket = new PacketPlayOutNamedEntitySpawn(npc);
    }

    private void updatePlayerLocation(@NotNull Location location)
    {
        npc.setLocation(location.getX(),  location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    private static void checkNeedRegister()
    {
        if(loadNpc.isEmpty())
        {
            if(isRunnable) {
                runnable.cancel();
                isRunnable = false;
            }
        }
        else
        {
            if(!isRunnable)
            {
                runnable.runTaskTimerAsynchronously(plugin, 10L, 20L);
                isRunnable = true;
            }
        }
    }

    public void teleport(@NotNull Location location)
    {
        Validate.notNull(location);
        final World world = location.getWorld() == null ? currentLocation.getWorld() : location.getWorld();
        location.setWorld(world);
        if(!world.equals(currentLocation.getWorld()))
        {
            throw new IllegalArgumentException("can't tp npc from dimension to another spawn new npc");
        }

        currentLocation = location;
        updatePlayerLocation(location);
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(npc);
        sendUpdatePacket(packet);

        if(hologram != null)
            hologram.teleport(location);
    }

    /**
     * move the npc max 8 block
     * @param vector the move vector
     */
    public void move(Vector vector)
    {
        final Location newLocation = currentLocation.clone().add(vector);
        PacketPlayOutEntity.PacketPlayOutRelEntityMove packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                npc.getId(),
                toXDelta(newLocation),
                toYDelta(newLocation),
                toZDelta(newLocation),
                true
        );
        this.currentLocation = newLocation;
        sendUpdatePacket(packet);

        if(hologram != null)
            hologram.move(vector);;
    }

    /**
     * turn the head and boy of the npc npc
     * @param yaw the yaw (horizontal)
     * @param pitch the pitch (vertical)
     */
    public void turn(float yaw, float pitch)
    {
        turnHead(yaw);
        turnBody(yaw, pitch);
    }

    /**
     * turn the npc head
     * @param yaw the yaw (horizontal)
     */
    public void turnHead(float yaw)
    {
        this.currentLocation.setYaw(yaw);
        PacketPlayOutEntityHeadRotation rotationPacket = new PacketPlayOutEntityHeadRotation(npc,
                NMSUtils.floatAngleToByte(yaw)
        );
        sendUpdatePacket(rotationPacket);
    }

    /**
     * turn the body npc
     * @param yaw the yaw (horizontal)
     * @param pitch the pitch (vertical)
     */
    public void turnBody(float yaw, float pitch)
    {
        PacketPlayOutEntity.PacketPlayOutEntityLook packet = new PacketPlayOutEntity.PacketPlayOutEntityLook(
                npc.getId(),
                NMSUtils.floatAngleToByte(yaw),
                NMSUtils.floatAngleToByte(pitch),
                true
        );
        this.currentLocation.setYaw(yaw);
        this.currentLocation.setPitch(pitch);
        sendUpdatePacket(packet);
    }

    /**
     * move and turn the npc
     * @param vector the move vector
     * @param yaw the yaw
     * @param pitch the pitch
     */
    public void moveAndTurn(Vector vector, float yaw, float pitch)
    {
        final Location newLocation = currentLocation.clone().add(vector);
        newLocation.setYaw(yaw);
        newLocation.setPitch(pitch);
        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(
                npc.getId(),
                toXDelta(newLocation),
                toYDelta(newLocation),
                toZDelta(newLocation),
                NMSUtils.floatAngleToByte(newLocation.getYaw()),
                NMSUtils.floatAngleToByte(newLocation.getPitch()),
                true
        );
        PacketPlayOutEntityHeadRotation rotationPacket = new PacketPlayOutEntityHeadRotation(npc,
                NMSUtils.floatAngleToByte(newLocation.getYaw())
        );
        this.currentLocation = newLocation;
        sendUpdatePacket(packet);
        sendUpdatePacket(rotationPacket);

        if(hologram != null)
            hologram.move(vector);;
    }

    private void sendUpdatePacket(Packet<?> packet)
    {
        loadPlayer.forEach(p -> {
            final Player player = Bukkit.getPlayer(p);
            if(player != null)
            {
                Reflection.sendPacket(player, packet);
            }
        });
    }

    private  short toXDelta(Location c)
    {
        return NMSUtils.toDelta(currentLocation.getX(), c.getX());
    }

    private short toYDelta(Location c)
    {
        return NMSUtils.toDelta(currentLocation.getY(), c.getY());
    }

    private short toZDelta(Location c)
    {
        return NMSUtils.toDelta(currentLocation.getZ(), c.getZ());
    }

    /**
     * not work for now !
     * @param location the path finder goal
     *
     */
    public void moveToWithPathFinder(Location location)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
        {
            final AStar a = new AStar(currentLocation, location, 100000, false, 1);
            moveToWithPathFinder(a.getPath());
        });
    }


    public void moveToWithPathFinder(Location[] trav)
    {
        if(trav.length == 0) return;
        final Location beg = trav[0];
        // tp if start distance if bigger than 8
        if(beg.distance(currentLocation) > 7)
        {
            teleport(beg);
        }
        new WalkRunnable(trav).runTaskTimerAsynchronously(plugin, 0L, 1L);
    }

    /**
     * remove the npc after you can use spawn()
     */
    public void delete()
    {
        if(!isLoad) throw new IllegalArgumentException("npc not load");
        isLoad = false;
        loadNpc.remove(this);
        sendUpdatePacket(packetInfoRemove);
        checkNeedRegister();
    }

    private static SecureRandom secureRandom = new SecureRandom();

    private static UUID getUUIDV2()
    {
        try
        {
            SecureRandom ng = secureRandom;

            byte[] randomBytes = new byte[16];
            ng.nextBytes(randomBytes);
            randomBytes[6]  &= 0x0f;  /* clear version        */
            randomBytes[6]  |= 0x2f;  /* set to version 2     */
            randomBytes[8]  &= 0x3f;  /* clear variant        */
            randomBytes[8]  |= 0x80;  /* set to IETF variant  */
            return (UUID) Reflection.getConstructor(Class.forName("java.util.UUID"), true, byte[].class).newInstance(randomBytes);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * gte the current location of the npc
     * @return the location
     */
    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    private static final Vector ZERO_ANGLE_VECTOR = new Vector(1, 0, 0);

    private class WalkRunnable extends BukkitRunnable
    {

        private final Location[] locations;

        public WalkRunnable(Location[] locations)
        {
            this.locations = smoothJourney(locations, 2);
        }

        int i = 0;

        @Override
        public void run()
        {

            final Location l = locations[i].add(0.5, 0, 0.5);
            final Location nextLoc = locations.length > (i + 1) ? locations[i + 1] : null;
            moveAndTurn(getVector(currentLocation.clone(), l), nextLoc == null ? currentLocation.getYaw() : (float) getAngle(l, nextLoc),0);
//            teleport(l);
            i++;
            if(i >= locations.length)
            {
                cancel();
            }
        }
    }

    private Vector getVector(@NotNull Location location1, @NotNull Location location2)
    {
        Validate.notNull(location1);
        Validate.notNull(location2);
        return new Vector(location2.getX() - location1.getX(), location2.getY() - location1.getY(), location2.getZ() - location1.getZ());
    }

    private double getAngle(Location l1, Location l2)
    {
        final Location editL2 = l2.clone();
        editL2.setY(l1.getY());
        return Math.toDegrees(getVector(l1, editL2).angle(ZERO_ANGLE_VECTOR));
    }

    public Location[] smoothJourney(Location[] locs, double speed)
    {
        ArrayList<Location> newTravel = new ArrayList<>();

		/*Location from = null, lastTo = null;
		double xDif, zDif, lastXDif = 0, lastZDif = 0;
		for(int i = 0; i < locs.length; i++)
		{
			if(from == null) from = locs[i];
			else
			{
				Location to = locs[i];
				xDif = to.getX() - from.getX();
				zDif = to.getZ() - from.getZ();
				if(from.getY() < to.getY())
				{
					newTravel.addAll(directTravel(from, lastTo, speed));

					double yPos = from.getY();
					while(yPos < to.getY())
					{
						yPos += 0.2D * speed;
						newTravel.add(new Location(from.getWorld(), from.getX(), yPos, from.getZ(), Double.valueOf(to.getX() + xDif).floatValue(), Double.valueOf(to.getZ() + zDif).floatValue()));
					}
					from = to;
					lastTo = null;
				}
				else
				{
					if((xDif != lastXDif || zDif != lastZDif) && lastTo != null)
					{
						newTravel.addAll(directTravel(from, lastTo, speed));

						from = to;
						lastTo = null;
					}
					else lastTo = to;
				}
				lastXDif = xDif;
				lastZDif = zDif;
			}
		}
		if(from != null && lastTo != null) newTravel.addAll(directTravel(from, lastTo, speed));*/
        // readheadEmile
        for(int i = 0; i < locs.length; i++)
        {
            Location from = locs[i], to = locs.length > i + 1 ? locs[i + 1] : null;
            double x = from.getX(), y = from.getY(), z = from.getZ();
            if(to != null)
            {
                double xDif = to.getX() - from.getX(), yDif = to.getY() - from.getY(), zDif = to.getZ() - from.getZ();
                if(yDif > 0)
                {
                    double yPos = from.getY();
                    while(from.getY() > to.getY() ? yPos >= to.getY() : yPos <= to.getY())
                    {
                        yPos += 0.2D * speed;
                        newTravel.add(new Location(from.getWorld(), x, y = yPos, z, Double.valueOf(to.getX() + xDif).floatValue(), Double.valueOf(to.getZ() + zDif).floatValue()));
                    }
                }

                if(xDif != 0)
                {
                    double xPos = from.getX();
                    while(from.getX() > to.getX() ? xPos >= to.getX() : xPos <= to.getX())
                    {
                        xPos += 0.1D * (xDif > 0 ? 1D : -1D) * speed;
                        newTravel.add(new Location(from.getWorld(), x = xPos, y, z, Double.valueOf(to.getX() + xDif).floatValue(), Double.valueOf(to.getZ() + zDif).floatValue()));
                    }
                }

                if(zDif != 0)
                {
                    double zPos = from.getZ();
                    while(from.getZ() > to.getZ() ? zPos >= to.getZ() : zPos <= to.getZ())
                    {
                        zPos += 0.1D * (zDif > 0 ? 1D : -1D) * speed;
                        newTravel.add(new Location(from.getWorld(), x, y, z = zPos, Double.valueOf(to.getX() + xDif).floatValue(), Double.valueOf(to.getZ() + zDif).floatValue()));
                    }
                }
            }
            else newTravel.add(from);
        }
        return newTravel.toArray(new Location[0]);
    }

    public UUID getUuid()
    {
        return uuid;
    }
}
