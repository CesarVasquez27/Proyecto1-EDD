/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Sucursal;

/**
 * Clase que representa una lista enlazada de sucursales.
 * Permite almacenar, agregar, imprimir y verificar si la lista está vacía.
 * Cada sucursal se representa mediante un objeto {@link NodoSucursal}.
 * 
 * @author Christian
 */
public class ListaSucursal {
    private NodoSucursal pFirts;  // Primer nodo de la lista de sucursales
    private int size;  // Tamaño de la lista

    /**
     * Constructor por defecto.
     * Inicializa la lista como vacía.
     */
    public ListaSucursal() {
        this.pFirts = null;
        this.size = 0;
    }

    /**
     * Obtiene el primer nodo de la lista de sucursales.
     * 
     * @return El primer nodo de tipo {@link NodoSucursal}, o null si la lista está vacía.
     */
    public NodoSucursal getpFirts() {
        return pFirts;
    }

    /**
     * Establece el primer nodo de la lista de sucursales.
     * 
     * @param pFirts Nodo que será el nuevo primer elemento de la lista.
     */
    public void setpFirts(NodoSucursal pFirts) {
        this.pFirts = pFirts;
    }

    /**
     * Obtiene el tamaño actual de la lista de sucursales.
     * 
     * @return El número de sucursales en la lista.
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
     * Verifica si la lista de sucursales está vacía.
     * 
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        return pFirts == null;
    }

    /**
     * Agrega una nueva sucursal a la lista.
     * Si la lista está vacía, la nueva sucursal se convierte en la primera.
     * Si ya existe un elemento, la sucursal se agrega al final.
     * 
     * @param nombreSucursal Nombre de la sucursal a agregar.
     */
    public void agregar(String nombreSucursal) {
        NodoSucursal pNew = new NodoSucursal(nombreSucursal);
        if (this.isEmpty()) {
            this.setpFirts(pNew);
        } else {
            if (size == 1) {
                this.pFirts.setpNext(pNew);
            } else {
                NodoSucursal aux = pFirts;
                while (aux.getpNext() != null) {
                    aux = aux.getpNext();
                }
                aux.setpNext(pNew);
            }
        }
        size++;
    }

    /**
     * Imprime los nombres de todas las sucursales en la lista.
     * Si la lista está vacía, imprime un mensaje indicando que la lista está vacía.
     */
    public void print() {
        if (!isEmpty()) {
            NodoSucursal aux = pFirts;
            while (aux != null) {
                System.out.println(aux.getNombreSucursal());
                aux = aux.getpNext();
            }
        } else {
            System.out.println("Lista Vacia");
        }
    }

    /**
     * Elimina una sucursal específica de la lista.
     * Actualmente, esta función no está implementada.
     * 
     * @param nombreParada Nombre de la sucursal a eliminar.
     * @throws UnsupportedOperationException Método no implementado.
     */
    public void eliminar(String nombreParada) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}

