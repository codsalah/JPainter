package painter;

import java.awt.*;

public class Rectangle extends Shape {

    // Constructor for full initialization
    public Rectangle(Point point1, Point point2, Color color, float stroke, boolean isFilled) {
        super(point1, point2, color, stroke, isFilled);
    }

    // Draw the rectangle with optional fill
    @Override
    public void draw(Graphics2D g2, boolean isFilled) {
        int x = Math.min(point1.x, point2.x);
        int y = Math.min(point1.y, point2.y);
        int width = Math.abs(point1.x - point2.x);
        int height = Math.abs(point1.y - point2.y);

        // Fill the rectangle if isFilled is true
        if (isFilled) {
            g2.setColor(color);
            g2.fillRect(x, y, width, height);
        }

        // Draw the outline of the rectangle
        g2.setColor(color);
        g2.setStroke(makeStroke());
        g2.drawRect(x, y, width, height);
    }

    // Check if a point is inside the rectangle
    // This is used for selection and editing or moving the shape (future feature)
    @Override
    public boolean contains(int px, int py) {
        int x = Math.min(point1.x, point2.x);
        int y = Math.min(point1.y, point2.y);
        int width = Math.abs(point1.x - point2.x);
        int height = Math.abs(point1.y - point2.y);

        // Check if the point is inside the rectangle
        return px >= x && px <= x + width &&
                py >= y && py <= y + height;
    }

}