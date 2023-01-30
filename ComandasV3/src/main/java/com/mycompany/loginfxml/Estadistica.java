package com.mycompany.loginfxml;

import dao.ProductoDataDAO;
import models.ProductoData;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;


public class Estadistica implements Initializable {

    @FXML
    private MenuItem menuSalir;

    @FXML
    private Label detalle;
    @FXML
    private Button btnSalir;

    @FXML
    private Button btnHoy;

    @FXML
    private BarChart<String, String> chartA;

    private ProductoDataDAO productoDataDAO = new ProductoDataDAO();

    private ComboBox<String> comboTramo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        crearChart();

    }

    private void crearChart() {

        XYChart.Series serie1 = new XYChart.Series<>();
        serie1.setName("Mes");

        for (ProductoData p : productoDataDAO.traerVentasMes()) {
            String nombre = p.getNombre();
            int venta = p.getVenta();

            serie1.getData().add(new XYChart.Data(nombre, venta));
        }
        XYChart.Series serie2 = new XYChart.Series<>();
        serie2.setName("Semana");

        for (ProductoData p : productoDataDAO.traerVentasSemana()) {
            String nombre = p.getNombre();
            int venta = p.getVenta();

            serie2.getData().add(new XYChart.Data(nombre, venta));
        }
        XYChart.Series serie3 = new XYChart.Series<>();
        serie3.setName("Hoy");

        for (ProductoData p : productoDataDAO.traerVentasHoy()) {
            String nombre = p.getNombre();
            int venta = p.getVenta();

            serie3.getData().add(new XYChart.Data(nombre, venta));
        }

        chartA.getData().addAll(serie1, serie2, serie3);

    }

    

    @FXML
    private void Salir(ActionEvent event) {
        try {
            App.setRoot("principal");
        } catch (IOException ex) {
            Logger.getLogger(Estadistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void abrirCarta(ActionEvent event) {

        try {
            App.setRoot("carta");
        } catch (IOException ex) {
            Logger.getLogger(Estadistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void irHoy(ActionEvent event) {

        try {
            App.setRoot("pedidos");
        } catch (IOException ex) {
            Logger.getLogger(Estadistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
