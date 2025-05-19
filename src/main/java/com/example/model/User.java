package com.example.model;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class User {
    private ObjectId id;
    private String name;
    private String email;
    private String password;
}
