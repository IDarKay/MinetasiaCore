package net.redheademile.minetasia.normes;

import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.component.TextComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutBoss;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("all")
public class BossBarAPI
{
	private static boolean init = false;
	private static JavaPlugin plugin;
	private static final List<BossBarAPI> bars = new ArrayList<>();

	private final UUID uuid;

	private Function<Player, String> title;
	private BarColor color;
	private BarStyle style;
	private boolean autoAdd;

	private float progress;
	private List<UUID> listen;

	private BossBarAPI(Function<Player, String> title, BarColor color, BarStyle style, boolean autoAdd)
	{
		this.uuid = UUID.randomUUID();

		this.title = title;
		this.color = color;
		this.style = style;
		this.autoAdd = autoAdd;

		this.progress = 0F;
		this.listen = new ArrayList<>();
	}

	private void update(Action action)
	{
		listen.forEach(p -> update(Bukkit.getPlayer(p), action));
	}

	private static final Field PACKET_PLAY_OUT_BOSS_A = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "a", true);
	private static final Field PACKET_PLAY_OUT_BOSS_B = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "b", true);
	private static final Field PACKET_PLAY_OUT_BOSS_C = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "c", true);
	private static final Field PACKET_PLAY_OUT_BOSS_D = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "d", true);
	private static final Field PACKET_PLAY_OUT_BOSS_E = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "e", true);
	private static final Field PACKET_PLAY_OUT_BOSS_F = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "f", true);
	private static final Field PACKET_PLAY_OUT_BOSS_G = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "g", true);
//	private static final Field PACKET_PLAY_OUT_BOSS_H = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "h", true);
//	private static final Field PACKET_PLAY_OUT_BOSS_I = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "i", true);
//	private static final Field PACKET_PLAY_OUT_BOSS_J = fr.idarkay.minetasia.normes.Reflection.getDeclaredField(PacketPlayOutBoss.class, "j", true);

	private void update(Player p, Action action)
	{
		try
		{
			PacketPlayOutBoss packetPlayOutBoss = new PacketPlayOutBoss();

			PACKET_PLAY_OUT_BOSS_A.set(packetPlayOutBoss, uuid);
			PACKET_PLAY_OUT_BOSS_B.set(packetPlayOutBoss, action.real);
			PACKET_PLAY_OUT_BOSS_C.set(packetPlayOutBoss,  new TextComponent(title.apply(p)).toChatBaseComponent());
			PACKET_PLAY_OUT_BOSS_D.set(packetPlayOutBoss, progress);
			PACKET_PLAY_OUT_BOSS_E.set(packetPlayOutBoss, color.real);
			PACKET_PLAY_OUT_BOSS_F.set(packetPlayOutBoss, style.real);
	//					PACKET_PLAY_OUT_BOSS_G.set(packetPlayOutBoss, false);
	//					PACKET_PLAY_OUT_BOSS_H.set(packetPlayOutBoss, false);
	//					PACKET_PLAY_OUT_BOSS_I.set(packetPlayOutBoss, false);

			Reflection.sendPacket(p, packetPlayOutBoss);

		} catch (IllegalArgumentException | IllegalAccessException e)
		{

		}
	}
	
	public void add(Player p)
	{
		this.listen.add(p.getUniqueId());
		update(p, Action.ADD);
	}
	
	public void remove(Player p)
	{
		this.listen.remove(p.getUniqueId());
		update(p, Action.REMOVE);
	}
	
	public void remove()
	{
		listen.forEach(uuid -> update(Bukkit.getPlayer(uuid), Action.REMOVE));
		bars.remove(this);
	}
	
	public void changeProgress(float progress)
	{
		this.progress = progress;
		update(Action.UPDATE_PCT);
	}
	
	public void changeTitle(Function<Player, String> title)
	{
		this.title = title;
		update(Action.UPDATE_NAME);
	}
	
	public void changeColor(BarColor color)
	{
		this.color = color;
		update(Action.UPDATE_STYLE);
	}
	
	public void changeStyle(BarStyle style)
	{
		this.style = style;
		update(Action.UPDATE_STYLE);
	}
	
	public boolean isAutoAdd() { return this.autoAdd; }
	
	public static BossBarAPI build(Function<Player, String> title, BarColor color, BarStyle style, boolean autoAdd)
	{
		if (!init)
		{
			init = true;
			Bukkit.getPluginManager().registerEvents(new Listener()
			{
				@EventHandler
				public void onJoin(PlayerJoinEvent e)
				{
					bars.forEach(bar -> { if(bar.isAutoAdd()) bar.add(e.getPlayer()); });
				}
				
				@EventHandler
				public void onQuit(PlayerQuitEvent e)
				{
					bars.forEach(bar -> { bar.remove(e.getPlayer()); });
				}
			}, plugin);
		}
		
		BossBarAPI bar = new BossBarAPI(title, color, style, autoAdd);
		if(autoAdd) Bukkit.getOnlinePlayers().forEach(p -> bar.add(p));
		bars.add(bar);
		return bar;
	}
	
	public static void setPlugin(JavaPlugin plugin) { BossBarAPI.plugin = plugin; }
	
	private static enum Action
	{
		ADD, REMOVE, UPDATE_PCT, UPDATE_NAME, UPDATE_STYLE, UPDATE_PROPERTIES;
		
		private Enum real;
		Action()
		{
			this.real = Reflection.e("PacketPlayOutBoss$Action", name());
		}
	}
	
	public static enum BarStyle
	{
		PROGRESS, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20;
		
		private Enum real;
		BarStyle()
		{
			this.real = Reflection.e("BossBattle$BarStyle", name());
		}
	}
	
	public static enum BarColor
	{
		PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE;
		
		private Enum real;
		BarColor()
		{
			this.real = Reflection.e("BossBattle$BarColor", name());
		}
	}
}
