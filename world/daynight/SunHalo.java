package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

import static pepse.PepseGameManager.windowDimensions;

public class SunHalo extends GameObject {


    public static class CircleCoordinate {
        // Fields to store the x and y coordinates of the point, the radius of the circle, and the center x and y coordinates
        public double x;
        public double y;
        public double radius;
        public double centerX;
        public double centerY;

        // Constructor to initialize the x and y coordinates, the radius, and the center x and y coordinates
        public CircleCoordinate(double x, double y, double radius, double centerX, double centerY) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
        }

        // Method to return the coordinate 12 degrees to the right of this coordinate
        public CircleCoordinate rotateRight(float axis) {
            // Calculate the new x and y coordinates using trigonometry, the radius, and the center coordinates
            double newX = centerX + radius * ( Math.cos(Math.toRadians(axis)));
            double newY = centerY + radius * ( Math.sin(Math.toRadians(axis)));
            // Return a new CircleCoordinate with the new x and y coordinates
            this.y = newY;
            this.x = newX + 10
            ;
            return new CircleCoordinate(newX, newY, radius, centerX, centerY);
        }
    }

    public SunHalo(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color){
        CircleCoordinate circleCoordinate = new CircleCoordinate(windowDimensions.x() / 2, 100, windowDimensions.y() - 100, windowDimensions.x() / 2, windowDimensions.y());

        GameObject haloSun = new GameObject(Vector2.ZERO,new Vector2(2000,2000),new OvalRenderable(color));
        sun.setCenter(new Vector2((float) circleCoordinate.x, (float) circleCoordinate.y));
        sun.setCenter(new Vector2((float) circleCoordinate.x, (float) circleCoordinate.y));
        haloSun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        Consumer<Float> consumer = numberAxis -> {
            circleCoordinate.rotateRight(numberAxis - 90);
            sun.setCenter(new Vector2((float) circleCoordinate.x, (float) circleCoordinate.y));
            haloSun.setCenter(new Vector2((float) circleCoordinate.x, (float) circleCoordinate.y));

        };
        new Transition<Float>(
                sun, // the game object being changed
                consumer, // the method to call
                0F, // initial transition value
                360F, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                30, // transtion fully over half a day
                Transition.TransitionType.TRANSITION_LOOP, // Choose appropriate ENUM value
                null);


        gameObjects.addGameObject(haloSun,layer);
        return haloSun;
    }
}
