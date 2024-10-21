/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Parada;

/**
 *
 * @author Christian
 */
public class NodoParada {
    private String nombreParada;  // Nombre de la parada
    private NodoParada pNext;  // Puntero al siguiente nodo

    public NodoParada() {
        this.nombreParada =  null;
        this.pNext = null;
    }

    public NodoParada(String nombreParada) {
        this.nombreParada = nombreParada;
        this.pNext = null;
    }

    public NodoParada(String nombreParada, NodoParada node) {
        this.nombreParada = nombreParada;
        this.pNext = node ;
    }

    public String getNombreParada() {
        return nombreParada;
    }

    public void setNombreParada(String nombreParada) {
        this.nombreParada = nombreParada;
    }

    public NodoParada getpNext() {
        return pNext;
    }

    public void setpNext(NodoParada pNext) {
        this.pNext = pNext;
    }
    @Override
    public String toString() {
        return "Parada: " + nombreParada;
    }
    
}
    
