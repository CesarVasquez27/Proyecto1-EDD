/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Sucursal;

/**
 *
 * @author Christian
 */
public class NodoSucursal {
    private String nombreSucursal;  // Nombre de la sucursal
    private NodoSucursal pNext;  // Puntero al siguiente nodo
    
    public NodoSucursal() {
        this.nombreSucursal = null;
        this.pNext = null;
    }

    public NodoSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
        this.pNext = null;
    }

    public NodoSucursal(String nombreSucursal, NodoSucursal node) {
        this.nombreSucursal = nombreSucursal;
        this.pNext = node;
    }
    
    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public NodoSucursal getpNext() {
        return pNext;
    }

    public void setpNext(NodoSucursal siguiente) {
        this.pNext = siguiente;
    }
    
}