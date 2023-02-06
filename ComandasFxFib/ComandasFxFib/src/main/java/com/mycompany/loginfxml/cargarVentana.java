package com.mycompany.loginfxml;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class cargarVentana {

    public void cargarVentanaModal(String ventana, int ancho, int alto) {
        try {

            // Cargo la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ventana + ".fxml"));

            // Cargo la ventana
            Parent root = loader.load();

            // Creo el Scene
            Scene scene = new Scene(root, ancho, alto);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
