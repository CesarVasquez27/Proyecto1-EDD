/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Sucursal;

/**
 * Clase que representa una sucursal dentro de una lista enlazada.
 * Cada sucursal tiene un nombre y un puntero al siguiente nodo en la lista.
 * 
 * @author Christian
 */
public class NodoSucursal {
    private String nombreSucursal;  // Nombre de la sucursal
    private NodoSucursal pNext;     // Puntero al siguiente nodo en la lista

    /**
     * Constructor por defecto que inicializa la sucursal sin nombre y sin referencia al siguiente nodo.
     */
    public NodoSucursal() {
        this.nombreSucursal = null;
        this.pNext = null;
    }

    /**
     * Constructor que inicializa la sucursal con un nombre dado.
     * 
     * @param nombreSucursal El nombre de la sucursal.
     */
    public NodoSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
        this.pNext = null;
    }

    /**
     * Constructor que inicializa la sucursal con un nombre y referencia a otro nodo.
     * 
     * @param nombreSucursal El nombre de la sucursal.
     * @param node El siguiente nodo de tipo {@link NodoSucursal}.
     */
    public NodoSucursal(String nombreSucursal, NodoSucursal node) {
        this.nombreSucursal = nombreSucursal;
        this.pNext = node;
    }

    /**
     * Obtiene el nombre de la sucursal.
     * 
     * @return El nombre de la sucursal como un {@code String}.
     */
    public String getNombreSucursal() {
        return nombreSucursal;
    }

    /**
     * Establece el nombre de la sucursal.
     * 
     * @param nombreSucursal El nuevo nombre de la sucursal.
     */
    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    /**
     * Obtiene el siguiente nodo en la lista de sucursales.
     * 
     * @return El siguiente nodo de tipo {@link NodoSucursal}, o {@code null} si no hay más nodos.
     */
    public NodoSucursal getpNext() {
        return pNext;
    }

    /**
     * Establece el siguiente nodo en la lista de sucursales.
     * 
     * @param siguiente El nodo que será el siguiente en la lista.
     */
    public void setpNext(NodoSucursal siguiente) {
        this.pNext = siguiente;
    }
}
