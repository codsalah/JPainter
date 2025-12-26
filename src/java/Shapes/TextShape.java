package Shapes;
import java.awt.*;

public class TextShape extends Shape {

    private String text;
    private String fontFamily;
    private int fontSize;
    private int fontStyle;

    public TextShape(int x, int y, Color color, int strokeSize,
                     String text, String fontFamily, int fontSize, int fontStyle) {

        super(x, y, color, strokeSize);

        this.text = text;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setFont(new Font(fontFamily, fontStyle, fontSize));
        g2.drawString(text, x, y);
    }
}
