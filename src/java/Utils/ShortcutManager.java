package Utils;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class ShortcutManager {

    public static void setup(
            JComponent root,
            JButton openBtn,
            JButton saveBtn,
            JButton brushBtn,
            JButton eraserBtn,
            JButton lineBtn,
            JButton rectBtn,
            JButton ovalBtn,
            JButton textBtn,
            JButton handBtn,
            JCheckBox dashedCheck,
            JCheckBox filledCheck,
            JButton undoBtn,
            JButton redoBtn,
            JButton clearBtn,
            List<JButton> colorButtons,
            JButton colorPaletteBtn) {
        
        // im is the input mapping for the root component
        // am is the action mapping for the root component
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        // bind will take (InputMap, ActionMap, int keyCode, int modifiers, String actionKey, AbstractButton button)
        // Key Stroke  ──> InputMap ──> Action name ──> ActionMap ──> Code runs
        // Files
        bindKey(im, am, KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "open", openBtn);
        bindKey(im, am, KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "save", saveBtn);

        // Tools
        bindKey(im, am, KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "brush", brushBtn);
        bindKey(im, am, KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "eraser", eraserBtn);
        bindKey(im, am, KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "line", lineBtn);
        bindKey(im, am, KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "rect", rectBtn);
        bindKey(im, am, KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "oval", ovalBtn);
        bindKey(im, am, KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "text", textBtn);
        bindKey(im, am, KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "hand", handBtn);

        // Toggles
        bindKey(im, am, KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "dashed", dashedCheck);
        bindKey(im, am, KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "filled", filledCheck);

        // Actions
        bindKey(im, am, KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK, "undo", undoBtn);
        bindKey(im, am, KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK, "redo", redoBtn);
        bindKey(im, am, KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "clear", clearBtn);

        // Colors (1-9)
        for (int i = 0; i < colorButtons.size(); i++) {
            int keyIndex = i % 9;
            int keyCode = KeyEvent.VK_1 + keyIndex;
            String actionKey = "color" + i;
            bindKey(im, am, keyCode, 0, actionKey, colorButtons.get(i));
        }

        // Color Palette
        bindKey(im, am, KeyEvent.VK_0, 0, "colorPalette", colorPaletteBtn);
    }

    // bind method is to bind a key to an action
    private static void bindKey(InputMap im, ActionMap am, int keyCode, int modifiers, String key, AbstractButton button) {
        // put the key stroke in the input map
        im.put(KeyStroke.getKeyStroke(keyCode, modifiers), key);
        
        // put the action in the action map
        am.put(key, new AbstractAction() {
            // when the action is performed, click the button
            @Override
            public void actionPerformed(ActionEvent e) {
                if (button != null) {
                    button.doClick();
                }
            }
        });
    }
}
