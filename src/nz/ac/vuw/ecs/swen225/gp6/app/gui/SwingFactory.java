package nz.ac.vuw.ecs.swen225.gp6.app.gui;

import nz.ac.vuw.ecs.swen225.gp6.renderer.MazeRenderer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

class SwingFactory {
    /**
     * The title sized text font.
     */
    public static final int TITLE = 1;
    /**
     * The subtitle sized text font.
     */
    public static final int SUBTITLE = 2;
    /**
     * The normal-sized text font.
     */
    public static final int TEXT = 3;

    //================================================================================================================//
    //========================================== Factory Methods =====================================================//
    //================================================================================================================//

    /**
     * Creates a panel with a clear see-through panel that can be used to group other components
     *
     * @param mgr the layout manager of the panel
     * @return a clear JPanel
     */
    public static JPanel createClearPanel(LayoutManager mgr) {
        return new JPanel() {{
            setLayout(mgr);
            setOpaque(false);
        }};
    }

    /**
     * Creates a BoxLayout panel with a clear see-through panel that can be used to group other components
     *
     * @param axis the axis to lay out components along. Can be one of:
     *             {@code BoxLayout.X_AXIS, BoxLayout.Y_AXIS,
     *             BoxLayout.LINE_AXIS} or {@code BoxLayout.PAGE_AXIS}
     * @return a clear JPanel
     */
    public static JPanel createClearPanel(int axis) {
        return new JPanel() {{
            setLayout(new BoxLayout(this, axis));
            setOpaque(false);
        }};
    }

