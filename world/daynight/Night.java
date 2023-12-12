package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;

import java.awt.*;

public class Night extends GameObject {
    private static final Color BASIC_NIGHT_COLOR = Color.BLACK;

    public Night(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowDimensions, float cycleLength){
        GameObject darkness = new GameObject(Vector2.ZERO,windowDimensions, new RectangleRenderable(BASIC_NIGHT_COLOR));
        darkness.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(darkness,layer);
        return darkness;
    }
}
