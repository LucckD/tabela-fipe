package com.github.lucckd.tabela_fipe.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiManager {

    private static String url = "https://parallelum.com.br/fipe/api/v1/";
    HttpClient client = HttpClient.newHttpClient();

    public String requisicao(String endereco) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + endereco)).build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String json = response.body();
        return json;
    }

}
