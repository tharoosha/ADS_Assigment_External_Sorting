import java.util.NoSuchElementException;

public class LeftistTree {
    private Node root;

    public static class Node {
        int key;
        int distance;
        Node left;
        Node right;
        int npl; // Null Path Length

        public Node(int key, int distance) {
            this.key = key;
            this.distance = distance;
            this.npl = 1; // Initial null path length
            this.left = null;
            this.right = null;
        }

        public int getKey() {
            return key;
        }
    }

    public Node insert(int key, int distance) {
        Node newNode = new Node(key, distance);
        root = merge(root, newNode);
        return newNode;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public Node extractMin() {
        if (root == null) {
            throw new NoSuchElementException("The leftist tree is empty");
        }
        Node minNode = root;
        root = merge(root.left, root.right);
        return minNode;
    }

    public void decreaseKey(Node node, int newDistance) {
        if (newDistance > node.distance) {
            throw new IllegalArgumentException("New distance is greater than current distance");
        }

        if (node == root) {
            node.distance = newDistance;
            return;
        }

        node.distance = newDistance;
        LeftistTree.Node temp_root = extractMin();
        temp_root.left = null;
        temp_root.right = null;
        root = merge(root, temp_root);
        // node.distance = newDistance;
        // Cut and re-insert node (simple simulation of decreaseKey without direct tree restructuring)
        // root = merge(root, node);
    }

    private Node merge(Node h1, Node h2) {

        if (h1 != null) {
            System.out.println("h1 distance: " + h1.distance);
        } else {
            System.out.println("h1 is null");
        }
    
        if (h2 != null) {
            System.out.println("h2 distance: " + h2.distance);
        } else {
            System.out.println("h2 is null");
        }


        if (h1 == null) return h2;
        if (h2 == null) return h1;

        if (h1.distance > h2.distance) {
            Node temp = h1;
            h1 = h2;
            h2 = temp;
        }
        if (h1.right != null) {
            System.out.println("h1.right distance after merge: " + h1.right.distance);
        }

        h1.right = merge(h1.right, h2);
        
        if (h1.right != null && h1.left != null) {
            System.out.println(h1.right.npl + " h1 left npl " + h1.left.npl);
        } else {
            if (h1.right == null) {
                System.out.println("h1.right is null");
            }
            if (h1.left == null) {
                System.out.println("h1.left is null");
            }
        }

        if (h1.left == null || (h1.right != null && h1.left.npl < h1.right.npl)) {
            Node temp = h1.left;
            h1.left = h1.right;
            h1.right = temp;
        }

        h1.npl = (h1.right == null) ? 1 : h1.right.npl + 1;
        return h1;
    }
}
