package fr.idarkay.minetasia.core.spigot.advancement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.advancement.*;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import fr.idarkay.minetasia.normes.Tuple;
import fr.idarkay.minetasia.normes.utils.BukkitUtils;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * File <b>MinetasiaAdvancement</b> located on fr.idarkay.minetasia.core.spigot.advancement
 * MinetasiaAdvancement is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 20/03/2020 at 22:19
 */
public class MinetasiaAdvancement implements MinetasiaBaseAdvancement
{

    private static String[] langToLoad;
    private static String worldFilter;
    private static File worldFile;


    /**
     *
     * get in config <b>field advancement_lang</b> <br>
     * <b>Example</b>: <br>
     *     advancement_lang: <br>
     *         - "fr" <br>
     *         - "en"
     *
     * @param fileConfiguration configuration where the are the advancement_lang field
     */
    public static void initLangToLoad(FileConfiguration fileConfiguration)
    {
        langToLoad = fileConfiguration.getStringList("advancement_lang").toArray(new String[0]);
        worldFilter = fileConfiguration.getString("world_folder_regex");
        worldFile = Bukkit.getServer().getWorldContainer();
    }



    protected NamespacedKey namespacedKey;
    private final AdvancementIcon icon;
    private final AdvancementFrame frame;
    private final Map<String, MinetasiaLangAdvancement> trad = new HashMap<>();
    private final MinetasiaLangAdvancement defaultLang;
    @NotNull private Criteria[] criteria = new Criteria[0];
    @Nullable private Tuple<Economy, Double> rewards = null;
    @Nullable private Tuple<String, Integer> statsCriteria = null;
    @Nullable private NamespacedKey parent = null;
    @Nullable private String background = null;
    private boolean isDefault = false;

    public MinetasiaAdvancement(NamespacedKey namespacedKey, AdvancementIcon icon, AdvancementFrame frame, String title, String description, String lang)
    {
        this.namespacedKey = namespacedKey;
        this.icon = icon;
        this.frame = frame;
        trad.put(lang, new MinetasiaAdvancementLang(this, lang, title, description));
        defaultLang = trad.get(lang);
    }

    public MinetasiaAdvancement(MinetasiaAdvancement advancement)
    {
        this.namespacedKey = advancement.namespacedKey;
        this.icon = advancement.icon;
        this.frame = advancement.frame;
        this.trad.putAll(advancement.trad);
        this.defaultLang = advancement.defaultLang;
    }

    protected MinetasiaAdvancement(Document document)
    {
        this.namespacedKey = BukkitUtils.namespaceKeyFromSting(document.getString("_id"));
        this.icon = AdvancementIcon.fromDocument(document.get("icon", Document.class));
        this.frame = AdvancementFrame.valueOf(document.getString("frame"));
        document.get("lang", Document.class).forEach((k, v) -> {
            trad.put(k, MinetasiaAdvancementLang.fromDocument(this, k, (Document) v));
        });
        defaultLang = trad.getOrDefault(MinetasiaLang.BASE_LANG, GeneralUtils.getFirstInMap(trad));
        final Document cr = document.get("criteria", Document.class);
        if(cr != null)
        {
            criteria = cr.entrySet().stream().map(e -> Criteria.fromDocument(e.getKey(), (Document) e.getValue())).collect(Collectors.toList()).toArray(new Criteria[0]);
        }
        final String pa = document.getString("parent");
        if(pa != null)
        {
            this.parent = BukkitUtils.namespaceKeyFromSting(pa);
        }
        final Document rew = document.get("rewards", Document.class);
        if(rew != null)
        {
            rewards = new Tuple<>(Economy.valueOf(rew.getString("type").toUpperCase()) ,Math.max(1, rew.getDouble("amount")));
        }
        background = document.getString("background");
        final Document statsC = document.get("stats_criteria", Document.class);
        if(statsC != null)
        {
            statsCriteria = new Tuple<>(statsC.getString("type") ,Math.max(1, statsC.getInteger("amount")));
        }
        this.isDefault = document.getBoolean("is_default", false);
    }

    public void setCriteria(@NotNull Criteria[] criteria)
    {
        this.criteria = criteria;
    }

    public void setRewards(@NotNull Tuple<Economy, Double> rewards)
    {
        Validate.isTrue(rewards.b() > 0, "rewards need be positive");
        Validate.isTrue(rewards.a() != null, "economy can't be null");
        this.rewards = rewards;
    }

    @Override
    public void setParent(@Nullable NamespacedKey parent)
    {
        this.parent = parent;
    }

    @Override
    public void setBackGround(@Nullable String texturePatch)
    {
        this.background = texturePatch;
    }

