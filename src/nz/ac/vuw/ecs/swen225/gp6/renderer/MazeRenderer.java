package nz.ac.vuw.ecs.swen225.gp6.renderer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.*;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.Timer;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Hero;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;

import static nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack.Images.reloadAllTexturepack;

/**
 * makes a jPanel that can be added to a JFrame
 * 
 * @author Loki
 */
public class MazeRenderer extends JPanel{
    //----------------------------fields-----------------------------------------------
    static final long serialVersionUID = 1L; //serialVersionUID
    private List<TexturePack> textures;
    private static TexturePack texturePack = null; //default texture pack
    private Tile[][] gameArray; //the array of tiles
    public Domain domain; //the domain controller
    public BufferedImage background; //the background image
    private int patternSize = 100; //the size of the pattern
    private int renderSize = 7; //the size of the render
    private int minRenderSize = 1, maxRenderSize = 50; //the min and max render size


//----------------------------------Constructor-----------------------------------------------------------//
    /**
     * Constructor. Takes a maze as parameters.
     * 
     * @param maze Maze to be rendered.
     */
    public MazeRenderer(Domain maze) {
        this.domain = maze;
        this.setOpaque(false);
        textures = getTexturePacksList();
        if(texturePack == null){texturePack = textures.get(0);}
        try {
            setTexturePack("Dogs");
        } catch (Exception e) {System.out.println("Dogs not found, using other texture pack");}
    }
//------------------------------------------paintComponent-------------------------------------------------//

   
    @Override
    public void paintComponent(Graphics g) {
        //call superclass to paint background
        super.paintComponent(g);
        //if changing Level draw chaing lvl animation and dont draw anything else
        if(changinglvl){changeLvl(g);return;}
        //draw the game as usal
        drawMaze(g);
        //if we drawing info draw it
        if(domain.heroIsOnInfo()){drawInfo(g);}
    }

