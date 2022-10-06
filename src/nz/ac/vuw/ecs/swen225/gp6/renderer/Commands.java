package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Commands {
    private final LogPanel logPanel;
    private HashMap<String, Runnable> commands = new HashMap<String, Runnable>();
    private List<String> helpCommands = new ArrayList<>();
    
    public Commands(LogPanel logPanel){
        this.logPanel = logPanel;
        addCommands("clear", "Clears the text area", this::clear);
        addCommands("help", "Displays the availble commands", this::help);
        addCommands("loadPack", "Reloads the texturepacks", this::loadPack);
        addCommands("test", "test", this::test);
    }

    /**
     * Adds a command to the commands HashMap
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
  //-----------------------------------------------COMANDS------------------------------------//
    /**
     * clears the textArea
     */
    public void clear(){
        logPanel.getTextArea().setText("");
    }

    /**
     * prints the help menu
     */
    public void help(){
        for(String s : helpCommands){
            logPanel.println(s);
        }
    }

    //will remove this before submission
    public void loadPack(){
        Textures textures = new Textures();
        //print number of textures loaded
        int num = textures.numberOfTexturePacks().length;
        logPanel.print("Loaded " + num + " textures");
    }

    //Testing command
    public void test(){
        logPanel.println("Running Test");
        Textures textures = new Textures();
        logPanel.println(textures.test());
    }

}
