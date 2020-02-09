package fr.idarkay.minetasia.core.spigot.bdd;

import java.util.HashMap;
import java.util.List;

/**
 * File <b>ORM</b> located on fr.idarkay.minetasia.core.spigot.bdd
 * ORM is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/02/2020 at 15:52
 */
public abstract class ORM
{

    private static HashMap<String, ORM> find = new HashMap<>();
    public static ORM current;



    public ORM(String name)
    {
        find.put(name, this);
    }

    public abstract void get(String name);





}
