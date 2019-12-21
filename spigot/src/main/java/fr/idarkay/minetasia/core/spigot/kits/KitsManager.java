package fr.idarkay.minetasia.core.spigot.kits;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;

import java.util.*;

/**
 * File <b>KitsManager</b> located on fr.idarkay.minetasia.core.spigot.kits
 * KitsManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/12/2019 at 17:18
 */
public class KitsManager {

    private Map<String, fr.idarkay.minetasia.core.api.utils.Kit> kits = new HashMap<>();
    private List<String> kitsName = new ArrayList<>();

    public KitsManager(MinetasiaCore plugin)
    {
        final List<String> filter = plugin.getConfig().getStringList("kits_load");


        Collection<String> fields = plugin.getFields("kits");
        if(filter.size() > 0 && !filter.get(0).equals("*"))
        {
            fields = filterList(fields, filter);
        }

        for(String f : fields)
        {
            Kit k = new Kit(MinetasiaCore.JSON_PARSER.parse(plugin.getValue("kits", f)).getAsJsonObject());
            kits.put(k.getName() + "_" + k.getIsoLang(), k);
            if(!kitsName.contains(k.getName())) kitsName.add(k.getName());
        }
    }

    public Map<String, fr.idarkay.minetasia.core.api.utils.Kit> getKits() {
        return kits;
    }

    private Collection<String> filterList(Collection<String> list, Collection<String> filter)
    {
        Collection<String> back = new ArrayList<>();
        for(String a : list)
        {
            for(String b : filter)
            {
                if(a.startsWith(b))
                {
                    back.add(a);
                    break;
                }
            }
        }
        return back;
    }

}
