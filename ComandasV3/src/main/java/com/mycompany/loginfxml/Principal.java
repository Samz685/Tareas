package com.mycompany.loginfxml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

public class Principal implements Initializable{

    private TextField txtUser;
    private PasswordField txtPassword;
    @FXML
    private Label info;
    @FXML
    private Button btnCarta;
    @FXML
    private Button btnComandas;
    @FXML
    private Button btnSalir;

  

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void verCarta(ActionEvent event) {
        try {
            App.setRoot("carta");
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void verComandas(ActionEvent event) {
        try {
            App.setRoot("pedidos");
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Salir(ActionEvent event) {
        
    System.exit(0);
    }

    
}