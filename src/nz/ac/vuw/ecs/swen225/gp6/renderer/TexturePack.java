package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;

/**
 * This class is for holding all the images used in the game.
 *
 * @author loki
 */
public class TexturePack {

  //--------------------------------------------------------fields------------------------------------------------------//
  private final String texturePackName;
  private final Font titleFont;
  private final Font subtitleFont;
  private final Font textFont;
  private final Color colorDefault = Color.BLACK;
  private final Color colorHover;
  private final Color colorSelected;

//----------------------------------------------------------getters---------------------------------------------//

  /**
   * The Constructor.
   *
   * @param name          the name of the texture pack
   * @param title         the title font
   * @param subtitle      the subtitle font
   * @param text          the text font
   * @param colorHover    the color of the hover
   * @param colorSelected the color of the selected
   */
  TexturePack(String name, Font title, Font subtitle, Font text, Color colorHover,
      Color colorSelected) {
    this.texturePackName = name;
    this.titleFont = title;
    this.subtitleFont = subtitle;
    this.textFont = text;
    this.colorHover = colorHover;
    this.colorSelected = colorSelected;
  }

  /**
   * get name.
   *
   * @return String
   */
  public String getName() {
    return texturePackName;
  }

  /**
   * get titleFont.
   *
   * @return font
   */
  public Font getTitleFont() {
    return titleFont;
  }

  /**
   * get subtitleFont.
   *
   * @return font
   */
  public Font getSubtitleFont() {
    return subtitleFont;
  }

  /**
   * get textFont.
   *
   * @return font
   */
  public Font getTextFont() {
    return textFont;
  }

  /**
   * get colorDefault.
   *
   * @return color
   */
  public Color getColorDefault() {
    return colorDefault;
  }

  /**
   * get colorHover.
   *
   * @return color
   */
  public Color getColorHover() {
    return colorHover;
  }

//------------------------------------------------constructor---------------------------------------------------//

  /**
   * get colorSelected.
   *
   * @return color
   */
  public Color getColorSelected() {
    return colorSelected;
  }

//--------------------------------------------------methods------------------------------------------------------//

  /**
   * get image enum.
   *
   * @param tile the tile
   * @return the BufferedImage of the tile
   */
  public BufferedImage getImage(Tile tile) {
    return Images.getImage(tile);
  }

  /**
   * get image from string.
   *
   * @param imgName the name of the image
   * @return the BufferedImage of the image
   */
  public BufferedImage getImage(String imgName) {
    return Images.getImage(imgName);
  }
//--------------------------------------------------Images enum---------------------------------------------------------//

  /**
   * This enum is for holding all the images used in the game.
   */
  public enum Images {

    /**
     * The background image for the game.
     */
    Background("background"),

    /**
     * The image for the repeatable pattern background.
     */
    Pattern("pattern"),

    /**
     * The image for the repeatable pattern background.
     */
    Pattern_2("pattern2"),

    /**
     * The image for the floor.
     */
    Floor("floor"),

    /**
     * The image for the hero.
     */
    Hero("hero"),

    /**
     * The image for the enemy.
     */
    Enemy("enemy"),

    /**
     * The image for the coin.
     */
    Coin("coin"),

    /**
     * The image for the blue key.
     */
    BlueKey("blueKey"),

    /**
     * The image for the green key.
     */
    GreenKey("greenKey"),

    /**
     * The image for the orange key.
     */
    OrangeKey("orangeKey"),

    /**
     * The image for the yellow key.
     */
    YellowKey("yellowKey"),

    /**
     * The image for the empty tile.
     */
    Empty_tile("empty_tile"),

    /**
     * The image for the wall.
     */
    Wall("wall_tile"),

    /**
     * The image for the blueLock.
     */
    BlueLock("blueLock"),

    /**
     * The image for the greenLock.
     */
    GreenLock("greenLock"),

    /**
     * The image for the orangeLock.
     */
    OrangeLock("orangeLock"),

    /**
     * The image for the yellowLock.
     */
    YellowLock("yellowLock"),

    /**
     * The image for the exit.
     */
    Exit("exitDoor"),

    /**
     * The image for the win screen.
     */
    WinScreen("winScreen"),

    /**
     * The image for the loose screen.
     */
    LoseScreen("loseScreen"),

