import java.util.ArrayList;
import java.util.Arrays;

public class Grafo {
    private ArrayList<Vertice> vertices;
    private int[][] matrizAdyacencia;  // Matriz para guardar las ponderaciones
    private int contarVertice;

    public Grafo(int maxVertices) {
        vertices = new ArrayList<>();
        matrizAdyacencia = new int[maxVertices][maxVertices];
        for (int i = 0; i < maxVertices; i++) {
            Arrays.fill(matrizAdyacencia[i], Integer.MAX_VALUE);  // Representa la ausencia de conexión
        }
        contarVertice = 0;
    }

    public void anadirVertice(Vertice v) {
        for (Vertice verticeExistente : vertices) {
            if (verticeExistente.x == v.x && verticeExistente.y == v.y) {
                throw new IllegalArgumentException("Ya existe un vértice en las coordenadas (" + v.x + ", " + v.y + ").");
            }
        }
        vertices.add(v);
        contarVertice++; // Incrementar el contador de vértices
    }

    // Metodo para añadir un lado ponderado
    public void anadirLado(int v1, int v2, int ponderacion) {
        if (v1 < contarVertice && v2 < contarVertice && v1 != v2) {
            matrizAdyacencia[v1][v2] = ponderacion; // Reemplaza o añade la nueva ponderación
            matrizAdyacencia[v2][v1] = ponderacion; // Para grafos no dirigidos
        } else {
            throw new IllegalArgumentException("Los índices de los vértices no son válidos.");
        }
    }


    public int[][] getMatrizAdyacencia() {
        return matrizAdyacencia;
    }

    public String dijkstra(int startVertex, int endVertex) {
        int n = getContarVertice();
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        int[] prev = new int[n]; // Para rastrear el camino

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[startVertex] = 0;

        for (int i = 0; i < n; i++) {
            // Encuentra el vértice no visitado con la menor distancia
            int minDist = Integer.MAX_VALUE;
            int currentVertex = -1;

            for (int v = 0; v < n; v++) {
                if (!visited[v] && dist[v] < minDist) {
                    minDist = dist[v];
                    currentVertex = v;
                }
            }

            // Si no se puede avanzar más, salir del bucle
            if (currentVertex == -1) {
                break;
            }

            visited[currentVertex] = true;

            // Actualizar las distancias de los vecinos del vértice actual
            for (int v = 0; v < n; v++) {
                if (!visited[v] && matrizAdyacencia[currentVertex][v] != Integer.MAX_VALUE) {
                    int newDist = dist[currentVertex] + matrizAdyacencia[currentVertex][v];
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        prev[v] = currentVertex; // Guardar el predecesor
                    }
                }
            }
        }

        // Construir el camino más corto
        StringBuilder path = new StringBuilder();
        int vertex = endVertex;
        while (vertex != -1) {
            path.insert(0, vertices.get(vertex).etiqueta + " ");
            vertex = prev[vertex];
        }

        // Verificar si hay un camino válido
        if (dist[endVertex] == Integer.MAX_VALUE) {
            return "No hay camino desde el vértice " + startVertex + " al vértice " + endVertex;
        }

        return "La distancia más corta desde el vértice " + startVertex + " al vértice " + endVertex + " es: " + dist[endVertex] +
                "\nCamino: " + path.toString().trim();
    }




    public ArrayList<Vertice> getVertices() {
        return vertices;
    }

    public int getContarVertice() {
        return contarVertice;
    }

    public String getEtiquetaVerticeSiguiente() {
        return "V" + (getContarVertice() + 1);  // Retorna algo como "V1", "V2", etc.
    }
}