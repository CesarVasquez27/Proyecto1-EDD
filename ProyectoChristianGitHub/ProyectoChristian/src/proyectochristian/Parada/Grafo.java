package proyectochristian.Parada;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Clase Grafo
 * Implementa la estructura de un grafo para representar la red de transporte con paradas y sucursales.
 * 
 * @author Cesar Augusto, Tomas Paraco, Christian
 */
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import proyectochristian.Sucursal.ListaConexion;
import proyectochristian.Sucursal.ListaSucursal;
import proyectochristian.Sucursal.NodoConexion;
import proyectochristian.Sucursal.NodoSucursal;

public class Grafo {
    private ListaParada listaParadas;  // Lista de paradas
    private ListaConexion listaConexiones;  // Lista de conexiones entre paradas
    private final ListaSucursal listaSucursales;  // Lista de sucursales asignadas
    private int t;  // Valor que define el rango de cobertura

    /**
     * Constructor de la clase Grafo.
     * @param tInicial Valor inicial de cobertura.
     */
    public Grafo(int tInicial) {
        this.listaParadas = new ListaParada();
        this.listaConexiones = new ListaConexion();
        this.listaSucursales = new ListaSucursal();
        this.t = tInicial;
    }

    /**
     * Asigna un nuevo valor de cobertura.
     * @param nuevoT Nuevo valor de cobertura.
     */
    public void setT(int nuevoT) {
        this.t = nuevoT;
    }

    /**
     * Obtiene el valor actual de cobertura.
     * @return Valor de cobertura.
     */
    public int getT() {
        return t;
    }

    /**
     * Carga una nueva red de transporte desde un archivo, eliminando la red anterior.
     * @param archivo Archivo con la información de la nueva red.
     */
    public void cargarNuevaRedDesdeArchivo(File archivo) {
        listaParadas = new ListaParada();
        listaConexiones = new ListaConexion();
        System.out.println("Red anterior eliminada. Cargando nueva red...");

        cargarDesdeArchivo(archivo);
        System.out.println("Nueva red cargada desde: " + archivo.getName());
    }

    /**
     * Carga las paradas y conexiones desde un archivo dado.
     * @param archivo Archivo de texto con las paradas y conexiones.
     */
    public void cargarDesdeArchivo(File archivo) {
       try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String lineaNombre = null; // Para almacenar el nombre de la línea
            String paradaAnterior = null; // Para manejar la conexión entre paradas
            boolean dentroDeLinea = false;
            
            // Limpiar datos previos
            listaParadas.clear(); 
            listaConexiones.clear(); 
            
            System.out.println("Red anterior eliminada. Cargando nueva red...");

            // Leer el archivo línea por línea
            while ((linea = br.readLine()) != null) {
                linea = linea.trim(); // Remueve espacios al inicio y al final

                if (linea.isEmpty()) {
                    // Saltar las líneas vacías
                    continue;
                }

                // Detecta el inicio de una nueva línea de metro
                if (linea.endsWith("[")) {
                    // Ejemplo: "Linea 1" : [
                    lineaNombre = linea.split(":")[0].replace("\"", "").trim();
                    dentroDeLinea = true;
                    paradaAnterior = null;
                    continue;
                }

                // Detecta el final de una línea de metro
                if (linea.endsWith("],") || linea.endsWith("]")) {
                    dentroDeLinea = false;
                    continue;
                }

                // Si estamos dentro de una línea de metro
                if (dentroDeLinea) {
                    // Detectar conexiones peatonales (ej: {"Capitolio":"El Silencio"})
                    if (linea.startsWith("{") && linea.endsWith("}")) {
                        // Remover llaves y dividir por los dos puntos
                        linea = linea.replace("{", "").replace("}", "").replace("\"", "");
                        String[] paradas = linea.split(":");

                        // Validar que la línea tenga dos paradas
                        if (paradas.length == 2) {
                            String parada1 = paradas[0].trim();
                            String parada2 = paradas[1].trim();

                            // Agregar paradas y conexión peatonal
                            listaParadas.agregar(parada1);
                            listaParadas.agregar(parada2);
                            listaConexiones.agregar(parada1, parada2); // Conexión peatonal
                        } else {
                            System.err.println("Formato inválido en la conexión peatonal: " + linea);
                        }
                    } else if (!linea.equals(",") && !linea.equals("")) {
                        // Detectar paradas normales
                        String paradaActual = linea.replace(",", "").replace("\"", "").trim();

                        // Validar que la línea no esté vacía
                        if (!paradaActual.isEmpty()) {
                            // Agregar la parada actual
                            listaParadas.agregar(paradaActual);

                            // Conectar con la parada anterior en la misma línea
                            if (paradaAnterior != null) {
                                listaConexiones.agregar(paradaAnterior, paradaActual);
                            }

                            paradaAnterior = paradaActual; // Actualizar la parada anterior
                        }
                    }
                }
            }

            System.out.println("Nueva red cargada desde: " + archivo.getName());

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    /**
     * Coloca una nueva sucursal en una parada específica.
     * @param nombreParada Nombre de la parada donde se coloca la sucursal.
     */
    public void colocarSucursal(String nombreParada) {
        listaSucursales.agregar(nombreParada);
        System.out.println("Sucursal colocada en: " + nombreParada);
    }

    /**
     * Elimina una sucursal de una parada específica.
     * @param nombreParada Nombre de la parada donde se elimina la sucursal.
     */
    public void quitarSucursal(String nombreParada) {
        listaSucursales.eliminar(nombreParada);
        System.out.println("Sucursal eliminada de: " + nombreParada);
    }

    /**
     * Verifica la cobertura de una sucursal mediante DFS o BFS.
     * @param nombreParada Nombre de la parada base.
     * @param usarDFS True para usar DFS, false para BFS.
     * @param paradasCubiertas Arreglo de paradas cubiertas.
     * @param numParadasCubiertas Número de paradas cubiertas.
     */
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
        int frente = 0, fin = 0, nivel = 0;
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

    /**
     * Revisa si todas las paradas están cubiertas por las sucursales existentes.
     */
    public void revisarCoberturaTotal() {
        // Cargar todas las paradas
        String[] todasParadas = new String[listaParadas.getSize()];
        NodoParada actual = listaParadas.getpFirst();
        int index = 0;
        while (actual != null) {
            todasParadas[index++] = actual.getNombreParada();
            actual = actual.getpNext();
        }

        // Revisar cobertura
        String[] paradasCubiertas = new String[listaParadas.getSize()];
        int numParadasCubiertas = 0;
        NodoSucursal sucursalActual = listaSucursales.getpFirst();
        while (sucursalActual != null) {
            verificarCoberturaSucursal(sucursalActual.getNombreSucursal(), false, paradasCubiertas, numParadasCubiertas);
            sucursalActual = sucursalActual.getpNext();
        }

        // Imprimir resultados
        boolean coberturaTotal = true;
        for (String parada : todasParadas) {
            if (!contiene(paradasCubiertas, numParadasCubiertas, parada)) {
                coberturaTotal = false;
                System.out.println("Colocar una sucursal en: " + parada);
            }
        }
        System.out.println(coberturaTotal ? "La cobertura es total." : "Faltan sucursales.");
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

    public void agregarLinea(String nombreLinea, String[] paradas) {
        System.out.println("Agregando nueva línea: " + nombreLinea);
        for (int i = 0; i < paradas.length - 1; i++) {
            listaParadas.agregar(paradas[i]);
            listaConexiones.agregar(paradas[i], paradas[i + 1]);
        }
    }
}

