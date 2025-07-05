package com.github.lucckd.tabela_fipe.principal;

import com.github.lucckd.tabela_fipe.model.DadosCarros;
import com.github.lucckd.tabela_fipe.model.DadosModelos;
import com.github.lucckd.tabela_fipe.model.DadosAnos;
import com.github.lucckd.tabela_fipe.model.DadosValor;
import com.github.lucckd.tabela_fipe.service.ApiManager;
import com.github.lucckd.tabela_fipe.service.ConverteDados;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.lucckd.tabela_fipe.helper.InputManager.*;

public class Principal {
    private ApiManager consumo = new ApiManager();
    private ConverteDados conversor = new ConverteDados();
    private static final String url_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private String urlFinal;
    private String tipoVeiculo;
    private Integer urlMarcas;
    private Integer urlModelo;
    private String MODELOS = "/modelos/";
    private String MARCAS = "/marcas/";


    public void menu() {
        System.out.print("""
                --- Menu --\n
                1- Carro
                2- Moto
                3- Caminhão
                
                Digite uma das opções para consultar valores:  """);
        escolhaVeiculo();

        consultaModelos();

        consultaAnos();
    }

    public void escolhaVeiculo() {
        List<String> opcao = List.of("Caminhão", "Carro", "Moto");
        var entrada = text();

        Optional<String> resultado = opcao.stream()
                .filter(e -> e.toLowerCase().startsWith(entrada.toLowerCase()))
                .findFirst();

        if (resultado.isPresent()) {
            switch (resultado.get()) {
                case "Carro" -> {
                    System.out.println("\nVamos consultar Carros!");
                    consultaMarcas("carros");
                }
                case "Moto" -> {
                    System.out.println("\nVamos consultar Motos!");
                    consultaMarcas("motos");
                }
                case "Caminhão" -> {
                    System.out.println("\nVamos consultar Caminhões!");
                    consultaMarcas("caminhoes");
                }
            }
        } else {
            if (entrada.startsWith("car")) {
                System.out.println("\nVocê quis dizer carro.");
                consultaMarcas("carros");
            } else if (entrada.startsWith("cam")) {
                System.out.println("\nVocê quis dizer caminhão.");
                consultaMarcas("caminhoes");
            } else if (entrada.startsWith("m")) {
                System.out.println("\nVocê quis dizer moto.");
                consultaMarcas("motos");
            } else {
                System.out.println("\nNada encontrado. Tente novamente.\n");
                menu();
            }
        }

    }

    private void consultaMarcas(String veiculo) {
        tipoVeiculo = veiculo;
        urlFinal = url_BASE + tipoVeiculo + MARCAS;
        var json = consumo.requisicao(urlFinal);
        var marcas = conversor.obterLista(json, DadosCarros.class);
        System.out.println("\n--- Marcas disponíveis ---");
        marcas.forEach(m -> System.out.println(m.codigo() + " - " + m.nome()));
        System.out.println("\nDigite o código da marca que deseja consultar");
    }

    private void consultaModelos() {
        DadosModelos resultado;

        while (true) {
            urlMarcas = inputInt();
            urlFinal = url_BASE + tipoVeiculo + MARCAS + urlMarcas + MODELOS;
            var json = consumo.requisicao(urlFinal);
            resultado = conversor.obterDados(json, DadosModelos.class);

            if (resultado.modelos() != null && !resultado.modelos().isEmpty()) {
                break;
            } else {
                System.out.println("\nNada encontrado. Tente novamente.");
            }
        }

        System.out.println("\n--- Modelos disponíveis ---\n");
        resultado.modelos().forEach(m -> System.out.println(m.codigo() + " - " + m.nome()));

        while (true) {
            System.out.println("\nDigite um trecho do modelo que deseja encontrar.");
            String modeloBusca = text();

            List<DadosCarros> modelosFiltrados = resultado.modelos().stream()
                    .filter(m -> m.nome().toLowerCase().contains(modeloBusca.toLowerCase()))
                    .collect(Collectors.toList());

            if (modelosFiltrados.isEmpty()) {
                System.out.println("\nNenhum modelo encontrado com esse trecho.");
            } else {
                System.out.println("\n--- Modelos encontrados ---\n");
                modelosFiltrados.forEach(m -> System.out.println(m.codigo() + " - " + m.nome()));
                break;
            }
        }
    }

    private void consultaAnos() {
        System.out.println("\nDigite o código do modelo que deseja consultar");
        urlModelo = inputInt();
        urlFinal = url_BASE + tipoVeiculo + MARCAS + urlMarcas + MODELOS + urlModelo + "/anos/";
        var json = consumo.requisicao(urlFinal);
        var modelosAno = conversor.obterLista(json, DadosAnos.class);

        System.out.println("\n--- Valores ---\n");

        for (DadosAnos ano : modelosAno) {
            String urlDadosValores = url_BASE + tipoVeiculo + MARCAS + urlMarcas + MODELOS + urlModelo + "/anos/" + ano.codigo();
            var jsonAno = consumo.requisicao(urlDadosValores);

            var dados = conversor.obterDados(jsonAno, DadosValor.class);

            if (dados.AnoModelo().length() > 4) {
                System.out.println("Marca: " + dados.Marca());
                System.out.println("Modelo: " + dados.Modelo());
                System.out.println("Ano: Indisponível");
                System.out.println("Valor: " + dados.Valor());
                System.out.println("Mês de Referência: " + dados.MesReferencia());
                System.out.println("\n--------\n");
            } else {
                System.out.println("Marca: " + dados.Marca());
                System.out.println("Modelo: " + dados.Modelo());
                System.out.println("Ano: " + dados.AnoModelo());
                System.out.println("Valor: " + dados.Valor());
                System.out.println("Mês de Referência: " + dados.MesReferencia());
                System.out.println("\n--------\n");
            }
        }
    }

}
