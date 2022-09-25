package nz.ac.vuw.ecs.swen225.gp6.recorder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BoxLayout;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ReplayPanel extends JPanel {
    // static final File dir = new File("src\nz\ac\vuw\ecs\swen225\gp6\recorder\innerrecorder\images");
    List<BufferedImage> imgs = new ArrayList<BufferedImage>();

    // public ReplayPanel(){
    //     if(imgs.isEmpty()) {loadImages();}
    //     this.setLayout(new GridLayout(2,2));
    //     imgs.forEach(img -> {
    //         JButton label = new JButton(new ImageIcon(img));
    //         System.out.println("making " + imgs.indexOf(img) + " labels");
    //         label.addMouseListener(new MouseAdapter() {
    //             @Override
    //             public void mousePressed(MouseEvent e) {
    //                 System.out.println("clicked " + img);
    //             }
    //         });
    //         this.add(label);
    //     });
    // }

    /**
    //  * Loads all images from the images folder into the imgs list.
    //  */
    // public void loadImages(){
    //     if(!dir.isDirectory()) {throw new IllegalStateException("Not a directory");}
    //     Stream.of(dir.listFiles()).forEach(f -> {
    //         try {
    //             imgs.add(ImageIO.read(f));
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     });
    // }
}
