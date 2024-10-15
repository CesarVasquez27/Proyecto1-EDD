/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Parada;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Cesar Augusto
 */
public class Grafo {
    private final Map<String, ListaParada> redTransporte; // Mapa que almacena paradas y sus conexiones

    // Constructor
    public Grafo() {
        this.redTransporte = new HashMap<>();
    }

    // Método para cargar la red de transporte desde un archivo JSON
    public void cargarDesdeArchivo(File archivo) {
        if (archivo == null || !archivo.exists()) {
            System.err.println("El archivo especificado no existe o es nulo.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            Gson gson = new Gson();
            // Definimos el tipo de datos que esperamos recibir del JSON
            Type tipo = new TypeToken<Map<String, List<Map<String, List<Object>>>>>() {}.getType();
            Map<String, List<Map<String, List<Object>>>> data = gson.fromJson(br, tipo);

            // Procesar los datos del archivo JSON
            for (String nombreRed : data.keySet()) {
                List<Map<String, List<Object>>> lineas = data.get(nombreRed);
                for (Map<String, List<Object>> linea : lineas) {
                    for (String nombreLinea : linea.keySet()) {
                        List<Object> paradas = linea.get(nombreLinea);
                        ListaParada listaParadas = new ListaParada();
                        for (Object parada : paradas) {
                            // Manejo de conexiones peatonales
                            if (parada instanceof String) {
                                listaParadas.agregar((String) parada);
                            } else if (parada instanceof Map) {
                                for (Map.Entry<String, String> entry : ((Map<String, String>) parada).entrySet()) {
                                    listaParadas.agregar(entry.getKey());
                                    listaParadas.agregar(entry.getValue());
                                }
                            }
                        }
                        redTransporte.put(nombreLinea, listaParadas);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado. Asegúrate de que la ruta sea correcta.");
        } catch (JsonSyntaxException e) {
            System.err.println("Error: La sintaxis del archivo JSON no es válida.");
        } catch (IOException e) {
            System.err.println("Error: Ocurrió un error de entrada/salida al leer el archivo.");
        } catch (ClassCastException e) {
            System.err.println("Error: Tipo de dato no válido encontrado en el archivo JSON.");
        }
    }

    // Método para mostrar el grafo
    public void mostrarGrafo() {
        for (String linea : redTransporte.keySet()) {
            System.out.println("Línea: " + linea);
            redTransporte.get(linea).print(); // Imprimir las paradas de la línea
        }
    }
    
}
