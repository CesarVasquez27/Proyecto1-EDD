package proyectochristian.Parada;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 //* @author Cesar Augusto
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import java.io.*;
import proyectochristian.Sucursal.ListaConexion;
import proyectochristian.Sucursal.ListaSucursal;
import proyectochristian.Sucursal.NodoConexion;

public class Grafo {
    private ListaParada listaParadas;  // Lista de paradas
    private ListaConexion listaConexiones;  // Lista de conexiones
    private final ListaSucursal listaSucursales;  // Lista de sucursales
    private int t;  // Valor de cobertura

    public Grafo(int tInicial) {
        this.listaParadas = new ListaParada();
        this.listaConexiones = new ListaConexion();
        this.listaSucursales = new ListaSucursal();
        this.t = tInicial;
    }

    public void setT(int nuevoT) {
        this.t = nuevoT;
    }

    public int getT() {
        return t;
    }

    public void cargarNuevaRedDesdeArchivo(File archivo) {
        listaParadas = new ListaParada();
        listaConexiones = new ListaConexion();
        System.out.println("Red anterior eliminada. Cargando nueva red...");

        cargarDesdeArchivo(archivo);
        System.out.println("Nueva red cargada desde: " + archivo.getName());
    }

    // Cargar la red desde un archivo JSON
    public void cargarDesdeArchivo(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            Gson gson = new Gson();
            JsonObject json = JsonParser.parseReader(br).getAsJsonObject();

            for (String nombreLinea : json.keySet()) {
                JsonElement paradasElement = json.get(nombreLinea);
                if (paradasElement.isJsonArray()) {
                    String paradaAnterior = null;
                    for (JsonElement paradaElement : paradasElement.getAsJsonArray()) {
                        String nombreParada = paradaElement.getAsString();

                        if (listaParadas.buscarParada(nombreParada) == null) {
                            listaParadas.agregar(nombreParada);
                        }
                        if (paradaAnterior != null) {
                            listaConexiones.agregar(paradaAnterior, nombreParada);
                        }
                        paradaAnterior = nombreParada;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    public void colocarSucursal(String nombreParada) {
        listaSucursales.agregar(nombreParada);
        System.out.println("Sucursal colocada en: " + nombreParada);
    }

    public void quitarSucursal(String nombreParada) {
        listaSucursales.eliminar(nombreParada);
        System.out.println("Sucursal eliminada de: " + nombreParada);
    }

    public void verificarCoberturaSucursal(String nombreParada, boolean usarDFS) {
        if (usarDFS) {
            System.out.println("Verificando cobertura con DFS...");
            dfs(nombreParada, 0);
        } else {
            System.out.println("Verificando cobertura con BFS...");
            bfs(nombreParada);
        }
    }

    private void dfs(String paradaActual, int profundidad) {
        if (profundidad > t) return;
        System.out.println("Parada alcanzada: " + paradaActual);

        NodoConexion conexiones = listaConexiones.obtenerConexiones(paradaActual);
        while (conexiones != null) {
            dfs(conexiones.getDestino(), profundidad + 1);
            conexiones = conexiones.getpNext();
        }
    }

    private void bfs(String nombreParada) {
        final int MAX_PARADAS = 100;
        String[] visitadas = new String[MAX_PARADAS];
        String[] cola = new String[MAX_PARADAS];
        int frente = 0;
        int fin = 0;
        int nivel = 0;
        cola[fin++] = nombreParada;
        int numVisitadas = 0;

        while (frente < fin && nivel <= t) {
            int size = fin - frente;

            for (int i = 0; i < size; i++) {
                String paradaActual = cola[frente++];
                System.out.println("Parada alcanzada: " + paradaActual);
                visitadas[numVisitadas++] = paradaActual;

                NodoConexion conexiones = listaConexiones.obtenerConexiones(paradaActual);
                while (conexiones != null) {
                    String destino = conexiones.getDestino();
                    boolean yaVisitada = false;
                    for (int j = 0; j < numVisitadas; j++) {
                        if (visitadas[j].equals(destino)) {
                            yaVisitada = true;
                            break;
                        }
                    }
                    if (!yaVisitada) {
                        cola[fin++] = destino;
                    }
                    conexiones = conexiones.getpNext();
                }
            }
            nivel++;
        }
    }

    public void mostrarGrafo() {
        Graph graph = new SingleGraph("Red de Transporte");
        graph.setStrict(false);
        graph.setAutoCreate(true);

        NodoParada actual = listaParadas.getpFirst();
        while (actual != null) {
            String nombreParada = actual.getNombreParada();
            graph.addNode(nombreParada);
            actual = actual.getpNext();
        }

        NodoConexion conexionActual = listaConexiones.getpFirst();
        while (conexionActual != null) {
            String origen = conexionActual.getOrigen();
            String destino = conexionActual.getDestino();
            graph.addEdge(origen + "-" + destino, origen, destino, true);
            conexionActual = conexionActual.getpNext();
        }

        graph.display();
    }
}