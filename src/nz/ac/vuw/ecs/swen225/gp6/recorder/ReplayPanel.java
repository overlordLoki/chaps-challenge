package nz.ac.vuw.ecs.swen225.gp6.recorder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;

import nz.ac.vuw.ecs.swen225.gp6.app.PanelCreator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ReplayPanel extends JPanel {
    static final File dir = new File("src\\nz\\ac\\vuw\\ecs\\swen225\\gp6\\recorder\\innerrecorder\\images");
    List<BufferedImage> imgs = new ArrayList<BufferedImage>();
    JPanel panel;

    public ReplayPanel(){
        panel = PanelCreator.createClearPanel(BoxLayout.X_AXIS);
        if(imgs.isEmpty()) {loadImages();}
        imgs.forEach(img -> {
            JLabel label = new JLabel(new ImageIcon(img));
            System.out.println("making replay labels");
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("clicked " + img);
                }
                
            });
            panel.add(label);
        });
        imgs.forEach(img -> panel.add(new JLabel(new ImageIcon(img))));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){System.out.println("mouse entered");}
            @Override
            public void mouseExited(MouseEvent e) {System.out.println("mouse exit");}
            @Override
            public void mousePressed(MouseEvent e) {System.out.println("mouse pressed");}
        });
    }

    public void loadImages(){
        if(!dir.isDirectory()) {throw new IllegalStateException("Not a directory");}
        Stream.of(dir.listFiles()).forEach(f -> {
            try {
                imgs.add(ImageIO.read(f));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
