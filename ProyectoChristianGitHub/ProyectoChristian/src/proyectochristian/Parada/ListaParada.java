/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Parada;

/**
 * Clase ListaParada que representa una lista enlazada simple de nodos de paradas.
 * Permite almacenar y gestionar paradas de transporte de manera secuencial.
 * 
 * @author Christian
 */
public class ListaParada {
    private NodoParada pFirst; // Primer nodo de la lista
    private int size; // Tamaño de la lista

    /**
     * Constructor por defecto. Inicializa la lista vacía.
     */
    public ListaParada() {
        this.pFirst = null;
        this.size = 0;
    }

    /**
     * Obtiene el primer nodo de la lista.
     * 
     * @return El primer nodo de la lista o null si la lista está vacía.
     */
    public NodoParada getpFirst() {
        return pFirst;
    }

    /**
     * Establece el primer nodo de la lista.
     * 
     * @param pFirst El nodo que será el primero en la lista.
     */
    public void setpFirst(NodoParada pFirst) {
        this.pFirst = pFirst;
    }

    /**
     * Devuelve el tamaño de la lista.
     * 
     * @return El número de nodos en la lista.
     */
    public int getSize() {
        return size;
    }

    /**
     * Establece el tamaño de la lista.
     * 
     * @param size El nuevo tamaño de la lista.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Verifica si la lista está vacía.
     * 
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        return pFirst == null;
    }

    /**
     * Agrega una nueva parada a la lista si no existe previamente.
     * @author Tomas Paraco
     * @param nombreParada El nombre de la parada a agregar.
     */
    public void agregar(String nombreParada) {
        if (buscarParada(nombreParada) != null) return; // Evitar duplicados
        NodoParada pNew = new NodoParada(nombreParada);
        if (this.isEmpty()) {
            this.setpFirst(pNew);
        } else {
            NodoParada aux = pFirst;
            while (aux.getpNext() != null) {
                aux = aux.getpNext();
            }
            aux.setpNext(pNew);
        }
        size++;
    }

    /**
     * Imprime las paradas almacenadas en la lista.
     * Si la lista está vacía, muestra "Lista Vacia".
     */
    public void print() {
        if (!isEmpty()) {
            NodoParada aux = pFirst;
            while (aux != null) {
                System.out.println(aux.getNombreParada());
                aux = aux.getpNext();
            }
        } else {
            System.out.println("Lista Vacia");
        }
    }
    public void clear() {
        pFirst = null;  // Aquí se reinicia la lista eliminando todas las paradas
    }

    /**
     * Busca una parada en la lista por su nombre.
     * 
     * @param nombreParada El nombre de la parada a buscar.
     * @return El nodo correspondiente si la parada es encontrada, null en caso contrario.
     */
    public NodoParada buscarParada(String nombreParada) {
        NodoParada actual = pFirst;
        while (actual != null) {
            if (actual.getNombreParada().equals(nombreParada)) {
                return actual;  // Parada encontrada
            }
            actual = actual.getpNext();
        }
        return null;  // Parada no encontrada
    }
    /**
     * Elimina Parada.
     * @author Christian
     * @param nombreParada El nombre de la parada a eliminar.
     */
    public void eliminar(String nombreParada) {
        if (pFirst == null) return; // Aquí devuelve una lista vacía
        if (pFirst.getNombreParada().equals(nombreParada)) {
            pFirst = pFirst.getpNext();
            size--;
            return;
        }

        // Buscar la parada en la lista
        NodoParada actual = pFirst;
        while (actual.getpNext() != null) {
            if (actual.getpNext().getNombreParada().equals(nombreParada)) {
                // Saltar el nodo que queremos eliminar
                actual.setpNext(actual.getpNext().getpNext());
                size--;
                return;
            }
            actual = actual.getpNext();
        }
    }
}
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

