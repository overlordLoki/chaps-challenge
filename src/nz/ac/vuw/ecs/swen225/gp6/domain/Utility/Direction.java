package nz.ac.vuw.ecs.swen225.gp6.domain.Utility;

import java.util.Arrays;

/**
 * Direction enum class that stores all possible moving directions for actors in this game.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
public enum Direction {
  Up(0, -1, 'U'),
  Down(0, 1, 'D'),
  Right(1, 0, 'R'),
  Left(-1, 0, 'L'),
  None(0, 0, ' ');

  public final int x;
  public final int y;
  public final char symbol;

  /**
   * x and y difference from location of hero.
   *
   * @param x      x difference
   * @param y      y difference
   * @param symbol symbol for move(U,D,L,R,' ')
   */
  Direction(int x, int y, char symbol) {
    this.x = x;
    this.y = y;
    this.symbol = symbol;
  }

  /**
   * gets a direction by a given symbol.
   *
   * @param symbol symbol to get direction equivalent of
   * @return direction equivalent of given symbol
   * @throws IllegalArgumentException when given symbol is invalid
   */
  public static Direction getDirFromSymbol(char symbol) {
    return Arrays.stream(Direction.values())
        .filter(p -> p.symbol == symbol)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }

  /**
   * transforms a given location with this direction.
   *
   * @param l loc to transform
   */
  public Loc transformLoc(Loc l) {
    return new Loc(l.x() + x, l.y() + y);
  }

}
