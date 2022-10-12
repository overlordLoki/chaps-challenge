package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public final class Commands {
    private final LogPanel logPanel;
    private MazeRenderer mazeRenderer;
    private HashMap<String, Runnable> commands = new HashMap<String, Runnable>();
    private List<String> helpCommands = new ArrayList<>();
    
    public Commands(LogPanel logPanel) {
        this.logPanel = logPanel;
        addCommands("clear", "Clears the text area", this::clear);
        addCommands("help", "Displays the availble commands", this::help);
        addCommands("test", "test", this::test);
        addCommands("packsNumber", "Displays the number of texture packs", this::numberOfPacks);
        addCommands("setDogs", "Sets the texture pack to dogs", this::setDogs);
    }

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
        if(commands.containsKey(s)){
            logPanel.println("");
            commands.get(s).run();
        }
    }
    //set themazeRenderer
    public void setRenderer(MazeRenderer mazeRenderer){
        this.mazeRenderer = mazeRenderer;
    }
  //-----------------------------------------------COMANDS------------------------------------//
    /**
     * clears the textArea
     */
    private void clear(){
        logPanel.getTextArea().setText("");
    }

    /**
     * prints the help menu
     */
    private void help(){
        for(String s : helpCommands){
            logPanel.println(s);
        }
    }

    /**
     * gets the number of textures loaded
     */
    private void numberOfPacks(){
        int num = mazeRenderer.getTexturePacks().size();
        logPanel.print("Loaded " + num + " textures");
    }

    //Testing command
    /**
     * this is for testing new functionality
     */
    private void test(){
        logPanel.println("Running Test");
        
    }

    //set texure pack to dogs
    /**
     * sets the texture pack to dogs
     *
     */
    private void setDogs(){
        mazeRenderer.setTexturePack("Dogs");
    }



}
