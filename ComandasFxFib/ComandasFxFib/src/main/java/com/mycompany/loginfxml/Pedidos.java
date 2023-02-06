package com.mycompany.loginfxml;

import models.ClienteData;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.Pedido;
import models.Producto;
import models.ProductoData;
import net.sf.jasperreports.engine.JRException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class Pedidos implements Initializable {

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
    private TextField textId;
    @FXML
    private Button btnAñadir;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnBorrar;
    @FXML
    private Label detalle;
    @FXML
    private Button btnSalir;
    @FXML
    private DatePicker textFecha;
    @FXML
    private TextField textCliente;
    @FXML
    private ComboBox<String> comboEstado;
    @FXML
    private ComboBox<String> comboProducto;
    private Pedido pedidoActual = null;
    static Carta carta;
    @FXML
    private Button btnHistorial;
    @FXML
    private Button btnEstadistica;
    @FXML
    private Button btnCarta;
    @FXML
    private Button btnReporte;
    cargarVentana cargarVentana = new cargarVentana();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        rellenarCombos();

        inicializarComponentes();

        verPedidosHoy();

    }

    private void inicializarComponentes() {
        cId.setCellValueFactory(new PropertyValueFactory("idPed"));
        cFecha.setCellValueFactory(new PropertyValueFactory("fecha"));
        cCliente.setCellValueFactory(new PropertyValueFactory("cliente"));
        cProducto.setCellValueFactory(new PropertyValueFactory("producto"));
        cEstado.setCellValueFactory(new PropertyValueFactory("estado"));

        textFecha.setValue(LocalDate.now());

        btnActualizar.setDisable(true);
        btnBorrar.setDisable(true);

    }

    @FXML
    private void mostrarPedidos(MouseEvent event) {

        Pedido pedido = tabla.getSelectionModel().getSelectedItem();

        System.out.println(tabla.getSelectionModel().getSelectedIndex());

        if (pedido != null) {

            textFecha.setValue(pedido.getFecha());
            textCliente.setText(pedido.getCliente());
            comboProducto.setValue(String.valueOf(pedido.getProducto()));
            comboEstado.setValue(pedido.getEstado());

            pedidoActual = pedido;

            btnActualizar.setDisable(false);
            btnBorrar.setDisable(false);

            System.out.println(pedidoActual);

        }
    }

    private Pedido leerFormulario() {
        LocalDate fecha = textFecha.getValue();
        String cliente = textCliente.getText();
        String producto = comboProducto.getValue();
        String estado = comboEstado.getValue();

        if ("".equals(cliente) || "".equals(producto) || "".equals(estado)) {
            detalle.setText("No puedes añadir una comanda vacía");
            return null;
        } else {
            Pedido p = new Pedido();
            p.setFecha(fecha);
            p.setCliente(cliente);
            p.setProducto(producto);
            p.setEstado(estado);
            return p;
        }
    }

    @FXML
    private void añadirPedido(ActionEvent event) {

        Pedido p = leerFormulario();

        if (p != null) {
            try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
                Transaction t = s.beginTransaction();
                s.save(p);
                t.commit();

                verPedidosHoy();
                detalle.setText("Pedido nuevo añadido con éxito!");

                pedidoActual = p;

                borrarFormulario();
            }
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

                verPedidosHoy();

                borrarFormulario();
                detalle.setText("El pedido ha sido borrado con éxito");
            }

        }
    }

    private void borrarFormulario() {

        textFecha.setValue(LocalDate.now());
        textCliente.setText("");
        comboProducto.setValue("");
        comboEstado.setValue("");
        btnActualizar.setDisable(true);
    }

    private Boolean pedirConfirmacion() {
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Borrar");
        confirmacion.setHeaderText("Solicitud de borrado");
        confirmacion.setContentText("El pedido de " + pedidoActual.getProducto() + " con ID " + pedidoActual.getIdPed() + " va a ser borrado.");
        var result = confirmacion.showAndWait();
        return (result.get()) == ButtonType.OK;
    }

    @FXML
    private void actualizarPedido(ActionEvent event) {

        if (pedidoActual != null) {

            pedidoActual.setCliente(textCliente.getText());
            pedidoActual.setProducto(comboProducto.getValue());
            pedidoActual.setEstado(comboEstado.getValue());

            try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
                Transaction t = s.beginTransaction();
                s.update(pedidoActual);
                t.commit();

                verPedidosHoy();
                detalle.setText("Pedido actualizado con éxito");

            }
        }
    }

    @FXML
    private void Salir(ActionEvent event) {
        try {
            App.setRoot("principal");
        } catch (IOException ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void abrirCarta(ActionEvent event) {

        try {
            App.setRoot("carta");
        } catch (IOException ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String fecha() {
        Date fecha0 = new Date();

        SimpleDateFormat formatofecha = new SimpleDateFormat("YYYY/MM/dd");

        return formatofecha.format(fecha0);
    }

    private void rellenarCombos() {

        ArrayList<Producto> productos = traerProductos();

        ObservableList<String> items = FXCollections.observableArrayList();

        for (var p : productos) {

            String producto = p.getNombre();

            items.add(producto);
        }
        comboProducto.setItems(items);

        ObservableList<String> items2 = FXCollections.observableArrayList();

        items2.addAll("PENDIENTE", "PREPARANDO", "LISTO", "ENTREGADO");
        comboEstado.setItems(items2);

    }

    public ArrayList<Producto> traerProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query q = s.createQuery("from Producto");
            productos = (ArrayList<Producto>) q.list();
        }

        return productos;

    }

    private void verPedidosHoy() {

        ArrayList<Pedido> pedidos = new ArrayList<>();
        try ( Session s = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Conexión realizada con éxito");

            Query q = s.createQuery("from Pedido where fecha = current_date()");
            pedidos = (ArrayList<Pedido>) q.list();
        }
        tabla.getItems().clear();
        tabla.getItems().addAll(pedidos);

    }

    @FXML
    private void irHistorial(ActionEvent event) {

        try {
            App.setRoot("historial");
        } catch (IOException ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void irEstadistica(ActionEvent event) {

        try {
            App.setRoot("Estadistica");
        } catch (IOException ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void verInforme(ActionEvent event) {

        cargarVentana.cargarVentanaModal("Dialogo_fecha_hoy", 410, 324);
        


    }

}
