import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    TreeSet<Point2D> treeSet;

    public PointSET()
    {
        treeSet = new TreeSet<>();
    }

    public boolean isEmpty()
    {
        return treeSet.isEmpty();
    }

    public int size()
    {
        return treeSet.size();
    }

    public void insert(Point2D p)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("'insert' argument cannot be null.");
        }

        treeSet.add(p);
    }

    public boolean contains(Point2D p)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("'contains' argument cannot be null.");
        }

        return treeSet.contains(p);
    }

    public void draw()
    {

    }

    public Iterable<Point2D> range(RectHV rect)
    {       
        ArrayList<Point2D> lp = new ArrayList<Point2D>();
        
        if (rect == null)
        {
            throw new IllegalArgumentException("'range' argument cannot be null.");
        }

        for (Point2D p : treeSet)
        {
            if (rect.contains(p))
            {
                lp.add(p);
            }
        }

        return lp;
    }

    public Point2D nearest(Point2D p)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("'nearest' argument cannot be null.");
        }

        double min = 2.0;         // size larger than diagonal of unit square
        Point2D minP = null;
        double distance = 0.0;

        for (Point2D pn : treeSet)
        {
            if (p.equals(pn))   continue;
            
            distance = p.distanceSquaredTo(pn);

            if (distance < min)
            {
                minP = pn;
            }
        }

        return minP;
    }

    public static void main(String[] args)
    {
        PointSET ps = new PointSET();

        System.out.println("PointSET is empty: " + ps.isEmpty());

        Point2D pa = new Point2D(0.3, 0.7);
        Point2D pb = new Point2D(0.3, 0.1);

        ps.insert(pa);
        ps.insert(pb);

        System.out.println("PointSET size: " + ps.size());

        System.out.println("PointSET contains pa " + pa.toString() + ": " + ps.contains(pa));
        System.out.println("PointSET contains [0.3, 0.7]: " + ps.contains(new Point2D(0.3, 0.7)));

        RectHV rect = new RectHV(0.2, 0.6, 0.4, 0.8);

        System.out.println("Points contained within rect " + rect.toString() + ": " + ps.range(rect));
        System.out.println("Nearest point to (0.2, 0.6): " + ps.nearest(new Point2D(0.2, 0.6)));
    }
}
