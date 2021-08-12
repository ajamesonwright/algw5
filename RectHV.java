import edu.princeton.cs.algs4.StdDraw;

public class RectHV {
    private final double xmin, xmax, ymin, ymax;

    public RectHV(double xmin, double ymin, double xmax, double ymax)
    {
        if (Double.isNaN(xmin) || Double.isNaN(ymin) || Double.isNaN(xmax) || Double.isNaN(ymax))
        {
            throw new IllegalArgumentException("Point coordinates must be of double format.");
        }
        if (xmin > xmax || ymin > ymax)
        {
            throw new IllegalArgumentException("Minimum coordinate cannot be greater than maximum coordinate.");
        }

        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    public double xmin()
    {
        return this.xmin;
    }

    public double ymin()
    {
        return this.ymin;
    }

    public double xmax()
    {
        return this.xmax;
    }

    public double ymax()
    {
        return this.ymax;
    }

    public boolean contains(Point2D p)
    {
        double px = p.x();
        double py = p.y();
        if (px < this.xmin())   return false;
        if (px > this.xmax())   return false;
        if (py < this.ymin())   return false;
        if (py > this.ymax())   return false;

        return true;
    }

    public boolean intersects(RectHV that)
    {
        return  this.xmax >= that.xmin && this.ymax >= that.ymin &&
                that.xmax >= this.xmin && that.ymax >= this.ymin;
    }

    public double distanceTo(Point2D p)
    {
        return Math.sqrt(this.distanceSquaredTo(p));
    }

    public double distanceSquaredTo(Point2D p)
    {
        double dx = 0.0;
        double dy = 0.0;

        if (p.x() > xmax)       dx = p.x() - xmax;
        else if (p.x() < xmin)  dx = xmin - p.x();
        if (p.y() > ymax)       dy = p.y() - ymax;
        else if (p.y() < ymin)  dy = ymin - p.y();

        return dx * dx + dy * dy;
    }

    public boolean equals(Object that)
    {
        if (this == that)   return true;
        if (that == null)   return false;
        
        if (this.getClass() != that.getClass()) return false;
        RectHV b = (RectHV) that;

        if (this.xmin != b.xmin)    return false;
        if (this.xmax != b.xmax)    return false;
        if (this.ymin != b.ymin)    return false;
        if (this.ymax != b.ymax)    return false;

        return true;
    }

    public void draw()
    {
        StdDraw.line(xmin, ymin, xmax, ymin);
        StdDraw.line(xmax, ymin, xmax, ymax);
        StdDraw.line(xmax, ymax, xmin, ymax);
        StdDraw.line(xmin, ymax, xmin, ymin);
    }

    public String toString()
    {
        return "[ " + xmin + ", " + xmax + " ] x [ " + ymin + ", " + ymax + " ]";
    }  
}