    public static JPanel creatTransparentPanel(TexturePack.Images img, float transparency){
        return new JPanel(){{
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            SwingFactory.setSize(this, 700, 700, 600, 600, 800, 800);}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
                g2d.drawImage(img.getImg(), 0, 0, getWidth(),getHeight(),null);
            }
        };
    }

    /**
     * Creates a panel with a background image that fits the size of the frame
     *
     * @param img  the background image
     * @param axis the axis to lay out components along. Can be one of:
     *             {@code BoxLayout.X_AXIS, BoxLayout.Y_AXIS,
     *             BoxLayout.LINE_AXIS} or {@code BoxLayout.PAGE_AXIS}
     * @return a JPanel with a background image
     */
    public static JPanel createBackgroundPanel(TexturePack.Images img, int axis) {
        return new JPanel() {{
            setLayout(new BoxLayout(this, axis));}
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img.getImg(), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
    }

    /**
     * Creates a panel with a repeating image that fits the size of the frame
     *
     * @param img    the image pattern to fill the background
     * @param render the render to be used to get the size of the frame
     * @param axis the axis to lay out components along. Can be one of:
     *            {@code BoxLayout.X_AXIS, BoxLayout.Y_AXIS,
     *             BoxLayout.LINE_AXIS} or {@code BoxLayout.PAGE_AXIS}
     * @return a JPanel with a repeating pattern background image
     */
    public static JPanel createRepeatableBackgroundPanel(TexturePack.Images img, MazeRenderer render, int axis) {
        return new JPanel(){{
            setLayout(new BoxLayout(this, axis));}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int size = render.getPatternSize();
                for (int i = 0; i < this.getWidth(); i+=size) {
                    for (int j = 0; j < this.getHeight(); j+=size) {
                        g.drawImage(img.getImg(), i, j, size,size,null);
                    }
                }
            }
        };
    }


    /**
     * This method is used to create a JLabel with texture-dynamic fonts.
     *
     * @param name     the name of the label
     * @param render   the renderer object
     * @param textType size of the text, should use the constants {@code PanelCreator.TITLE},
     *                 {@code PanelCreator.SUBTITLE}, or {@code PanelCreator.TEXT} to specify
     * @param Centered true if this label should be center aligned
     * @return the JLabel
     */
    public static JLabel createLabel(String name, MazeRenderer render, int textType, boolean Centered) {
        return new JLabel(name) {{
            setForeground(render.getCurrentTexturePack().getColorDefault());
            if (Centered) setAlignmentX(CENTER_ALIGNMENT);}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                TexturePack tp = render.getCurrentTexturePack();
                setFont(switch (textType) {
                    case TITLE    -> tp.getTitleFont();
                    case SUBTITLE -> tp.getSubtitleFont();
                    default       -> tp.getTextFont();
                });
                setForeground(tp.getColorDefault());
            }
        };
    }

    /**
     * This method is used to create a JLabel with texture-dynamic fonts that automatically updates the text.
     *
     * @param display  method to invoke the information to be displayed
     * @param render   the renderer object
     * @param textType size of the text, should use the constants {@code PanelCreator.TITLE},
     *                 {@code PanelCreator.SUBTITLE}, or {@code PanelCreator.TEXT} to specify
     * @param Centered true if this label should be center aligned
     * @return the JLabel
     */
    public static JLabel createInfoLabel(Supplier<String> display, MazeRenderer render, int textType, boolean Centered) {
        return new JLabel(display.get()) {{
            if (Centered) setAlignmentX(CENTER_ALIGNMENT);
            setForeground(render.getCurrentTexturePack().getColorDefault());}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setText(display.get());
                TexturePack tp = render.getCurrentTexturePack();
                setFont(switch (textType) {
                    case TITLE    -> tp.getTitleFont();
                    case SUBTITLE -> tp.getSubtitleFont();
                    default       -> tp.getTextFont();
                });
                setForeground(tp.getColorDefault());
            }
        };
    }

    /**
     * This method is used to create a JLabel with texture-dynamic fonts with executable action upon pressed.
     *
     * @param name     the name of the label
     * @param render   the renderer object
     * @param textType size of the text, should use the constants {@code PanelCreator.TITLE},
     *                 {@code PanelCreator.SUBTITLE}, or {@code PanelCreator.TEXT} to specify
     * @param Centered true if this label should be center aligned
     * @param runnable the action to be executed when the label is pressed
     * @return the JLabel
     */
    public static JLabel createActionLabel(String name, MazeRenderer render, int textType, boolean Centered, Runnable runnable) {
        return new JLabel(name) {{
            if (Centered) setAlignmentX(CENTER_ALIGNMENT);
            setForeground(render.getCurrentTexturePack().getColorDefault());
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){setForeground(render.getCurrentTexturePack().getColorHover());}
                public void mouseExited(MouseEvent e) {setForeground(render.getCurrentTexturePack().getColorDefault());}
                public void mousePressed(MouseEvent e) {runnable.run();}
            });}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                TexturePack tp = render.getCurrentTexturePack();
                setFont(switch (textType) {
                    case TITLE    -> tp.getTitleFont();
                    case SUBTITLE -> tp.getSubtitleFont();
                    default       -> tp.getTextFont();
                });
            }
        };
    }

    /**
     * This method is used to create a JLabel with texture-dynamic fonts that automatically updates the text.
     *
     * @param display  method to invoke the information to be displayed
     * @param render   the renderer object
     * @param textType size of the text, should use the constants {@code PanelCreator.TITLE},
     *                 {@code PanelCreator.SUBTITLE}, or {@code PanelCreator.TEXT} to specify
     * @param Centered true if this label should be center aligned
     * @return the JLabel
     */
    public static JLabel createInfoActionLabel(Supplier<String> display, MazeRenderer render, int textType, boolean Centered, BooleanSupplier mouseActionGuard, Runnable runnable) {
        return new JLabel(display.get()) {{
            if (Centered) setAlignmentX(CENTER_ALIGNMENT);
            setForeground(render.getCurrentTexturePack().getColorDefault());
            setFocusable(true);
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    if (mouseActionGuard.getAsBoolean()) return;
                    setForeground(render.getCurrentTexturePack().getColorHover());
                }
                public void mouseExited(MouseEvent e) {
                    if (mouseActionGuard.getAsBoolean()) return;
                    setForeground(render.getCurrentTexturePack().getColorDefault());
                }
                public void mousePressed(MouseEvent e) {
                    if (mouseActionGuard.getAsBoolean()) return;
                    setForeground(render.getCurrentTexturePack().getColorSelected());
                    grabFocus();
                    runnable.run();
                }
            });}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setText(display.get());
                TexturePack tp = render.getCurrentTexturePack();
                setFont(switch (textType) {
                    case TITLE    -> tp.getTitleFont();
                    case SUBTITLE -> tp.getSubtitleFont();
                    default       -> tp.getTextFont();
                });
            }
        };
    }



    //================================================================================================================//
    //=========================================== Helper Method ======================================================//
    //================================================================================================================//

    /**
     * Sets the size of a component
     *
     * @param Component the component to be set
     * @param pX the preferred width of the component
     * @param pY the preferred height of the component
     * @param miX the minimum width of the component
     * @param miY the minimum height of the component
     * @param maX the maximum width of the component
     * @param maY the maximum height of the component
     */
    public static void setSize(Component Component, int pX, int pY, int miX, int miY, int maX, int maY) {
        Component.setPreferredSize(new Dimension(pX, pY));
        Component.setMinimumSize(new Dimension(miX, miY));
        Component.setMaximumSize(new Dimension(maX, maY));
    }

    /**
     * Adds all components to a panel
     *
     * @param parent the panel to be added to
     * @param components the components to be added
     */
    public static void addAll(JComponent parent, Component... components) {
        Arrays.stream(components).forEach(parent::add);
    }
}
