package nz.ac.vuw.ecs.swen225.gp6.domain.Utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Null;

/**
 * This class aids with finding clasess based on their package location and string name.
 */
public class ClassFinder {
    /**
     * This method finds a class object with a given package location and name.
     * 
     * @param packageName the package (relative to source) to look for class in, 
     *  MUST BE DOTTED NOTATION not /
     * @param className class name, should be same as file name 
     * @return class object
     * 
     * @throws ClassNotFoundException
     */
    public static Class<?> findClass(String packageName, String className) throws ClassNotFoundException {
        return Class.forName(packageName + className);
    }

    /**
     * This method finds all classes that a package contains (ones that have the same name as their file).
     * 
     * @param packageName the package (relative to source) to look for classes in,
     * MUST BE DOTTED NOTATION not /.
     * Must be in format: package.subpackage.subpackage. (dot at end)
     * @return list of class objects
     * 
     * @throws Exception
     */
    public static List<Class<?>> findAllClassesIn(String packageName) throws Exception{
        InputStream stream = ClassLoader.getSystemClassLoader()
            .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        return reader.lines()
        .filter(line -> line.endsWith(".class")) //classes will be filtered in form className.class
        .map(line -> {
            try{
                return findClass(packageName, line.substring(0, line.lastIndexOf('.'))); //remove .class
            } catch(ClassNotFoundException e){
                return Null.class.getClass();
            }}
        )
        .filter(c -> c != Null.class.getClass())
        .collect(Collectors.toList());
    }

    
}
