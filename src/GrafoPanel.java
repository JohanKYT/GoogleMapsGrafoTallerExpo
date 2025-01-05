import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GrafoPanel extends JPanel {
    private Grafo grafo;
    private double zoomFactor = 1.0; // Factor de zoom inicial (1.0 es tamaño normal)
    private static final double ZOOM_STEP = 0.1; // Incremento de zoom por rueda
    private int offsetX = 0; // Desplazamiento en X
    private int offsetY = 0; // Desplazamiento en Y
    private int prevMouseX = 0; // Coordenada X del ratón cuando comienza el arrastre
    private int prevMouseY = 0; // Coordenada Y del ratón cuando comienza el arrastre
    private boolean isDragging = false; // Flag para verificar si se está arrastrando

    public GrafoPanel(Grafo grafo) {
        this.grafo = grafo;

        // Agregar un MouseWheelListener para manejar el zoom
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Detectar si la rueda se movió hacia arriba (acercar) o hacia abajo (alejar)
                if (e.getWheelRotation() < 0) {
                    // Acercar (zoom in)
                    zoomFactor += ZOOM_STEP;
                } else {
                    // Alejar (zoom out)
                    zoomFactor = Math.max(0.1, zoomFactor - ZOOM_STEP); // Evitar que el zoom sea demasiado pequeño
                }
                repaint(); // Redibujar el grafo con el nuevo zoom
            }
        });

        // Agregar un MouseListener y MouseMotionListener para manejar el desplazamiento con clic izquierdo
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Detectar si el botón izquierdo fue presionado
                if (e.getButton() == MouseEvent.BUTTON1) {
                    prevMouseX = e.getX(); // Guardar las coordenadas actuales del ratón
                    prevMouseY = e.getY();
                    isDragging = true; // Indicar que el ratón está siendo arrastrado
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Detener el arrastre cuando se suelta el botón izquierdo
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isDragging = false;
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Solo mover el grafo si el botón izquierdo está presionado
                if (isDragging) {
                    // Calcular el desplazamiento en X y Y
                    int deltaX = e.getX() - prevMouseX;
                    int deltaY = e.getY() - prevMouseY;

                    // Actualizar el desplazamiento global
                    offsetX += deltaX;
                    offsetY += deltaY;

                    // Actualizar las coordenadas del ratón para el siguiente evento de arrastre
                    prevMouseX = e.getX();
                    prevMouseY = e.getY();

                    // Redibujar el grafo con el nuevo desplazamiento
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Configuración inicial
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        // Trasladar el origen al centro del panel y aplicar el desplazamiento
        int width = getWidth();
        int height = getHeight();
        //Desplazar el grafo segun el offset
        g2d.translate(width / 2 + offsetX, height / 2 + offsetY);
        // Aplicar el zoom: Escalar el gráfico
        g2d.scale(zoomFactor, zoomFactor); // Escalar el contenido del grafo

        // Dibujar las aristas
        int[][] adjMatrix = grafo.getMatrizAdyacencia();
        ArrayList<Vertice> vertices = grafo.getVertices();

        // Ajustar el tamaño de la fuente para reducir el tamaño de las etiquetas al hacer zoom
        Font originalFont = g2d.getFont();
        Font scaledFont = originalFont.deriveFont((float) (originalFont.getSize() / zoomFactor)); // Reducir el tamaño de la fuente
        g2d.setFont(scaledFont);

        for (int i = 0; i < grafo.getContarVertice(); i++) {
            for (int j = i + 1; j < grafo.getContarVertice(); j++) {
                if (adjMatrix[i][j] != Integer.MAX_VALUE) {
                    Vertice v1 = vertices.get(i);
                    Vertice v2 = vertices.get(j);

                    // Dibujar la arista
                    g2d.drawLine(v1.x, v1.y, v2.x, v2.y);

                    // Dibujar la ponderación en el punto medio
                    int midX = (v1.x + v2.x) / 2;
                    int midY = (v1.y + v2.y) / 2;
                    g2d.drawString(String.valueOf(adjMatrix[i][j]), midX, midY);
                }
            }
        }

        // Dibujar los vértices y sus etiquetas
        for (Vertice vertice : vertices) {
            int x = vertice.x;
            int y = vertice.y;

            // Dibujar el vértice, considerando el zoom
            g2d.fillOval(x - 5, y - 5, 10, 10);

            // Dibujar la etiqueta del vértice, con la posición ajustada según el zoom
            g2d.drawString(vertice.etiqueta, x + 10, y);
        }

        // Restaurar la fuente original después de dibujar
        g2d.setFont(originalFont);
    }

    // Metodo para obtener el zoom actual
    public double getZoomFactor() {
        return zoomFactor;
    }

    // Metodo para ajustar el zoom programáticamente (si se quiere agregar un botón para zoom)
    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        repaint();
    }
}

