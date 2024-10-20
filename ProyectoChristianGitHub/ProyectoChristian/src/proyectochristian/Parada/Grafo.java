package proyectochristian.Parada;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 //* @author Cesar Augusto
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import proyectochristian.Sucursal.ListaSucursal;
import proyectochristian.Sucursal.NodoSucursal;

public class Grafo {
    private final Map<String, ListaParada> redTransporte; // Línea -> lista de paradas
    private final Map<String, String> conexionesPeatonales; // Conexiones peatonales
    private final ListaSucursal sucursales;  // Lista enlazada de sucursales
    private int t = 3;  // Valor de t por defecto

    // Constructor
    public Grafo() {
        this.redTransporte = new HashMap<>();
        this.conexionesPeatonales = new HashMap<>();
        this.sucursales = new ListaSucursal();  // Inicializa lista enlazada de sucursales
    }

    // Cambiar el valor de 't'
    public void setT(int nuevoT) {
        this.t = nuevoT;
        System.out.println("El valor de 't' se ha actualizado a: " + nuevoT);
    }

    // Colocar una nueva sucursal en una parada
    public void colocarSucursal(String parada) {
        if (!existeSucursal(parada)) {
            sucursales.agregar(parada);
            System.out.println("Sucursal colocada en: " + parada);
        } else {
            System.out.println("La sucursal ya está colocada en: " + parada);
        }
    }

    // Eliminar una sucursal de una parada
    public void eliminarSucursal(String parada) {
        NodoSucursal anterior = null;
        NodoSucursal actual = sucursales.getpFirts();

        while (actual != null) {
            if (actual.getNombreSucursal().equals(parada)) {
                if (anterior == null) {
                    sucursales.setpFirts(actual.getpNext());
                } else {
                    anterior.setpNext(actual.getpNext());
                }
                sucursales.setSize(sucursales.getSize() - 1);
                System.out.println("Sucursal eliminada de: " + parada);
                return;
            }
            anterior = actual;
            actual = actual.getpNext();
        }
        System.out.println("No se encontró ninguna sucursal en: " + parada);
    }

    // Verificar si existe una sucursal en una parada
    private boolean existeSucursal(String parada) {
        NodoSucursal actual = sucursales.getpFirts();
        while (actual != null) {
            if (actual.getNombreSucursal().equals(parada)) {
                return true;
            }
            actual = actual.getpNext();
        }
        return false;
    }

    // Imprimir todas las sucursales colocadas
    public void imprimirSucursales() {
        System.out.println("Sucursales colocadas:");
        sucursales.print();
    }

    // Cargar red de transporte desde archivo JSON
    public void cargarNuevaRedDesdeArchivo(File archivo) {
        redTransporte.clear();
        conexionesPeatonales.clear();
        System.out.println("Red de transporte anterior eliminada. Cargando nueva red...");
        cargarDesdeArchivo(archivo);
        System.out.println("Nueva red cargada desde: " + archivo.getName());
    }

    // Leer y procesar archivo JSON
    private void cargarDesdeArchivo(File archivo) {
        if (archivo == null || !archivo.exists()) {
            System.err.println("Archivo inválido: " + archivo.getName());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            Gson gson = new Gson();
            Type tipo = new TypeToken<Map<String, List<Map<String, List<Object>>>>>() {}.getType();
            Map<String, List<Map<String, List<Object>>>> data = gson.fromJson(br, tipo);

            for (String red : data.keySet()) {
                List<Map<String, List<Object>>> lineas = data.get(red);
                for (Map<String, List<Object>> linea : lineas) {
                    for (String nombreLinea : linea.keySet()) {
                        redTransporte.putIfAbsent(nombreLinea, new ListaParada());
                        for (Object parada : linea.get(nombreLinea)) {
                            if (parada instanceof String) {
                                redTransporte.get(nombreLinea).agregar((String) parada);
                            } else if (parada instanceof Map) {
                                for (Map.Entry<String, String> entry : ((Map<String, String>) parada).entrySet()) {
                                    redTransporte.get(nombreLinea).agregar(entry.getKey());
                                    redTransporte.get(nombreLinea).agregar(entry.getValue());
                                    conexionesPeatonales.put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | ClassCastException e) {
            System.err.println("Error al cargar archivo: " + e.getMessage());
        }
    }

    // Mostrar grafo usando GraphStream
    public void mostrarGrafo() {
        Graph graph = new SingleGraph("Red de Transporte");
        graph.setStrict(false);
        graph.setAutoCreate(true);

        for (String linea : redTransporte.keySet()) {
            ListaParada paradas = redTransporte.get(linea);
            NodoParada actual = paradas.getpFirst();

            while (actual != null && actual.getpNext() != null) {
                String origen = actual.getNombreParada();
                String destino = actual.getpNext().getNombreParada();
                if (!origen.equals(destino)) {
                    graph.addEdge(origen + "-" + destino, origen, destino, true);
                }
                actual = actual.getpNext();
            }
        }
        graph.display();
    }

    // Obtener paradas adyacentes
    private List<String> obtenerAdyacentes(String parada) {
        List<String> adyacentes = new ArrayList<>();
        for (ListaParada linea : redTransporte.values()) {
            NodoParada actual = linea.getpFirst();
            while (actual != null) {
                if (actual.getNombreParada().equals(parada) && actual.getpNext() != null) {
                    adyacentes.add(actual.getpNext().getNombreParada());
                }
                actual = actual.getpNext();
            }
        }
        return adyacentes;
    }

    // Cobertura con BFS
    public Set<String> coberturaBFS(String inicio) {
        Set<String> visitadas = new HashSet<>();
        Queue<String> cola = new LinkedList<>();
        cola.add(inicio);
        visitadas.add(inicio);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            for (String adyacente : obtenerAdyacentes(actual)) {
                if (!visitadas.contains(adyacente) && visitadas.size() < t) {
                    visitadas.add(adyacente);
                    cola.add(adyacente);
                }
            }
        }
        return visitadas;
    }

    // Cobertura con DFS
    public Set<String> coberturaDFS(String inicio) {
        Set<String> visitadas = new HashSet<>();
        dfs(inicio, visitadas);
        return visitadas;
    }

    private void dfs(String actual, Set<String> visitadas) {
        if (visitadas.size() >= t) return;
        visitadas.add(actual);

        for (String adyacente : obtenerAdyacentes(actual)) {
            if (!visitadas.contains(adyacente)) {
                dfs(adyacente, visitadas);
            }
        }
    }
}
