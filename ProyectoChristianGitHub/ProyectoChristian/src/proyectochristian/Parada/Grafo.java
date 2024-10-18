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

public class Grafo {
    private final Map<String, ListaParada> redTransporte; // Mapa línea -> lista de paradas
    private final Map<String, String> conexionesPeatonales; // Para conexiones peatonales

    // Constructor
    public Grafo() {
        this.redTransporte = new HashMap<>();  // Ahora usa ListaParada
        this.conexionesPeatonales = new HashMap<>();  // Almacena conexiones peatonales
    }

    // Método para cargar una nueva red de transporte (reemplaza la anterior)
    public void cargarNuevaRedDesdeArchivo(File archivo) {
        // Limpiar la red anterior
        redTransporte.clear();
        conexionesPeatonales.clear();  // Limpiar las conexiones peatonales también
        System.out.println("Red de transporte anterior eliminada. Cargando nueva red...");

        // Cargar la nueva red desde el archivo
        cargarDesdeArchivo(archivo);

        System.out.println("Nueva red de transporte cargada desde el archivo: " + archivo.getName());
    }

    // Método para cargar un archivo JSON
    public void cargarDesdeArchivo(File archivo) {
        if (archivo == null || !archivo.exists()) {
            System.err.println("El archivo especificado no existe o es nulo: " + archivo.getName());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            Gson gson = new Gson();
            Type tipo = new TypeToken<Map<String, List<Map<String, List<Object>>>>>() {}.getType();
            Map<String, List<Map<String, List<Object>>>> data = gson.fromJson(br, tipo);

            // Procesar datos del JSON
            for (String nombreRed : data.keySet()) {
                List<Map<String, List<Object>>> lineas = data.get(nombreRed);
                for (Map<String, List<Object>> linea : lineas) {
                    for (String nombreLinea : linea.keySet()) {
                        List<Object> paradas = linea.get(nombreLinea);

                        // Si la línea no existe en el mapa, la creamos
                        redTransporte.putIfAbsent(nombreLinea, new ListaParada());

                        // Agregar paradas a la línea usando tu clase ListaParada
                        for (Object parada : paradas) {
                            if (parada instanceof String) {
                                redTransporte.get(nombreLinea).agregar((String) parada);  // Usar el método agregar
                            } else if (parada instanceof Map) {
                                // Conexión peatonal {"Parada1":"Parada2"}
                                for (Map.Entry<String, String> entry : ((Map<String, String>) parada).entrySet()) {
                                    String parada1 = entry.getKey();
                                    String parada2 = entry.getValue();

                                    // Añadir ambas paradas a la línea
                                    redTransporte.get(nombreLinea).agregar(parada1);
                                    redTransporte.get(nombreLinea).agregar(parada2);

                                    // Guardar la conexión peatonal (unión entre ambas paradas)
                                    conexionesPeatonales.put(parada1, parada2);
                                    conexionesPeatonales.put(parada2, parada1);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (ClassCastException e) {
            System.err.println("Error: Tipo de dato inválido en el JSON.");
        }
    }

    // Método para mostrar el grafo usando GraphStream
    public void mostrarGrafo() {
        Graph graph = new SingleGraph("Red de Transporte");
        graph.setStrict(false);
        graph.setAutoCreate(true);

        // Crear los nodos y las conexiones (aristas)
        for (String linea : redTransporte.keySet()) {
            ListaParada paradas = redTransporte.get(linea);
            NodoParada actual = paradas.getpFirst();

            while (actual != null && actual.getpNext() != null) {
                String origen = actual.getNombreParada();
                String destino = actual.getpNext().getNombreParada();

                // Verificar si origen y destino tienen una conexión peatonal
                if (conexionesPeatonales.containsKey(origen) && conexionesPeatonales.get(origen).equals(destino)) {
                    destino = conexionesPeatonales.get(origen);  // Unificar las paradas
                }

                // Crear aristas entre paradas consecutivas
                if (!origen.equals(destino)) {
                    graph.addEdge(origen + "-" + destino, origen, destino, true);
                }

                actual = actual.getpNext();
            }
        }

        // Estilo visual del grafo
        graph.display();
    }
}
