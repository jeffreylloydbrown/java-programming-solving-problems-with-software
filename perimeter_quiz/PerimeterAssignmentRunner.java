import edu.duke.*;
import java.io.File;

public class PerimeterAssignmentRunner {
    /** getNumPoints() returns an integer that is the number of points in Shape `s`.
     *  @param s:   the Shape we are counting points in
     *  @return:    number of points in Shape `s`
     */
    public int getNumPoints (Shape s) {
        // Initalize a count and just loop over the points, adding 1 to the counter
        // on each loop.  We don't do anything with the actual points.
        int points = 0;
        for (Point p : s.getPoints()) {
            points = points + 1;
        }
        return points;
    }
    
    public double getPerimeter (Shape s) {
        // Start with totalPerim = 0
        double totalPerim = 0;
        // Start with prevPt = the last point
        Point prevPt = s.getLastPoint();
        // For each point currPt in the shape,
        for (Point currPt : s.getPoints()) {
            // Find distance from prevPt to currPt, name it currDist
            double currDist = prevPt.distance(currPt);
            // Update totalPerim to be totalPerim + currDist
            totalPerim = totalPerim + currDist;
            // Update prevPt to be currPt.
            prevPt = currPt;
        }
        // totalPerim is my answer
        return totalPerim;
    }

    /** getAverageLength() returns the average length of all sides in Shape `s`.
     *  @param s:   a Shape to examine
     *  @return:    the average length of all sides in the shape
     */
    public double getAverageLength(Shape s) {
        // Average length is just the total length divided by the number of sides. 
        // Total length is just the perimeter.  Number of sides is the same as the
        // number of points.
        
        // The more maintainable way to write this is to use getPerimeter() and
        // getNumPoints(), appropriately adjusted.  If we really needed better 
        // performance, we would do this work with a single loop here as using those
        // methods will process the shape twice instead of once.  For non-huge shapes,
        // this isn't worth the difference.
        
        return getPerimeter(s)/getNumPoints(s);
    }

    /** getLargestSide() returns the length of the longest side in Shape `s`.
     *  @param s:  the Shape to examine
     *  @return:   the length of the longest side in Shape `s`.
     */
    public double getLargestSide(Shape s) {
        // Start with longestSide = 0
        double longestSide = 0;
        // Start with prevPt = the last point
        Point prevPt = s.getLastPoint();
        // For each point currPt in the shape,
        for (Point currPt : s.getPoints()) {
            // Find distance from prevPt to currPt, name it currDist
            double currDist = prevPt.distance(currPt);
            // if currDist > longestSide, update longestSide to be currDist
            if (currDist > longestSide) {
                longestSide = currDist;
            }
            // Update prevPt to be currPt.
            prevPt = currPt;
        }
        // longestSide is my answer
        return longestSide;
    }

    public double getLargestX(Shape s) {
        // Put code here
        return 0.0;
    }

    public double getLargestPerimeterMultipleFiles() {
        // Put code here
        return 0.0;
    }

    public String getFileWithLargestPerimeter() {
        // Put code here
        File temp = null;    // replace this code
        return temp.getName();
    }
    
    public void testPerimeter() {
        FileResource fr = new FileResource();
        Shape s = new Shape(fr);
        int points = getNumPoints(s);
        System.out.println("number of points = " + points);
        double length = getPerimeter(s);
        System.out.println("perimeter = " + length);
        System.out.println("average side length = " + getAverageLength(s));
        System.out.println("longest side length = " + getLargestSide(s));
    }

    public void testPerimeterMultipleFiles() {
        // Put code here
    }

    public void testFileWithLargestPerimeter() {
        // Put code here
    }

    // This method creates a triangle that you can use to test your other methods
    public void triangle(){
        Shape triangle = new Shape();
        triangle.addPoint(new Point(0,0));
        triangle.addPoint(new Point(6,0));
        triangle.addPoint(new Point(3,6));
        for (Point p : triangle.getPoints()){
            System.out.println(p);
        }
        double peri = getPerimeter(triangle);
        System.out.println("perimeter = "+peri);
    }

    // This method prints names of all files in a chosen folder that you can use to test your other methods
    public void printFileNames() {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            System.out.println(f);
        }
    }

    public static void main (String[] args) {
        PerimeterAssignmentRunner pr = new PerimeterAssignmentRunner();
        pr.testPerimeter();
    }
}
