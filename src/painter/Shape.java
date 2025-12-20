package painter;

import java.awt.*;

public abstract class Shape {

    // Fields
    protected Color color;
    protected float stroke;
    // first point is the starting point (click)
    protected Point point1;
    // second point is the ending point (release)
    protected Point point2;
    protected boolean isFilled;
    protected boolean dashed;

    // Constructor for full initialization
    public Shape(Point point1, Point point2, Color color, float stroke, boolean isFilled) {
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
        this.stroke = stroke;
        this.isFilled = isFilled;
    }

    // Getters and Setters
    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStroke(float stroke) {
        this.stroke = stroke;
    }

    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    // Abstract methods ?????????
    public abstract void draw(Graphics2D g2, boolean isFilled);

    // Protected methods for stroke creation meaning it's only used within the class
    // or subclasses (inheritance) ??????????
    // Updated to use strokeWidth attribute
    protected Stroke makeStroke() {
        if (dashed) {
            return new BasicStroke(
                    0.4f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    10f,
                    new float[] { 10f, 10f },
                    0f);
        }
        return new BasicStroke(stroke);
    }

    // Keeping this from previous version as it's useful for interaction,
    // though not explicitly mentioned in the new spec.
    public abstract boolean contains(int x, int y);
}