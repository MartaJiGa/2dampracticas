package com.svalero.JavaFX.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svalero.JavaFX.models.Book;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApiService {
    private static final String API_URL = "http://localhost:8080/bookapi";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static CompletableFuture<List<Book>> fetchBooks() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> new Gson().fromJson(json, new TypeToken<List<Book>>() {}.getType()));
    }
}
