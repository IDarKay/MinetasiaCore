package fr.idarkay.minetasia.core.api.utils;

import java.sql.Connection;

/**
 * File <b>SQLManager</b> located on fr.idarkay.minetasia.core.api.utils
 * SQLManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/11/2019 at 17:58
 * @since 1.0
 * all methods for use SQL
 * for update methods use {@link SQLManager#updateAsynchronously(String, Object...)}
 * for get use {@link SQLManager#getSQL()} and create yourself statement
 */
@Deprecated
public interface SQLManager {

    /**
     * This method is to open and test the sql connection
     *
     * @return The connection of the database (can be null or closed)
     */
    @Deprecated
    Connection getSQL();

    /**
     * This method is to execute an update request (UPDATE, DELETE, CREATE, DROP,
     * ...) <br>
     *
     * its same of {@link SQLManager#updateAsynchronously(String, Object...)} but 
     * isn't Asynchronously use only if you very very need none async ! In none 
     * Asynchronously the main thread of server will be cut !
     * 
     * 
     * @param query Query (with '?') which will be send to the database
     * @param args  Object which will replace the '?' (in order) of the query
     */
    @Deprecated
    void update(String query, Object... args);

    /**
     * This method is to execute an update request (UPDATE, DELETE, CREATE, DROP,
     * ...)
     *
     * @param query Query (with {@code '?'}) which will be send to the database
     * @param args  Object which will replace the {@code '?'} (in order) of the query
     */
    @Deprecated
    void updateAsynchronously(String query, Object... args);

    /**
     * The method is to check if the connection work
     *
     * @return True if the connection work, false otherwise
     */
    @Deprecated
    boolean connectionIsOk();
    
}
