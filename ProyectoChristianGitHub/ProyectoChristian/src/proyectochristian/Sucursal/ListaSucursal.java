/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Sucursal;

/**
 *
 * @author Christian
 */
public class ListaSucursal {
    private NodoSucursal pFirts;
    private int size;

    public ListaSucursal() {
        this.pFirts = null;
        this.size = 0;
    }

    public NodoSucursal getpFirts() {
        return pFirts;
    }

    public void setpFirts(NodoSucursal pFirts) {
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
    
    public void agregar(String nombreSucursal){
        NodoSucursal pNew=new NodoSucursal(nombreSucursal);
        if (this.isEmpty()){
            this.setpFirts(pNew);
        }else{
            if(size==1){
            this.pFirts.setpNext(pNew);
            }else{
                NodoSucursal aux=pFirts;
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
            NodoSucursal aux=pFirts;
            while(aux!=null){
                System.out.println(aux.getNombreSucursal());
                aux=aux.getpNext();}
        }else{
            System.out.println("Lista Vacia");
        }}
}
