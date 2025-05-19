package com.example;

import com.example.view.UserView;
import com.formdev.flatlaf.FlatIntelliJLaf;

public class Main {
    public static void main(String[] args) {
        try {
            FlatIntelliJLaf.setup(); // FlatDarkLaf() se quiser tema escuro
        } catch (Exception e) {
            System.err.println("Erro ao aplicar tema moderno: " + e);
        }

        new UserView();
    }
}
