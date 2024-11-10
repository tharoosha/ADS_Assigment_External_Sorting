import java.util.*;

public class DijkstraFibonacciHeap {
    private final Graph graph;

    public DijkstraFibonacciHeap(Graph graph) {
        this.graph = graph;
    }

    public void findShortestPaths(int source) {
        int vertices = graph.getVertices();
        int[] distances = new int[vertices];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        FibonacciHeap<Integer> heap = new FibonacciHeap<>();
        Map<Integer, FibonacciHeap.Entry<Integer>> nodeMap = new HashMap<>();

        // Insert all vertices into the heap
        for (int i = 0; i < vertices; i++) {
            FibonacciHeap.Entry<Integer> node = heap.enqueue(i, distances[i]);
            nodeMap.put(i, node);
        }

        while (!heap.isEmpty()) {
            FibonacciHeap.Entry<Integer> minNode = heap.dequeueMin();
            int u = minNode.getValue();

            // Traverse through all adjacent vertices of u
            for (int[] neighbor : graph.getNeighbors(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];

                // Relaxation step
                if (distances[u] != Integer.MAX_VALUE && distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    heap.decreaseKey(nodeMap.get(v), distances[v]);
                }
            }
        }

        // Print shortest paths from the source to each vertex
        for (int i = 0; i < vertices; i++) {
            System.out.println("Distance from " + source + " to " + i + " is " + distances[i]);
        }
    }
}
