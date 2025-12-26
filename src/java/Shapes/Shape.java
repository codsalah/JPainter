package Shapes;
import java.awt.*;

public abstract class Shape {

    // Attributes
    protected int x, y;
    protected Color color;
    protected boolean dashed = false;
    protected boolean filled = false;

    protected int strokeSize = 3;

    // Constructor
    public Shape(int x, int y, Color color, int strokeSize) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.strokeSize = strokeSize;
    }

    // Accessors
    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    // Abstract Method
    public abstract void draw(Graphics2D g2);

    // Using Stroke interface and return object based on the dashed flag
    protected Stroke makeStroke() {
        if (dashed) {
            float dashValue = (float) strokeSize * 3f;
            return new BasicStroke(
                    (float) strokeSize,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    10f,
                    new float[] { dashValue, dashValue },
                    0f);
        }

        return new BasicStroke((float) strokeSize);
    }
}