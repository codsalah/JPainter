import java.awt.*;

public class Rectangle extends Shape {

    // Attributes
    int width, height;

    // Constructors
    public Rectangle(int x, int y, Color color, int strokeSize, int width, int height) {
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
            g2.fillRect(x, y, width, height);
        } else {
            g2.setStroke(makeStroke());
            g2.drawRect(x, y, width, height);
        }
    }

}