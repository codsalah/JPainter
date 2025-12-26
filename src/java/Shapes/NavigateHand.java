package Shapes;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavigateHand extends MouseAdapter {
    public interface NavigableView {
        // Interface to be implemented by the workspace view
        void pan(double deltaX, double deltaY);

        void repaintView();
    }

    private final NavigableView view;
    private Point lastMousePoint;
    private boolean enabled = false;

    public NavigateHand(NavigableView view) {
        this.view = view;
    }

    // Activates or deactivates the navigation tool.
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            lastMousePoint = null;
        }
    }

    // Capture initial point to start calculating deltas
    @Override
    public void mousePressed(MouseEvent e) {
        if (!enabled)
            return;
        lastMousePoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!enabled || lastMousePoint == null)
            return;

        Point currentPoint = e.getPoint();

        // Calculate the movement delta from the previous position
        double dx = currentPoint.x - lastMousePoint.x;
        double dy = currentPoint.y - lastMousePoint.y;

        // Apply the translation to the workspace view
        view.pan(dx, dy);

        // Request a redraw for smooth, continuous feedback
        view.repaintView();

        // Update the last position for the next movement delta
        lastMousePoint = currentPoint;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Immediate stop on mouse release
        lastMousePoint = null;
    }
}
