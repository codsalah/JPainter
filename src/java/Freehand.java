import java.awt.*;

public class Freehand extends Oval {

    public Freehand(int x, int y, Color color, int strokeSize) {
        super(x, y, color, strokeSize, strokeSize, strokeSize);

        setFilled(true);

        setDashed(false);
    }

}