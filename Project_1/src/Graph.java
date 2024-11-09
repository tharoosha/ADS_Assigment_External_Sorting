import java.util.*;

public class Graph {
    private final int num_vertices;
    private final List<List<int[]>> adjacencyList;
    private int source_node_number;

    public Graph(int num_vertices) {
        this.num_vertices = num_vertices;
        adjacencyList = new ArrayList<>();
        for (int i = 0; i < num_vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void generateRandomGraph(int num_vertices, int density) {
        Random rand = new Random();

        // max_edges = n(n-1)/2
        int maxEdges = num_vertices * (num_vertices - 1) / 2;

        // density  = (number_of_edges / maximum_num_of_possible_edges)*100
        int targetEdges = (int) (maxEdges * (density / 100.0));

        for (int i = 0; i < targetEdges; i++) {
            int u = rand.nextInt(num_vertices);
            int v = rand.nextInt(num_vertices);
            int cost = rand.nextInt(1000) + 1;

            if (u != v && !edgeExists(u, v)) {
                addEdge(u, v, cost);
            }
        }
    }

    public void addEdge(int u, int v, int cost) {
        adjacencyList.get(u).add(new int[]{v, cost});
        adjacencyList.get(v).add(new int[]{u, cost}); // Since it's an undirected graph
    }

    private boolean edgeExists(int u, int v) {
        for (int[] edge : adjacencyList.get(u)) {
            if (edge[0] == v) return true;
        }
        return false;
    }

    public boolean isConnected() {
        boolean[] visited = new boolean[num_vertices];
        dfs(0, visited);

        for (boolean isVisited : visited) {
            if (!isVisited) return false;
        }
        return true;
    }

    private void dfs(int vertex, boolean[] visited) {
        visited[vertex] = true;
        for (int[] edge : adjacencyList.get(vertex)) {
            if (!visited[edge[0]]) {
                dfs(edge[0], visited);
            }
        }
    }

    public List<int[]> getNeighbors(int vertex) {
        return adjacencyList.get(vertex);
    }

    public int getSource() {
        return source_node_number;
    }

    public void setSource(int source) {
        this.source_node_number = source;
    }

    public int getVertices() {
        return num_vertices;
    }
}
