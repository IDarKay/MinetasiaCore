package fr.idarkay.minetasia.core.spigot.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import fr.idarkay.minetasia.core.api.event.FRSMessageEvent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * make by RedHeadEmile
 * @since 1.0
 */
@Deprecated
public class FRSClient
{
	private JavaPlugin plugin;
	
	private static boolean enable = false, connected = false, fixEnable = false;
	
	private Runnable runnable;
	private PrintWriter out;
	
	private String ip, password;
	private int port, timeout;
	
	private static Socket s;
	private static InputStream inS;
	private static OutputStream outS;

	private Thread t1, t2;
	private List<String> waiting;
	
	private Map<String, String> responses;

	@Deprecated
	public FRSClient(JavaPlugin plugin)
	{
		this.plugin = plugin;
		waiting = new ArrayList<>();
	}

	@Deprecated
	private void breakThread()
	{
		try
		{
			Class.forName("org.spigotmc.AsyncCatcher").getDeclaredField("enabled").set(null, false);
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Connect to a server
	 * @param out Output to print debug and message
	 * @param port Port of the server
	 * @param password Password of the server (if needed)
	 * @throws IOException 
	 */
	@Deprecated
	public boolean startConnection(OutputStream out, String ip, int port, String password, int timeout)
	{
		breakThread();
		
		if(enable) return false;
		if(responses == null) responses = new HashMap<>();

		if(out != null)
		{
			this.out = new PrintWriter(out, true);
			
			this.ip = ip;
			this.port = port;
			this.timeout = timeout;
			this.password = password;

			runnable = new Runnable()
			{
				@Override
				public void run()
				{

					try
					{
						s = new Socket();
						s.setKeepAlive(true);
						s.connect(new InetSocketAddress(ip, port), 15 * 1000);
						FRSClient.this.out.println("Connected !");
						enable = true;
						connected = true;
						fixEnable = true;
						
						inS = s.getInputStream();
						outS = s.getOutputStream();
						StringBuilder builder = new StringBuilder();
						
						if(waiting != null)
						{
							for (String value : waiting) outS.write(value.getBytes());
							waiting.clear();
						}
						
						while(enable)
						{
							try
							{
								int available = inS.available();
								if(available > 0)
								{
									byte[] received = new byte[available];
									inS.read(received);
									builder.append(new String(received));
									
									String result = builder.toString();
									if(result.endsWith("\n"))
									{
										result = new String(result.substring(0, result.length() - 1));
										builder.delete(0, builder.length());
										
										for(String line : result.split("\n"))
										{
											try
											{
												onMessageReceived(line);
											}
											catch(Exception e) { e.printStackTrace(); }
										}
									}
								}
							}
							catch (IOException e) { e.printStackTrace(); }
							try
							{
								Thread.sleep(10);
							}
							catch (InterruptedException ignore) { }
						}
					}
					catch(IOException e)
					{
						FRSClient.this.out.println("Connection impossible/lost !");
						fixEnable = false;
					}
				}
			};
		}
		
		t2 = new Thread(runnable);
		t2.start();

		t1 = new Thread()
		{
			@Override
			public void run()
			{
				while(true)
				{
					if(enable)
					{
						try
						{
							if(connected)
							{
								connected = false;
								send("areyouhere", false);
							}
							else
							{
								FRSClient.this.out.println("Time out");
								restart();
							}
						}
						catch(Exception ignore) { }
					}
					try
					{
						Thread.sleep(10_000L);
					}
					catch(Exception e) { if(!enable) break; }
				}
			}
		};
		t1.start();
		return true;
	}
	@Deprecated
	private void restart()
	{
		shutdown(false);
		out.println("Retrying...");
		startConnection(null, ip, port, password, timeout);
	}
	@Deprecated
	public void shutdown(boolean onDisable)
	{
		try
		{
			fixEnable = false;
			enable = false;
			if(onDisable) t1.interrupt();
			t2.interrupt();
			s.close();
			outS.close();
			inS.close();
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	@Deprecated
	private synchronized String send(String message, boolean needRep) throws IOException
	{
		if(needRep)
		{
			if(s != null && !s.isClosed())
			{
				long time = System.currentTimeMillis();
				String identifier = Long.toString(System.currentTimeMillis());
				outS.write((identifier + " " + message.replace("\n", "\\n") + "\n").getBytes());
				outS.flush();
				while(!responses.containsKey(identifier) && System.currentTimeMillis() - time <= 10000L)
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) { e.printStackTrace(); }
				
				String response = responses.get(identifier);
				if(response != null)
				{
					responses.remove(identifier);
					return response.replace("\\n", "\n");
				}
			}
			return null;
		}
		else
		{
			if(s != null && !s.isClosed() && outS != null && s.isConnected())
			{
				outS.write(("0 " + message.replace("\n", "\\n") + "\n").getBytes());
				outS.flush();
				return null;
			}
			else waiting.add(message);
			return null;
		}
	}
	@Deprecated
	private void onMessageReceived(String message)
	{
		if(message.equals("imhere")) connected = true;
		else
		{
			String[] data = message.split(" ");
			if(data[0].equals("publish"))
			{
				String channel = data[1], msg = "";
				for(int i = 2; i < data.length; i++) msg += (i == 2 ? "" : " ") + data[i];
				
				onPublish(channel, msg);
			}
			else if(data[0].equals("rep"))
			{
				String identifer = data[1];
				StringBuilder value = new StringBuilder();
				for(int i = 2; i < data.length; i++) value.append(i == 2 ? "" : " ").append(data[i]);
				
				responses.put(identifer, value.toString());
			}
		}
	}
	@Deprecated
	private void onPublish(String channel, String message)
	{
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new FRSMessageEvent(channel, message)));
	}
	@Deprecated
	public boolean isConnected()
	{
		return fixEnable;
	}
	@Deprecated
	public void publish(String channel, String message, boolean... sync)
	{
		Consumer<BukkitTask> cons = task ->
		{
			try
			{
				send("publish " + channel + " " + (message == null ? "null" : message), false);
			}
			catch (IOException e) { e.printStackTrace(); }
		};
		if(sync.length == 0 || !sync[0]) Bukkit.getScheduler().runTaskAsynchronously(plugin, cons);
		else cons.accept(null);
	}
	@Deprecated
	public String getValue(@NotNull String key)
	{
		Validate.notNull(key);
		try
		{
			String result = send("get " + key.replace(' ', '_'), true);
			if(result == null || result.isEmpty() || result.equals("null")) return null;
			return result;
		}
		catch(Exception e) { return null; }
	}
	@Deprecated
	public Set<String> getFields(String key)
	{
		try
		{
			String result = send("getall " + key.replace(' ', '_'), true);
			if(result == null || result.isEmpty()) return Collections.emptySet();
			Set<String> keys = new HashSet<>();
			Collections.addAll(keys, result.split(" "));
			return keys;
		}
		catch(Exception e) { return Collections.emptySet(); }
	}
	@Deprecated
	public Map<String, String> getValues(String key, Set<String> fields)
	{
		try
		{
			HashMap<String, String> valuesPerFields = new HashMap<String, String>();
			for(String field : fields)
			{
				String value = getValue(key + "/" + field);
				valuesPerFields.put(field, value);
			}
			return valuesPerFields;
		}
		catch(Exception e) { return Collections.emptyMap(); }
	}
	@Deprecated
	public void setValue(@NotNull String key, @Nullable String value, boolean sync)
	{
		Validate.notNull(key);
		final Consumer<BukkitTask> cons = task ->
		{
			try
			{
				send("set " +key.replace(' ', '_') + " " +  value, false);
			}
			catch (IOException e) { e.printStackTrace(); }
		};
		if(!sync) Bukkit.getScheduler().runTaskAsynchronously(plugin, cons);
		else cons.accept(null);
	}

}