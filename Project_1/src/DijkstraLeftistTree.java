import java.util.Arrays;

public class DijkstraLeftistTree {
    private final Graph graph;
    private LeftistTree.Node[] nodeMap;

    public DijkstraLeftistTree(Graph graph) {
        this.graph = graph;
    }

    public void findShortestPaths(int source) {
        int num_vertices = graph.getVertices();
        int[] distances = new int[num_vertices];
        boolean[] visited = new boolean[num_vertices];

        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        LeftistTree priorityQueue = new LeftistTree();
        nodeMap = new LeftistTree.Node[num_vertices];

        // Insert all vertices into the priority queue
        for (int i = 0; i < num_vertices; i++) {
            nodeMap[i] = priorityQueue.insert(i, distances[i]);
            
        }

        while (!priorityQueue.isEmpty()) {
            LeftistTree.Node minNode = priorityQueue.extractMin();
            System.err.println("rootNode "+ minNode.getKey());
            
            // Check if minNode.left and minNode.right are non-null before accessing them
            if (minNode.left != null) {
                System.err.println("minNode left " + minNode.left.getKey());
            } else {
                System.err.println("minNode left is null");
            }

            if (minNode.right != null) {
                System.err.println("minNode right " + minNode.right.getKey());
            } else {
                System.err.println("minNode right is null");
            }

            int u = minNode.getKey();

            if (visited[u]) {
                continue;
            }

            // Mark this node as visited
            visited[u] = true;
            
            System.err.println("Processing node " + u);

            // Traverse through all adjacent vertices of u
            for (int[] neighbor : graph.getNeighbors(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];

                System.err.println(v);
                System.err.println(weight);
                // Relaxation step
                if (!visited[v] && distances[u] != Integer.MAX_VALUE && distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    priorityQueue.decreaseKey(nodeMap[v], distances[v]);
                }
            }
        }

        // return distances
    }
}
