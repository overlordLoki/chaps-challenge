package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Logging {
    public record Log(LocalDateTime date, String message) {
    }

    /**
     * Log the string to the log file
     * 
     * @param string The string to log
     */
    public static void log(String message) throws IOException {
        // get time and date string
        String time = LocalDateTime.now().toString();
        // write to file
        FileWriter out = new FileWriter("res/log.txt", true);
        out.write(time + ": " + message + "\n");
        out.close();
    }

    /**
     * Get the log file
     * 
     * @return List of log entries
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
     * Clear the log file
     */
    public static void clearLogs() throws IOException {
        FileWriter out = new FileWriter("res/log.txt", false);
        out.write("");
        out.close();
    }

}
