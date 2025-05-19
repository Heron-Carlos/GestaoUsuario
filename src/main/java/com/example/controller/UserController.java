package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;

import java.util.List;

/**
 * Controlador responsável pela lógica de negócio relacionada ao usuário.
 * Faz a mediação entre a camada de visualização e a camada de dados.
 */
public class UserController {
    private final UserRepository userRepository;

    /**
     * Construtor que inicializa o repositório de usuários.
     */
    public UserController() {
        this.userRepository = new UserRepository();
    }

    /**
     * Cria e salva um novo usuário com os dados fornecidos.
     *
     * @param name     Nome do usuário.
     * @param email    Email do usuário.
     * @param password Senha do usuário.
     */
    public void saveUser(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
    }

    /**
     * Atualiza os dados do usuário existente.
     *
     * @param user     Usuário a ser atualizado.
     * @param name     Novo nome do usuário.
     * @param email    Novo email do usuário.
     * @param password Nova senha do usuário.
     */
    public void updateUser(User user, String name, String email, String password) {
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.update(user);
    }

    /**
     * Remove um usuário do banco de dados pelo seu ID.
     *
     * @param id ID do usuário a ser deletado.
     */
    public void deleteUser(String id) {
        userRepository.delete(id);
    }

    /**
     * Retorna a lista de todos os usuários cadastrados.
     *
     * @return Lista de usuários.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id ID do usuário.
     * @return Usuário encontrado ou null se não existir.
     */
    public User findById(String id) {
        return userRepository.findById(new org.bson.types.ObjectId(id));
    }
}
