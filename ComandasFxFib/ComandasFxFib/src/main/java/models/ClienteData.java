/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Syzer
 */
public class ClienteData {
    
    private String nombre;
    private Integer numPedidos;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumPedidos() {
        return numPedidos;
    }

    public void setNumPedidos(Integer numPedidos) {
        this.numPedidos = numPedidos;
    }

    @Override
    public String toString() {
        return "ClienteNumVentas{" + "nombre=" + nombre + ", numPedidos=" + numPedidos + '}';
    }
    
    
    
}
