/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Parada;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
/**
 *
 * @author Cesar Augusto
 */
public class Grafo {
    private final Map<String, List<String>> redTransporte; // Mapa línea -> lista de paradas

    // Constructor
    public Grafo() {
        this.redTransporte = new HashMap<>();
    }

    // Método para cargar varios archivos JSON
    public void cargarDesdeArchivos(List<File> archivos) {
        for (File archivo : archivos) {
            cargarDesdeArchivo(archivo);  // Cargar cada archivo individualmente
        }
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
                        redTransporte.putIfAbsent(nombreLinea, new ArrayList<>());

                        // Agregar paradas a la línea
                        for (Object parada : paradas) {
                            if (parada instanceof String) {
                                redTransporte.get(nombreLinea).add((String) parada);
                            } else if (parada instanceof Map) {
                                for (Map.Entry<String, String> entry : ((Map<String, String>) parada).entrySet()) {
                                    redTransporte.get(nombreLinea).add(entry.getKey());
                                    redTransporte.get(nombreLinea).add(entry.getValue());
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
            List<String> paradas = redTransporte.get(linea);
            for (int i = 0; i < paradas.size() - 1; i++) {
                String origen = paradas.get(i);
                String destino = paradas.get(i + 1);

                // Crear aristas entre paradas consecutivas
                graph.addEdge(origen + "-" + destino, origen, destino, true);
            }
        }

        // Estilo visual del grafo
        graph.display();
    }
}