import java.awt.*;
import javax.swing.*;

public class PainterFrame extends JFrame {

    public PainterFrame() {
        super("Painter App");

        final Painter canvas = new Painter();

        // ===== Tool Buttons =====
        JButton brushBtn = new JButton("Brush");
        JButton eraserBtn = new JButton("Eraser");
        JButton lineBtn = new JButton("Line");
        JButton rectBtn = new JButton("Rectangle");
        JButton ovalBtn = new JButton("Oval");

        // Set tool on button click (Lambda expression)
        brushBtn.addActionListener(e -> canvas.setTool(Painter.BRUSH));
        eraserBtn.addActionListener(e -> canvas.setTool(Painter.ERASER));
        lineBtn.addActionListener(e -> canvas.setTool(Painter.LINE));
        rectBtn.addActionListener(e -> canvas.setTool(Painter.RECT));
        ovalBtn.addActionListener(e -> canvas.setTool(Painter.OVAL));

        // hand tool
        JButton handBtn = new JButton("Hand");
        handBtn.addActionListener(e -> canvas.setTool(Painter.HAND));

        // undo button
        JButton undoBtn = new JButton("Undo");
        undoBtn.addActionListener(e -> canvas.undo());

        // clear button
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> canvas.clear());

        // open button
        JButton openBtn = new JButton("Open");
        openBtn.addActionListener(e -> canvas.openImage());

        // save button
        JButton saveBtn = new JButton("Save (PNG)");
        saveBtn.addActionListener(e -> canvas.saveImage());

        // Color buttons
        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        // Predefined colors
        Color[] defaultColors = { Color.BLACK, Color.GRAY, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.BLUE, Color.MAGENTA, Color.PINK };

        // Add color buttons to panel
        for (Color c : defaultColors) {
            JButton cBtn = createColorButton(c, canvas);
            colorPanel.add(cBtn);
        }

        // Color palette button
        JButton colorPaletteBtn = new JButton("...");
        colorPaletteBtn.setToolTipText("Color Palette");
        colorPaletteBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Select Color", canvas.getCurrentColor());
            if (c != null) {
                canvas.setCurrentColor(c);
            }
        });
        colorPanel.add(colorPaletteBtn);

        // Size slider to adjust stroke size
        JLabel sizeLabel = new JLabel("Stroke: 3");
        JSlider sizeSlider = new JSlider(1, 100, 3);
        sizeSlider.addChangeListener(e -> {
            int val = sizeSlider.getValue();
            canvas.setStrokeSize(val);
            sizeLabel.setText("Stroke: " + val);
        });

        // Zoom controls
        JButton zoomInBtn = new JButton("+");
        JButton zoomOutBtn = new JButton("-");
        JButton zoomResetBtn = new JButton("Reset View");
        JLabel zoomDisplay = new JLabel("Zoom: 100%");

        // Update timer or listener for zoom display could be added,
        // but for now we update it on button clicks
        zoomInBtn.addActionListener(e -> {
            canvas.zoomIn();
            zoomDisplay.setText("Zoom: " + (int) (canvas.getZoomScale() * 100) + "%");
        });
        zoomOutBtn.addActionListener(e -> {
            canvas.zoomOut();
            zoomDisplay.setText("Zoom: " + (int) (canvas.getZoomScale() * 100) + "%");
        });
        zoomResetBtn.addActionListener(e -> {
            canvas.resetZoom();
            zoomDisplay.setText("Zoom: 100%");
        });

        // Toolbar panels
        JPanel topToolbar = new JPanel();
        topToolbar.setLayout(new BoxLayout(topToolbar, BoxLayout.Y_AXIS));

        // Row one include (Files, Tools, and Options)
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Files:"));
        row1.add(openBtn);
        row1.add(saveBtn);
        row1.add(new JLabel("   Tools:"));
        row1.add(brushBtn);
        row1.add(eraserBtn);
        row1.add(rectBtn);
        row1.add(ovalBtn);
        row1.add(handBtn);
        row1.add(new JLabel("   "));
        row1.add(undoBtn);
        row1.add(clearBtn);

        // Row two include (Colors and Size)
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Colors:"));
        row2.add(colorPanel);
        row2.add(new JLabel("   Size:"));
        row2.add(sizeLabel);
        row2.add(sizeSlider);

        // Row three include (Options and View)
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox dashedCheck = new JCheckBox("Dashed");
        JCheckBox filledCheck = new JCheckBox("Filled");
        // set action listeners to change the dashed and filled flags
        dashedCheck.addActionListener(e -> canvas.setDashed(dashedCheck.isSelected()));
        filledCheck.addActionListener(e -> canvas.setFilled(filledCheck.isSelected()));

        row3.add(new JLabel("Options:"));
        row3.add(dashedCheck);
        row3.add(filledCheck);
        row3.add(new JLabel("   View:"));
        row3.add(zoomOutBtn);
        row3.add(zoomResetBtn);
        row3.add(zoomInBtn);
        row3.add(zoomDisplay);
        row3.add(new JLabel("   (Use CTRL + MouseWheel to zoom in/out)"));

        topToolbar.add(row1);
        topToolbar.add(row2);
        topToolbar.add(row3);

        // Frame layout
        setLayout(new BorderLayout());
        add(topToolbar, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER); // Start with the canvas in the center

        setSize(1400, 950);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Create color button and add it to the color panel
    private JButton createColorButton(Color color, Painter canvas) {
        JButton btn = new JButton();
        btn.setBackground(color);
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        // set action listener to change the current color
        btn.addActionListener(e -> canvas.setCurrentColor(color));
        return btn;
    }

    public static void main(String[] args) {
        // Ensure UI updates match the professional feel
        SwingUtilities.invokeLater(() -> new PainterFrame());
    }
}
