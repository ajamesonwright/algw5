import java.util.ArrayList;

public class KdTree<Key extends Comparable<Key>, Value> {
    
    private class Node
    {
        private Point2D p;
        private Key x;
        private Key y;
        private Node left, right;
        private int count;
        
        public Node(Key x, Key y)
        {
            p = new Point2D((Double) x, (Double) y);
            this.x = x;
            this.y = y;
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
    private boolean compareOnY = false;
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
        return n.count;
    }

    public void put(Key x, Key y)
    {
        if (x == null || y == null)
        {
            throw new IllegalArgumentException("Point input arguments cannot be null");
        }


        depth = 0;
        root = put(root, x, y);
        compareOnY = !compareOnY;
    }

    private Node put(Node n, Key x, Key y)
    {
        if (n == null)
        {
            return new Node(x, y);
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

        n.count = 1 + size(n.left) + size(n.right);

        return n;
    }

    public Key get(Key x, Key y)
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
    
    public Iterable<Node> iterator()
    {
        ArrayList<Node> al = new ArrayList<Node>();
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

        for (Node n : iterator())
        {
            sb.append(n.toString() + "\n");
        }

        return sb.toString();
    }
    public static void main(String[] args)
    {
        KdTree<Double, Double> kd = new KdTree<>();

        kd.put(0.5, 0.5);
        kd.put(0.3, 0.3);
        kd.put(0.2, 0.2);
        kd.put(0.4, 0.4);
        kd.put(0.7, 0.7);
        kd.put(0.6, 0.6);
        kd.put(0.8, 0.8);

        Double d = kd.get(0.1, 0.1);
        System.out.println("Get (0.1, 0.1): " + d);

        System.out.println(kd.root.toString() + "\n\n");

        System.out.println(kd.toString());
    }
}
