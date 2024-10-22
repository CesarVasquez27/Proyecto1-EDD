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
import proyectochristian.Sucursal.NodoSucursal;

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

    private void verificarCoberturaSucursal(String nombreParada, boolean usarDFS, String[] paradasCubiertas, int numParadasCubiertas) {
    if (usarDFS) {
        dfs(nombreParada, 0, paradasCubiertas, numParadasCubiertas);
    } else {
        bfs(nombreParada, paradasCubiertas, numParadasCubiertas);
    }
}

    private void dfs(String paradaActual, int profundidad, String[] visitadas, int numVisitadas) {
    if (profundidad > t || contiene(visitadas, numVisitadas, paradaActual)) return;
    visitadas[numVisitadas++] = paradaActual;
    NodoConexion conexiones = listaConexiones.obtenerConexiones(paradaActual);
    while (conexiones != null) {
        dfs(conexiones.getDestino(), profundidad + 1, visitadas, numVisitadas);
        conexiones = conexiones.getpNext();
    }
}

private void bfs(String nombreParada, String[] visitadas, int numVisitadas) {
    String[] cola = new String[100];
    int frente = 0;
    int fin = 0;
    int nivel = 0;
    cola[fin++] = nombreParada;
    visitadas[numVisitadas++] = nombreParada;

    while (frente < fin && nivel <= t) {
        int size = fin - frente;
        for (int i = 0; i < size; i++) {
            String paradaActual = cola[frente++];
            NodoConexion conexiones = listaConexiones.obtenerConexiones(paradaActual);
            while (conexiones != null) {
                String destino = conexiones.getDestino();
                if (!contiene(visitadas, numVisitadas, destino)) {
                    cola[fin++] = destino;
                    visitadas[numVisitadas++] = destino;
                }
                conexiones = conexiones.getpNext();
            }
        }
        nivel++;
    }
}
    
    private boolean contiene(String[] array, int size, String value) {
        for (int i = 0; i < size; i++) {
        if (array[i].equals(value)) return true;
    }
    return false;
}
    public void revisarCoberturaTotal() {
    // Crear un arreglo con todas las paradas
    String[] todasParadas = new String[listaParadas.getSize()];
    NodoParada actual = listaParadas.getpFirst();
    int index = 0;
    while (actual != null) {
        todasParadas[index++] = actual.getNombreParada();
        actual = actual.getpNext();
    }

    // Crear un arreglo con las paradas cubiertas
    String[] paradasCubiertas = new String[listaParadas.getSize()];
    int numParadasCubiertas = 0;
    NodoSucursal sucursalActual = listaSucursales.getpFirts();
    while (sucursalActual != null) {
        verificarCoberturaSucursal(sucursalActual.getNombreSucursal(), false, paradasCubiertas, numParadasCubiertas);
        sucursalActual = sucursalActual.getpNext();
    }

    // Verificar si todas las paradas están cubiertas
    boolean coberturaTotal = true;
        for (String todasParada : todasParadas) {
            boolean cubierto = false;
            for (int j = 0; j < numParadasCubiertas; j++) {
                if (todasParada.equals(paradasCubiertas[j])) {
                    cubierto = true;
                    break;
                }
            }
            if (!cubierto) {
                coberturaTotal = false;
                break;
            }
        }

    // Imprimir resultados de la cobertura
    if (coberturaTotal) {
        System.out.println("La cobertura es total.");
    } else {
        System.out.println("La cobertura no es total. Sugerencias de nuevas sucursales:");
        for (String todasParada : todasParadas) {
            boolean cubierto = false;
            for (int j = 0; j < numParadasCubiertas; j++) {
                if (todasParada.equals(paradasCubiertas[j])) {
                    cubierto = true;
                    break;
                }
            }
            if (!cubierto) {
                System.out.println("Colocar una sucursal en la parada: " + todasParada);
            }
        }
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
    
    /**
 *
 * @author Tomas Paraco
     * @param nombreLinea
     * @param paradas
 */
    
    public void agregarLinea(String nombreLinea, String[] paradas) {
    System.out.println("Agregando nueva línea: " + nombreLinea);
    for (int i = 0; i < paradas.length - 1; i++) {
        String parada1 = paradas[i];
        String parada2 = paradas[i + 1];
        listaParadas.agregar(parada1);
        listaParadas.agregar(parada2);
        listaConexiones.agregar(parada1, parada2);
    }
    System.out.println("Línea agregada: " + nombreLinea);
}
}
