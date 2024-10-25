/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import proyectochristian.Parada.Grafo;
import proyectochristian.Parada.NodoParada;
import proyectochristian.Sucursal.NodoConexion;


/**
 * Clase GrafoMenu que implementa la interfaz gráfica para gestionar la cobertura de sucursales en la red de transporte.
 * @author tomas
 */

public class GrafoMenu extends JFrame {
    private Grafo grafo;
    private JFileChooser fileChooser;
    private JPanel graphPanel;

    /**
     * Constructor de la clase GrafoMenu.
     * Inicializa los componentes de la interfaz y el grafo.
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
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(6, 1));
        panelBotones.add(btnCargar);
        panelBotones.add(btnMostrarGrafo);
        panelBotones.add(btnEstablecerT);
        panelBotones.add(btnAgregarSucursal);
        panelBotones.add(btnAgregarLinea);
        panelBotones.add(btnRevisarCobertura);

        // Panel para el grafo
        graphPanel = new JPanel();
        graphPanel.setLayout(new BorderLayout());

        // Añadir el panel de botones y el panel del grafo a la ventana
        add(panelBotones, BorderLayout.WEST);
        add(graphPanel, BorderLayout.CENTER);

        // Acciones de los botones
        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    grafo.cargarNuevaRedDesdeArchivo(selectedFile);
                }
            }
        });

        btnMostrarGrafo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarGrafoEnPanel();
            }
        });

        btnEstablecerT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Ingrese el valor de t:");
                if (input != null) {
                    int nuevoT = Integer.parseInt(input);
                    grafo.setT(nuevoT);
                }
            }
        });

        btnAgregarSucursal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String parada = JOptionPane.showInputDialog("Ingrese el nombre de la parada para la nueva sucursal:");
                if (parada != null) {
                    grafo.colocarSucursal(parada);
                }
            }
        });

        btnAgregarLinea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreLinea = JOptionPane.showInputDialog("Ingrese el nombre de la nueva línea:");
                String paradas = JOptionPane.showInputDialog("Ingrese las paradas de la línea separadas por comas:");
                if (nombreLinea != null && paradas != null) {
                    String[] listaParadas = paradas.split(",");
                    grafo.agregarLinea(nombreLinea, listaParadas);
                }
            }
        });

        btnRevisarCobertura.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grafo.revisarCoberturaTotal();
            }
        });
    }

    /**
     * Método para mostrar el grafo en el panel de la interfaz.
     */
    private void mostrarGrafoEnPanel() {
        graphPanel.removeAll(); // Limpiar el panel
        Graph graph = new SingleGraph("Red de Transporte");
        graph.setStrict(false);
        graph.setAutoCreate(true);

        NodoParada actual = grafo.getListaParadas().getpFirst();
        while (actual != null) {
            String nombreParada = actual.getNombreParada();
            if (nombreParada != null) {
                graph.addNode(nombreParada).addAttribute("ui.label", nombreParada);
            }
            actual = actual.getpNext();
        }

        NodoConexion conexionActual = grafo.getListaConexiones().getpFirst();
        while (conexionActual != null) {
            String origen = conexionActual.getOrigen();
            String destino = conexionActual.getDestino();
            if (origen != null && destino != null) {
                graph.addEdge(origen + "-" + destino, origen, destino, true);
            }
            conexionActual = conexionActual.getpNext();
        }

        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        ViewPanel viewPanel = viewer.addDefaultView(false); // false indica que no se requiere una vista cerrada
        graphPanel.add(viewPanel, BorderLayout.CENTER);
        graphPanel.revalidate();
        graphPanel.repaint();
    }



    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GrafoMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new GrafoMenu().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
