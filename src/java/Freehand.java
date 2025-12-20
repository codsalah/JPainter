import java.awt.*;

public class Freehand extends Oval {
    
    public Freehand(int x, int y, Color color, int width, int height) {
        super(x, y, color, width, height);

        setFilled(true);

        setDashed(false);
    }
    
}