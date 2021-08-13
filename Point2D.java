public class Point2D implements Comparable<Point2D>{
    private double x, y;

    public Point2D(double x, double y)
    {
        if (Double.isNaN(x) || Double.isNaN(y))
        {
            throw new IllegalArgumentException("Point coordinates must be of double format.");
        }
        // account for -0.0 and convert to +0.0
        if (x == 0.0)   this.x = 0.0;
        else            this.x = x;

        if (y == 0.0)   this.y = 0.0;
        else            this.y = y;
    }

    public double x()
    {
        return this.x;
    }

    public double y()
    {
        return this.y;
    }

    public double distanceTo(Point2D that)
    {
        if (that == null)
        {
            return Double.POSITIVE_INFINITY;
        }
        
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceSquaredTo(Point2D that)
    {
        if (that == null)
        {
            return Double.POSITIVE_INFINITY;
        }

        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return dx * dx + dy * dy;
    }

    public int compareTo(Point2D that)
    {
        if (this.y() < that.y())    return -1;
        if (this.y() > that.y())    return 1;
        if (this.x() < that.x())    return -1;
        if (this.x() > that.x())    return 1;
        
        return 0;
    }

    public int compareToX(Point2D that)
    {
        if (this.x() < that.x())    return -1;
        if (this.x() > that.x())    return 1;
        
        return 0;
    }

    public int compareToY(Point2D that)
    {
        if (this.y() < that.y())    return -1;
        if (this.y() > that.y())    return 1;

        return 0;
    }

    public boolean equals(Object that)
    {
        if (that == null)
        {
            return false;
        }
        
        if (this.getClass() != that.getClass())
        {
            return false;
        }

        Point2D b = (Point2D) that;

        if (this.compareTo(b) != 0)
        {
            return false;
        }

        return true;
    }

    public void draw()
    {

    }

    public String toString()
    {
        return "( " + this.x() + ", " + this.y() + " )";
    }    
}
