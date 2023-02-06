/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Syzer
 */
public class ProductoData {
    
    private String nombre;
    private Integer venta;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getVenta() {
        return venta;
    }

    public void setVenta(Integer venta) {
        this.venta = venta;
    }

    @Override
    public String toString() {
        return "ProductoData{" + "nombre=" + nombre + ", venta=" + venta + '}';
    }
    
    
    
}
