package com.mycompany.loginfxml;

import models.ProductoData;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

    private static final String VENTAS_MES = "SELECT pr.nombre as nProducto, sum(pr.precio) as suma FROM producto pr\n"
            + "INNER JOIN pedido p\n"
            + "WHERE pr.nombre = p.producto\n"
            + "and pr.nombre in (SELECT producto FROM pedido)\n"
            + "and p.fecha > current_date - interval 1 month\n"
            + "group by pr.nombre";
    private static final String VENTAS_SEMANA = "SELECT pr.nombre as nProducto, sum(pr.precio) as suma FROM producto pr\n"
            + "INNER JOIN pedido p\n"
            + "WHERE pr.nombre = p.producto\n"
            + "and pr.nombre in (SELECT producto FROM pedido)\n"
            + "and p.fecha > current_date - interval 1 week\n"
            + "group by pr.nombre";
    private static final String VENTAS_HOY = "SELECT pr.nombre as nProducto, sum(pr.precio) as suma FROM producto pr\n"
            + "INNER JOIN pedido p\n"
            + "WHERE pr.nombre = p.producto\n"
            + "and pr.nombre in (SELECT producto FROM pedido)\n"
            + "and p.fecha = current_date\n"
            + "group by pr.nombre";

    private String paramDinamico = VENTAS_SEMANA;

    private ComboBox<String> comboTramo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        crearChart();

    }

    private void crearChart() {

        XYChart.Series serie1 = new XYChart.Series<>();
        serie1.setName("Mes");

        for (ProductoData p : traerVentas(VENTAS_MES)) {
            String nombre = p.getNombre();
            int venta = p.getVenta();

            serie1.getData().add(new XYChart.Data(nombre, venta));
        }
        XYChart.Series serie2 = new XYChart.Series<>();
        serie2.setName("Semana");

        for (ProductoData p : traerVentas(VENTAS_SEMANA)) {
            String nombre = p.getNombre();
            int venta = p.getVenta();

            serie2.getData().add(new XYChart.Data(nombre, venta));
        }
        XYChart.Series serie3 = new XYChart.Series<>();
        serie3.setName("Hoy");

        for (ProductoData p : traerVentas(VENTAS_HOY)) {
            String nombre = p.getNombre();
            int venta = p.getVenta();

            serie3.getData().add(new XYChart.Data(nombre, venta));
        }

        chartA.getData().addAll(serie1, serie2, serie3);

    }

    public ArrayList<ProductoData> traerVentas(String paramDinamico) {

        ArrayList<ProductoData> listaVentas = new ArrayList<>();

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(paramDinamico);
            List<Object[]> rows = query.list();

            for (Object[] row : rows) {
                ProductoData pV = new ProductoData();
                pV.setNombre(row[0].toString());
                pV.setVenta(Integer.parseInt(row[1].toString()));
                listaVentas.add(pV);
            }
        } catch (Exception e) {
        }
        return listaVentas;
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
