package fr.idarkay.minetasia.mongoinit;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.*;
/**
 * File <b>Main</b> located on fr.idarkay.minetasia.mongoinit
 * Main is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 09/02/2020 at 17:21
 */
public class Main
{
    public static void main(String[] args)
    {

        System.out.println("Arguments : ");
        System.out.println("host:<host>");
        System.out.println("user:<user>");
        System.out.println("pass:<pass>");
        System.out.println("dbname:<dbname>");
        System.out.println("port:<port>");
        System.out.println("create:<collection_name_1>,<collection_name_2>,<collection_name_3>...");

        final String host = getOrDefault(args, "host", "mongodb://localhost:27017");
        final String user = getOrDefault(args, "user", "initializer");
        final String pass = getOrDefault(args, "pass", "initializerpass");
        final String dbname = getOrDefault(args, "dbname", "minetasia");
        final int port = Integer.parseInt(getOrDefault(args, "port", "27017"));
        final String collectionToCreate = getOrDefault(args, "create", null);
//        if(collectionToCreate == null) throw new IllegalArgumentException("no create args specified");
//        final String[] collectionToCreateList = collectionToCreate.split(",");

        final MongoCredential credential = MongoCredential.createCredential(user, dbname, pass.toCharArray());
        final ConnectionString connectionString = new ConnectionString(host);
        final MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToSslSettings(builder -> builder.enabled(true))
                .applyConnectionString(connectionString)
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(host, port))))
                .build();

        final MongoClient mongoClient = MongoClients.create(host);

        final MongoDatabase database = mongoClient.getDatabase(dbname);

//        for(String s : collectionToCreateList)
//        {
//            database.createCollection(s);
//        }

        final MongoCollection<Document> online_player = database.getCollection("online_users");

        List<String> serverId = Arrays.asList("hub#04769135-3238-455e-a3a2-8512dbe0e7bf", "hub#3ff661d2-d9f6-42d4-93ee-2228d52e53ae", "hub#3df49c070-2d98-4f25-a1e2-842c5124518e");

        final Long time2 = System.currentTimeMillis();

        Map<UUID, Document> onlinePlayer = new HashMap<>();

        for(int i = 0 ; i < 300_000; i++)
        {
            UUID uuid = UUID.randomUUID();
            onlinePlayer.put(uuid, new Document("_id", uuid.toString()).append("servers_id", serverId.get((int) (Math.random() * serverId.size()))));
        }

        System.out.println("Total time pus : " + (System.currentTimeMillis() - time2));

        online_player.insertMany(new ArrayList<>(onlinePlayer.values()));

        for(int i = 0; i < 15; i++)
        {
            final Long time = System.currentTimeMillis();

            System.out.println(online_player.countDocuments(eq("servers_id", "hub#3ff661d2-d9f6-42d4-93ee-2228d52e53ae")));

            System.out.println("Total time (" + i + ") : " + (System.currentTimeMillis() - time));
        }



//        final MongoCollection<Document> server = database.getCollection("servers");
//
//        for (String s : serverId)
//        {
//            Document docs = new Document();
//            docs.append("_id", s ).append("ip", "127.0.0.1").append("port", "25565");
//            server.insertOne(docs);
//        }



//        final MongoCollection<Document> collection = database.getCollection("users");
//
//        collection.deleteOne(eq("_id", "3ff661d2-d9f6-42d4-93ee-2228d52e53ae"));
//
//        Document doc = Document.parse("{\"_id\":\"3ff661d2-d9f6-42d4-93ee-2228d52e53ae\",\"lang\":\"fr\",\"username\":\"IDarKay\",\"money\":{},\"servers_id\":\"hub#04769135-3238-455e-a3a2-8512dbe0e7bf\"}");
//        collection.insertOne(doc);
//
//
//        for (Document document :
//                collection.aggregate(Collections.singletonList( new Document("$lookup", Document.parse("{from: \"servers\",localField: \"servers_id\",foreignField: \"_id\",as: \"servers\"}") )))
//        )
//        {
//            System.out.println("FIND      FIND " + document.toJson());
//        }

        mongoClient.close();

    }


    private static String getOrDefault(String[] args, String key, String def)
    {
        for(String s : args)
        {
            if(s.startsWith(key)) return s.split(":", 2)[1];
        }
        return def;
    }

}
