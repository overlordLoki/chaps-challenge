package nz.ac.vuw.ecs.swen225.gp6.renderer;

import static nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack.Images.reloadAllTexturePack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JPanel;
import javax.swing.Timer;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Hero;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;

/**
 * makes a jPanel that can be added to a JFrame.
 *
 * @author Loki
 */
public class MazeRenderer extends JPanel {

  //----------------------------fields------------------------------------------------------------------------------//
  static final long serialVersionUID = 1L; //serialVersionUID
  private static TexturePack texturePack = null; //this is not a bug it is a feature. see report
  private final List<TexturePack> textures;
  private Domain domain; //the domain controller
  private int patternSize = 100; //the size of the pattern
  private int renderSize = 7; //the size of the render
  private boolean changingLvl = false; //if the level is changing

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
    if (texturePack == null) {
      texturePack = textures.get(0);
    } //this is not a bug it is a feature. see report
    try {
      setTexturePack("Dogs");
    } catch (Exception e) {
      System.out.println("Dogs not found, using other texture pack");
    }
  }
//------------------------------------------paintComponent-------------------------------------------------//

  /**
   * get current texture pack.
   *
   * @return texturePack
   */
  public static TexturePack getTexturePack() {
    return texturePack;
  }

  /**
   * set the current texturePack given a string input.
   *
   * @param texturePack the texture pack to be set
   */
  public void setTexturePack(String texturePack) {
    for (TexturePack tp : textures) {
      if (tp.getName().equals(texturePack)) {
        MazeRenderer.texturePack = tp; //this is not a bug it is a feature. see report
        patternSize = 100;
        reloadAllTexturePack();
        return;
      }
    }
    System.out.println("texture pack not found");
  }

  @Override
  public void paintComponent(Graphics g) {
    //call superclass to paint background
    super.paintComponent(g);
    //if changing Level draw changing lvl animation and don't draw anything else
    if (changingLvl) {
      changeLvl(g);
      return;
    }
    //draw the game as usual
    drawMaze(g);
    //if we are drawing info draw it
    if (domain.heroIsOnInfo()) {
      drawInfo(g);
    }
  }

//-----------------------------------------load in texture packs----------------------------------------------//

  /**
   * draws the maze.
   */
  private void drawMaze(Graphics g) {
    //get the maze array
    //the array of tiles
    Tile[][] gameArray = domain.getGameArray();
    //viewport of the maze
    Tile[][] viewport = Viewport.getViewport(gameArray, renderSize);
    //get the width and height of the maze
    int tileWidth = (getWidth() / viewport.length);
    int tileHeight = (getHeight() / viewport[1].length);
    //loop through the maze array and paint the tiles
    for (int i = 0; i < viewport.length; i++) {
      for (int j = 0; j < viewport[1].length; j++) {
        //clear the floor
        g.drawImage(texturePack.getImage("floor"), i * tileWidth, j * tileHeight, tileWidth,
            tileHeight, null);
        // if there is an item draw on top of the floor or a wall tile
        Tile tile = viewport[i][j];
        if (tile.type() == TileType.Floor) {
          continue;
        }
        //if hero tile then draw the hero depending on the direction
        if (tile.type() == TileType.Hero) {
          Hero hero = (Hero) tile;
          BufferedImage img = getHeroImg(hero.dir());
          g.drawImage(img, i * tileWidth, j * tileHeight, tileWidth, tileHeight, null);
        } else {
          g.drawImage(texturePack.getImage(tile), i * tileWidth, j * tileHeight, tileWidth,
              tileHeight, null);
        }
      }
    }
  }

  /**
   * get the hero image depending on the direction.
   *
   * @param dir the direction the hero is facing
   * @return the BufferedImage of the hero
   */
  private BufferedImage getHeroImg(Direction dir) {
    return switch (dir) {
      case Up -> texturePack.getImage("heroBack");
      case Down -> texturePack.getImage("heroFront");
      case Left -> texturePack.getImage("heroSide");
      case Right -> texturePack.getImage("hero");
      default -> null;
    };
  }

  /**
   * get the texture pack list.
   *
   * @return the list of texture packs
   */
  public List<TexturePack> getTexturePacksList() {
    File texturePackRoot = new File("res/texturesPacks");
    File[] listOfFiles = texturePackRoot.listFiles();
    List<TexturePack> textures1 = new ArrayList<>();
    //for each texture in the folder add it to the list
    assert listOfFiles != null;
    for (File file : listOfFiles) {
      if (!checkSettingsFile(file)) {
        TexturePack tp = new TexturePack(file.getName(),
            new Font("Arial", Font.BOLD, 80),
            new Font("Arial", Font.BOLD, 40),
            new Font("Arial", Font.BOLD, 30),
            Color.ORANGE, Color.RED);
        textures1.add(tp);
      } else {
        try {
          String settings = readSettings(file);
          String[] settingsArray = settings.split(",");
          Font tile = new Font(settingsArray[0], Font.decode(settingsArray[1]).getStyle(),
              Integer.parseInt(settingsArray[2]));
          Font subtitle = new Font(settingsArray[3], Font.decode(settingsArray[4]).getStyle(),
              Integer.parseInt(settingsArray[5]));
          Font text = new Font(settingsArray[6], Font.decode(settingsArray[7]).getStyle(),
              Integer.parseInt(settingsArray[8]));
          TexturePack tp = new TexturePack(file.getName(), tile, subtitle, text, Color.RED,
              Color.ORANGE);
          textures1.add(tp);
        } catch (Exception e) {
          System.out.println("reading setting failed!");
        }
      }
    }
    return textures1;
  }

  /**
   * read in setting from setting.txt
   *
   * @param texturePackFile the texture pack folder
   * @return the settings as a string
   */
  private String readSettings(File texturePackFile) {
    StringBuilder inputs = new StringBuilder();
    try {
      File settingFile = new File(texturePackFile.getPath() + "/settings.txt");
      Scanner sc = new Scanner(settingFile);
      while (sc.hasNextLine()) {
        String line = sc.nextLine();
        String[] split = line.split(":");
        String key = split[0].trim();
        String value = split[1].trim();
        switch (key) {
          case "title", "subtitle", "text", "colorHover", "colorSelected" -> inputs.append(value);
          default -> System.out.println("invalid setting: " + key);
        }
      }
      sc.close();
    } catch (FileNotFoundException e) {
      System.out.println("settings.txt not found");
    }
    return inputs.toString();
  }

