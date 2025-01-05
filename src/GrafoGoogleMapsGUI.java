import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GrafoGoogleMapsGUI {
    private JTable tbMatrizadyacencia;
    private JTextArea txtResultado;
    private JButton agregarVérticeButton;
    private JButton agregarLadoButton;
    private JButton DijkstraButton;
    private JTextField txtX;
    private JTextField txtY;
    private JTextField txtV1;
    private JTextField txtV2;
    private JButton dibujarGrafoButton;
    private JPanel pGeneral;
    private JPanel pGrafo= null;
    private JTextField txtVinicio;
    private JTextField textVFinal;
    private JTextField textPonderacion;
    private JTextField textNombre;
    private JScrollPane JScroll_txtResultado;

    private DefaultTableModel modeloTabla;
    private Grafo grafo = new Grafo(20);
    private GrafoPanel grafoPanel = new GrafoPanel(grafo);

    public GrafoGoogleMapsGUI() {
        agregarVérticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int x = Integer.parseInt(txtX.getText());
                    int y = Integer.parseInt(txtY.getText());
                    String nombre = textNombre.getText().trim();

                    String etiqueta = nombre.isEmpty() ? grafo.getEtiquetaVerticeSiguiente() : nombre;
                    Vertice vertice = new Vertice(x, y, etiqueta);
                    grafo.anadirVertice(vertice);

                    actualizarModeloTabla();
                    imprimirGrafo();

                    txtX.setText("");
                    txtY.setText("");
                    textNombre.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese coordenadas válidas y nombre.");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        agregarLadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int v1Index = Integer.parseInt(txtV1.getText());
                    int v2Index = Integer.parseInt(txtV2.getText());
                    int ponderacion = Integer.parseInt(textPonderacion.getText());

                    grafo.anadirLado(v1Index, v2Index, ponderacion);

                    actualizarModeloTabla();
                    imprimirGrafo();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese índices válidos y ponderación.");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        dibujarGrafoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear el panel del grafo y añadirlo al panel pGrafo
                GrafoPanel nuevoGrafoPanel = new GrafoPanel(grafo); // Crear un nuevo panel para el grafo
                pGrafo.removeAll(); // Eliminar cualquier componente existente en pGrafo
                pGrafo.setLayout(new BorderLayout()); // Usar un layout adecuado (como BorderLayout)
                pGrafo.add(nuevoGrafoPanel, BorderLayout.CENTER); // Agregar el GrafoPanel
                pGrafo.revalidate(); // Asegurarse de que el contenedor se actualice
                pGrafo.repaint(); // Redibujar el contenedor
            }
        });


        DijkstraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int verticeInicio = Integer.parseInt(txtVinicio.getText());
                    int verticeFinal = Integer.parseInt(textVFinal.getText());
                    String resultado = grafo.dijkstra(verticeInicio, verticeFinal);

                    // Agregar el resultado de Dijkstra al final del texto existente en txtResultado
                    txtResultado.append("\nCamino más corto entre " + grafo.getVertices().get(verticeInicio).etiqueta +
                            " y " + grafo.getVertices().get(verticeFinal).etiqueta + ":\n");
                    txtResultado.append(resultado); // Agregar el resultado de Dijkstra

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese índices válidos para los vértices.");
                }
            }
        });

    }

    private void actualizarModeloTabla() {
        int[][] adjMatrix = grafo.getMatrizAdyacencia();
        int vertexCount = grafo.getContarVertice();
        modeloTabla = new DefaultTableModel();
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);

        for (int i = 0; i < vertexCount; i++) {
            modeloTabla.addColumn(grafo.getVertices().get(i).etiqueta);
        }

        for (int i = 0; i < vertexCount; i++) {
            Object[] row = new Object[vertexCount];
            for (int j = 0; j < vertexCount; j++) {
                row[j] = adjMatrix[i][j] != Integer.MAX_VALUE ? adjMatrix[i][j] : "-";
            }
            modeloTabla.addRow(row);
        }
        tbMatrizadyacencia.setModel(modeloTabla);
    }

    private void imprimirGrafo() {
        txtResultado.setText(""); // Limpiar el JTextArea
        txtResultado.append("Vertices:\n");
        for (int i = 0; i < grafo.getVertices().size(); i++) {
            txtResultado.append(i + ": " + grafo.getVertices().get(i).toString() + "\n");
        }

        txtResultado.append("\nLados:\n");
        int[][] adjMatrix = grafo.getMatrizAdyacencia();  // Cambié de boolean a int, suponiendo que usas ponderaciones
        for (int i = 0; i < grafo.getContarVertice(); i++) {
            for (int j = i + 1; j < grafo.getContarVertice(); j++) {
                // Verifica si hay una arista entre los vértices i y j
                // Verificar que el valor no sea 0 (sin arista) y tampoco sea Integer.MAX_VALUE (valor de no existencia)
                if (adjMatrix[i][j] != 0 && adjMatrix[i][j] != Integer.MAX_VALUE) {
                    // Imprime la arista solo una vez para grafo no dirigido
                    txtResultado.append(grafo.getVertices().get(i).etiqueta + " <-> " +
                            grafo.getVertices().get(j).etiqueta + " (Ponderación: " +
                            adjMatrix[i][j] + ")\n");
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GrafoGoogleMapsGUI");
        frame.setContentPane(new GrafoGoogleMapsGUI().pGeneral);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