    /**
     * The image for the help tile.
     */
    InfoTile("help"),

    /**
     * The image for the hero back.
     */
    HeroBack("heroBack"),

    /**
     * The image for the hero front.
     */
    HeroFront("heroFront"),

    /**
     * The image for the hero left.
     */
    HeroLeft("heroSide"),

    /**
     * The image for the hero right.
     */
    HeroRight("hero"),

    /**
     * The image for the pop-up
     */
    PopUp("popUp");

    //name of the image
    private String name;
    //the image we store in ram.
    private BufferedImage img;

    /**
     * Constructor for the enum, loads the image and keeps it as a variable, so we don't need to
     * reload the file from disk everytime we redraw
     *
     * @param path the path of the image
     */
    Images(String path) {
      this.img = loadImg(path);
    }

    /**
     * get the image.
     *
     * @param img the image enum to extract the image from
     * @return BufferedImage
     */
    public static BufferedImage getImage(Images img) {
      return img.getImg();
    }

    /**
     * get the image for the Tile provided.
     *
     * @param tile the tile to extract the image from
     * @return BufferedImage
     */
    public static BufferedImage getImage(Tile tile) {
      return switch (tile.type()) {
        case Floor -> getImage(Empty_tile);
        case Empty -> Images.Empty_tile.getImg();
        case Wall -> Images.Wall.getImg();
        case BlueKey -> Images.BlueKey.getImg();
        case GreenKey -> Images.GreenKey.getImg();
        case YellowKey -> Images.YellowKey.getImg();
        case OrangeKey -> Images.OrangeKey.getImg();
        case BlueLock -> Images.BlueLock.getImg();
        case GreenLock -> Images.GreenLock.getImg();
        case YellowLock -> Images.YellowLock.getImg();
        case OrangeLock -> Images.OrangeLock.getImg();
        case ExitDoor, ExitDoorOpen -> Images.Exit.getImg();
        case Coin -> Images.Coin.getImg();
        case Periphery -> Images.Pattern.getImg();
        case Info -> Images.InfoTile.getImg();
        default -> Images.loadCustom(tile.info().getImageName());
      };
    }

    /**
     * get the image for the String provided.
     *
     * @param imgName the name of the image to extract the image from
     * @return BufferedImage
     */
    public static BufferedImage getImage(String imgName) {
      for (Images img : Images.values()) {
        if (img.getName().equals(imgName)) {
          return img.getImg();
        }
      }
      System.out.println("Image not found: " + imgName);
      return null;
    }

    /**
     * load the image from the file.
     *
     * @param path the path of the image
     * @return BufferedImage
     */
    public static BufferedImage loadCustom(String path) {
      for (Images img : Images.values()) {
        if (img.getName().equals(path)) {
          return img.getImg();
        }
      }
      try {
        return ImageIO.read(new File("res/customTextures/" + path + ".png"));
      } catch (IOException e) {
        try {
          return ImageIO.read(new File("res/customTextures/default.png"));
        } catch (IOException | IllegalArgumentException ex) {
          System.out.println("Error loading image: " + path);
          return null;
        }
      }
    }

    /**
     * reload all the images and caches them when changing texture packs.
     */
    public static void reloadAllTexturePack() {
      for (Images i : Images.values()) {
        i.img = i.loadImg(i.getName());
      }
    }

    /**
     * get the name.
     *
     * @return String
     */
    public String getName() {
      return name;
    }

    /**
     * get the image that is cashed.
     *
     * @return BufferedImage
     */
    public BufferedImage getImg() {
      return img;
    }

    /**
     * load the image from the disk.
     *
     * @param imageName the name of the image
     * @return BufferedImage
     */
    public BufferedImage loadImg(String imageName) {
      this.name = imageName;
      try {
        File file = new File(
            "res/texturesPacks/" + MazeRenderer.getTexturePack().getName() + "/" + imageName
                + ".png");
        return ImageIO.read(file);
      } catch (IOException e) {
        try {
          File file = new File("res/defaultTextures/" + imageName + ".png");
          return ImageIO.read(file);
        } catch (IOException ex) {
          System.out.println("Error loading image: " + imageName);
          return null;
        }

      }
    }

  }

}