//----------------------------------------draw change lvl-----------------------------------------------------------//

  /**
   * checkSettingsFile if it exists.
   *
   * @param folder the folder to check
   * @return true if it exists
   */
  private boolean checkSettingsFile(File folder) {
    File[] listOfFiles = folder.listFiles();
    List<String> files = new ArrayList<>();
    assert listOfFiles != null;
    for (File file : listOfFiles) {
      files.add(file.getName());
    }
    return files.contains("settings.txt");
  }

  /**
   * run the change lvl animation.
   *
   * @param g the graphics object
   */
  private void changeLvl(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setColor(Color.WHITE);
    g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
    g.drawString("Level " + 2, getWidth() / 2 - 100, getHeight() / 2);
  }

//---------------------------------------drawing info----------------------------------------------------------//

  /**
   * change the level.
   *
   * @param observer the observer to trigger after the cutscene completes
   */
  public void changeLevel(Runnable observer) {
    changingLvl = true;
    // run sequence here
    AtomicInteger cutsceneFrames = new AtomicInteger();
    Timer timer = new Timer(10, unused -> {
      this.repaint();
      if (cutsceneFrames.getAndIncrement() >= 50) { // max 50 frames
        changingLvl = false;
        observer.run();
        ((Timer) unused.getSource()).stop();
      }
    });
    timer.start();
  }

//--------------------------------------getters and setters----------------------------------------------------------//

  /**
   * draw the info of the game.
   *
   * @param g the graphics object
   */
  private void drawInfo(Graphics g) {
    //draw the box
    BufferedImage box = getImage("popUp");
    g.drawImage(box, 250, 100, 200, 200, null);
    //draw the message in the box
    String message1 = "find the keys and dont";
    String message2 = "let the time run out";
    g.setColor(Color.BLACK);
    g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    g.drawString(message1, 260, 200);
    g.drawString(message2, 280, 230);
  }

  /**
   * get render size.
   *
   * @return int
   */
  public int getRenderSize() {
    return renderSize;
  }

  /**
   * set render size.
   *
   * @param renderSize the render size to be set
   */
  public void setRenderSize(int renderSize) {
    this.renderSize = renderSize;
  }

  /**
   * getter for patternSize.
   *
   * @return patternSize
   */
  public int getPatternSize() {
    return patternSize;
  }

  /**
   * get current texture pack.
   *
   * @return texturePack
   */
  public TexturePack getCurrentTexturePack() {
    return texturePack;
  }

  /**
   * set the maze to be rendered.
   *
   * @param maze the maze to be rendered
   */
  public void setMaze(Domain maze) {
    this.domain = maze;
  }

  //get list of TexturePacks

  /**
   * get list of TexturePacks.
   *
   * @return textures
   */
  public List<TexturePack> getTexturePacks() {
    return textures.stream().toList();
  }

  /**
   * use next texture pack.
   */
  public void useNextTexturePack() {
    int nextIndex = (textures.indexOf(texturePack) + 1) % textures.size();
    texturePack = textures.get(nextIndex);
    reloadAllTexturePack();
  }

  /**
   * use previous texture pack.
   */
  public void usePrevTexturePack() {
    int nextIndex = (textures.indexOf(texturePack) - 1) % textures.size();
      if (nextIndex < 0) {
          nextIndex = textures.size() - 1;
      }
    texturePack = textures.get(nextIndex);
    reloadAllTexturePack();
  }

  /**
   * increase viewport distance.
   */
  public void increaseViewDistance() {
    //the min and max render size
    int maxRenderSize = 50;
    if (renderSize < maxRenderSize) {
      renderSize++;
    }
  }

  /**
   * decrease viewport distance.
   */
  public void decreaseViewDistance() {
    int minRenderSize = 1;
    if (renderSize > minRenderSize) {
      renderSize--;
    }
  }

  /**
   * get a image from the image provided.
   *
   * @param imgName name of the image
   * @return BufferedImage
   */
  public BufferedImage getImage(String imgName) {
    return texturePack.getImage(imgName);
  }

  /**
   * get a image from the image provided.
   *
   * @param tile tile to get image from
   * @return BufferedImage
   */
  public BufferedImage getImage(Tile tile) {
    return texturePack.getImage(tile);
  }


  /**
   * Get the current domain.
   *
   * @return the current domain
   */
  public Domain getDomain() {
    return domain;
  }

}
