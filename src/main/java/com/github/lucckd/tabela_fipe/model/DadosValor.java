package com.github.lucckd.tabela_fipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosValor(String Valor, String Marca, String Modelo, String AnoModelo, String MesReferencia) {
}
