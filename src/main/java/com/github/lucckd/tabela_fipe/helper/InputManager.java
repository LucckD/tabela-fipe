package com.github.lucckd.tabela_fipe.helper;

import java.util.Scanner;

public class InputManager {
    private static final Scanner input = new Scanner(System.in);

    public static Integer inputInt() {
        while(true) {
            try {
                return input.nextInt();
            } catch (Exception e) {
                System.out.println("\nErro: Entrada inválida. Digite apenas números.");
                clearBuffer();
            }
        }
    }

    public static String text() {
        try {
            String entrada = input.next();
            clearBuffer();
            String textoValidado = ValidarTexto.validar(entrada);
            return textoValidado;
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return text();
        }
    }

    public static void clearBuffer() {
        input.nextLine();
    }
}
