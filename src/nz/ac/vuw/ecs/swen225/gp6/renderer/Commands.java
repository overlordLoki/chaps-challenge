package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
/**
 * This class is used to store the commands that the user can input into the log panel.
 *
 * @author loki
 */
public final class Commands {
//--------------------------------------------------fields-------------------------------------------------------//
    private final LogPanel logPanel;
    private MazeRenderer mazeRenderer;
    private final HashMap<String, Runnable> commands = new HashMap<>();
    private final List<String> helpCommands = new ArrayList<>();
//--------------------------------------------------constructor-------------------------------------------------------//
    /**
     * Constructor for Commands
     *
     * @param logPanel the logPanel to be used
     */
    public Commands(LogPanel logPanel) {
        this.logPanel = logPanel;
        addCommands("help", "Displays the available commands", this::help);
        addCommands("clear", "Clears the text area", this::clear);
        addCommands("packsNumber", "Displays the number of texture packs", this::numberOfPacks);
        addCommands("setDogs", "Sets the texture pack to dogs", this::setDogs);
        addCommands("setCats", "Sets the texture pack to cats", this::setCats);
        addCommands("autoLose", "Sets the game to auto lose", this::autoLose);
    }
//--------------------------------------------------methods-------------------------------------------------------//
    /**
     * Adds a command to the commands HashMap
     * 
     * @param command the command to be added
     * @param description the description of the command
     * @param action the action to be performed
     */
    public void addCommands(String command, String description, Runnable action){
        commands.put(command, action);
        helpCommands.add(command + " - " + description);
    }

    /**
     * invokes the command
     *
     * @param s the command to be invoked
     */
    public void invoke(String s){
        commands.getOrDefault(s, () -> logPanel.println("Command not found, type \"help\" to see a list of available commands")).run();
    }
    
    /**
     * Displays the available commands
     *
     * @param mazeRenderer the renderer for the maze
     */
    public void setRenderer(MazeRenderer mazeRenderer){
        this.mazeRenderer = mazeRenderer;
    }
//-----------------------------------------------COMMANDS-----------------------------------------------------------//
    /**
     * clears the textArea
     */
    private void clear(){
        logPanel.clear();
    }

    /**
     * prints the help menu
     */
    private void help(){
        logPanel.println("//=======================================//");
        logPanel.println("Available commands:");
        helpCommands.forEach(logPanel::println);
        logPanel.println("//=======================================//");
    }

    /**
     * gets the number of textures loaded
     */
    private void numberOfPacks(){
        int num = mazeRenderer.getTexturePacks().size();
        logPanel.print("Loaded " + num + " textures");
    }

    /**
     * sets the texture pack to dogs
     */
    private void setDogs(){
        mazeRenderer.setTexturePack("Dogs");
    }

    /**
     * sets the texture pack to cats
     */
    private void setCats(){
        mazeRenderer.setTexturePack("Cats");
    }


    /**
     * auto lose
     */
    private void autoLose(){
        mazeRenderer.getDomain().getEventListener(DomainEvent.onLose).forEach(Runnable::run);
    }
}
