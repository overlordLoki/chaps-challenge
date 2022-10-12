package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Textures {
    
    private File[] listOfFiles;

    /**
     * Constructor.
     */
    public Textures() {
        File folder = new File("res/textures");
        listOfFiles = folder.listFiles();
    }
    //get list of files. gets the number of texture packs 
    /**
     * get the number of texture packs loaded
     * @return the number of texture packs
     */
    public File[] numberOfTexturePacks() {
        //return number of folders in the directory
        return listOfFiles;
    }

    /**
     * get texture packs loaded
     * @return texture packs
     */
    public List<String> getTexturePacks() {
        File folder = new File("res/textures");
        File[] listOfFiles = folder.listFiles();
        List<String> textures = new ArrayList<>();
        //for each texture in the folder add it to the list
        for (File file : listOfFiles) {
            if (file.isFile()) {
                textures.add(file.getName());
            }
        }
        return textures;
    }

}
