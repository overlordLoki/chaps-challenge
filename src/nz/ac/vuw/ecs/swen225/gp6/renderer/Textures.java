package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Textures {
    
    private File[] listOfFiles;

    public Textures() {
        File folder = new File("res/textures");
        listOfFiles = folder.listFiles();
    }

    public File[] numberOfTexturePacks() {
        //return number of folders in the directory
        return listOfFiles;
    }

    //make a texture pack from folder.
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

    public String test(){
        File file = listOfFiles[0];
        return file.getPath();
    }
}
