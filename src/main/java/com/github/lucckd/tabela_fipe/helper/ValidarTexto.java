package com.github.lucckd.tabela_fipe.helper;

public class ValidarTexto {
    public static String validar(String entrada) throws Exception {
        if(!entrada.matches("[a-zA-ZÀ-ÿ\\s]+")) {
            throw new Exception("Texto inválido. Digite apenas letras.");
        }
        return entrada;
    }
}
