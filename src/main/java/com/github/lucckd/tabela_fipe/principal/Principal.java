package com.github.lucckd.tabela_fipe.principal;

import com.github.lucckd.tabela_fipe.model.DadosCarros;
import com.github.lucckd.tabela_fipe.model.DadosModelos;
import com.github.lucckd.tabela_fipe.service.ApiManager;
import com.github.lucckd.tabela_fipe.service.ConverteDados;

import java.util.List;
import java.util.Optional;

import static com.github.lucckd.tabela_fipe.helper.InputManager.*;

public class Principal {
    private ApiManager consumo = new ApiManager();
    private ConverteDados conversor = new ConverteDados();
    private static final String url_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private String urlFinal;
    private String tipoVeiculo;
    private Integer urlMarcas;
    private String textoModelos = "/modelos/";
    private String textoMarcas = "/marcas/";

    public void menu() {
        System.out.print("""
                --- Menu --\n
                1- Carro
                2- Moto
                3- Caminhão
                
                Digite uma das opções para consultar valores:  """);
        escolhaVeiculo();

        System.out.println("\nDigite o código ou a marca que deseja consultar");
        consultaModelos();
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
            urlFinal = url_BASE + tipoVeiculo + textoMarcas;
            var json = consumo.requisicao(urlFinal);
            var marcas = conversor.obterLista(json, DadosCarros.class);
            System.out.println("\n--- Marcas disponíveis ---");
            marcas.forEach(m -> System.out.println(m.codigo() + " - " + m.nome()));
    }

    private void consultaModelos() {
        //metodo para pegar carro por escrita também?
        urlMarcas = inputInt();
        urlFinal = url_BASE + tipoVeiculo + textoMarcas + urlMarcas + textoModelos;
        var json = consumo.requisicao(urlFinal);
        DadosModelos resultado = conversor.obterDados(json, DadosModelos.class);

        if (resultado.modelos() == null) {
            System.out.println("\nNada encontrado, tente novamente.");
            consultaModelos();
        } else {
            System.out.println("\n--- Modelos disponíveis ---\n");
            resultado.modelos().forEach(m -> System.out.println(m.codigo() + " - " + m.nome()));
        }
    }

}
