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
 * Agrega una nueva conexión entre dos paradas si no existe previamente.
 * 
 * @param origen El nombre de la parada de origen.
 * @param destino El nombre de la parada de destino.
 */
public void agregar(String origen, String destino) {
    // No agregar si origen y destino son iguales
    if (origen.equals(destino)) {
        return;
    }

    // Verificar si la conexión ya existe en cualquier dirección
    NodoConexion actual = pFirst;
    while (actual != null) {
        if ((actual.getOrigen().equals(origen) && actual.getDestino().equals(destino)) ||
            (actual.getOrigen().equals(destino) && actual.getDestino().equals(origen))) {
            return; // La conexión ya existe
        }
        actual = actual.getpNext();
    }

    // Agregar conexión en ambas direcciones
    NodoConexion nuevaConexion1 = new NodoConexion(origen, destino);
    NodoConexion nuevaConexion2 = new NodoConexion(destino, origen);

    // Agregar primera conexión
    nuevaConexion1.setpNext(pFirst);
    pFirst = nuevaConexion1;

    // Agregar conexión inversa
    nuevaConexion2.setpNext(pFirst);
    pFirst = nuevaConexion2;
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
