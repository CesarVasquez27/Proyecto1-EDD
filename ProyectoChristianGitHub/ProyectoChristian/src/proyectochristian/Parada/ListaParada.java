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
    private NodoParada pFirts;
    private int size;

    public ListaParada() {
        this.pFirts = null;
        this.size = 0;
    }

    public NodoParada getpFirts() {
        return pFirts;
    }

    public void setpFirts(NodoParada pFirts) {
        this.pFirts = pFirts;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public boolean isEmpty(){
        return pFirts==null;
    }
    
    public void agregar(String nombreParada){
        NodoParada pNew=new NodoParada(nombreParada);
        if (this.isEmpty()){
            this.setpFirts(pNew);
        }else{
            if(size==1){
            this.pFirts.setpNext(pNew);
            }else{
                NodoParada aux=pFirts;
                while(aux.getpNext()!=null){
                    aux=aux.getpNext();
                }
                aux.setpNext(pNew);
            }
        }
        size++;
    
    }
    
    public void print(){
        if (!isEmpty()){
            NodoParada aux=pFirts;
            while(aux!=null){
                System.out.println(aux.getNombreParada());
                aux=aux.getpNext();}
        }else{
            System.out.println("Lista Vacia");
        }}
    
    
}    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

