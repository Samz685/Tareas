/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.loginfxml;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import net.sf.jasperreports.engine.JRException;

/**
 * FXML Controller class
 *
 * @author Syzer
 */
public class Dialogo_fecha_hoy implements Initializable {

    @FXML
    private Label info;
    @FXML
    private DatePicker txtFecha1;
    @FXML
    private DatePicker txtFecha2;
    @FXML
    private Button btnPDF;
    @FXML
    private Button btnGenerarInforme;
  

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        

    }    

    @FXML
    private void generarPDF(ActionEvent event) {

        if (txtFecha1.getValue() != null && txtFecha2.getValue() == null) {
            Date fecha1 = java.sql.Date.valueOf(txtFecha1.getValue());
            try {
                Informe.pdfReportPedidosHoy(fecha1);
            } catch (JRException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (txtFecha1.getValue() != null && txtFecha2.getValue() != null) {
            Date fecha1 = Date.valueOf(txtFecha1.getValue());
            Date fecha2 = Date.valueOf(txtFecha2.getValue());

            try {
                Informe.pdfReportPedidosPeriodo(fecha1, fecha2);
            } catch (JRException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            info.setText("No has introducido ninguna fecha");
            info.setStyle("-fx-background-color:red; -fx-text-fill:white;");
        }

    }

    @FXML
    private void generarInforme(ActionEvent event) {
        
        
        
        
        if (txtFecha1.getValue() != null && txtFecha2.getValue() == null){
            Date fecha1 = Date.valueOf(txtFecha1.getValue());
            try {
                Informe.showReportPedidosHoy(fecha1);
            } catch (JRException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            }       
        } else if (txtFecha1.getValue() != null && txtFecha2.getValue() != null){
            Date fecha1 = Date.valueOf(txtFecha1.getValue());
            Date fecha2 = Date.valueOf(txtFecha2.getValue());
            try {
            Informe.showReportPedidosPeriodo(fecha1, fecha2);
        } catch (JRException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        } else{
            info.setText("No has introducido ninguna fecha");
            info.setStyle("-fx-background-color:red; -fx-text-fill:white;");
        }
    }
    
}
