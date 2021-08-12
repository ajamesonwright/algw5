import java.util.ArrayList;

import edu.princeton.cs.algs4.BST;

public class KdTree<T extends Comparable<T>> {
    
    private class Node
    {
        private Point2D p;
        private T x;
        private T y;
        private Node left, right;
        private int size;
        private int count;
        
        public Node(T x, T y, int size)
        {
            if (x == null || y == null)
            {
                throw new IllegalArgumentException("Node constructor arguments cannot be null.");
            }

            p = new Point2D((Double) x, (Double) y);
            this.x = x;
            this.y = y;
            this.size = size;
        }

        public String toString()
        {
            String l, r;
            if (left == null)   l = "null";
            else                l = left.p.toString();
            if (right == null)  r = "null";
            else                r = right.p.toString();

            return "Point: " + p.toString() + ", left: " + l + ", right: " + r;
        }
    }

    private Node root;
    private int depth;

    public boolean isEmpty()
    {
        return size(root) == 0;
    }

    public int size()
    {
        return size(root);
    }

    public int size(Node n)
    {
        if (n == null)
        {
            return 0;
        }
        return n.size;
    }

    public boolean contains(T x, T y)
    {
        if (x == null)
        {
            throw new IllegalArgumentException("'contains()' argument cannot be null");
        }

        return get(x, y) != null;
    }

    public void put(T x, T y)
    {
        if (x == null || y == null)
        {
            throw new IllegalArgumentException("Point input arguments cannot be null");
        }

        depth = 0;
        root = put(root, x, y);
    }

    private Node put(Node n, T x, T y)
    {
        if (n == null)
        {
            return new Node(x, y, 1);
        }

        int cmp;
        if (depth % 2 == 0) cmp = x.compareTo(n.x);
        else                cmp = y.compareTo(n.y);

        depth++;

        if (cmp < 0)
        {
            n.left = put(n.left, x, y);
        }
        else if (cmp > 0)
        {
            n.right = put(n.right, x, y);
        }
        else
        {
            n.right = put(n.right, x, y);   // equal comparators placed to the right by convention
        }

        n.size = 1 + size(n.left) + size(n.right);

        return n;
    }

    public T get(T x, T y)
    {
        Node n = root;
        int cmp;

        depth = 0;
        while (n != null)
        {
            if (depth % 2 == 0) cmp = x.compareTo(n.x);
            else                cmp = y.compareTo(n.y);

            if (cmp < 0)        n = n.left;
            else if (cmp > 0)   n = n.right;
            else                return n.y;

            depth++;
        }

        return null;
    }
    
    public Iterable<Node> lrIterator()
    {
        ArrayList<Node> al = new ArrayList<>();
        inorder(root, al);
        return al;
    }

    private void inorder(Node n, ArrayList<Node> al)
    {
        if (n == null)
        {
            return;
        }
        inorder(n.left, al);
        al.add(n);
        inorder(n.right, al);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (Node n : lrIterator())
        {
            sb.append(n.toString() + "\n");
        }

        return sb.toString();
    }

    public String toTree()
    {
        return "";
    }
    public static void main(String[] args)
    {
        KdTree<Double> kd = new KdTree<>();

        kd.put(0.5, 0.5);
        kd.put(0.3, 0.3);
        kd.put(0.2, 0.2);
        kd.put(0.4, 0.4);
        kd.put(0.7, 0.7);
        kd.put(0.6, 0.6);
        kd.put(0.8, 0.8);

        Double d = kd.get(0.2, 0.2);
        System.out.println("Get (0.2, 0.2): " + d);

        System.out.println("kd contains ( 0.3, 0.3 ): " + kd.contains(0.3, 0.3));

        System.out.println("kd contains ( 0.3, 0.2 ): " + kd.contains(0.3, 0.2));

        System.out.println(kd.root.toString() + "\n\n");

        //System.out.println(kd.toString());

        kd = new KdTree<>();
        kd.put(0.5, 0.6);
        kd.put(0.2, 0.7);
        kd.put(0.1, 0.8);
        kd.put(0.1, 0.6);
        System.out.println(kd.toString());
        System.out.println(kd.size());
        System.out.println(kd.root.toString());

        BST<Double, Double> tree = new BST<>();

        System.out.println("Tree size: " + tree.size());
        tree.put(0.5, 0.6);
        System.out.println("Tree size: " + tree.size());
    }
}
