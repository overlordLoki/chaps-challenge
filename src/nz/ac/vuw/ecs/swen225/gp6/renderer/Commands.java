package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
/**
 * This class is used to store the commands that the user can input into the log panel.
 * @author loki
 */
public final class Commands {
//--------------------------------------------------fields-------------------------------------------------------//
    private final LogPanel logPanel;
    private MazeRenderer mazeRenderer;
    private HashMap<String, Runnable> commands = new HashMap<String, Runnable>();
    private List<String> helpCommands = new ArrayList<>();
//--------------------------------------------------constructor-------------------------------------------------------//
    /**
     * Constructor for Commands
     * @param logPanel
     */
    public Commands(LogPanel logPanel) {
        this.logPanel = logPanel;
        addCommands("help", "Displays the availble commands", this::help);
        addCommands("clear", "Clears the text area", this::clear);
        addCommands("packsNumber", "Displays the number of texture packs", this::numberOfPacks);
        addCommands("setDogs", "Sets the texture pack to dogs", this::setDogs);
        addCommands("setCats", "Sets the texture pack to cats", this::setCats);
        addCommands("autoWin", "Sets the game to auto win", this::autoWin);
        addCommands("autoLose", "Sets the game to auto lose", this::autoLose);
    }
//--------------------------------------------------methods-------------------------------------------------------//
    /**
     * Adds a command to the commands HashMap
     * 
     * @param command
     * @param discription
     * @param action
     */
    public void addCommands(String command, String discription, Runnable action){
        commands.put(command, action);
        helpCommands.add(command + " - " + discription);
    }

    /**
     * invokes the command
     * @param command
     */
    public void invoke(String s){
        commands.getOrDefault(s, () -> logPanel.println("Command not found")).run();
    }
    
    /**
     * Displays the available commands
     */
    public void setRenderer(MazeRenderer mazeRenderer){
        this.mazeRenderer = mazeRenderer;
    }
//-----------------------------------------------COMANDS-----------------------------------------------------------//
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
     *
     */
    private void setDogs(){
        mazeRenderer.setTexturePack("Dogs");
    }

    /**
     * sets the texture pack to cats
     *
     */
    private void setCats(){
        mazeRenderer.setTexturePack("Cats");
    }

    /**
     * auto win
     *
     */
    private void autoWin(){
        mazeRenderer.domain.getEventListener(DomainEvent.onWin).forEach(r -> r.run());
    }
    /**
     * auto lose
     *
     */
    private void autoLose(){
        mazeRenderer.domain.getEventListener(DomainEvent.onLose).forEach(r -> r.run());
    }

    
    

}
