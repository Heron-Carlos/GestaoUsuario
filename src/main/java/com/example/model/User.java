package com.example.model;

import lombok.Data;
import org.bson.types.ObjectId;

/**
 * Representa o modelo de dados do usuário.
 * Contém informações básicas como nome, email e senha.
 */
@Data
public class User {
    /** Identificador único do usuário no banco de dados. */
    private ObjectId id;

    /** Nome completo do usuário. */
    private String name;

    /** Email do usuário. */
    private String email;

    /** Senha do usuário. */
    private String password;
}
