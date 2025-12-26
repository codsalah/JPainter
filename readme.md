# PainterJava - Java Swing Painter Application
A desktop drawing application built with **Java Swing**, providing an intuitive canvas for creating digital artwork with various drawing tools, shapes, and styling options.

## Project Overview

**PainterJava** (JPainter) is a cross-platform painting application that enables users to create drawings with multiple tools. 

It features zoom in/out, undo/redo functionality, and comprehensive keyboard shortcuts for power users.

### Key Highlights
- Clean, intuitive UI with icon-based toolbar
- 2000x2000 virtual canvas with zoom and pan support
- Full undo/redo stack with clear-undo capability
- Professional rendering with anti-aliasing and high-quality graphics
- File I/O for saving/loading artwork as PNG images
- Comprehensive keyboard shortcuts for all features

## Features

### Drawing Tools
- **Brush** - Freehand drawing with adjustable stroke size
- **Eraser** - Remove content with adjustable size
- **Line** - Draw straight lines
- **Rectangle** - Create rectangles with optional fill
- **Oval** - Draw circles and ellipses with optional fill
- **Text** - Add text with customizable font, size, and style
- **Freehand Control** - Draw continuous paths for artistic strokes

### Shape Styling
- **Dashed Borders** - Toggle dashed style for shape outlines
- **Filled Shapes** - Toggle fill/outline rendering for shapes
- **Adjustable Stroke Size** - 1-100 pixel stroke width (slider control)
- **Color Selection** - 9 preset colors + custom color palette

### Navigation & Utilities
- **Hand Tool** - Pan/drag the canvas for exploring large artworks
- **Zoom** - Dynamic zoom scaling (supports mouse wheel scrolling)
- **Undo/Redo** - Full operation history stack
- **Clear Sheet** - Clear all drawings with undo support
- **File Operations** - Save as PNG, Open existing PNG files

## Keyboard Shortcuts
### Tools
| Action    | Shortcut     |
| --------- | ------------ |
| Brush     | Ctrl+Shift+B |
| Eraser    | Ctrl+Shift+E |
| Line      | Ctrl+Shift+L |
| Rectangle | Ctrl+Shift+R |
| Oval      | Ctrl+Shift+V |
| Text      | Ctrl+Shift+T |
| Hand      | Ctrl+Shift+H |

### Utilities
| Action | Shortcut     |
| ------ | ------------ |
| Dashed | Ctrl+Shift+D |
| Filled | Ctrl+Shift+F |
| Undo   | Ctrl+Z       |
| Redo   | Ctrl+Y       |
| Clear  | Ctrl+Shift+C |

### Files & Colors
| Action        | Shortcut     |
| ------------- | ------------ |
| Open          | Ctrl+Shift+O |
| Save          | Ctrl+Shift+S |
| Colors 1-9    | 1-9          |
| Color Palette | 0            |


## Project Structure

```bash
PainterJava/
├── src/
│   ├── java/
│   │   ├── Main.java                 # Main entry point
│   │   ├── Shapes/                   # Shape classes
│   │   │   ├── Shape.java            
│   │   │   ├── Line.java             
│   │   │   ├── Rectangle.java        
│   │   │   ├── Oval.java             
│   │   │   ├── PathShape.java        
│   │   │   ├── TextShape.java        
│   │   │   └── NavigateHand.java     
│   │   ├── UIFrame/                  # UI Components
│   │   │   ├── PainterFrame.java     
│   │   │   └── Painter.java          
│   │   └── Utils/                    # Shortcuts
│   │       └── ShortcutManager.java  
│   └── icons/                        # PNG icon resources
│       └── icons8-*.png              
└──                               
```

## Architecture

### Core Components

#### **Painter.java** (Main Canvas)
- Extends `JPanel` and implements drawing logic
- Manages shape collection and rendering pipeline
- Handles mouse input for all drawing tools
- Implements zoom/pan transformations
- Manages undo/redo stacks
- Provides file I/O (open/save PNG)

#### **PainterFrame.java** (Main Window)
- Extends `JFrame` and sets up UI layout
- Creates and binds tool buttons
- Manages color palette and stroke size controls
- Integrates `ShortcutManager` for keyboard support

#### **Shape Hierarchy**
- `Shape` (abstract base class) - Common attributes and rendering utilities
  - `Line` - 2-point line drawing with dash support
  - `Rectangle` - Rectangular shapes with fill/dash support
  - `Oval` - Circular/oval shapes with fill/dash support
  - `PathShape` - Freehand drawing using Path2D
  - `TextShape` - Text rendering with custom fonts and sizes
  - `NavigateHand` - Canvas panning/navigation tool

#### **ShortcutManager.java**
- Sets up Swing InputMap/ActionMap for keyboard shortcuts
- Maps key combinations to UI button actions

## Setup and Installation

### Prerequisites
- **Java Development Kit (JDK)** - Version 8 or higher
- **Java Runtime Environment (JRE)** - Version 8 or higher

### Building the Project

#### Option 1: Command Line (javac)
```bash
# Navigate to the source directory
cd src/java

# Compile all Java files
javac -d ../../bin *.java Shapes/*.java UIFrame/*.java Utils/*.java

# Run the application
cd ../../bin
java Main
```

#### Option 2: Using an IDE
1. Open the project in **IntelliJ IDEA**, **Eclipse**, **VSCode**, or **NetBeans**
2. Configure the source folder: `src/java`
3. Set output directory: `bin` (done by default)
4. Ensure icon resources are accessible from the classpath
5. Build and run via IDE

## How to Use

### Starting the Application
```bash
java Main
```


### Key Libraries
- `javax.swing.*` - UI components (JFrame, JPanel, JButton, JColorChooser, etc.)
- `java.awt.*` - Graphics and event handling
- `java.awt.geom.Path2D` - Vector path drawing
- `java.io.*` - File operations
- `javax.imageio.ImageIO` - Image I/O operations

### External Resources
- **Icons** - icons8 PNG icons (30x30 pixels) for toolbar buttons



## Future work
- **Text Editing** - Text cannot be edited after creation
- **Layer System** - No layer support; all shapes on single canvas
- **Selection Tool** - Cannot select and move existing shapes
- **Clipboard** - No copy/paste functionality
- **Transformations** - No rotate/scale for individual shapes


## Contributing

Contributions are welcome! Feel free to submit issues or pull requests to improve the functionality or add new features.

### Contributors

*   [Mahmoud Shaker Github](https://github.com/MahmoudShaker15) | [LinkedIn](https://www.linkedin.com/in/mahmoud-shaker-el-rifai/)
*   [Salah Algamasy Github](https://github.com/codsalah) | [LinkedIn](https://www.linkedin.com/in/salah-algamasy/)

