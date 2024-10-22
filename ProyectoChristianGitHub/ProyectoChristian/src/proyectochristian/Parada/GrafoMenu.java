/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectochristian.Parada;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *
 * @author tomas
 */

public class GrafoMenu extends JFrame {
    private Grafo grafo;
    private JFileChooser fileChooser;

    public GrafoMenu() {
        grafo = new Grafo(3);  // Valor inicial de t
        fileChooser = new JFileChooser();

        setTitle("Cobertura de Sucursales");
        setSize(800, 600);
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

        // Añadir el panel de botones a la ventana
        add(panelBotones, BorderLayout.WEST);

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
                grafo.mostrarGrafo();
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
            @Override
            public void actionPerformed(ActionEvent e) {
                grafo.revisarCoberturaTotal();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GrafoMenu().setVisible(true);
            }
        });
    }
}
