import java.io.*;
import java.util.*;

// dijikstra -r n(vertices) d(density) x(source node number)
public class dijikstra {
    public static void main(String[] args) {
        if (args.length < 2 || (!args[0].equals("-r") && !args[0].equals("-l") && !args[0].equals("-f"))) {
            System.out.println("Usage: ");
            System.out.println("Random Mode: dijikstra -r n d x");
            System.out.println("User Input Mode (Leftist Tree): dijikstra -l file-name");
            System.out.println("User Input Mode (Fibonacci Heap): dijikstra -f file-name");
            return;
        }

        try{
            if (args[0].equals("-r")) {
                int num_vertices = Integer.parseInt(args[1]);
                int density = Integer.parseInt(args[2]);
                int source_node_number = Integer.parseInt(args[3]);

                Graph graph = new Graph(num_vertices);
                graph.generateRandomGraph(num_vertices, density);

                while (!graph.isConnected()) {
                    graph.generateRandomGraph(num_vertices, density);
                }

                // Print confirmation of graph generation
                System.out.println("Random connected graph generated with " + num_vertices + " vertices and density " + density + "%.");
                System.out.println("Source node is: " + source_node_number);
                
                DijkstraLeftistTree dijkstraLeftistTree = new DijkstraLeftistTree(graph);

                System.out.println("Finding Shortest Path Dijkstra with Leftist Tree:");
                long startTime = System.currentTimeMillis();
                dijkstraLeftistTree.findShortestPaths(graph.getSource());
                // int[] distances = dijkstraLeftistTree.findShortestPaths(graph.getSource());
                long endTime = System.currentTimeMillis();

                // printDistances(graph.getSource(), distances);
                System.out.println("Execution time (Leftist Tree): " + (endTime - startTime) + " ms");



                // DijkstraFibonacciHeap dijkstraFibonacciHeap = new DijkstraFibonacciHeap(graph);

                // System.out.println("Finding Shortest Path Dijkstra with Fibonacci Heap:");
                // long startTime = System.currentTimeMillis();
                // dijkstraFibonacciHeap.findShortestPaths(graph.getSource());
                // long endTime = System.currentTimeMillis();

                // System.out.println("Execution time (Fibonacci Heap): " + (endTime - startTime) + " ms");
            } else {
                String fileName = args[1];
                Graph graph = readGraphFromFile(fileName);
                System.out.println("Graph loaded from file: " + fileName);

                if (args[0].equals("-l")) {
                    System.out.println("Running Dijkstra with Leftist Tree:");
                    
                    printGraph(graph);

                    DijkstraLeftistTree dijkstraLeftistTree = new DijkstraLeftistTree(graph);

                    System.out.println("Finding Shortest Path Dijkstra with Leftist Tree:");
                    long startTime = System.currentTimeMillis();
                    dijkstraLeftistTree.findShortestPaths(graph.getSource());
                    // int[] distances = dijkstraLeftistTree.findShortestPaths(graph.getSource());
                    long endTime = System.currentTimeMillis();

                    // printDistances(graph.getSource(), distances);
                    System.out.println("Execution time (Leftist Tree): " + (endTime - startTime) + " ms");

                } else if (args[0].equals("-f")) {
                    System.out.println("Running Dijkstra with Fibonacci Heap:");
                    // Display the graph structure to confirm it was created successfully
                    printGraph(graph);

                    DijkstraFibonacciHeap dijkstraFibonacciHeap = new DijkstraFibonacciHeap(graph);

                    System.out.println("Finding Shortest Path Dijkstra with Fibonacci Heap:");
                    long startTime = System.currentTimeMillis();
                    dijkstraFibonacciHeap.findShortestPaths(graph.getSource());
                    long endTime = System.currentTimeMillis();

                    System.out.println("Execution time (Fibonacci Heap): " + (endTime - startTime) + " ms");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }

    }

    private static Graph readGraphFromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        int source = Integer.parseInt(br.readLine().trim());
        String[] nm = br.readLine().trim().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);

        Graph graph = new Graph(n);
        graph.setSource(source);

        for (int i = 0; i < m; i++) {
            String[] edgeData = br.readLine().trim().split(" ");
            int u = Integer.parseInt(edgeData[0]);
            int v = Integer.parseInt(edgeData[1]);
            int cost = Integer.parseInt(edgeData[2]);
            graph.addEdge(u, v, cost);
        }

        return graph;
    }

    private static void printGraph(Graph graph) {
        for (int i = 0; i < graph.getVertices(); i++) {

            System.out.println("Edge from " + i + " to "+ i + " with cost 0");


            List<int[]> neighbors = graph.getNeighbors(i);
            for (int[] edge : neighbors) {
                System.out.println("Edge from " + i + " to " + edge[0] + " with cost " + edge[1]);
            }
        }
    }

    private static void printDistances(int source, int[] distances) {
        for (int i = 0; i < distances.length; i++) {
            System.out.println("Distance from " + source + " to " + i + " is " + (distances[i] == Integer.MAX_VALUE ? "∞" : distances[i]));
        }
    }
    


}
