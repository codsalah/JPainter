package Shapes;
import java.awt.*;

public class Oval extends Shape {

    // Attributes
    int width, height;

    // Constructors
    public Oval(int x, int y, Color color, int strokeSize, int width, int height) {
        super(x, y, color, strokeSize);
        this.width = width;
        this.height = height;
    }

    // Accessor
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Method
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);

        if (filled) {
            g2.fillOval(x, y, width, height);
        } else {
            g2.setStroke(makeStroke());
            g2.drawOval(x, y, width, height);
        }
    }

}
