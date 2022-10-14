package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.util.List;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.GridLayout;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
/**
 *  @author Loki
 */
public class InventoryPanel extends JPanel{
//----------------------------------------------Fields------------------------------------------------------------------//
    private Domain maze;
//-----------------------------------------------constructor---------------------------------------------------------------//

    /**
     * Constructor. Takes a maze as parameters.
     *
     * @param Maze  the maze to be displayed
     * @param isGamePanel whether the panel is for the game or the editor
     * @param mazeRenderer the renderer for the maze
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
                    if (maze == null) return;
                    List<Tile> inventory = maze.getInventory();
                    if (slotNum >= inventory.size()) return;
                   g.drawImage(mazeRenderer.getImage(inventory.get(slotNum)), (getWidth()-size)/2, (getHeight()-size)/2, size,size,null);
                }
            });
        }
    }
//-------------------------------------------setters and getters methods----------------------------------//
    /**
     * static method to create a new inventoryPanel
     *
     * @param render the renderer for the maze
     * @return inventoryPanel
     */
    public static InventoryPanel of(MazeRenderer render) {
        return new InventoryPanel(null, true, render);
    }

    /**
     * set the maze to be rendered
     *
     * @param maze the maze to be rendered
     */
    public void setMaze(Domain maze) {this.maze = maze;}
}
