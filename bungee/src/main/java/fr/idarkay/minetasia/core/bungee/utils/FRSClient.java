package fr.idarkay.minetasia.core.bungee.utils;

import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.event.FRSMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public final class FRSClient
{
	private boolean enable, connected, fixEnable = false;
	
	//private PrintWriter out;
	
	private String ip, password;
	private int port;
	
	private static Socket       s;
	private static InputStream  inS;
	private static OutputStream outS;
	
	private Map<String, String> responses;
	
	private Thread t1, t2;
	private Runnable runnable;
	
	private final MinetasiaCoreBungee main;
	
	@ConstructorProperties({"main"})
	public FRSClient(MinetasiaCoreBungee main)
	{
		this.main = main;
	}
	
	/**
	 * Connect to a server
	 *
	 * @param outputStream Output to print debug and message
	 * @param port         Port of the server
	 * @param password     Password of the server (if needed)
	 */
	public boolean startConnection(OutputStream outputStream, String ip, int port, String password)
	{
		if (enable) return false;
		if (responses == null) responses = new HashMap<>();
		
		if (outputStream != null)
		{
			//out = new PrintWriter(outputStream, true);
			
			this.ip = ip;
			this.port = port;
			this.password = password;
			
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
								if (connected)
								{
									connected = false;
									send("areyouhere", false);
								}
								else
								{
									ProxyServer.getInstance().getLogger().info("Time out");
									restart();
								}
							}
							catch (Exception e)
							{
							}
						}
						try
						{
							Thread.sleep(10_000L);
						}
						catch(Exception exe) { if(!enable) break; }
					}
				}
			};
			t1.start();
			
			runnable = () -> {
				enable = true;
				try
				{
					connected = true;
					s = new Socket();
					s.connect(new InetSocketAddress(ip, port), 5000);
					s.setKeepAlive(true);
					fixEnable = true;
					
					ProxyServer.getInstance().getLogger().info("Connected to FRS server !");
					
					inS = s.getInputStream();
					outS = s.getOutputStream();
					StringBuilder builder = new StringBuilder();
					
					while (enable)
					{
						try
						{
							int available = inS.available();
							if (available > 0)
							{
								byte[] received = new byte[available];
								inS.read(received);
								builder.append(new String(received));
								
								String result = builder.toString();
								if (result.endsWith("\n"))
								{
									result = new String(result.substring(0, result.length() - 1));
									builder.delete(0, builder.length());
									
									for(String line : result.split("\n"))
									{
										try
										{
											onMessageReceived(line);
										}
										catch (Exception e)
										{
											e.printStackTrace();
										}
									}
								}
							}
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						try
						{
							Thread.sleep(10);
						}
						catch (InterruptedException e)
						{
							if(!enable) break;
						}
					}
				}
				catch (IOException e)
				{
					ProxyServer.getInstance().getLogger().severe("Connection impossible/lost !");
					fixEnable = false;
				}
			};
		}
		
		t2 = new Thread(runnable);
		t2.start();
		
		return true;
	}
	
	private void restart()
	{
		ProxyServer.getInstance().getLogger().info("Retrying...");
		shutdown(false);
		startConnection(null, ip, port, password);
	}
	
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
		catch (Exception e) { }
	}
	
	private synchronized String send(String message, boolean needRep) throws IOException
	{
		if(outS == null) return needRep ? "" : null;
		if (needRep)
		{
			long time = System.currentTimeMillis();
			String identifier = Long.toString(time);
			outS.write((identifier + " " + message.replace("\n", "\\n") + "\n").getBytes());
			outS.flush();
			while (!responses.containsKey(identifier) && System.currentTimeMillis() - time <= 10000L)
			{
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			String response = responses.get(identifier);
			responses.remove(identifier);
			return response.replace("\\n", "\n");
		}
		else
		{
			outS.write(("0 " + message.replace("\n", "\\n") + "\n").getBytes());
			outS.flush();
			return null;
		}
	}

	public boolean isConnected()
	{
		return fixEnable;
	}

	private void onMessageReceived(String message)
	{
		if (message.equals("imhere")) connected = true;
		else
		{
			String[] data = message.split(" ");
			if (data[0].equals("publish"))
			{
				String channel = data[1], msg = "";
				for (int i = 2; i < data.length; i++) msg += (i == 2 ? "" : " ") + data[i];
				
				onPublish(channel, msg);
			}
			else if (data[0].equals("rep"))
			{
				String identifer = data[1];
				String value     = "";
				for (int i = 2; i < data.length; i++) value += (i == 2 ? "" : " ") + data[i];
				
				responses.put(identifer, value);
			}
		}
	}
	
	private void onPublish(String channel, String message)
	{
		main.getProxy()
		    .getScheduler()
		    .runAsync(main, () -> main.getProxy()
		                              .getPluginManager()
		                              .callEvent(new FRSMessageEvent(channel, message)));
	}
	
	public void publish(String channel, String message, boolean... sync)
	{
		Runnable pub = () ->
		{
			try
			{
				send("publish " + channel + " " + message, false);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		};
		if(sync.length == 0 || !sync[0]) main.getProxy().getScheduler().runAsync(main, pub);
		else pub.run();
	}


	public String getValue(@NotNull String key)
	{
		try
		{
			String result = send("get " + key.replace(' ', '_'), true);
			if (result == null || result.isEmpty()) return null;
			return result;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public Set<String> getFields(String key)
	{
		try
		{
			String result = send("getall " + key.replace(' ', '_'), true);
			if (result == null || result.isEmpty()) return Collections.emptySet();
			Set<String> keys = new HashSet<>();
			Collections.addAll(keys, result.split(" "));
			return keys;
		}
		catch (Exception e)
		{
			return Collections.emptySet();
		}
	}

	public Map<String, String> getValues(String key, Set<String> fields)
	{
		try
		{
			HashMap<String, String> valuesPerFields = new HashMap<>();
			for (String field : fields)
			{
				final String value = getValue(key + "/" + field);
				valuesPerFields.put(field, value);
			}
			return valuesPerFields;
		}
		catch (Exception e)
		{
			return Collections.emptyMap();
		}
	}

	public void setValue(@NotNull String key, @Nullable String value, boolean sync)
	{
		Runnable run = () -> {
			try
			{
				send("set " +key.replace(' ', '_') + " " +  value, false);
			}
			catch (IOException e)
			{
				//e.printStackTrace();
			}
		};
		if(!sync) main.getProxy().getScheduler().runAsync(main, run);
		else run.run();
	}

}