/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Sucursal;

/**
 * Clase que representa una lista enlazada de conexiones entre paradas.
 * Cada conexión está representada por un objeto {@link NodoConexion}.
 * 
 * Permite agregar nuevas conexiones y obtener las conexiones asociadas a una parada específica.
 * 
 * @author Cesar Augusto
 */
public class ListaConexion {
    private NodoConexion pFirst;  // Primer nodo de la lista de conexiones

    /**
     * Constructor por defecto.
     * Inicializa la lista de conexiones vacía.
     */
    public ListaConexion() {
        this.pFirst = null;
    }

    /**
     * Obtiene el primer nodo de la lista de conexiones.
     * 
     * @return El primer nodo de tipo {@link NodoConexion}, o null si la lista está vacía.
     */
    public NodoConexion getpFirst() {
        return pFirst;
    }

    /**
     * Agrega una nueva conexión entre dos paradas.
     * Si la lista está vacía, la nueva conexión se convierte en la primera.
     * En caso contrario, se agrega al final de la lista.
     * 
     * @param origen  El nombre de la parada de origen.
     * @param destino El nombre de la parada de destino.
     * 
     * @author Tomas Paraco
     */
    public void agregar(String origen, String destino) {
        NodoConexion nuevaConexion = new NodoConexion(origen, destino);
        if (pFirst == null) {
            pFirst = nuevaConexion;
        } else {
            NodoConexion actual = pFirst;
            while (actual.getpNext() != null) {
                actual = actual.getpNext();
            }
            actual.setpNext(nuevaConexion);
        }
    }
    public void clear() {
        pFirst = null;  // Aquí se reinicia la lista eliminando todas las paradas
    }

    /**
     * Obtiene la primera conexión que coincide con la parada de origen dada.
     * Recorre la lista y devuelve el nodo de la conexión asociada al origen solicitado.
     * 
     * @param origen El nombre de la parada de origen.
     * @return El nodo de tipo {@link NodoConexion} correspondiente a la conexión encontrada,
     *         o null si no hay ninguna conexión para ese origen.
     */
    public NodoConexion obtenerConexiones(String origen) {
        NodoConexion actual = pFirst;
        while (actual != null) {
            if (actual.getOrigen().equals(origen)) {
                return actual;
            }
            actual = actual.getpNext();
        }
        return null;
    }
}


