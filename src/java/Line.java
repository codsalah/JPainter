import java.awt.*;

public class Line extends Shape {

    // Attributes
    int x2, y2;

    // Constructor
    public Line(int x, int y, Color color, int strokeSize, int x2, int y2) {
        super(x, y, color, strokeSize);
        this.x2 = x2;
        this.y2 = y2;
    }

    // Accessors
    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    // Method
    @Override
    public void draw(Graphics2D g2) {

        g2.setColor(color);
        g2.setStroke(makeStroke());
        g2.drawLine(x, y, x2, y2);
    }

}
