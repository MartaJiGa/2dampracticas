package com.svalero.JavaFX.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void showBookList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/svalero/JavaFX/view.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Lista de Libros");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
