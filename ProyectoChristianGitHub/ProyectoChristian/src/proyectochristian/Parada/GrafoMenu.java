/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package proyectochristian.Parada;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.swing_viewer.SwingViewer;
import proyectochristian.Sucursal.NodoConexion;

/**
 * Clase que representa el menú principal para gestionar la red de transporte.
 * Provee opciones para cargar, mostrar y modificar el grafo,
 * así como revisar la cobertura de las sucursales.
 *
 * @author Cesar Augusto, Tomas Paraco, Christian
 */
public class GrafoMenu extends JFrame {
    private Grafo grafo;
    private JFileChooser fileChooser;
    private final JPanel graphPanel;
    private JTextArea textAreaCobertura;

    /**
     * Constructor que inicializa la interfaz gráfica y los componentes principales.
     */
    public GrafoMenu() {
        grafo = new Grafo(3);  // Valor inicial de t
        fileChooser = new JFileChooser();
        setTitle("Cobertura de Sucursales");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear componentes de la interfaz
        JButton btnCargar = new JButton("Cargar Red");
        JButton btnMostrarGrafo = new JButton("Mostrar Grafo");
        JButton btnEstablecerT = new JButton("Establecer t");
        JButton btnAgregarSucursal = new JButton("Agregar Sucursal");
        JButton btnAgregarLinea = new JButton("Agregar Línea");
        JButton btnRevisarCobertura = new JButton("Revisar Cobertura");

        // Panel para los botones
        JPanel panelBotones = new JPanel(new GridLayout(6, 1));
        panelBotones.add(btnCargar);
        panelBotones.add(btnMostrarGrafo);
        panelBotones.add(btnEstablecerT);
        panelBotones.add(btnAgregarSucursal);
        panelBotones.add(btnAgregarLinea);
        panelBotones.add(btnRevisarCobertura);

        // Panel para el grafo
        graphPanel = new JPanel(new BorderLayout());

        // TextArea para mostrar la cobertura y mensajes
        textAreaCobertura = new JTextArea();
        textAreaCobertura.setEditable(false);
        JScrollPane scrollTextArea = new JScrollPane(textAreaCobertura);

        // Añadir los paneles a la ventana
        add(panelBotones, BorderLayout.WEST);
        add(graphPanel, BorderLayout.CENTER);
        add(scrollTextArea, BorderLayout.SOUTH);

        // Acciones de los botones
        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        grafo.cargarNuevaRedDesdeArchivo(selectedFile);
                        mostrarMensaje("Red cargada correctamente desde: " + selectedFile.getName());
                    } catch (Exception ex) {
                        mostrarError("Error al cargar la red: " + ex.getMessage());
                    }
                }
            }
        });

        btnMostrarGrafo.addActionListener(e -> mostrarGrafoEnPanel());

        btnEstablecerT.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Ingrese el valor de t:");
            if (input != null) {
                try {
                    int nuevoT = Integer.parseInt(input);
                    grafo.setT(nuevoT);
                    mostrarMensaje("Valor de t establecido en: " + nuevoT);
                } catch (NumberFormatException ex) {
                    mostrarError("Valor de t inválido.");
                }
            }
        });

        btnAgregarSucursal.addActionListener(e -> {
            String[] options = {"Agregar", "Des-seleccionar"};
            int seleccion = JOptionPane.showOptionDialog(
                null,
                "¿Qué desea hacer?",
                "Agregar o Des-seleccionar Parada",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0] // Opción por defecto
            );

            if (seleccion == 0) { // Opción "Agregar"
                String parada = JOptionPane.showInputDialog("Ingrese el nombre de la parada para la nueva sucursal:");
                if (parada != null) {
                    grafo.colocarSucursal(parada); // Llama al método para agregar la sucursal
                    mostrarMensaje("Sucursal agregada: " + parada);
                }
            } else if (seleccion == 1) { // Opción "Des-seleccionar"
                String parada = JOptionPane.showInputDialog("Ingrese el nombre de la parada a des-seleccionar:");
                if (parada != null) {
                    grafo.quitarSucursal(parada); // Llama al método para quitar la sucursal
                    mostrarMensaje("Sucursal des-seleccionada: " + parada);
                }
            }
    });


        btnAgregarLinea.addActionListener(e -> {
            String nombreLinea = JOptionPane.showInputDialog("Ingrese el nombre de la nueva línea:");
            String paradas = JOptionPane.showInputDialog("Ingrese las paradas de la línea separadas por comas:");
            if (nombreLinea != null && paradas != null) {
                String[] listaParadas = paradas.split(",");
                grafo.agregarLinea(nombreLinea, listaParadas);
                mostrarMensaje("Línea agregada: " + nombreLinea);
            }
        });

        btnRevisarCobertura.addActionListener(e -> {
            if (grafo != null) {
                String cobertura = grafo.revisarCoberturaTotal();
                textAreaCobertura.setText(cobertura);
            } else {
                mostrarError("El grafo no está inicializado.");
            }
        });
    }

    /**
    * Muestra el grafo en el panel correspondiente.
    */
    private void mostrarGrafoEnPanel() {
        // Limpiar el panel de grafo para evitar superposiciones al renderizar nuevamente
        graphPanel.removeAll();  

        // Crear un objeto grafo utilizando la implementación SingleGraph de GraphStream
        Graph graph = new SingleGraph("Red de Transporte");
        graph.setStrict(false);  // Permitir que el grafo no sea estrictamente acíclico
        graph.setAutoCreate(true);  // Crear nodos automáticamente si no existen al añadir aristas

        // Definición del estilo visual de los nodos y las aristas
        String styleSheet =
            "node {" +
            "   fill-color: black;" +  // Color de relleno de los nodos
            "   size: 20px;" +  // Tamaño de los nodos
            "   text-size: 12px;" +  // Tamaño del texto del nodo
            "   text-color: white;" +  // Color del texto dentro del nodo
            "   text-background-mode: rounded-box;" +  // Fondo redondeado para el texto
            "   text-background-color: black;" +  // Fondo negro del texto
            "   text-alignment: right;" +  // Alinear el texto a la derecha del nodo
            "}" +
            "edge {" +
            "   fill-color: gray;" +  // Color de las aristas
            "   size: 3px;" +  // Grosor de las aristas
            "}";

        // Aplicar el estilo al grafo
        graph.setAttribute("ui.stylesheet", styleSheet);

        // Recorrer y agregar las paradas (nodos) desde la lista del grafo personalizado
        NodoParada paradaActual = grafo.getListaParadas().getpFirst();
        while (paradaActual != null) {
            String nombreParada = paradaActual.getNombreParada();
            graph.addNode(nombreParada).setAttribute("ui.label", nombreParada);  // Añadir nodo con etiqueta
            paradaActual = paradaActual.getpNext();  // Pasar al siguiente nodo en la lista
        }

        // Recorrer y agregar las conexiones (aristas) entre paradas
        NodoConexion conexionActual = grafo.getListaConexiones().getpFirst();
        while (conexionActual != null) {
            String origen = conexionActual.getOrigen();
            String destino = conexionActual.getDestino();
            // Añadir una arista entre el origen y el destino con un identificador único
            graph.addEdge(origen + "-" + destino, origen, destino, true);  
            conexionActual = conexionActual.getpNext();  // Pasar a la siguiente conexión
        }

        // Crear un visor para mostrar el grafo en el panel sin abrir una nueva ventana
        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);  // Panel donde se muestra el grafo
        viewer.enableAutoLayout();  // Habilitar el diseño automático para los nodos y aristas

        if (viewPanel != null) {
            // Ajustar la vista para que todo el grafo sea visible dentro del panel
            viewer.getDefaultView().getCamera().setViewPercent(1.0);  
            viewer.getDefaultView().getCamera().resetView();  // Restablecer la vista para centrar el grafo

            // Establecer el tamaño preferido del panel de visualización
            viewPanel.setPreferredSize(new Dimension(1500, 1500));  

            // Añadir el panel de visualización a un JScrollPane para permitir el desplazamiento
            JScrollPane scrollPane = new JScrollPane(viewPanel);  
            scrollPane.setPreferredSize(new Dimension(graphPanel.getWidth(), graphPanel.getHeight()));

            // Agregar el JScrollPane al panel principal y actualizar la interfaz
            graphPanel.add(scrollPane, BorderLayout.CENTER);
            graphPanel.revalidate();  // Actualizar el diseño del panel
            graphPanel.repaint();  // Repintar el panel para reflejar los cambios
        } else {
            // Mostrar un mensaje de error si no se pudo crear el ViewPanel
            mostrarError("Error: No se pudo obtener el ViewPanel.");
        }
    }

    /**
    * Muestra un mensaje informativo en el JTextArea correspondiente.
    * 
    * @param mensaje El mensaje a mostrar.
    */
    private void mostrarMensaje(String mensaje) {
        textAreaCobertura.append(mensaje + "\n");  // Añadir el mensaje al final del JTextArea
    }

    /**
     * Muestra un mensaje de error en un cuadro de diálogo emergente.
    * 
    * @param mensaje El mensaje de error a mostrar.
    */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);  // Cuadro de error
    }

    /**
     * Método principal que inicia la aplicación.
    * 
    * @param args Los argumentos de la línea de comandos.
    */
    public static void main(String[] args) {
        // Ejecuta la aplicación en el hilo de la interfaz gráfica (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> new GrafoMenu().setVisible(true));
    }
}

