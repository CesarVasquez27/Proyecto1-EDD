/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Sucursal;

/**
 * Clase que representa una conexión entre dos puntos en un grafo.
 * Cada conexión tiene un origen y un destino, ambos de tipo {@code String}.
 * Además, la clase permite enlazar con otro nodo para formar una lista enlazada de conexiones.
 * 
 * @author Cesar Augusto
 */
public class NodoConexion {
    private final String origen;   // Punto de origen de la conexión
    private final String destino;  // Punto de destino de la conexión
    private NodoConexion pNext;    // Puntero al siguiente nodo de la lista enlazada

    /**
     * Constructor que inicializa el nodo de conexión con un origen y un destino.
     * 
     * @param origen El punto de origen de la conexión.
     * @param destino El punto de destino de la conexión.
     */
    public NodoConexion(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
        this.pNext = null;
    }

    /**
     * Obtiene el punto de origen de la conexión.
     * 
     * @return El origen de la conexión como un {@code String}.
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Obtiene el punto de destino de la conexión.
     * 
     * @return El destino de la conexión como un {@code String}.
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Obtiene el siguiente nodo en la lista enlazada de conexiones.
     * 
     * @return El siguiente nodo de tipo {@link NodoConexion}, o {@code null} si no hay más nodos.
     */
    public NodoConexion getpNext() {
        return pNext;
    }

    /**
     * Establece el siguiente nodo en la lista enlazada de conexiones.
     * 
     * @param pNext El nodo que será el siguiente en la lista.
     */
    public void setpNext(NodoConexion pNext) {
        this.pNext = pNext;
    }
}


