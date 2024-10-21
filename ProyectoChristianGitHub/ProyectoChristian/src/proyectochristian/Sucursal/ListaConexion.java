/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Sucursal;

/**
 *
 * @author Cesar Augusto
 */
public class ListaConexion {
    private NodoConexion pFirst;

    public ListaConexion() {
        this.pFirst = null;
    }

    public NodoConexion getpFirst() {
        return pFirst;
    }

 /**
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

