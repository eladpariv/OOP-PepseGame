package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Terrain {
    private GameObjectCollection myGameObjects;
    private int myGroundLayer;
    private Vector2 myWindowDimensions;
    private int mySeed;
    private int groundHeightAtX0;
    public static HashMap<Integer,Integer> groundHeightAtHashMap;
    private static final int TERRAIN_DEPTH = 20;

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {

        groundHeightAtHashMap = new HashMap<>();
        myGameObjects = gameObjects;
        myGroundLayer = groundLayer;
//        groundHeightAtX0 = groundHeightAt(0); // For which reason needed this variable?
        myWindowDimensions = windowDimensions;
        mySeed = seed;
    }

    public float groundHeightAt(float x) {
        return (float) smoothNoise(x / PepseGameManager.windowDimensions.x()) * PepseGameManager.windowDimensions.y();
    }
    private double smoothNoise(float x) {
        double fractionalPartOfX = x - (int) x;
        return Math.min((1 - Math.cos(fractionalPartOfX * Math.PI * 2)) / 4,0.3);
    }

    public void createInRange(int minX, int maxX) {
        minX = (int) (Math.floor(minX / Block.SIZE) * Block.SIZE);
        maxX = (int) (Math.floor(maxX / Block.SIZE) * Block.SIZE);
        for (int x = minX; x <= maxX; x += Block.SIZE ){
            int curHeight =  (int) (Math.floor((PepseGameManager.windowDimensions.y() - groundHeightAt(x)) / Block.SIZE));
            for (int j = 1; j <= curHeight / 2; ++j) {
                int y = (int) PepseGameManager.windowDimensions.y() - j * Block.SIZE;
                groundHeightAtHashMap.put(x,y);
                Block groundBlock = new Block(new Vector2(x,y), new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                myGameObjects.addGameObject(groundBlock,myGroundLayer );
            }
        }
    }
}
