package com.example.view;

import com.example.model.User;
import com.example.repository.UserRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Interface gráfica (view) para gestão de usuários.
 * Permite criar, editar, buscar, listar e deletar usuários utilizando
 * uma tabela JTable para exibição dos dados e campos de formulário para entrada.
 * Integra com o repositório UserRepository para persistência dos dados.
 */
public class UserView extends JFrame {
    private final UserRepository userRepository;
    private JTable table;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JTextField txtBuscar;
    private JComboBox<String> cmbFiltro;



    private User selectedUser = null; // Usuário selecionado para edição

    /**
     * Construtor da view, inicializa componentes e carrega tabela com dados.
     */
    public UserView() {
        userRepository = new UserRepository();
        initComponents();
        carregarTabela();
    }

    /**
     * Inicializa todos os componentes gráficos da interface,
     * configura layout, botões, campos e eventos.
     */
    private void initComponents() {
        setTitle("Gestão de Usuários");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout(10, 10));


        // Painel do formulário de cadastro/edição
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Campo Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(20);
        formPanel.add(txtNome, gbc);

        // Campo Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);

        // Campo Senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        txtSenha = new JPasswordField(20);
        formPanel.add(txtSenha, gbc);

        // Botões Salvar e Cancelar
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // centraliza botão
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        formPanel.add(buttonPanel, gbc);

        // Painel de busca por nome
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Buscar por:"));

        cmbFiltro = new JComboBox<>(new String[]{"Nome", "ID", "Email"});
        searchPanel.add(cmbFiltro);

        txtBuscar = new JTextField(20);
        searchPanel.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar Todos");

        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnMostrarTodos);

        // Agrupa formulário e busca no topo da tela
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formWrapper.add(formPanel);
        topPanel.add(formWrapper);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(searchPanel);

        panel.add(topPanel, BorderLayout.NORTH);

        // Tabela para exibição dos usuários
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Painel e botão para deletar usuário selecionado
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDeletar = new JButton("Deletar Selecionado");
        deletePanel.add(btnDeletar);
        panel.add(deletePanel, BorderLayout.SOUTH);

        add(panel);

        // Eventos dos botões e seleção da tabela

        // Salvar novo usuário ou atualizar existente
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String senha = new String(txtSenha.getPassword());

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedUser == null) {
                User user = new User();
                user.setName(nome);
                user.setEmail(email);
                user.setPassword(senha);

                userRepository.save(user);
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                selectedUser.setName(nome);
                selectedUser.setEmail(email);
                selectedUser.setPassword(senha);

                userRepository.update(selectedUser);
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }

            carregarTabela();
            limparCampos();
        });

        // Cancelar edição e limpar campos
        btnCancelar.addActionListener(e -> limparCampos());

        // Buscar usuários por nome
        btnBuscar.addActionListener(e -> {
            String termo = txtBuscar.getText().trim();
            String filtro = cmbFiltro.getSelectedItem().toString();

            if (termo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um termo para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            switch (filtro) {
                case "Nome":
                    carregarTabelaPorNome(termo);
                    break;
                case "ID":
                    carregarTabelaPorId(termo);
                    break;
                case "Email":
                    carregarTabelaPorEmail(termo);
                    break;
            }
        });


        // Mostrar todos os usuários
        btnMostrarTodos.addActionListener(e -> {
            txtBuscar.setText("");
            carregarTabela();
        });

        // Ao selecionar linha na tabela, carregar dados nos campos para edição
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                String id = table.getValueAt(row, 0).toString();
                selectedUser = userRepository.findById(new org.bson.types.ObjectId(id));

                if (selectedUser != null) {
                    txtNome.setText(selectedUser.getName());
                    txtEmail.setText(selectedUser.getEmail());
                    txtSenha.setText(selectedUser.getPassword());
                }
            }
        });

        // Deletar usuário selecionado na tabela
        btnDeletar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String id = table.getValueAt(selectedRow, 0).toString();
                userRepository.delete(id);
                JOptionPane.showMessageDialog(this, "Usuário deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                carregarTabela();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um usuário para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
    }

    /**
     * Carrega a tabela com todos os usuários cadastrados.
     */
    private void carregarTabela() {
        List<User> users = userRepository.findAll();
        preencherTabela(users);
    }

    /**
     * Carrega a tabela filtrando usuários pelo nome informado.
     *
     * @param nome Nome para busca.
     */
    private void carregarTabelaPorNome(String nome) {
        List<User> users = userRepository.findByName(nome);
        preencherTabela(users);
    }

    /**
     * Preenche a tabela JTable com a lista de usuários recebida.
     *
     * @param users Lista de usuários a exibir na tabela.
     */
    private void preencherTabela(List<User> users) {
        String[] colunas = {"ID", "Nome", "Email", "Senha"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        for (User user : users) {
            Object[] row = {
                    user.getId().toString(),
                    user.getName(),
                    user.getEmail(),
                    user.getPassword()
            };
            model.addRow(row);
        }

        table.setModel(model);
    }

    /**
     * Limpa os campos do formulário e reseta a seleção de usuário.
     */
    private void limparCampos() {
        txtNome.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
        selectedUser = null;
        table.clearSelection();
    }

    private void carregarTabelaPorId(String id) {
        User user = userRepository.findById(new org.bson.types.ObjectId(id));
        if (user != null) {
            preencherTabela(List.of(user));
        } else {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void carregarTabelaPorEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            preencherTabela(List.of(user));
        } else {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
