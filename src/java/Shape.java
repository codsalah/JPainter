import java.awt.*;

public abstract class Shape {

    // Attributes
    protected int x, y;
    protected Color color;
    protected boolean dashed = false;
    protected boolean filled = false;

    // Constructor
    public Shape(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    // Accessors
    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    // Abstract Method
    public abstract void draw(Graphics2D g2) ;

    // Using Stroke interface and return object based on the dashed flag
    protected Stroke makeStroke() {
        if (dashed) {
            return new BasicStroke(
                    3f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    10f,
                    new float[]{10f, 10f},
                    0f
            );
        }

        return new BasicStroke(3f);
    }
}