import java.util.ArrayList;

import edu.princeton.cs.algs4.StdDraw;

public class KdTree<T extends Comparable<T>> {
    
    private class Node
    {
        private Point2D p;
        private Node left, right;
        private int size;
        private int d;
        
        public Node(Point2D pa, int size, int d)
        {
            if (pa == null)
            {
                throw new IllegalArgumentException("Node constructor argument cannot be null.");
            }

            this.p = pa;
            this.size = size;
            this.d = d;
        }

        public String toString()
        {
            String l, r;
            if (left == null)   l = "null";
            else                l = left.p.toString();
            if (right == null)  r = "null";
            else                r = right.p.toString();

            return "Point: " + p.toString() + ", left: " + l + ", right: " + r + ", depth: " + d;
        }
    }

    public Node root;
    public Node nearest;
    private int depth;
    public ArrayList<Node> aList = new ArrayList<Node>();

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

    public boolean contains(Point2D p)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("'contains()' argument cannot be null");
        }

        return get(p) != null;
    }

    public boolean contains(T x, T y)
    {
        if (x == null || y == null)
        {
            throw new IllegalArgumentException("'contains()' argument cannot be null");
        }

        return get(new Point2D((Double) x, (Double) y)) != null;
    }

    public void insert(Point2D p)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("Point input arguments cannot be null");
        }

        depth = 0;
        root = put(root, p);
    }

    private Node put(Node n, Point2D p)
    {
        if (n == null)
        {
            Node nn = new Node(p, 1, depth);
            aList.add(nn);
            return nn;
        }

        int cmp;
        if (depth % 2 == 0) cmp = p.compareToX(n.p);
        else                cmp = p.compareToY(n.p);

        depth++;

        if (cmp < 0)
        {
            n.left = put(n.left, p);
        }
        else if (cmp > 0)
        {
            n.right = put(n.right, p);
        }
        else
        {
            n.right = put(n.right, p);   // equal comparators placed to the right by convention
        }

        n.size = 1 + size(n.left) + size(n.right);

        return n;
    }

    public Point2D get(Point2D p)
    {
        Node n = root;
        int cmp;

        depth = 0;
        while (n != null)
        {
            if (depth % 2 == 0) cmp = p.compareToX(n.p);
            else                cmp = p.compareToY(n.p);

            if (cmp < 0)        n = n.left;
            else if (cmp > 0)   n = n.right;
            else                return n.p;

            depth++;
        }

        return null;
    }

    /**
     * Function to return a list of nodes in the path from the root node to the node representing the input point argument.
     * @param p - the Point2D object to search for.
     * @return an iterable containing all nodes in the path back to the root in the order <em>p</em> to <em>root</em>.
     * Returns <em>null</em> if not found.
     */
    public Iterable<Node> getPath(Point2D p)
    {
        ArrayList<Node> al = new ArrayList<Node>();

        Node n = root;
        int cmp;
        boolean found = false;

        depth = 0;

        while (n != null)
        {
            if (depth % 2 == 0) cmp = p.compareToX(n.p);
            else                cmp = p.compareToY(n.p);

            if (cmp < 0)
            {
                al.add(0, n);
                n = n.left;
            }
            else if (cmp > 0)
            {
                al.add(0, n);
                n = n.right;
            }
            else
            {
                if (p.equals(n.p))
                {
                    found = true;
                    break;
                }
                else
                {
                    al.add(0, n);
                    n = n.right;
                }
            }

            depth++;
        }

        if (found)  return al;
        else        return null;
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

    public Iterable<Node> iOrderIterator()
    {
        return aList;
    }

    public Iterable<Point2D> range(RectHV rect)
    {
        ArrayList<Point2D> al = new ArrayList<>();

        Node n = root;
        range(n, rect, al);

        return al;
    }

    private void range(Node n, RectHV rect, ArrayList<Point2D> al)
    {
        if (n == null)
        {
            return;
        }

        double cmpMin, cmpMax;
        if (n.d % 2 == 0)
        {
            cmpMin = rect.xmin() - n.p.x();
            cmpMax = rect.xmax() - n.p.x();
        }
        else
        {
            cmpMin = rect.ymin() - n.p.y();
            cmpMax = rect.ymax() - n.p.y();
        }

        if (rect.contains(n.p))     al.add(n.p);

        if (cmpMax < 0)
        {
            range(n.left, rect, al);
        }
        else if (cmpMin > 0)
        {
            range(n.right, rect, al);
        }
        else
        {
            range(n.left, rect, al);
            range(n.right, rect, al);
        }
    }

    public Node nearest(Point2D p)
    {
        Node n = root;
        nearest = root;
        nearest(n, p);

        return nearest;
    }

    public void nearest(Node n, Point2D p)
    {
        if (n == null)
        {
            return;
        }
        if (p.distanceTo(n.p) < p.distanceTo(nearest.p))
        {
            nearest = n;
        }

        int cmp;
        boolean cmpType = n.d % 2 == 0;         // true -> compare on X
        if (cmpType)        cmp = p.compareToX(n.p);
        else                cmp = p.compareToY(n.p);

        RectHV temp;
        if (cmp < 0)
        {
            if (cmpType)    temp = new RectHV(n.p.x(), 0, 1, 1);    // rect occupying everything to the right of n.p.x()
            else            temp = new RectHV(0, n.p.y(), 1, 1);    // rect occupying everything above n.p.y()

            if (n.left != null && p.distanceTo(n.left.p) < temp.distanceTo(p))        // is query point closer to next node point than bisecting line?
            {
                nearest(n.left, p);
            }
            else
            {
                nearest(n.left, p);
                nearest(n.right, p);
            }
        }
        else if (cmp > 0)
        {
            if (cmpType)    temp = new RectHV(0, 0, n.p.x(), 1);    // rect occupying everything to the left of n.p.x()
            else            temp = new RectHV(0, 0, 1, n.p.y());    // rect occupying everything below n.p.y()

            if (n.right != null && p.distanceTo(n.right.p) < temp.distanceTo(p))       // is query point closer to next node point than bisecting line?
            {
                nearest(n.right, p);
            }
            else
            {
                nearest(n.right, p);
                nearest(n.left, p);
            }
        }
        else
        {
            nearest(n.left, p);
            nearest(n.right, p);
        }
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

    public void draw()
    {
        for (Node n : iOrderIterator())
        {
            double x = n.p.x();
            double y = n.p.y();
            double xMin = 0.0;
            double xMax = 1.0;
            double yMin = 0.0;
            double yMax = 1.0;
            
            for (Node nn : getPath(n.p))
            {
                if (n.d % 2 == nn.d % 2)   continue;

                double nx, ny;
                nx = nn.p.x();
                ny = nn.p.y();

                if (nx > x && nx < xMax)  xMax = nx;
                if (nx < x && nx > xMin)  xMin = nx;
                if (ny > y && ny < yMax)  yMax = ny;
                if (ny < y && ny > yMin)  yMin = ny;
            }

            if (n.d % 2 == 0)
            {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(x, yMin, x, yMax);
            }
            else
            {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(xMin, y, xMax, y);
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledCircle(n.p.x(), n.p.y(), 0.005);
        }
    }
    public static void main(String[] args)
    {
        KdTree<Double> kd = new KdTree<Double>();

        int paSize = 8;
        Point2D[] pa = new Point2D[paSize];
        
        pa[0] = new Point2D(0.5, 0.5);
        pa[1] = new Point2D(0.3, 0.3);
        pa[2] = new Point2D(0.2, 0.2);
        pa[3] = new Point2D(0.4, 0.4);
        pa[4] = new Point2D(0.7, 0.7);
        pa[5] = new Point2D(0.6, 0.6);
        pa[6] = new Point2D(0.8, 0.8);
        pa[7] = new Point2D(0.1, 0.1);


        for (int i = 0; i < paSize; i++)
        {
            kd.insert(pa[i]);
        }

        Point2D p = kd.get(new Point2D(0.2, 0.2));
        System.out.println("Get (0.2, 0.2): " + p.toString());
        p = kd.get(pa[2]);
        System.out.println("Get (0.2, 0.2): " + p.toString());

        System.out.println("kd contains ( 0.3, 0.3 ): " + kd.contains(0.3, 0.3));

        System.out.println("kd contains ( 0.3, 0.2 ): " + kd.contains(0.3, 0.2));

        System.out.println(kd.root.toString() + "\n\n");

        //System.out.println(kd.toString());

        kd = new KdTree<Double>();

        kd.insert(new Point2D(0.5, 0.6));
        kd.insert(new Point2D(0.2, 0.7));
        kd.insert(new Point2D(0.1, 0.8));
        kd.insert(new Point2D(0.1, 0.6));
        System.out.println(kd.toString());
        System.out.println("Tree size: " + kd.size());

        KdTree<Double>.Node n = kd.root;
        n = n.left;
        System.out.println("Subtree rooted at " + n.toString() + " size: -> " + n.size);
        n = n.left;
        System.out.println("Subtree rooted at " + n.toString() + " size: -> " + n.size);

        System.out.println("All points contained within the rectangle (0.15, 0.65, 0.25, 0.75)");
        for (Point2D pRange : kd.range(new RectHV(0.15, 0.65, 0.25, 0.75)))
        {
            System.out.println(pRange.toString());
        }

        Point2D pNearestTest = new Point2D(0.15, 0.55);
        System.out.println("Nearest point to " + pNearestTest.toString() + " -> " + kd.nearest(pNearestTest));

        pNearestTest = new Point2D(0.8, 0.8);
        System.out.println("Nearest point to " + pNearestTest.toString() + " -> " + kd.nearest(pNearestTest));
        
        pNearestTest = new Point2D(0.1, 0.7);
        System.out.println("Nearest point to " + pNearestTest.toString() + " -> " + kd.nearest(pNearestTest));

        Point2D pTest = new Point2D(0.6, 0.9);
        kd.insert(pTest);
        System.out.println("\nNodes in the path to point pTest: " + pTest.toString());
        for (KdTree<Double>.Node nPathTest : kd.getPath(pTest))
        {
            System.out.println(nPathTest.toString());
        }

        pTest = new Point2D(0.65, 0.9);
        kd.insert(pTest);
        System.out.println("\nNodes in the path to point pTest: " + pTest.toString());
        for (KdTree<Double>.Node nPathTest : kd.getPath(pTest))
        {
            System.out.println(nPathTest.toString());
        }
    }
}
