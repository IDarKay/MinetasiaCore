package fr.idarkay.minetasia.common.ServerConnection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * File <b>client</b> located on fr.idarkay.langfilegenerator
 * client is a part of LangFileGenerator.
 * <p>
 * Copyright (c) 2020 LangFileGenerator.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 09/02/2020 at 10:13
 */
public class MessageClient
{

    private static MessageReceiver receiver;

    public static void setReceiver(MessageReceiver receiver)
    {
        MessageClient.receiver = receiver;
    }

    public static String send(String ip, int port, String msg, boolean needRep)
    {
        try(final Socket con = new Socket(ip, port))
        {
            con.setKeepAlive(false);
            con.setReuseAddress(false);

            send(con, msg);

            if(!needRep) return null;

           return read(con);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void send(Socket con, String msg) throws IOException
    {
        final BufferedOutputStream out = new BufferedOutputStream(con.getOutputStream());

        out.write(msg.getBytes());
        out.flush();
    }

    public static void process(Socket socket)
    {
        receiver.recived(socket);
    }

    public static String read(Socket con) throws IOException
    {
        final BufferedInputStream in = new BufferedInputStream(con.getInputStream());
        final byte[] b = new byte[4096];
        int i = in.read(b);
        if(i < 0) return null;
        return new String(b, 0, i);
    }

}
