package fr.idarkay.minetasia.core.spigot.kits;

import com.mongodb.client.model.Filters;
import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.api.utils.MainKit;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.bson.Document;

import java.util.*;

/**
 * File <b>KitsManager</b> located on fr.idarkay.minetasia.core.spigot.kits
 * KitsManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/12/2019 at 17:18
 */
public class KitsManager {

    private Map<String, MainKit> kits = new HashMap<>();

    public KitsManager(MinetasiaCore plugin)
    {
//        final List<String> filter = plugin.getConfig().getStringList("kits_load");
//        boolean all = filter.size() > 0 && filter.get(0).equals("*");

        for(final Document d : plugin.getMongoDbManager().getCollection(MongoCollections.KITS).find(Filters.regex("_id", plugin.getConfig().getString("kits_load_regex", ".*"))))
        {
//            if(all || validate(d.getString("_id"), filter))
//            {
                kits.put(d.getString("_id"), new KitMain(d, plugin));
//            }
        }
    }

    public Map<String, MainKit> getKits()
    {
        return kits;
    }

//    private boolean validate(String name, List<String> filter)
//    {
//        for(String f : filter)
//        {
//            if(name.startsWith(f)) return true;
//        }
//        return false;
//    }

}
