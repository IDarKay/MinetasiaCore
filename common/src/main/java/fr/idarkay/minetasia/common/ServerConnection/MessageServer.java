package fr.idarkay.minetasia.common.ServerConnection;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * File <b>server</b> located on fr.idarkay.langfilegenerator
 * server is a part of LangFileGenerator.
 * <p>
 * Copyright (c) 2020 LangFileGenerator.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 09/02/2020 at 10:13
 */
public class MessageServer
{
    private ServerSocket server = null;
    private boolean isRunning = true;
    private final int port;

    public MessageServer(int port)
    {
        this.port = port;
        try
        {
            server = new ServerSocket(port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public int getPort()
    {
        return port;
    }

    public void open()
    {
        new Thread(() ->
        {
            while (isRunning)
            {
                try
                {
                   MessageClient.process(server.accept());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            try {

                server.close();

            } catch (IOException e) {

                e.printStackTrace();

                server = null;

            }

        }).start();
    }

    public void close()
    {
        isRunning = false;
    }

}
