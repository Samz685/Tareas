package com.mycompany.loginfxml;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import models.Pedido;
import models.Producto;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class Historial implements Initializable {

    @FXML
    private TableView<Pedido> tabla;
    @FXML
    private TableColumn<Pedido, Integer> cId;
    @FXML
    private TableColumn<Pedido, Date> cFecha;
    @FXML
    private TableColumn<Pedido, String> cCliente;
    @FXML
    private TableColumn<Pedido, String> cProducto;
    @FXML
    private TableColumn<Pedido, String> cEstado;

    @FXML
    private MenuItem menuSalir;
    private Button btnActualizar;
    @FXML
    private Button btnBorrar;
    @FXML
    private Label detalle;
    @FXML
    private Button btnSalir;
    private TextField textFecha;
    private TextField textCliente;
    private ComboBox<String> comboEstado;
    private ComboBox<String> comboProducto;
    private Pedido pedidoActual = null;
    static Carta carta;
    @FXML
    private Button btnHoy;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cId.setCellValueFactory(new PropertyValueFactory("idPed"));
        cFecha.setCellValueFactory(new PropertyValueFactory("fecha"));
        cCliente.setCellValueFactory(new PropertyValueFactory("cliente"));
        cProducto.setCellValueFactory(new PropertyValueFactory("producto"));
        cEstado.setCellValueFactory(new PropertyValueFactory("estado"));

        
        verHistorial();

    }

    @FXML
    private void mostrarPedidos(MouseEvent event) {

        Pedido pedido = tabla.getSelectionModel().getSelectedItem();

        System.out.println(tabla.getSelectionModel().getSelectedIndex());

        if (pedido != null) {

            pedidoActual = pedido;

            System.out.println(pedidoActual);

        }
    }

    private void verHistorial() {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Conexión realizada con éxito");

            Query q = s.createQuery("from Pedido");
            pedidos = (ArrayList<Pedido>) q.list();
        }
        tabla.getItems().clear();
        tabla.getItems().addAll(pedidos);

    }

    @FXML
    private void Salir(ActionEvent event) {
        try {
            App.setRoot("principal");
        } catch (IOException ex) {
            Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void abrirCarta(ActionEvent event) {

        try {
            App.setRoot("carta");
        } catch (IOException ex) {
            Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void irHoy(ActionEvent event) {

        try {
            App.setRoot("pedidos");
        } catch (IOException ex) {
            Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void borrarPedido(ActionEvent event) {

        if ((pedidoActual != null) && pedirConfirmacion()) {

            try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
                Transaction t = s.beginTransaction();
                s.delete(pedidoActual);
                t.commit();

                pedidoActual = null;

                verHistorial();

                detalle.setText("El pedido ha sido borrado con éxito");
            }

        }
    }

    private Boolean pedirConfirmacion() {
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Borrar");
        confirmacion.setHeaderText("Solicitud de borrado");
        confirmacion.setContentText("El pedido de " + pedidoActual.getProducto() + " con ID " + pedidoActual.getIdPed() + " va a ser borrado.");
        var result = confirmacion.showAndWait();
        return (result.get()) == ButtonType.OK;
    }

}
