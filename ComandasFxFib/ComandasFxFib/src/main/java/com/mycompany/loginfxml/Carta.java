package com.mycompany.loginfxml;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.input.MouseEvent;
import models.Producto;
import net.sf.jasperreports.engine.JRException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class Carta implements Initializable {

    @FXML
    private TableView<Producto> tabla;
    @FXML
    private TableColumn<Producto, Integer> cId;
    @FXML
    private TableColumn<Producto, String> cNombre;
    @FXML
    private TableColumn<Producto, String> cTipo;
    @FXML
    private TableColumn<Producto, Integer> cPrecio;
    @FXML
    private TableColumn<Producto, String> cDisponibilidad;

    @FXML
    private MenuItem menuSalir;
    
    @FXML
    private TextField textNombre;
    @FXML
    private TextField textTipo;
    @FXML
    private TextField textPrecio;
    @FXML
    private ComboBox<String> comboDisponibilidad;
    @FXML
    private Button btnAñadir;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnSalir;
    @FXML
    private Label detalle;
    private Producto productoActual = null;
    @FXML
    private Button btnPedidos;
    @FXML
    private Button btnEstadistica;
    @FXML
    private Button btnInforme;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll("DISPONIBLE", "AGOTADO");
        

        cId.setCellValueFactory(new PropertyValueFactory("idPro"));
        cNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        cTipo.setCellValueFactory(new PropertyValueFactory("tipo"));
        cPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
        cDisponibilidad.setCellValueFactory(new PropertyValueFactory("disponibilidad"));
        comboDisponibilidad.setItems(items);

        btnActualizar.setDisable(true);
        btnBorrar.setDisable(true);
        

        actualizarTabla();

    }

    @FXML
    private void mostrarCarta(MouseEvent event) {

        Producto producto = tabla.getSelectionModel().getSelectedItem();

        System.out.println(tabla.getSelectionModel().getSelectedIndex());

        if (producto != null) {
            
            textNombre.setText(producto.getNombre());
            textTipo.setText(producto.getTipo());
            textPrecio.setText(String.valueOf(producto.getPrecio()));
            comboDisponibilidad.setValue(producto.getDisponibilidad());

            productoActual = producto;

            btnActualizar.setDisable(false);
            btnBorrar.setDisable(false);

            System.out.println(productoActual);

        }
    }

    private void actualizarTabla() {
        ArrayList<Producto> productos = new ArrayList<>();
        try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Conexión realizada con éxito");

            Query q = s.createQuery("from Producto");
            productos = (ArrayList<Producto>) q.list();
        }
        tabla.getItems().clear();
        tabla.getItems().addAll(productos);

    }

    private Producto leerFormulario() {
        String nombre = textNombre.getText();
        String tipo = textTipo.getText();
        int precio = Integer.parseInt(textPrecio.getText());
        String disponibilidad = comboDisponibilidad.getValue();

        if ("".equals(nombre) || "".equals(tipo) || "".equals(precio) || "".equals(disponibilidad)) {
            detalle.setText("No puedes añadir un producto vacío");
            return null;
        } else {
            Producto p = new Producto();
            p.setNombre(nombre);
            p.setTipo(tipo);
            p.setPrecio(precio);
            p.setDisponibilidad(disponibilidad);
            return p;
        }
    }

    
    @FXML
    private void añadirProducto(ActionEvent event) {
  
        Producto p = leerFormulario();

        if (p != null) {
            try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
                Transaction t = s.beginTransaction();
                s.save(p);
                t.commit();

                actualizarTabla();
                detalle.setText("Producto nuevo añadido con éxito!");

                productoActual = p;

                borrarFormulario();
            }
        }
    }

    @FXML
    private void borrarProducto(ActionEvent event) {

        if ((productoActual != null) && pedirConfirmacion()) {

            try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
                Transaction t = s.beginTransaction();
                s.delete(productoActual);
                t.commit();

                productoActual = null;

                actualizarTabla();

                borrarFormulario();
                detalle.setText("El producto ha sido borrado con éxito");
            }

        }
    }

    private void borrarFormulario() {
        
        textNombre.setText("");
        textTipo.setText("");
        textPrecio.setText("");
        comboDisponibilidad.setValue("");
        btnActualizar.setDisable(true);
    }

    private Boolean pedirConfirmacion() {
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Borrar");
        confirmacion.setHeaderText("Solicitud de borrado");
        confirmacion.setContentText("El producto " + productoActual.getNombre() + " va a ser borrado.");
        var result = confirmacion.showAndWait();
        return (result.get()) == ButtonType.OK;
    }

    @FXML
    private void actualizarProducto (ActionEvent event) {

        if (productoActual != null) {

            productoActual.setNombre(textNombre.getText());
            productoActual.setTipo(textTipo.getText());
            productoActual.setPrecio(Integer.parseInt(textPrecio.getText()));
            productoActual.setDisponibilidad(comboDisponibilidad.getValue());

            try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
                Transaction t = s.beginTransaction();
                s.update(productoActual);
                t.commit();

                actualizarTabla();
                detalle.setText("Producto actualizado con éxito");

            }
        }
    }



    @FXML
    private void abrirPedidos(ActionEvent event) {
        
        try {
            App.setRoot("pedidos");
        } catch (IOException ex) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Salir(ActionEvent event) {
        try {
            App.setRoot("principal");
        } catch (IOException ex) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Producto> traerProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query q = s.createQuery("from Producto");
            productos = (ArrayList<Producto>) q.list();
        }
       
        return productos;

    }

    @FXML
    private void Cerrar(ActionEvent event) {
    }

    @FXML
    private void irPedidos(ActionEvent event) {
        
        try {
            App.setRoot("pedidos");
        } catch (IOException ex) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void irEstadistica(ActionEvent event) {
        
        try {
            App.setRoot("estadistica");
        } catch (IOException ex) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void verInforme(ActionEvent event) throws SQLException {
        
        try {
            Informe.showReportCarta();
        } catch (JRException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Informe.pdfReportCarta();
        } catch (JRException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Carta
                    .class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

}
