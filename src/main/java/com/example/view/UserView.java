package com.example.view;

import com.example.controller.UserController;
import com.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Tela principal da aplicação que permite cadastro, edição e remoção
 * de usuários através de uma interface Swing. Esta view interage com a
 * camada de controle (UserController) para realizar operações.
 */
public class UserView extends JFrame {
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPanel userListPanel = new JPanel();
    private final UserController userController = new UserController();
    private User editingUser = null;

    /**
     * Construtor que configura a interface gráfica, monta os componentes
     * e carrega a lista de usuários via controller.
     */
    public UserView() {
        setTitle("Cadastro de Usuários");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = createFormPanel();
        JScrollPane listScrollPane = new JScrollPane(userListPanel);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Usuários Cadastrados"));
        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(formPanel, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);

        loadUsers();
        setVisible(true);
    }

    /**
     * Cria o painel de formulário com campos para nome, email, senha
     * e botão para salvar.
     *
     * @return JPanel com o formulário.
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Informações do Usuário"));
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        nameField.setFont(font);
        emailField.setFont(font);
        passwordField.setFont(font);

        JLabel nameLabel = new JLabel("Nome:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Senha:");
        nameLabel.setFont(font);
        emailLabel.setFont(font);
        passwordLabel.setFont(font);

        JButton saveButton = new JButton("Salvar");
        saveButton.setFont(font);
        saveButton.addActionListener(e -> saveUser());

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(nameLabel)
                        .addComponent(emailLabel)
                        .addComponent(passwordLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameField)
                        .addComponent(emailField)
                        .addComponent(passwordField)
                        .addComponent(saveButton, GroupLayout.Alignment.TRAILING))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(emailLabel)
                        .addComponent(emailField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordLabel)
                        .addComponent(passwordField))
                .addComponent(saveButton)
        );

        return panel;
    }

    /**
     * Carrega todos os usuários através do controller e exibe na interface.
     */
    private void loadUsers() {
        userListPanel.removeAll();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        List<User> users = userController.getAllUsers();

        for (User user : users) {
            JPanel userCard = new JPanel(new BorderLayout(10, 10));
            userCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            userCard.setBackground(Color.WHITE);
            userCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            JLabel userText = new JLabel("<html><b>Nome:</b> " + user.getName() +
                    "<br><b>Email:</b> " + user.getEmail() +
                    "<br><b>Senha:</b> " + user.getPassword() + "</html>");
            userText.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setOpaque(false);

            JButton editButton = new JButton("Editar");
            JButton deleteButton = new JButton("Deletar");

            editButton.addActionListener(e -> fillFormForEdit(user));
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente deletar este usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    userController.deleteUser(user.getId().toString());
                    loadUsers();
                }
            });

            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            userCard.add(userText, BorderLayout.CENTER);
            userCard.add(buttonPanel, BorderLayout.EAST);
            userCard.setAlignmentX(Component.LEFT_ALIGNMENT);

            userListPanel.add(userCard);
            userListPanel.add(Box.createVerticalStrut(10));
        }

        userListPanel.revalidate();
        userListPanel.repaint();
    }

    /**
     * Preenche o formulário com os dados do usuário selecionado para edição.
     *
     * @param user Usuário a ser editado.
     */
    private void fillFormForEdit(User user) {
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        editingUser = user;
    }

    /**
     * Salva um novo usuário ou atualiza um existente,
     * dependendo se está editando ou não.
     * Valida campos antes de salvar.
     */
    private void saveUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        if (editingUser != null) {
            userController.updateUser(editingUser, name, email, password);
            editingUser = null;
        } else {
            userController.saveUser(name, email, password);
        }

        clearFields();
        loadUsers();
    }

    /**
     * Limpa os campos do formulário.
     */
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }
}
