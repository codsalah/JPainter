import java.awt.*;
import java.awt.geom.Path2D;

// this class is used to draw freehand lines on the canvas
// it extends the Shape class and uses Path2D to draw the lines
public class PathShape extends Shape {
    private Path2D path;

    // Constructor
    public PathShape(int x, int y, Color color, int strokeSize) {
        super(x, y, color, strokeSize);
        path = new Path2D.Float();
        path.moveTo(x, y);
    }

    // Add a point to the path
    public void addPoint(int x, int y) {
        path.lineTo(x, y);
    }

    // Draw the path
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(makeStroke());
        g2.draw(path);
    }
}
