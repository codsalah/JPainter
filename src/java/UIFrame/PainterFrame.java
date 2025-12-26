package UIFrame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import Utils.ShortcutManager;

public class PainterFrame extends JFrame {

    private static final int CONFIGURABLE_ICON_SIZE = 30;
    private static final int CONFIGURABLE_LABEL_TEXT_SIZE = 15;

    public PainterFrame() {
        super("Painter App"); 

        final Painter canvas = new Painter();

        // ===== Tool Buttons =====
        JButton brushBtn = createIconButton("Brush", "icons8-brush-96.png");
        JButton eraserBtn = createIconButton("Eraser", "icons8-eraser-96.png");
        JButton lineBtn = createIconButton("Line", "icons8-line-96.png");
        JButton rectBtn = createIconButton("Rectangle", "icons8-square-96.png");
        JButton ovalBtn = createIconButton("Oval", "icons8-oval-48.png");
        JButton textBtn = createIconButton("Text", "icons8-text-96.png");

        // Set tool on button click (Lambda expression)
        brushBtn.addActionListener(e -> canvas.setTool(Painter.BRUSH));
        eraserBtn.addActionListener(e -> canvas.setTool(Painter.ERASER));
        lineBtn.addActionListener(e -> canvas.setTool(Painter.LINE));
        rectBtn.addActionListener(e -> canvas.setTool(Painter.RECT));
        ovalBtn.addActionListener(e -> canvas.setTool(Painter.OVAL));
        textBtn.addActionListener(e -> canvas.setTool(Painter.TEXT));

        // hand tool
        JButton handBtn = createIconButton("Hand", "icons8-hand-96.png");
        handBtn.addActionListener(e -> canvas.setTool(Painter.HAND));

        // undo button
        JButton undoBtn = createIconButton("Undo", "icons8-undo-96.png");
        undoBtn.addActionListener(e -> canvas.undo());

        // redo button
        JButton redoBtn = createIconButton("Redo", "icons8-undo-96.png");
        // Flip the undo icon to create a redo icon
        if (redoBtn.getIcon() != null) {
            redoBtn.setIcon(flipIcon((ImageIcon) redoBtn.getIcon()));
        }
        redoBtn.addActionListener(e -> canvas.redo());

        // clear button
        // clear button
        JButton clearBtn = createIconButton("Clear", "icons8-clear-96.png");
        clearBtn.addActionListener(e -> canvas.clear());

        // open button
        // open button
        JButton openBtn = createIconButton("Open", "icons8-open-48.png");
        openBtn.addActionListener(e -> canvas.openImage());

        // save button
        // save button
        JButton saveBtn = createIconButton("Save (PNG)", "icons8-save-961.png");
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
            if (s.equals("Bold")) {
                canvas.setTextFontStyle(Font.BOLD);
            } else if (s.equals("Italic")) {
                canvas.setTextFontStyle(Font.ITALIC);
            } else {
                canvas.setTextFontStyle(Font.PLAIN);
            }
        });

        // Zoom controls
        JButton zoomInBtn = new JButton("+");
        JButton zoomOutBtn = new JButton("-");
        JButton zoomResetBtn = new JButton("Reset View");
        JLabel zoomDisplay = new JLabel("Zoom: 100%");
        configureComponentFont(zoomDisplay); // Only for the label text

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
        row1.add(createLabel("Files:"));
        row1.add(openBtn);
        row1.add(saveBtn);
        row1.add(createLabel("   Tools:"));
        row1.add(brushBtn);
        row1.add(eraserBtn);
        row1.add(lineBtn);
        row1.add(rectBtn);
        row1.add(ovalBtn);
        row1.add(textBtn);
        row1.add(handBtn);

        row1.add(createLabel("   Options:"));
        JCheckBox dashedCheck = new JCheckBox("Dashed");
        JCheckBox filledCheck = new JCheckBox("Filled");
        configureComponentFont(dashedCheck);
        configureComponentFont(filledCheck);
        dashedCheck.addActionListener(e -> canvas.setDashed(dashedCheck.isSelected()));
        filledCheck.addActionListener(e -> canvas.setFilled(filledCheck.isSelected()));
        row1.add(dashedCheck);
        row1.add(filledCheck);

        row1.add(createLabel("   "));
        row1.add(undoBtn);
        row1.add(redoBtn);
        row1.add(clearBtn);

        // Row two: Colors, Size, View
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(createLabel("Colors:"));
        row2.add(colorPanel);
        row2.add(createLabel("   Size:"));
        row2.add(sizeLabel);
        row2.add(sizeSlider);

        row2.add(createLabel("   Font:"));
        row2.add(fontBox);
        row2.add(createLabel("Size:"));
        row2.add(fontSizeSpinner);
        row2.add(createLabel("Style:"));
        row2.add(styleBox);

        row2.add(createLabel("   View:"));
        row2.add(zoomOutBtn);
        row2.add(zoomResetBtn);
        row2.add(zoomInBtn);
        row2.add(new JLabel("Zoom: 100%")); // Reverting zoomDisplay to simple label for now if needed, or configuring it manually? 
        // Actually, zoomDisplay was defined earlier. Let's just wrap it.
        // Wait, zoomDisplay is a JLabel. I should have configured it earlier or pass it to configureComponentFont.
        // Since I can't easily jump back, I will just re-add it here with configuration if I can, but I can't re-declare it.
        // I'll assume zoomDisplay variable is valid and I need to replace the `row2.add(zoomDisplay)` line if I wanted to wrap, but I can't.
        // I will just use `row2.add(zoomDisplay);` and ensure zoomDisplay was configured.
        // Looking at previous chunks, I might have missed configuring zoomDisplay explicitly in this pass if I removed it from the declaration block.
        // Let's check line 139: JLabel zoomDisplay = new JLabel("Zoom: 100%");
        // I will add a chunk to configure it there.
        row2.add(zoomDisplay);
        row2.add(createLabel("   (Use CTRL + MouseWheel to zoom)"));

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
        ShortcutManager.setup(getRootPane(), openBtn, saveBtn, brushBtn, eraserBtn, lineBtn, rectBtn, ovalBtn, textBtn, handBtn,
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

    private ImageIcon flipIcon(ImageIcon icon) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.drawImage(icon.getImage(), 0, 0, w, h, w, 0, 0, h, null);
        g2.dispose();
        return new ImageIcon(img);
    }

    private JButton createIconButton(String text) {
        return createIconButton(text, null);
    }

    private JButton createIconButton(String text, String iconFileName) {
        JButton button = new JButton();
        button.setToolTipText(text);
        configureComponentFont(button);
        
        if (iconFileName != null) {
            try {
                String iconPath = "src/icons/" + iconFileName;
                File iconFile = new File(iconPath);
                
                if (iconFile.exists()) {
                    BufferedImage img = ImageIO.read(iconFile);
                    Image scaledImg = img.getScaledInstance(CONFIGURABLE_ICON_SIZE, CONFIGURABLE_ICON_SIZE, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(scaledImg));
                    return button; 
                }
            } catch (IOException e) {
                System.err.println("Could not load icon: " + iconFileName);
            }
        }
        // Fallback or explicit text button
        button.setText(text);
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        configureComponentFont(label);
        return label;
    }

    private void configureComponentFont(JComponent component) {
        Font f = component.getFont();
        if (f == null) {
            f = new Font("SansSerif", Font.PLAIN, CONFIGURABLE_LABEL_TEXT_SIZE);
        } else {
            f = f.deriveFont((float) CONFIGURABLE_LABEL_TEXT_SIZE);
        }
        component.setFont(f);
    }

    public static void main(String[] args) {
        // Ensure UI updates match the professional feel
        SwingUtilities.invokeLater(() -> new PainterFrame());
    }
}