    @Override
    public void setStatsCriteria(@Nullable Tuple<String, Integer> statsCriteria)
    {
        this.statsCriteria = statsCriteria;
    }

    @Override
    public void setDefault()
    {
        this.isDefault = true;
    }

    public boolean isDefault()
    {
        return isDefault;
    }

    @Nullable
    public Tuple<String, Integer> getStatsCriteria()
    {
        return statsCriteria;
    }

    @NotNull
    @Override
    public MinetasiaLangAdvancement getLang(@NotNull String lang)
    {
        return trad.getOrDefault(lang, defaultLang);
    }

    public Tuple<Economy, Double> getRewards()
    {
        return rewards;
    }

    @NotNull
    @Override
    public final JsonObject toJson(@NotNull String lang)
    {
        final JsonObject jsonObject = new JsonObject();
        final JsonObject display = new JsonObject();
        final MinetasiaLangAdvancement advancementLang = getLang(lang);
        display.add("icon", icon.toJson());
        display.addProperty("title", advancementLang.getTitle());
        display.addProperty("description", advancementLang.getDescription());
        display.addProperty("frame", frame.value);
        display.addProperty("show_toast", false); //remove popup
        if(background != null)
        {
            display.addProperty("background", background);
        }
        jsonObject.add("display", display);
        if(parent != null)
        {
            jsonObject.addProperty("parent", parent.getNamespace() + "_" + lang + ":" + parent.getKey());
        }
        if(criteria.length != 0)
        {
            final JsonObject criteria = new JsonObject();
            for (Criteria c : this.criteria)
            {
                criteria.add(c.getName(), c.toJson());
            }
            jsonObject.add("criteria", criteria);
        }
        return jsonObject;
    }

    @NotNull
    @Override
    public NamespacedKey getNamespacedKey()
    {
        return namespacedKey;
    }

    @NotNull
    @Override
    public Document toDocument()
    {
        final Document document = new Document();
        document.append("_id", namespacedKey.toString());
        document.append("icon", icon.toDocument());
        document.append("frame", frame.name());
        if(criteria.length != 0)
        {
            final Document criteria = new Document();
            for (Criteria c : this.criteria)
            {
                criteria.append(c.getName(), c.toDocument());
            }
            document.append("criteria", criteria);
        }
        final Document lang = new Document();
        trad.forEach((k, v) -> lang.append(k, v.toLangDocument()));
        document.append("lang", lang);
        if(parent != null)
        {
            document.append("parent", parent.toString());
        }
        if(rewards != null)
        {
            document.append("rewards", new Document("type", rewards.a().name()).append("amount", rewards.b()));
        }
        if(background != null)
        {
            document.append("background", background);
        }
        if(statsCriteria != null)
        {
            document.append("stats_criteria", new Document("type", statsCriteria.a()).append("amount", statsCriteria.b()));
        }
        document.append("is_default", isDefault);
        return document;
    }

    @Override
    public void withCriteria(@NotNull Criteria... criteria)
    {
        this.criteria = criteria;
    }



    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void loadAdvancement()
    {
        for (String s : langToLoad)
        {
            final MinetasiaLangAdvancement advancementLang = getLang(s);

            for (File f : worldFile.listFiles())
            {
                if(!(f.isDirectory() && f.getName().matches(worldFilter)))
                {
                    continue;
                }
                final File datapacks = new File(f, "datapacks" + File.separator + "minetasia");
                if(!datapacks.exists())
                {
                    try
                    {
                        datapacks.mkdirs();
                        final File pack = new File(datapacks, "pack.mcmeta");
                        final JsonObject mcmeta = new JsonObject();
                        final JsonObject p = new JsonObject();
                        p.addProperty("description", "Data pack form custom advancement of Minetasia");
                        p.addProperty("pack_format", 1);
                        mcmeta.add("pack", p);

                        try (FileWriter writer = new FileWriter(pack))
                        {
                            writer.write(gson.toJson(mcmeta));
                            writer.flush();
                        }

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                final File file = new File(datapacks, "data" + File.separator + advancementLang.getNamespacedKey().getNamespace() + "_" + s
                        + File.separator + "advancements"  + File.separator + advancementLang.getNamespacedKey().getKey() + ".json");


                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }

                if(!file.exists()){
                    try {
                        file.createNewFile();
                        FileWriter writer = new FileWriter(file);
                        writer.write(gson.toJson(toJson(s)));
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


//        if(Bukkit.getAdvancement(namespacedKey) == null){
//            Bukkit.reloadData();
////            Bukkit.getUnsafe().loadAdvancement(getID(), getJSON());
//        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final MinetasiaAdvancement that = (MinetasiaAdvancement) o;
        return Objects.equals(namespacedKey, that.namespacedKey);
    }
}
