package fr.idarkay.minetasia.common.ServerConnection;

import java.net.Socket;

/**
 * File <b>MessageReceiver</b> located on fr.idarkay.minetasia.common.ServerConnection
 * MessageReceiver is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 09/02/2020 at 12:11
 */
@FunctionalInterface
public interface MessageReceiver
{

    void recived(Socket socket);

}
