/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Parada;

/**
 * Clase que representa un nodo en la lista de paradas.
 * Cada nodo almacena el nombre de una parada y un puntero al siguiente nodo en la lista.
 * 
 * @author Christian
 */
public class NodoParada {
    private String nombreParada;  // Nombre de la parada
    private NodoParada pNext;  // Puntero al siguiente nodo

    /**
     * Constructor por defecto. 
     * Inicializa el nodo con un nombre y un puntero nulo.
     */
    public NodoParada() {
        this.nombreParada = null;
        this.pNext = null;
    }

    /**
     * Constructor que inicializa el nodo con el nombre de una parada.
     * 
     * @param nombreParada El nombre de la parada que se almacena en este nodo.
     */
    public NodoParada(String nombreParada) {
        this.nombreParada = nombreParada;
        this.pNext = null;
    }

    /**
     * Constructor que inicializa el nodo con el nombre de la parada y 
     * un puntero al siguiente nodo.
     * 
     * @param nombreParada El nombre de la parada.
     * @param node El nodo siguiente en la lista.
     */
    public NodoParada(String nombreParada, NodoParada node) {
        this.nombreParada = nombreParada;
        this.pNext = node;
    }

    /**
     * Obtiene el nombre de la parada almacenada en este nodo.
     * 
     * @return El nombre de la parada.
     */
    public String getNombreParada() {
        return nombreParada;
    }

    /**
     * Establece el nombre de la parada en este nodo.
     * 
     * @param nombreParada El nuevo nombre de la parada.
     */
    public void setNombreParada(String nombreParada) {
        this.nombreParada = nombreParada;
    }

    /**
     * Obtiene el siguiente nodo en la lista.
     * 
     * @return El nodo siguiente o null si es el último nodo.
     */
    public NodoParada getpNext() {
        return pNext;
    }

    /**
     * Establece el siguiente nodo en la lista.
     * 
     * @param pNext El nodo que seguirá a este en la lista.
     */
    public void setpNext(NodoParada pNext) {
        this.pNext = pNext;
    }

    /**
     * Devuelve una representación en forma de texto del nodo.
     * 
     * @return Una cadena con el formato "Parada: [nombre de la parada]".
     */
    @Override
    public String toString() {
        return "Parada: " + nombreParada;
    }
}

    
