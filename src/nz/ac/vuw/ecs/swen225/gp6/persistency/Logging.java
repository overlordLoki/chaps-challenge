package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * This utility class is responsible for saving and loading logs.
 *
 * @author Benjamin Hong - 300605520
 */
public final class Logging {

  /**
   * A private constructor to prevent instantiation.
   */
  private Logging() {
  }

  /**
   * Log the string to the log file.
   *
   * @param message The string to log
   * @throws IOException If the file cannot be written to
   */
  public static void log(String message) throws IOException {
    String time = LocalDateTime.now().toString();
    FileOutputStream fileStream = new FileOutputStream("res/log.txt", true);
    OutputStreamWriter out = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
    out.write(time + ": " + message + "\n");
    out.close();
  }

  /**
   * Get a list of all the logs. The log file exists at res/logs.txt
   *
   * @return List of log entries
   * @throws IOException If the file cannot be read
   */
  public static List<Log> getLogs() throws IOException {
    List<String> lines = Files.readAllLines(Paths.get("res/log.txt"));

    return lines.stream().map(line -> {
      if (!line.contains(": ")) {
        return null;
      }
      String dateString = line.substring(0, line.indexOf(": "));
      LocalDateTime date = LocalDateTime.parse(dateString);
      String message = line.substring(line.indexOf(": ") + 1).strip();
      return new Log(date, message);
    }).filter(Objects::nonNull).toList();
  }

  /**
   * Clear the log file.
   *
   * @return Whether the operation was successful
   */
  public static boolean clearLogs() {
    File file = new File("res/log.txt");
    return file.delete();
  }

  /**
   * A record that represents a log entry. It contains the time and the message.
   */
  public record Log(LocalDateTime date, String message) {

  }

}
