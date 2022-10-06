package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.util.List;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.GridLayout;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.renderer.*;
/**
 *  @author Loki
 */
public class InventoryPanel extends JPanel{
    //maze
    private Domain maze;
    /**
     * Constructor. Takes a maze as parameters.
     * @param maze
     */
    public InventoryPanel(Domain Maze, boolean isGamePanel,MazeRenderer mazeRenderer) {
        this.maze = Maze;
        //use grid layout based on boolean. gamePanel is 4,2 and inventoryPanel is 1x9
        this.setLayout(isGamePanel ? new GridLayout(4,2): new GridLayout(1,9));
        for(int i = 0; i < 8; i++) {
            int slotNum = i;
            this.add(new JPanel(){
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(mazeRenderer.getImage("empty_tile"), 0, 0, getWidth(),getHeight(),null);
                    int size = Math.min(getWidth(), getHeight());
                    List<Tile> inventory = maze.getInventory();
                    if (slotNum >= inventory.size()) return;
                   g.drawImage(mazeRenderer.getImage(inventory.get(slotNum)), (getWidth()-size)/2, (getHeight()-size)/2, size,size,null);
                }
            });
        }
    }
    /**
     * set the maze to be rendered
     * @param maze
     */
    public void setMaze(Domain maze) {this.maze = maze;}
}
