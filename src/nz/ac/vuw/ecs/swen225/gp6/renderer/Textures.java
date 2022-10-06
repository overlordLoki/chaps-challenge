package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.io.File;

public class Textures {
    

    public File[] loadPack() {
        File folder = new File("res/textures");
        File[] listOfFiles = folder.listFiles();
        //return number of folders in the directory
        return listOfFiles;
    }
}
