# Making custom tiles
Here's how you can add custom tiles to the game without having to edit the source code.

## Writing the custom tile
The Enemy.java file is an example of a custom tile that implements the Tile interface. It's a simple enemy that moves around the map and kills the player if it touches them. You can use this as a template for your own custom tiles.

## Adding the custom tile to the game
First, we compile the custom tiles. In the src/custom/Tiles directory, run the following command:
```
javac -cp ../../ *.java
```
Now, put the entire custom directory into a .jar file. You can do this by running the following command:
```
jar -cf custom.jar ../../custom
```
Finally, put the .jar file into the levels directory and specify the custom tile in the level XML like below. 
```
<cell x="9" y="7">
    <custom class="Enemy" source="level2.jar" />
</cell>
```
Now, when you run the game, it will load the custom tiles from the .jar file, and work even if the custom folder is deleted.