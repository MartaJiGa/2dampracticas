package com.svalero.JavaFX.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svalero.JavaFX.models.Book;
import io.reactivex.rxjava3.core.Observable;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApiService {
    private static final String API_URL = "http://localhost:8080/books";
    private static final HttpClient client = HttpClient.newHttpClient();

    public Observable<List<Book>> fetchBooks(){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        CompletableFuture<String> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

        return Observable.fromFuture(future)
                .map(json -> new Gson().fromJson(json, new TypeToken<List<Book>>() {}.getType()));
    }
}
