package proyectochristian.Parada;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 //* @author Cesar Augusto
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

    public void cargarDesdeArchivo(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String parada1 = partes[0];
                String parada2 = partes[1];

                listaParadas.agregar(parada1);
                listaParadas.agregar(parada2);
                listaConexiones.agregar(parada1, parada2);
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
    // Suponiendo que tenemos un límite en el número máximo de paradas
    final int MAX_PARADAS = 100; 
    String[] visitadas = new String[MAX_PARADAS]; // Arreglo para marcar paradas visitadas
    String[] cola = new String[MAX_PARADAS]; // Arreglo para simular la cola
    int frente = 0; // Índice del frente de la cola
    int fin = 0; // Índice del final de la cola
    int nivel = 0; // Contador de niveles

    // Inicializamos la cola con la parada inicial
    cola[fin++] = nombreParada; // Añadimos la parada inicial
    int numVisitadas = 0; // Contador de paradas visitadas

    while (frente < fin && nivel <= t) {
        int size = fin - frente; // Número de nodos en el nivel actual

        for (int i = 0; i < size; i++) {
            String paradaActual = cola[frente++]; // Extraemos la parada del frente
            System.out.println("Parada alcanzada: " + paradaActual);
            visitadas[numVisitadas++] = paradaActual; // Marcamos la parada como visitada

            NodoConexion conexiones = listaConexiones.obtenerConexiones(paradaActual);
            while (conexiones != null) {
                String destino = conexiones.getDestino();

                // Comprobamos si la parada ya ha sido visitada
                boolean yaVisitada = false;
                for (int j = 0; j < numVisitadas; j++) {
                    if (visitadas[j].equals(destino)) {
                        yaVisitada = true;
                        break;
                    }
                }

                // Si no ha sido visitada, la agregamos a la cola
                if (!yaVisitada) {
                    cola[fin++] = destino; // Añadimos la parada al final de la cola
                }
                conexiones = conexiones.getpNext(); // Avanzamos a la siguiente conexión
            }
        }
        nivel++; // Aumentamos el nivel después de procesar todas las paradas actuales
    }
}

    public void mostrarGrafo() {
    Graph graph = new SingleGraph("Red de Transporte");
    graph.setStrict(false);
    graph.setAutoCreate(true);

    // Agregar nodos al grafo
    NodoParada actual = listaParadas.getpFirst();
    while (actual != null) {
        // Asegúrate de que getNombreParada() devuelva un valor no nulo y único
        String nombreParada = actual.getNombreParada();
        if (nombreParada != null) {
            graph.addNode(nombreParada);
        } else {
            System.err.println("Nombre de parada nulo encontrado.");
        }
        actual = actual.getpNext();
    }

    // Agregar conexiones al grafo
    NodoConexion conexionActual = listaConexiones.getpFirst();
    while (conexionActual != null) {
        String origen = conexionActual.getOrigen();
        String destino = conexionActual.getDestino();
        // Asegúrate de que los nombres de las paradas no sean nulos
        if (origen != null && destino != null) {
            graph.addEdge(origen + "-" + destino, origen, destino, true);
        } else {
            System.err.println("Conexión con origen o destino nulo: " + origen + " - " + destino);
        }
        conexionActual = conexionActual.getpNext();
    }

    // Mostrar el grafo
    graph.display();
    }
}