    /**
     * draws the maze
     */
    private void drawMaze(Graphics g) {
        //get the maze array
        gameArray = domain.getGameArray();
        //viewport of the maze
        Tile[][] viewport = Viewport.getViewport(gameArray, renderSize);
        //get the width and height of the maze
        int tileWidth = (getWidth() / viewport.length);
        int tileHeight = (getHeight() / viewport[1].length);
        //loop through the maze array and paint the tiles
        for (int i = 0; i < viewport.length; i++) {
            for (int j = 0; j < viewport[1].length; j++) {
                //clear the floor
                g.drawImage(texturePack.getImage("floor"), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
                // if there is a item draw on top of the floor or a wall tile
                Tile tile = viewport[i][j];
                if(tile.type() == TileType.Floor) {continue;}
                //if hero tile then draw the hero depending on the direction
                if(tile.type() == TileType.Hero) {
                    Hero hero = (Hero) tile;
                    BufferedImage img = getHeroImg(hero.dir());
                    g.drawImage(img, i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
                }else{
                    g.drawImage(texturePack.getImage(tile), i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
                }
            }
        }
    }


    /**
     * get the hero image depending on the direction
     * @param dir
     * @return
     */
    private BufferedImage getHeroImg(Direction dir) {
        switch(dir) {
            case Up:
                return texturePack.getImage("heroBack");
            case Down:
                return texturePack.getImage("heroFront");
            case Left:
                return texturePack.getImage("heroSide");
            case Right:
                return texturePack.getImage("hero");
            default: return null;
        }
    }


//-----------------------------------------load in texture packs----------------------------------------------//
    

    /**
     * get the texture pack list
     * @return
     */
    public List<TexturePack> getTexturePacksList() {
        File texturePackRoot = new File("res/texturesPacks");
        File[] listOfFiles = texturePackRoot.listFiles();
        List<TexturePack> textures1 = new ArrayList<>();
        //for each texture in the folder add it to the list
        for (File file : listOfFiles) {
            // if(!checkFolder(file)){
            //     System.out.println("not a folder: " + file.getName());
            //     continue;
            // }
            //check if settings file exists
            if(!checkSettingsFile(file)){
                TexturePack tp = new TexturePack(file.getName(), 
                                                new Font("Arial", Font.BOLD, 80),
                                                new Font("Arial", Font.BOLD, 40), 
                                                new Font("Arial", Font.BOLD, 30),
                                                Color.ORANGE, Color.RED);
                textures1.add(tp);
                continue;
            }else{
                try{
                    String settings = readSettings(file);
                    String[] settingsArray = settings.split(",");
                    Font tile = new Font(settingsArray[0], Font.decode(settingsArray[1]).getStyle(), Integer.parseInt(settingsArray[2]));
                    Font subtitle = new Font(settingsArray[3], Font.decode(settingsArray[4]).getStyle(), Integer.parseInt(settingsArray[5]));
                    Font text = new Font(settingsArray[6], Font.decode(settingsArray[7]).getStyle(), Integer.parseInt(settingsArray[8]));
                    TexturePack tp = new TexturePack(file.getName(),tile, subtitle,text,Color.RED, Color.ORANGE);
                    textures1.add(tp);
                }catch(Exception e) {System.out.println("reading setting failed!");}
            }
        }
        //exit safely if no texture packs found. 
        if(textures1.size() == 0) {
            System.out.println("no texture packs found");
            System.exit(0);
        }
        return textures1;
    }

    //check if folder has all png files required for a texture pack
    /**
     * check if folder has all png files required for a texture pack
     * @param folder
     * @return
     */
    private boolean checkFolder(File folder) {
        File[] listOfFiles = folder.listFiles();
        List<String> files = new ArrayList<>();
        for (File file : listOfFiles) {
            files.add(file.getName());
        }
        String[] mustContain = {"background","blueKey","blueLock","coin","empty_tile","enemy","exitDoor","floor","greenKey","greenLock",
                        "hero","heroBack","heroFront","heroSide","empty_tile","loseScreen","orangeKey",
                        "orangeLock","pattern","pattern2","wall_tile","winScreen","yellowKey","yellowLock","help"};
        for(String s : mustContain) {
            if(!files.contains(s+".png")) {
                System.out.println("missing file: " + s + " in " + folder.getName());
                return false;
            }
        }
        return true;
    }

    
    /**
     * set the current texturePack and returns the new background image
     * 
     * @param texturePack
     */
    public void setTexturePack(TexturePack texturePack) {
        MazeRenderer.texturePack = texturePack;
        patternSize = 100;
        reloadAllTexturepack();
    }


    //set texture pack given a string input
    /**
     * set the current texturePack given a string input
     * @param texturePack
     */
    public void setTexturePack(String texturePack) {
        for(TexturePack tp : textures) {
            if(tp.getName().equals(texturePack)) {
                MazeRenderer.texturePack = tp;
                patternSize = 100;
                reloadAllTexturepack();
                return;
            }
        }
        System.out.println("texture pack not found");
    }

    //read in setting from setting.txt
    /**
     * read in setting from setting.txt
     * @param texturePackFile
     * @return
     */
    private String readSettings(File texturePackFile) {
        String inputs = "";
        try {
            File settingFile = new File(texturePackFile.getPath() + "/settings.txt");
            Scanner sc = new Scanner(settingFile);
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] split = line.split(":");   
                String key = split[0].trim();
                String value = split[1].trim();
                switch(key) {
                    case "title":
                        inputs += value;
                        break;
                    case "subtitle":
                        inputs += value;
                        break;
                    case "text":
                        inputs += value;
                        break;
                    case "colorHover":
                        inputs += value;
                        break;
                    case "colorSelected":
                        inputs += value;
                        break;  
                    default:
                        System.out.println("invalid setting: " + key);
                        break;
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("settings.txt not found");
        }
        return inputs;
    }

    //checkSettingsFile
    /**
     * checkSettingsFile if it exists
     * @param folder
     * @return
     */
    private boolean checkSettingsFile(File folder) {
        File[] listOfFiles = folder.listFiles();
        List<String> files = new ArrayList<>();
        for (File file : listOfFiles) {
            files.add(file.getName());
        }
        if(!files.contains("settings.txt")) {
            return false;
        }
        return true;
    }

//----------------------------------------draw change lvl-----------------------------------------------------------//

    /**
     * run the change lvl animation
     * @param g
     */
    private void changeLvl(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString("Level " + 2, getWidth()/2 - 100, getHeight()/2);
    }

    private boolean changinglvl = false; //if the level is changing
    /**
     * change the level
     * @param observer
     */
    public void changeLevel(Runnable observer){
        changinglvl = true;
        // run sequence here
        AtomicInteger cutsceneFrames = new AtomicInteger();
        Timer timer = new Timer(10, unused->{
            this.repaint();
            if (cutsceneFrames.getAndIncrement() >= 50){ // max 50 frames
                changinglvl = false;
                observer.run();
                ((Timer)unused.getSource()).stop();
            }
        });
        timer.start();
    }

//---------------------------------------drawing info----------------------------------------------------------//

    /**
     * draw the info of the game
     * @param g
     */
    private void drawInfo(Graphics g) {
        //draw the box
        BufferedImage box = getImage("popUp");
        g.drawImage(box, 250, 100, 200, 200, null);
        //draw the message in the box
        String message = domain.getInfoHint();
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString(message, 260, 200);
    }

//--------------------------------------getters and setters----------------------------------------------------------//
    //Basic getters and setters

    /**
     * get min render size
     * @return minRenderSize
     */
    public int getMinRenderSize() {return minRenderSize;}
    /**
     * get maximum render size
     * @return maxRenderSize
     */
    public int getMaxRenderSize() {return maxRenderSize;}
    

    /**
     * get render size
     * @return int
     */
    public int getRenderSize() {return renderSize;}

    /**
     * set render size
     * @param renderSize
     */
    public void setRenderSize(int renderSize) {this.renderSize = renderSize;}

    /**
     * getter for patternSize
     * @return patternSize
     */
    public int getPatternSize() {return patternSize;}
    /**
     * get current texture pack
     * @return texturePack
     */
    public TexturePack getCurrentTexturePack(){return texturePack;}

    /**
     * get current texture pack
     * @return texturePack
     */
    public static TexturePack getTexturePack(){return texturePack;}

    /**
     * set the maze to be rendered
     * @param maze
     */
    public void setMaze(Domain maze) {this.domain = maze;}

    //get list of TexturePacks
    /**
     * get list of TexturePacks
     * @return textures
     */
    public List<TexturePack> getTexturePacks() {return textures;}

    /**
     * use next texture pack
     */
    public void useNextTexturePack(){
        int nextIndex = (textures.indexOf(texturePack) + 1 ) % textures.size();
        texturePack = textures.get(nextIndex);
        reloadAllTexturepack();
    }

    /**
     * use previous texture pack
     */
    public void usePrevTexturePack(){
        int nextIndex = (textures.indexOf(texturePack) - 1 ) % textures.size();
        if(nextIndex < 0) nextIndex = textures.size() - 1;
        texturePack = textures.get(nextIndex);
        reloadAllTexturepack();
    }

    /**
     * increase viewport distance
     */
    public void increaseViewDistance() {
        if(renderSize < maxRenderSize) {
            renderSize++;
        }
    }

    /**
     * decrease viewport distance
     */
    public void decreaseViewDistance() {
        if(renderSize > minRenderSize) {
            renderSize--;
        }
    }

    /**
     * get a image from the image provided
     * @param String
     * @return BufferedImage
     */
    public BufferedImage getImage(String imgName) {return texturePack.getImage(imgName);}

    /**
     * get a image from the image provided
     * @param Tile
     * @return BufferedImage
     */
    public BufferedImage getImage(Tile tile) {return texturePack.getImage(tile);}

}
