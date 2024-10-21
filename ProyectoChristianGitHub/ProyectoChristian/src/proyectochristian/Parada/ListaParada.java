/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Parada;

/**
 *
 * @author Christian
 */
public class ListaParada {
    private NodoParada pFirst;
    private int size;

    public ListaParada() {
        this.pFirst = null;
        this.size = 0;
    }

    public NodoParada getpFirst() {
        return pFirst;
    }

    public void setpFirst(NodoParada pFirst) {
        this.pFirst = pFirst;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public boolean isEmpty(){
        return pFirst==null;
    }
    
    /**
 *
 * @author Tomas Paraco
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
    public void print(){
        if (!isEmpty()){
            NodoParada aux=pFirst;
            while(aux!=null){
                System.out.println(aux.getNombreParada());
                aux=aux.getpNext();}
        }else{
            System.out.println("Lista Vacia");
        }}
    
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
    
    

}    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

