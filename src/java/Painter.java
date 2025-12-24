import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Painter extends JPanel implements NavigateHand.NavigableView {

    // Tools
    public static final int BRUSH = 0;
    public static final int ERASER = 1;
    public static final int LINE = 2;
    public static final int RECT = 3;
    public static final int OVAL = 4;
    public static final int HAND = 5;

    private int tool = BRUSH;

    private Color currentColor = Color.BLACK;
    private boolean dashed = false;
    private boolean filled = false;

    private int baseStrokeSize = 3;

    // Zoom and Pan state (Translation then Scale)
    private double zoomScale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    // Workspace state (Infinite feel, but we can treat a specific area as the
    // "sheet")
    private int sheetWidth = 2000;
    private int sheetHeight = 2000;

    // Workspace origin (where the mouse is clicked)
    private int startX, startY;

    private ArrayList<Shape> shapes = new ArrayList<>();
    private Shape previewShape = null;
    private PathShape currentPath = null;
    private BufferedImage backgroundImage = null;

    // Save Workspace for undo after clean
    private ArrayList<Shape> savedShapesBeforeClear = null;
    private BufferedImage savedBackgroundBeforeClear = null;
    private boolean canUndoClear = false;

    private NavigateHand navigateHand;

    public Painter() {
        setBackground(new Color(50, 50, 50)); // Dark background for the area outside fixed workspace
        navigateHand = new NavigateHand(this);
        setPreferredSize(new Dimension(sheetWidth, sheetHeight));

        MouseAdapter mouse = new MouseAdapter() {

            // Converts raw mouse coordinates to canvas coordinates, adjusting for
            // offset and zoom so the pointer tracks correctly during zoom operations
            private int toCanvasX(int screenX) {
                return (int) ((screenX - offsetX) / zoomScale);
            }

            private int toCanvasY(int screenY) {
                return (int) ((screenY - offsetY) / zoomScale);
            }

            // Ensures that coordinates do not fall outside the visual "sheet"
            // boundaries (0 to width/height) dynamically based on the sheet size.
            private int clamp(int val, int max) {
                return Math.max(0, Math.min(val, max));
            }

            // Handles mouse press events for different tools (Brush, Eraser, Line,
            // Rect, Oval, Hand)
            @Override
            public void mousePressed(MouseEvent e) {
                if (tool == HAND) {
                    navigateHand.mousePressed(e);
                    return;
                }
                // Convert mouse coords to canvas coords and clamp them to the sheet size
                int cx = clamp(toCanvasX(e.getX()), sheetWidth);
                int cy = clamp(toCanvasY(e.getY()), sheetHeight);
                startX = cx;
                startY = cy;

                // Handle mouse press events for different tools
                // (Brush, Eraser, Line, Rect, Oval, Hand)
                if (tool == BRUSH || tool == ERASER) {
                    Color c = (tool == BRUSH) ? currentColor : Color.WHITE; // Use White for eraser on canvas
                    currentPath = new PathShape(cx, cy, c, getDynamicStrokeSize());
                    currentPath.setDashed(dashed);
                    shapes.add(currentPath);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (tool == HAND) {
                    navigateHand.mouseDragged(e);
                    return;
                }
                // Convert mouse coords to canvas coords and clamp them to the sheet size
                int cx = clamp(toCanvasX(e.getX()), sheetWidth);
                int cy = clamp(toCanvasY(e.getY()), sheetHeight);

                // Handle mouse drag events for different tools
                // (Brush, Eraser, Line, Rect, Oval, Hand)
                if (tool == BRUSH || tool == ERASER) {
                    if (currentPath != null) {
                        currentPath.addPoint(cx, cy);
                        repaint();
                    }
                    return;
                }

                // Preview for LINE/RECT/OVAL
                previewShape = createShape(startX, startY, cx, cy);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // if hand tool is selected, let the navigateHand handle it
                if (tool == HAND) {
                    navigateHand.mouseReleased(e);
                    return;
                }
                // clamp the coordinates to the sheet size
                int cx = clamp(toCanvasX(e.getX()), sheetWidth);
                int cy = clamp(toCanvasY(e.getY()), sheetHeight);

                if (tool == LINE || tool == RECT || tool == OVAL) {
                    Shape finalShape = createShape(startX, startY, cx, cy);
                    shapes.add(finalShape);
                    previewShape = null;
                }
                currentPath = null;
                repaint();
            }

            // handle mouse wheel events by
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    // Zoom if control (ctrl) is pressed
                    // if the wheel is scrolled up, zoom in, else zoom out
                    double zoomFactor = (e.getWheelRotation() < 0) ? 1.1 : 1.0 / 1.1;
                    zoomAt(e.getX(), e.getY(), zoomFactor);
                } else {
                    // Pan if not scrolling with control (ctrl) pressed
                    offsetX -= e.getUnitsToScroll() * 5;
                    offsetY -= e.getUnitsToScroll() * 5;
                    repaint();
                }
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

    private int getDynamicStrokeSize() {
        // Zooming in (zoomScale > 1) -> brush renders smaller relative to content for
        // precision
        return (int) Math.max(1, baseStrokeSize / zoomScale);
    }

    private Shape createShape(int x1, int y1, int x2, int y2) {
        int dStroke = getDynamicStrokeSize();
        if (tool == LINE) {
            Line line = new Line(x1, y1, currentColor, dStroke, x2, y2);
            line.setDashed(dashed);
            return line;
        }

        int left = Math.min(x1, x2);
        int top = Math.min(y1, y2);
        int w = Math.abs(x2 - x1);
        int h = Math.abs(y2 - y1);

        if (tool == RECT) {
            Rectangle r = new Rectangle(left, top, currentColor, dStroke, w, h);
            r.setDashed(dashed);
            r.setFilled(filled);
            return r;
        }

        Oval o = new Oval(left, top, currentColor, dStroke, w, h);
        o.setDashed(dashed);
        o.setFilled(filled);
        return o;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Professional dark background for the container
        g2.setColor(new Color(33, 33, 33));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Apply Transfrom (Translation then Scale)
        g2.translate(offsetX, offsetY);
        g2.scale(zoomScale, zoomScale);

        // Clip drawing to workspace sheet
        g2.setClip(0, 0, sheetWidth, sheetHeight);

        // Drawing workspace (Infinite feel, but we can treat a specific area as the
        // "sheet")
        // For this design, let's assume a default large sheet of 2000x2000
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, sheetWidth, sheetHeight);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, null);
        }

        for (Shape s : shapes) {
            s.draw(g2);
        }

        if (previewShape != null) {
            previewShape.draw(g2);
        }
    }

    public void zoomIn() {
        zoomAt(getWidth() / 2, getHeight() / 2, 1.1);
    }

    public void zoomOut() {
        zoomAt(getWidth() / 2, getHeight() / 2, 1.0 / 1.1);
    }

    public void resetZoom() {
        zoomScale = 1.0;
        offsetX = 20; // Slight padding
        offsetY = 20;
        repaint();
    }

    private void zoomAt(int pivotX, int pivotY, double factor) {
        // Content-based zoom: keep the point under the mouse stable
        double mouseCanvasX = (pivotX - offsetX) / zoomScale;
        double mouseCanvasY = (pivotY - offsetY) / zoomScale;

        zoomScale *= factor;
        zoomScale = Math.max(0.01, Math.min(zoomScale, 100.0));

        offsetX = pivotX - (mouseCanvasX * zoomScale);
        offsetY = pivotY - (mouseCanvasY * zoomScale);

        repaint();
    }

    // Setters
    public void setTool(int tool) {
        this.tool = tool;
        previewShape = null;
        navigateHand.setEnabled(tool == HAND);
    }

    @Override
    public void pan(double deltaX, double deltaY) {
        offsetX += deltaX;
        offsetY += deltaY;
    }

    @Override
    public void repaintView() {
        repaint();
    }

    // Setters
    public void setCurrentColor(Color c) {
        this.currentColor = c;
    }

    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public void setStrokeSize(int size) {
        this.baseStrokeSize = size;
    }

    // Undo the last shape or clear
    public void undo() {
        // If the last action was Clear, undo should restore everything
        if (canUndoClear) {
            shapes = new ArrayList<>(savedShapesBeforeClear);
            backgroundImage = savedBackgroundBeforeClear;

            previewShape = null;
            currentPath = null;

            canUndoClear = false;
            repaint();
            return;
        }

        // Normal undo: remove last shape
        if (!shapes.isEmpty()) {
            shapes.remove(shapes.size() - 1);
            previewShape = null;
            repaint();
        }
    }

    // Clear the canvas
    public void clear() {
        // save current state so Undo can restore it
        savedShapesBeforeClear = new ArrayList<>(shapes);
        savedBackgroundBeforeClear = backgroundImage;
        canUndoClear = true;

        // clear current state
        shapes.clear();
        previewShape = null;
        currentPath = null;
        backgroundImage = null;

        repaint();
    }

    // Open an image file
    public void openImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                backgroundImage = ImageIO.read(chooser.getSelectedFile());
                if (backgroundImage != null) {
                    sheetWidth = backgroundImage.getWidth();
                    sheetHeight = backgroundImage.getHeight();
                    resetZoom();
                }
                repaint();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage());
            }
        }
    }

    public void saveImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getAbsolutePath() + ".png");
            }
            try {
                // Determine bounds of content or save a fixed area
                // For simplicity, let's save the workspace
                BufferedImage image = new BufferedImage(sheetWidth, sheetHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = image.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, sheetWidth, sheetHeight);

                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, null);
                }
                for (Shape s : shapes) {
                    s.draw(g2);
                }
                g2.dispose();
                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(this, "Image saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage());
            }
        }
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public double getZoomScale() {
        return zoomScale;
    }
}
