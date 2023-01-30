package com.mycompany.loginfxml;

import dao.PedidoDAO;
import dao.ProductoDAO;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import models.Pedido;
import models.Producto;

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
    private ComboBox<Producto> comboProducto;
    private Pedido pedidoActual = null;
    static Carta carta;
    @FXML
    private Button btnHistorial;
    @FXML
    private Button btnEstadistica;
    @FXML
    private Button btnCarta;
    private PedidoDAO pedidoDAO;
    private ProductoDAO productoDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        inicializarComponentes();
        
        pedidoDAO = new PedidoDAO();
        productoDAO = new ProductoDAO();
        
        rellenarCombos();
        verPedidosHoy();

    }

    private void inicializarComponentes() {
        cId.setCellValueFactory(new PropertyValueFactory("idPed"));
        cFecha.setCellValueFactory(new PropertyValueFactory("fecha"));
        cCliente.setCellValueFactory(new PropertyValueFactory("cliente"));
        cProducto.setCellValueFactory((var fila) -> {
            Pedido p = fila.getValue();
            return new ReadOnlyObjectWrapper(p.getProducto());
        });
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
            comboProducto.setValue(pedido.getProducto());
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
        Producto producto = comboProducto.getValue();
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

            pedidoDAO.add(p);
            verPedidosHoy();
            borrarFormulario();
            detalle.setText("Pedido nuevo añadido con éxito!");
            pedidoActual = p;
        }
    }

    @FXML
    private void borrarPedido(ActionEvent event) {

        if ((pedidoActual != null) && pedirConfirmacion()) {

            pedidoDAO.delete(pedidoActual);
            pedidoActual = null;
            verPedidosHoy();
            borrarFormulario();
            detalle.setText("El pedido ha sido borrado con éxito");

        }
    }

    private void borrarFormulario() {
        
 
        textFecha.setValue(LocalDate.now());
        textCliente.setText("");
        comboProducto.setValue(null);
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

            pedidoDAO.update(pedidoActual);
            verPedidosHoy();
            detalle.setText("Pedido actualizado con éxito");
            borrarFormulario();
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

        ObservableList<Producto> productos = FXCollections.observableArrayList();
        
        for (Producto p : traerProductos()) {
            productos.add(p);

        }
        comboProducto.setItems(productos);

        comboProducto.setConverter(new StringConverter<Producto>() {
            @Override
            public String toString(Producto object) {
                if (object != null) {
                    return object.getNombre();
                } else {
                    return "SIN PRODUCTO";
                }
            }

            @Override
            public Producto fromString(String param) {
                Producto producto = new Producto();

                if (productoDAO.getProducto(param) != null) {
                    for (Producto p : productoDAO.getAll()) {
                        producto = p;
                    }
                }
                return producto;

            }
        });


        ObservableList<String> items2 = FXCollections.observableArrayList();

        items2.addAll("PENDIENTE", "PREPARANDO", "LISTO", "ENTREGADO");
        comboEstado.setItems(items2);

    }

    public ArrayList<Producto> traerProductos() {

        ArrayList<Producto> productos = new ArrayList<>();

        if (productoDAO.getAll() != null) {
            for (Producto p : productoDAO.getAll()) {
                productos.add(p);
            }
        }
        return productos;
    }

    private void verPedidosHoy() {

        if (pedidoDAO.getAllToday()!= null) {
            tabla.getItems().clear();
            for (Pedido p : pedidoDAO.getAllToday()) {
                tabla.getItems().add(p);
            }
        }
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

}
