/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Sucursal;

/**
 *
 * @author Cesar Augusto
 */
public class NodoConexion {
    private String origen;
    private String destino;
    private NodoConexion pNext;

    public NodoConexion(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
        this.pNext = null;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public NodoConexion getpNext() {
        return pNext;
    }

    public void setpNext(NodoConexion pNext) {
        this.pNext = pNext;
    }
}

