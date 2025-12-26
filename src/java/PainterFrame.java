import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
        JButton textBtn = new JButton("Text");

        // Set tool on button click (Lambda expression)
        brushBtn.addActionListener(e -> canvas.setTool(Painter.BRUSH));
        eraserBtn.addActionListener(e -> canvas.setTool(Painter.ERASER));
        lineBtn.addActionListener(e -> canvas.setTool(Painter.LINE));
        rectBtn.addActionListener(e -> canvas.setTool(Painter.RECT));
        ovalBtn.addActionListener(e -> canvas.setTool(Painter.OVAL));
        textBtn.addActionListener(e -> canvas.setTool(Painter.TEXT));

        // hand tool
        JButton handBtn = new JButton("Hand");
        handBtn.addActionListener(e -> canvas.setTool(Painter.HAND));

        // undo button
        JButton undoBtn = new JButton("Undo");
        undoBtn.addActionListener(e -> canvas.undo());

        // redo button
        JButton redoBtn = new JButton("Redo");
        redoBtn.addActionListener(e -> canvas.redo());

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

        List<JButton> colorButtons = new ArrayList<>();
        // Add color buttons to panel
        for (Color c : defaultColors) {
            JButton cBtn = createColorButton(c, null, canvas);
            colorPanel.add(cBtn);
            colorButtons.add(cBtn);
        }

        // Color palette button
        JButton colorPaletteBtn = createColorButton(Color.WHITE, "...", canvas);
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

        // Font family chooser
        String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        JComboBox<String> fontBox = new JComboBox<>(fontFamilies);
        fontBox.setSelectedItem("Arial");
        fontBox.setPreferredSize(new Dimension(180, 25));
        fontBox.addActionListener(e -> {
            String family = (String) fontBox.getSelectedItem();
            canvas.setTextFontFamily(family);
        });

        // Font size control
        JSpinner fontSizeSpinner = new JSpinner(new SpinnerNumberModel(18, 8, 200, 1));
        fontSizeSpinner.addChangeListener(e -> {
            int size = (int) fontSizeSpinner.getValue();
            canvas.setTextFontSize(size);
        });

        // Font style chooser
        String[] styles = {"Plain", "Bold", "Italic"};
        JComboBox<String> styleBox = new JComboBox<>(styles);
        styleBox.setSelectedItem("Plain");
        styleBox.addActionListener(e -> {
            String s = (String) styleBox.getSelectedItem();
            if (s.equals("Bold")) canvas.setTextFontStyle(Font.BOLD);
            else if (s.equals("Italic")) canvas.setTextFontStyle(Font.ITALIC);
            else canvas.setTextFontStyle(Font.PLAIN);
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

        // Row one: Files, Tools, Options, Utility
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Files:"));
        row1.add(openBtn);
        row1.add(saveBtn);
        row1.add(new JLabel("   Tools:"));
        row1.add(brushBtn);
        row1.add(eraserBtn);
        row1.add(lineBtn);
        row1.add(rectBtn);
        row1.add(ovalBtn);
        row1.add(textBtn);
        row1.add(handBtn);

        row1.add(new JLabel("   Options:"));
        JCheckBox dashedCheck = new JCheckBox("Dashed");
        JCheckBox filledCheck = new JCheckBox("Filled");
        dashedCheck.addActionListener(e -> canvas.setDashed(dashedCheck.isSelected()));
        filledCheck.addActionListener(e -> canvas.setFilled(filledCheck.isSelected()));
        row1.add(dashedCheck);
        row1.add(filledCheck);

        row1.add(new JLabel("   "));
        row1.add(undoBtn);
        row1.add(redoBtn);
        row1.add(clearBtn);

        // Row two: Colors, Size, View
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Colors:"));
        row2.add(colorPanel);
        row2.add(new JLabel("   Size:"));
        row2.add(sizeLabel);
        row2.add(sizeSlider);

        row2.add(new JLabel("   Font:"));
        row2.add(fontBox);
        row2.add(new JLabel("Size:"));
        row2.add(fontSizeSpinner);
        row2.add(new JLabel("Style:"));
        row2.add(styleBox);

        row2.add(new JLabel("   View:"));
        row2.add(zoomOutBtn);
        row2.add(zoomResetBtn);
        row2.add(zoomInBtn);
        row2.add(zoomDisplay);
        row2.add(new JLabel("   (Use CTRL + MouseWheel to zoom)"));

        topToolbar.add(row1);
        topToolbar.add(row2);

        // Frame layout
        setLayout(new BorderLayout());
        add(topToolbar, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

        setSize(1400, 950);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Setup keyboard shortcuts with visual feedback
        ShortcutManager.setup(getRootPane(), openBtn, saveBtn, brushBtn, eraserBtn, rectBtn, ovalBtn, textBtn, handBtn,
                dashedCheck, filledCheck, undoBtn, redoBtn, clearBtn, colorButtons, colorPaletteBtn);
        
        setVisible(true);
    }

    // Create circular button
    private JButton createColorButton(Color color, String text, Painter canvas) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed()) {
                    g2.setColor(color.darker());
                } else {
                    g2.setColor(color);
                }
                g2.fillOval(2, 2, getWidth() - 5, getHeight() - 5);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawOval(2, 2, getWidth() - 5, getHeight() - 5);
                g2.dispose();
            }

            @Override
            public boolean contains(int x, int y) {
                double radius = getWidth() / 2.0;
                return Math.pow(x - radius, 2) + Math.pow(y - radius, 2) <= Math.pow(radius, 2);
            }
        };
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        if (text == null || text.isEmpty()) {
            btn.addActionListener(e -> canvas.setCurrentColor(color));
        }
        return btn;
    }

    public static void main(String[] args) {
        // Ensure UI updates match the professional feel
        SwingUtilities.invokeLater(() -> new PainterFrame());
    }
}
