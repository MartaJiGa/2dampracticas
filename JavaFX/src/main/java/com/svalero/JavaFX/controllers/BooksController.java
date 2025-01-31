package com.svalero.JavaFX.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.svalero.JavaFX.models.Book;
import com.svalero.JavaFX.services.ApiService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BooksController {
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> genreColumn;
    @FXML private TableColumn<Book, String> publicationDateColumn;
    @FXML private TableColumn<Book, Boolean> availableColumn;
    @FXML private TableColumn<Book, String> authorColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        publicationDateColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));

        loadBooks();
    }

    private void loadBooks() {
        CompletableFuture<List<Book>> futureBooks = ApiService.fetchBooks();
        futureBooks.thenAccept(books -> {
            javafx.application.Platform.runLater(() -> {
                booksTable.getItems().setAll(books);
            });
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }
}
