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
            String parada = JOptionPane.showInputDialog("Ingrese el nombre de la parada para la nueva sucursal:");
            if (parada != null) {
                grafo.colocarSucursal(parada);
                mostrarMensaje("Sucursal agregada: " + parada);
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
     * Muestra el grafo en el panel correspondiente usando GraphStream.
     * Crea los nodos y las conexiones a partir de los datos del grafo.
     */
    private void mostrarGrafoEnPanel() {
        graphPanel.removeAll();

        Graph graph = new SingleGraph("Red de Transporte");
        graph.setStrict(false);
        graph.setAutoCreate(true);

        NodoParada paradaActual = grafo.getListaParadas().getpFirst();
        while (paradaActual != null) {
            String nombreParada = paradaActual.getNombreParada();
            graph.addNode(nombreParada).setAttribute("ui.label", nombreParada);
            paradaActual = paradaActual.getpNext();
        }

        NodoConexion conexionActual = grafo.getListaConexiones().getpFirst();
        while (conexionActual != null) {
            String origen = conexionActual.getOrigen();
            String destino = conexionActual.getDestino();
            graph.addEdge(origen + "-" + destino, origen, destino, true);
            conexionActual = conexionActual.getpNext();
        }

        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);
        viewer.enableAutoLayout();

        if (viewPanel != null) {
            viewer.getDefaultView().getCamera().setViewPercent(1.2);
            viewPanel.setPreferredSize(new Dimension(graphPanel.getWidth(), graphPanel.getHeight()));
            graphPanel.add(viewPanel, BorderLayout.CENTER);
            graphPanel.revalidate();
            graphPanel.repaint();
        } else {
            mostrarError("Error: No se pudo obtener el ViewPanel.");
        }
    }

    /**
     * Muestra un mensaje en la interfaz agregándolo al JTextArea.
     * 
     * @param mensaje El mensaje a mostrar.
     */
    private void mostrarMensaje(String mensaje) {
        textAreaCobertura.append(mensaje + "\n");
    }

    /**
     * Muestra un mensaje de error en un cuadro de diálogo.
     * 
     * @param mensaje El mensaje de error a mostrar.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Método principal que inicia la aplicación.
     * 
     * @param args Los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GrafoMenu().setVisible(true));
    }
}

