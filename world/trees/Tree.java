package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.LeaveBlock;
import pepse.world.Terrain;
import pepse.PepseGameManager;


import java.awt.*;
import java.util.Random;

import static pepse.PepseGameManager.*;

public class Tree {
    private static final Color BASE_TRUNK_COLOR = new Color(100, 50, 20);
    private static final int GRAVITY_LEAVES = 90;
    private GameObjectCollection myGameObjects;
    private Transition t;

    public Tree(GameObjectCollection gameObjects) {
        myGameObjects = gameObjects;

    }

    public void createInRange(int minX, int maxX) {
        Random random = new Random();
        minX = (int) (Math.floor(minX / Block.SIZE) * Block.SIZE);
        maxX = (int) (Math.floor(maxX / Block.SIZE) * Block.SIZE);

        for (int x = minX; x <= maxX; x += Block.SIZE ){
            int h = random.nextInt(Terrain.groundHeightAtHashMap.get(x));
            if (random.nextInt(40) == 1){
                for (int j = Terrain.groundHeightAtHashMap.get(x) ; j > h; j -= 30) {
                    Block treeBlock = new Block(new Vector2(x,j),new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR))) ;
                    myGameObjects.addGameObject(treeBlock, Layer.STATIC_OBJECTS + 10);
                }

                // Leaves
                int lenOfTrunk = Terrain.groundHeightAtHashMap.get(x)  - h;
                int curY = (int) (h + lenOfTrunk * 0.1);
                int counter = 0;
                while (counter <= 32){
                    Random random1 = new Random();
                    for (int k = (int) (x - lenOfTrunk * 0.3) + counter * 30; k <= (int) (x + lenOfTrunk * 0.3) - counter * 30 ; k += 30) {
                        LeaveBlock leaveBlock = new LeaveBlock(new Vector2(k,curY),new RectangleRenderable(ColorSupplier.approximateColor(new Color(50, 200, 30)))) ;
                        leaveBlock.physics().preventIntersectionsFromDirection(Vector2.ZERO);
                        new ScheduledTask(leaveBlock, (float) (random1.nextInt(100)*0.01), true,
                                () -> {
                                    leaveBlock.transition1 = leaveBlock.transition =  new Transition<Float>(
                                            leaveBlock, // the game object being changed
                                            leaveBlock.renderer()::setRenderableAngle,
                                            0f, // initial transition value
                                            45f, // final transition value
                                            Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                                            1f, // transtion fully over half a day
                                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                                            null);
                                });

                        new ScheduledTask(leaveBlock, (float) (random1.nextInt(30)), false,
                                () -> {
                                        leaveBlock.transition = new Transition<Float>(
                                                leaveBlock, // the game object being changed
                                                leaveBlock.transform()::setVelocityX,
                                                0f, // initial transition value
                                                45f, // final transition value
                                                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                                                1f, // transtion fully over half a day
                                                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                                                null);

                                    leaveBlock.setVelocity(Vector2.DOWN.mult(GRAVITY_LEAVES));
                                    leaveBlock.renderer().fadeOut(5,()->{
                                        new ScheduledTask(leaveBlock, random1.nextInt(10), false,
                                                () -> {
                                                    leaveBlock.setVelocity(Vector2.ZERO);
                                                    leaveBlock.setTopLeftCorner(leaveBlock.firstTopLeftCorner);
                                                    leaveBlock.renderer().setOpaqueness(1);
                                                    createTrasitionFalling(leaveBlock);
                                                });
                                    });
                                });

                        myGameObjects.addGameObject(leaveBlock, LEAVES_LAYER);
                        myGameObjects.layers().shouldLayersCollide(PepseGameManager.LEAVES_LAYER, GROUND_LAYER
                                , true);
                    }
                    curY -= Block.SIZE;
                    counter++;
                }
            }
        }
    }
    private void createTrasitionFalling(LeaveBlock leaveBlock){
        Random random = new Random();
        new ScheduledTask(leaveBlock, (float) (random.nextInt(30)), false,
                () -> {
                    leaveBlock.transition = new Transition<Float>(
                            leaveBlock, // the game object being changed
                            leaveBlock.transform()::setVelocityX,
                            0f, // initial transition value
                            45f, // final transition value
                            Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                            1f, // transtion fully over half a day
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                            null);

                    leaveBlock.setVelocity(Vector2.DOWN.mult(GRAVITY_LEAVES));
                    leaveBlock.renderer().fadeOut(5,()->{
                        new ScheduledTask(leaveBlock, random.nextInt(10), false,
                                () -> {
                                    leaveBlock.setVelocity(Vector2.ZERO);
                                    leaveBlock.setTopLeftCorner(leaveBlock.firstTopLeftCorner);
                                    leaveBlock.renderer().setOpaqueness(1);
                                });
                    });
                });
    }
}
