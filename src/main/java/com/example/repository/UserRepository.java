package com.example.repository;

import com.example.model.User;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por acessar a coleção "users" no MongoDB
 * e executar operações CRUD relacionadas ao usuário.
 */
public class UserRepository {
    private final MongoCollection<Document> userCollection;

    /**
     * Construtor que cria conexão com MongoDB e inicializa a coleção "users".
     */
    public UserRepository() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("poo2db");
        userCollection = database.getCollection("users");
    }

    /**
     * Salva um novo usuário na coleção.
     *
     * @param user Usuário a ser salvo.
     */
    public void save(User user) {
        Document doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        userCollection.insertOne(doc);
    }

    /**
     * Busca e retorna todos os usuários cadastrados.
     *
     * @return Lista de usuários encontrados.
     */
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

    /**
     * Atualiza os dados de um usuário existente na coleção,
     * identificando pelo email.
     *
     * @param user Usuário com dados atualizados.
     */
    public void update(User user) {
        Document filter = new Document("email", user.getEmail());
        Document update = new Document("$set", new Document("name", user.getName())
                .append("password", user.getPassword()));
        userCollection.updateOne(filter, update);
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id ID do usuário.
     * @return Usuário encontrado ou null se não existir.
     */
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

    /**
     * Deleta um usuário da coleção pelo seu ID.
     *
     * @param id ID do usuário a ser deletado (em String).
     */
    public void delete(String id) {
        userCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }
}
