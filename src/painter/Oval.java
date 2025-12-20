package painter;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Oval extends Shape {

    // Constructor for full initialization
    public Oval(Point point1, Point point2, Color color, float stroke, boolean isFilled) {
        super(point1, point2, color, stroke, isFilled);
    }

    // Draw the oval with optional fill
    @Override
    public void draw(Graphics2D g2, boolean isFilled) {
        // Calculate the bounding box of the oval
        // This is used to draw the oval and check if a point is inside it
        // width and height are the dimensions of the bounding box
        int x = Math.min(point1.x, point2.x);
        int y = Math.min(point1.y, point2.y);
        // why this works ? because it's the distance between the two points
        // point from the click to the point where the mouse is released
        int width = Math.abs(point1.x - point2.x);
        int height = Math.abs(point1.y - point2.y);

        // Fill the oval if isFilled is true
        if (isFilled) {
            g2.setColor(color);
            g2.fillOval(x, y, width, height);
        }

        // Draw the outline of the oval
        g2.setColor(color);
        g2.setStroke(makeStroke());
        g2.drawOval(x, y, width, height);
    }

    // Check if a point is inside the oval (true if inside)
    // This is used for selection and editing or moving the shape (future feature)
    @Override
    public boolean contains(int px, int py) {
        int x = Math.min(point1.x, point2.x);
        int y = Math.min(point1.y, point2.y);
        // point from the clicked to the point where the mouse is released
        int width = Math.abs(point1.x - point2.x);
        int height = Math.abs(point1.y - point2.y);

        // Check if the point is inside the bounding box of the oval
        Ellipse2D ellipse = new Ellipse2D.Float(x, y, width, height);
        return ellipse.contains(px, py);
    }

}
