package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun extends GameObject {
    static float sunCycleLength;
    public Sun(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }
    public static GameObject create(
            GameObjectCollection gameObjects, int layer, Vector2 windowDimensions, float cycleLength){
        sunCycleLength = cycleLength;
        GameObject sun = new GameObject(Vector2.ZERO,new Vector2(300,300),new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun,layer);
        return sun;
    }
}
