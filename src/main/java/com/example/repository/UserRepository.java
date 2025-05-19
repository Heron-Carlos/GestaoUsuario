package com.example.repository;

import com.example.model.User;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final MongoCollection<Document> userCollection;

    public UserRepository() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("poo2db");
        userCollection = database.getCollection("users");
    }

    public void save(User user) {
        Document doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        userCollection.insertOne(doc);
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        FindIterable<Document> docs = userCollection.find();
        for (Document doc : docs) {
            User user = new User();
            user.setId(doc.getObjectId("_id"));
            user.setName(doc.getString("name"));
            user.setEmail(doc.getString("email"));
            user.setPassword(doc.getString("password"));
            users.add(user);
        }
        return users;
    }

    public void update(User user) {
        Document filter = new Document("email", user.getEmail());
        Document update = new Document("$set", new Document("name", user.getName())
                .append("password", user.getPassword()));
        userCollection.updateOne(filter, update);
    }


    public User findById(ObjectId id) {
        Document doc = userCollection.find(Filters.eq("_id", id)).first();
        if (doc != null) {
            User user = new User();
            user.setId(doc.getObjectId("_id"));
            user.setName(doc.getString("name"));
            user.setEmail(doc.getString("email"));
            user.setPassword(doc.getString("password"));
            return user;
        }
        return null;
    }

    public void delete(String id) {
        userCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

}