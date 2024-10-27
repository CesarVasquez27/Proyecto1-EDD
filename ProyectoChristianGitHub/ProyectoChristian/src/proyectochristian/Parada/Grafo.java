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
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import java.io.File;
import java.io.FileReader;
import proyectochristian.Sucursal.ListaConexion;
import proyectochristian.Sucursal.ListaSucursal;
import proyectochristian.Sucursal.NodoConexion;
import proyectochristian.Sucursal.NodoSucursal;
import javax.swing.JOptionPane;

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
    try (FileReader reader = new FileReader(archivo)) {
        // Limpiar datos previos
        listaParadas.clear();
        listaConexiones.clear();

        System.out.println("Red anterior eliminada. Cargando nueva red...");

        // Parsear el archivo JSON
        JsonObject jsonCompleto = JsonParser.parseReader(reader).getAsJsonObject();
        
        // Obtener el nombre de la red (primera clave)
        String nombreRed = jsonCompleto.keySet().iterator().next();
        JsonArray lineas = jsonCompleto.getAsJsonArray(nombreRed);

        // Procesar cada línea
        for (JsonElement lineaElement : lineas) {
            JsonObject lineaObj = lineaElement.getAsJsonObject();
            
            // Obtener el nombre de la línea y su array de paradas
            String nombreLinea = lineaObj.keySet().iterator().next();
            JsonArray paradas = lineaObj.getAsJsonArray(nombreLinea);

            String paradaAnterior = null;

            // Procesar cada parada en la línea
            for (JsonElement paradaElement : paradas) {
                String paradaActual = null;

                if (paradaElement.isJsonObject()) {
                    // Caso de conexión peatonal
                    JsonObject conexion = paradaElement.getAsJsonObject();
                    String origen = conexion.keySet().iterator().next();
                    String destino = conexion.get(origen).getAsString();

                    listaParadas.agregar(origen);
                    listaParadas.agregar(destino);
                    listaConexiones.agregar(origen, destino);
                    
                    paradaActual = origen;
                } else {
                    // Caso de parada normal
                    paradaActual = paradaElement.getAsString();
                    listaParadas.agregar(paradaActual);
                }

                // Crear conexión con la parada anterior si existe
                if (paradaAnterior != null && paradaActual != null) {
                    listaConexiones.agregar(paradaAnterior, paradaActual);
                }

                paradaAnterior = paradaActual;
            }
        }

        System.out.println("Nueva red cargada desde: " + archivo.getName());

    } catch (Exception e) {
        System.err.println("Error al cargar el archivo: " + e.getMessage());
        e.printStackTrace();
    }
}
       

    /**
    * Coloca una nueva sucursal en una parada específica.
    * @param nombreParada Nombre de la parada donde se coloca la sucursal.
    */
    public void colocarSucursal(String nombreParada) {
        // Verificar si la parada existe
        if (!existeParada(nombreParada)) {
            mostrarMensajeError("Esta parada no existe");
            return;
        }

        // Verificar si ya hay una sucursal en esta parada
        if (listaSucursales.buscarSucursal(nombreParada) != null) {
            mostrarMensajeAdvertencia("Ya hay una sucursal aquí");
            return;
        }

        // Agregar la sucursal si pasa las validaciones
        listaSucursales.agregar(nombreParada);
        mostrarMensajeExito("Sucursal colocada exitosamente en: " + nombreParada);
    }

    /**
    * Verifica si una parada existe en la red de transporte.
    * @param nombreParada Nombre de la parada a verificar.
    * @return true si la parada existe, false en caso contrario.
    */
    private boolean existeParada(String nombreParada) {
        NodoParada actual = listaParadas.getpFirst();
        while (actual != null) {
            if (actual.getNombreParada().equalsIgnoreCase(nombreParada)) {
                return true;  // Termina el ciclo al encontrar la parada
            }
            actual = actual.getpNext();
        }
        return false;
    }

    /**
    * Elimina una sucursal de una parada específica.
    * @param nombreParada Nombre de la parada donde se elimina la sucursal.
    */
    public void quitarSucursal(String nombreParada) {
        // Verificar si la parada existe
        if (!existeParada(nombreParada)) {
            mostrarMensajeError("Esta parada no existe");
            return;
        }

        // Verificar si hay una sucursal en esta parada
        if (listaSucursales.buscarSucursal(nombreParada) == null) {
            mostrarMensajeAdvertencia("No hay sucursal para quitar en esta parada");
            return;
        }

        // Eliminar la sucursal si existe
        listaSucursales.eliminar(nombreParada);
        mostrarMensajeExito("Sucursal eliminada exitosamente de: " + nombreParada);
    }

    /**
    * Muestra un mensaje de error en un cuadro de diálogo.
    * @param mensaje El mensaje de error a mostrar.
    */
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
    * Muestra un mensaje de advertencia en un cuadro de diálogo.
    * @param mensaje El mensaje de advertencia a mostrar.
    */
    private void mostrarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    /**
    * Muestra un mensaje de éxito en un cuadro de diálogo.
    * @param mensaje El mensaje de éxito a mostrar.
    */
    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
    * Verifica la cobertura de una sucursal mediante DFS o BFS.
    * @param nombreParada Nombre de la parada base.
    * @param usarDFS True para usar DFS, false para BFS.
    * @return Un arreglo con las paradas cubiertas.
    */
    private String[] verificarCoberturaSucursal(String nombreParada, boolean usarDFS) {
        String[] paradasCubiertas = new String[listaParadas.getSize()];
        int[] numParadasCubiertas = {0};  // Contenedor para el contador

        if (usarDFS) {
            dfs(nombreParada, 0, paradasCubiertas, numParadasCubiertas);
        } else {
            bfs(nombreParada, paradasCubiertas, numParadasCubiertas);
        }

        // Filtrar las paradas no nulas
        return filtrarParadasCubiertas(paradasCubiertas, numParadasCubiertas[0]);
    }

    /**
    * Aplica DFS para encontrar las paradas cubiertas desde la parada actual.
    */
    private void dfs(String paradaActual, int profundidad, String[] visitadas, int[] numVisitadas) {
        if (profundidad > t || contiene(visitadas, numVisitadas[0], paradaActual)) return;

        if (numVisitadas[0] < visitadas.length) {
            visitadas[numVisitadas[0]++] = paradaActual;  // Incrementa el contador de forma segura
        }

        NodoConexion conexiones = listaConexiones.obtenerConexiones(paradaActual);
        while (conexiones != null) {
            dfs(conexiones.getDestino(), profundidad + 1, visitadas, numVisitadas);
            conexiones = conexiones.getpNext();
        }
    }


    /**
    * Aplica BFS para encontrar las paradas cubiertas desde la parada inicial.
    */
    private void bfs(String nombreParada, String[] visitadas, int[] numVisitadas) {
        String[] cola = new String[100];  // Asegúrate de que sea suficiente
        int frente = 0, fin = 0, nivel = 0;

        cola[fin++] = nombreParada;
        if (numVisitadas[0] < visitadas.length) {
            visitadas[numVisitadas[0]++] = nombreParada;
        }

        while (frente < fin && nivel <= t) {
            int size = fin - frente;
            for (int i = 0; i < size; i++) {
                String paradaActual = cola[frente++];
                NodoConexion conexiones = listaConexiones.obtenerConexiones(paradaActual);

                while (conexiones != null) {
                    String destino = conexiones.getDestino();
                    if (!contiene(visitadas, numVisitadas[0], destino)) {
                        cola[fin++] = destino;
                        if (numVisitadas[0] < visitadas.length) {
                            visitadas[numVisitadas[0]++] = destino;
                        }
                    }
                    conexiones = conexiones.getpNext();
                }
            }
            nivel++;
        }
    }



    /**
    * Revisa si todas las paradas están cubiertas por las sucursales existentes.
    * @return Un String con los resultados de la cobertura.
    */
    public String revisarCoberturaTotal() {
        StringBuilder resultado = new StringBuilder();

        // Cargar todas las paradas
            String[] todasParadas = new String[listaParadas.getSize()];
        NodoParada actual = listaParadas.getpFirst();
        int index = 0;
        while (actual != null) {
            todasParadas[index++] = actual.getNombreParada().toLowerCase(); // Normaliza mayúsculas/minúsculas
            actual = actual.getpNext();
        }

        // Revisar cobertura
        String[] paradasCubiertas = new String[listaParadas.getSize()];
        int numParadasCubiertas = 0;

        NodoSucursal sucursalActual = listaSucursales.getpFirst();
        while (sucursalActual != null) {
            String[] coberturaSucursal = verificarCoberturaSucursal(sucursalActual.getNombreSucursal(), false);
            numParadasCubiertas = combinarListas(paradasCubiertas, numParadasCubiertas, coberturaSucursal);
            sucursalActual = sucursalActual.getpNext();
        }

        // Generar resultados: Revisar cuáles paradas NO fueron cubiertas
        boolean coberturaTotal = true;
        for (String parada : todasParadas) {
            if (!contiene(paradasCubiertas, numParadasCubiertas, parada)) {
                coberturaTotal = false;
                resultado.append("Colocar una sucursal en: ").append(parada).append("\n");
            }
        }

        if (coberturaTotal) {
            resultado.append("La cobertura es total.\n");
        } else {
            resultado.append("Faltan sucursales por colocar.\n");
        }

        return resultado.toString();
    }

    /**
    * Combina dos listas de paradas cubiertas, evitando duplicados.
    */
    private int combinarListas(String[] destino, int numDestino, String[] origen) {
        for (String parada : origen) {
            if (parada != null && !contiene(destino, numDestino, parada)) {
                if (numDestino < destino.length) {
                    destino[numDestino++] = parada;
                } else {
                    break;  // Evita desbordamiento si el arreglo destino está lleno
                }
            }
        }
        return numDestino;
    }

    /**
    * Filtra las paradas cubiertas no nulas.
    */
    private String[] filtrarParadasCubiertas(String[] paradas, int size) {
        String[] resultado = new String[size];
        int indexResultado = 0;

        for (int i = 0; i < size; i++) {
            if (paradas[i] != null && !contiene(resultado, indexResultado, paradas[i])) {
                resultado[indexResultado++] = paradas[i].toLowerCase(); // Normaliza mayúsculas/minúsculas
            }
        }

        // Ajustar el tamaño del arreglo final para devolver solo los elementos válidos
        String[] paradasFiltradas = new String[indexResultado];
        System.arraycopy(resultado, 0, paradasFiltradas, 0, indexResultado);

        return paradasFiltradas;
    }
    
    /**
    * Verifica si un valor está presente en el arreglo hasta cierto tamaño.
    * 
    * @param array Arreglo en el que se busca el valor.
    * @param size Tamaño efectivo del arreglo a revisar.
    * @param value Valor a buscar.
    * @return True si el valor está presente, false en caso contrario.
    */
    private boolean contiene(String[] array, int size, String value) {
        for (int i = 0; i < size; i++) {
            if (array[i] != null && array[i].equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
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
    
    // Método para obtener la lista de paradas
    public ListaParada getListaParadas() {
        return listaParadas;
    }

    // Método para obtener la lista de conexiones
    public ListaConexion getListaConexiones() {
        return listaConexiones;
    }

    // Método para obtener la lista de sucursales
    public ListaSucursal getListaSucursales() {
        return listaSucursales;
        
}
    
}
