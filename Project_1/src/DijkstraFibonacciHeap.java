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
        Map<Integer, FibonacciHeap.Node<Integer>> nodeMap = new HashMap<>();

        // Insert all vertices into the heap
        for (int i = 0; i < vertices; i++) {
            FibonacciHeap.Node<Integer> node = heap.insert(i, distances[i]);
            nodeMap.put(i, node);
        }

        while (!heap.isEmpty()) {
            FibonacciHeap.Node<Integer> minNode = heap.extractMin();
            int u = minNode.getKey();

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

    // Internal class representing the Fibonacci Heap
    static class FibonacciHeap<T> {
        private Node<T> minNode;
        private int size;

        public static class Node<T> {
            T key;
            int degree;
            boolean marked;
            Node<T> parent;
            Node<T> child;
            Node<T> left;
            Node<T> right;
            double priority;

            public Node(T key, double priority) {
                this.key = key;
                this.priority = priority;
                this.left = this;
                this.right = this;
            }

            public T getKey() {
                return key;
            }
        }

        public Node<T> insert(T key, double priority) {
            Node<T> node = new Node<>(key, priority);
            minNode = mergeNodes(minNode, node);
            size++;
            return node;
        }

        public boolean isEmpty() {
            return minNode == null;
        }

        public Node<T> extractMin() {
            Node<T> extractedMin = minNode;
            if (extractedMin != null) {
                if (extractedMin.child != null) {
                    Node<T> child = extractedMin.child;
                    do {
                        child.parent = null;
                        child = child.right;
                    } while (child != extractedMin.child);
                    minNode = mergeNodes(minNode, extractedMin.child);
                }

                if (extractedMin.right == extractedMin) {
                    minNode = null;
                } else {
                    minNode = extractedMin.right;
                    extractedMin.right.left = extractedMin.left;
                    extractedMin.left.right = extractedMin.right;
                    consolidate();
                }
                size--;
            }
            return extractedMin;
        }

        public void decreaseKey(Node<T> node, double newPriority) {
            if (newPriority > node.priority) {
                throw new IllegalArgumentException("New priority is greater than current priority");
            }
            node.priority = newPriority;
            Node<T> parent = node.parent;

            if (parent != null && node.priority < parent.priority) {
                cut(node, parent);
                cascadingCut(parent);
            }

            if (node.priority < minNode.priority) {
                minNode = node;
            }
        }

        private void cut(Node<T> node, Node<T> parent) {
            if (node.right != node) {
                node.right.left = node.left;
                node.left.right = node.right;
            }

            if (parent.child == node) {
                if (node.right != node) {
                    parent.child = node.right;
                } else {
                    parent.child = null;
                }
            }

            parent.degree--;
            node.parent = null;
            node.left = node;
            node.right = node;
            node.marked = false;
            minNode = mergeNodes(minNode, node);
        }

        private void cascadingCut(Node<T> node) {
            Node<T> parent = node.parent;
            if (parent != null) {
                if (!node.marked) {
                    node.marked = true;
                } else {
                    cut(node, parent);
                    cascadingCut(parent);
                }
            }
        }

        private void consolidate() {
            int arraySize = (int) Math.floor(Math.log(size) / Math.log(2)) + 1;
            List<Node<T>> degreeTable = new ArrayList<>(Collections.nCopies(arraySize, null));

            List<Node<T>> nodes = new ArrayList<>();
            for (Node<T> current = minNode; nodes.isEmpty() || nodes.get(0) != current; current = current.right) {
                nodes.add(current);
            }

            for (Node<T> current : nodes) {
                int degree = current.degree;

                while (degree >= degreeTable.size()) {
                    degreeTable.add(null);
                }

                // Consolidate nodes of the same degree
                while (degreeTable.get(degree) != null) {
                    Node<T> other = degreeTable.get(degree);
                    if (current.priority > other.priority) {
                        Node<T> temp = current;
                        current = other;
                        other = temp;
                    }
                    linkHeaps(other, current);
                    degreeTable.set(degree, null);
                    degree++;
                    
                    // Ensure degreeTable can accommodate the new degree
                    while (degree >= degreeTable.size()) {
                        degreeTable.add(null);
                    }
                }
                degreeTable.set(degree, current);
            }

            minNode = null;
            for (Node<T> node : degreeTable) {
                if (node != null) {
                    minNode = mergeNodes(minNode, node);
                }
            }
        }

        private void linkHeaps(Node<T> minNode, Node<T> other) {
            other.left.right = other.right;
            other.right.left = other.left;

            other.parent = minNode;
            if (minNode.child == null) {
                minNode.child = other;
                other.right = other;
                other.left = other;
            } else {
                other.left = minNode.child;
                other.right = minNode.child.right;
                minNode.child.right.left = other;
                minNode.child.right = other;
            }
            minNode.degree++;
            other.marked = false;
        }

        private Node<T> mergeNodes(Node<T> a, Node<T> b) {
            if (a == null) return b;
            if (b == null) return a;

            if (a.priority > b.priority) {
                Node<T> temp = a;
                a = b;
                b = temp;
            }

            Node<T> aRight = a.right;
            a.right = b.right;
            b.right.left = a;
            b.right = aRight;
            aRight.left = b;

            return a;
        }
    }
}
