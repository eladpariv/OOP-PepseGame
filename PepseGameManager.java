package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import java.awt.*;


public class PepseGameManager extends GameManager {
    public static final int LEAVES_LAYER = Layer.STATIC_OBJECTS + 11 ;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    public static final int TRUNK_LAYER = Layer.STATIC_OBJECTS + 10;
    public static final int GROUND_LAYER = Layer.STATIC_OBJECTS + 5;
    private static final int CYCLE_LENGTH = 30;
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final int CREATION_FACTOR = 5;
    private GameObjectCollection gameObjects;
    private GameObject sky;
    private GameObject night;
    private GameObject sun;
    private GameObject haloSun;

    public static Vector2 windowDimensions;
    private WindowController myWindowController;
    private Avatar avatar;
    private Tree tree;
    private Terrain terrain;
    private float lastAvatarLocation;


    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        gameObjects = this.gameObjects();
        myWindowController = windowController;
        windowDimensions = myWindowController.getWindowDimensions();

        sky = Sky.create(gameObjects, myWindowController.getWindowDimensions(), Layer.BACKGROUND);
        sky.setTag("Sky");

        terrain = new Terrain(gameObjects, Layer.STATIC_OBJECTS + 5, windowDimensions, 2);
        terrain.createInRange(0, (int) windowDimensions.x());

        night = Night.create(gameObjects, Layer.FOREGROUND, windowDimensions, CYCLE_LENGTH);
        night.setTag("Night");
        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
                15, // transtion fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null);

        sun = Sun.create(gameObjects, Layer.BACKGROUND, windowDimensions, CYCLE_LENGTH);
        sun.setTag("Sun");

        Color colorHaloSun = new Color(255, 255, 0, 20);
        haloSun = SunHalo.create(gameObjects, Layer.BACKGROUND, sun, colorHaloSun);
        haloSun.setTag("SunHalo");

        tree = new Tree(gameObjects());
        tree.createInRange(0, (int) windowDimensions.x());

        avatar = Avatar.create(gameObjects(), AVATAR_LAYER, new Vector2(windowDimensions.x() / 2, Terrain.groundHeightAtHashMap.get(0) - 60), inputListener, imageReader);

        gameObjects.addGameObject(new GameObject(Vector2.ZERO, Vector2.ZERO, null), -195);

        setCamera(new Camera(avatar, Vector2.ZERO.add(new Vector2(0, -200)),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float dimX = myWindowController.getWindowDimensions().x() - 60;
        //move right:
        if ((avatar.getCenter().x() - lastAvatarLocation) >= CREATION_FACTOR * Block.SIZE) {
            terrain.createInRange((int) (lastAvatarLocation + (dimX / 2)),
                    (int) (lastAvatarLocation + (dimX / 2) + 2 * CREATION_FACTOR * Block.SIZE));
            tree.createInRange((int) (lastAvatarLocation + (dimX / 2) + CREATION_FACTOR * Block.SIZE),
                    (int) (lastAvatarLocation + (dimX / 2) + 2 * CREATION_FACTOR * Block.SIZE));
            for (GameObject go : gameObjects()) {
                if (checkTagForDelete(go) &&
                        avatar.getCenter().x() - go.getCenter().x() >= dimX / 2) {
                    gameObjects().removeGameObject(go);
                }
            }
            lastAvatarLocation = avatar.getCenter().x();
        }

        //move left:
        else if (lastAvatarLocation - avatar.getCenter().x() >= CREATION_FACTOR * Block.SIZE) {
            terrain.createInRange((int) (lastAvatarLocation - (dimX / 2) - 2 * CREATION_FACTOR * Block.SIZE),
                    (int) (lastAvatarLocation - (dimX / 2)));
            tree.createInRange((int) (lastAvatarLocation - (dimX / 2) - 2 * CREATION_FACTOR * Block.SIZE),
                    (int) (lastAvatarLocation - (dimX / 2) - CREATION_FACTOR * Block.SIZE));
            for (GameObject go : gameObjects()) {
                if (checkTagForDelete(go) &&
                        avatar.getCenter().x() - go.getCenter().x() <= - ( dimX / 2)) {
                    gameObjects().removeGameObject(go);
                }
            }
            lastAvatarLocation = avatar.getCenter().x();
        }

        if  (avatar.getTopLeftCorner().y() > - 137 + Terrain.groundHeightAtHashMap.get((int)Math.floor(avatar.getCenter().x()/30)*30)) {
            avatar.setTopLeftCorner(new Vector2(avatar.getTopLeftCorner().x() ,-140 + Terrain.groundHeightAtHashMap.get((int)Math.floor(avatar.getCenter().x()/30)*30)));

//            avatar.setTopLeftCorner(new Vector2(avatar.getTopLeftCorner().x(),
//                    terrain.groundHeightAtHashMap.get(avatar.getTopLeftCorner().x()) - avatar.getDimensions().y() - Block.SIZE));
            avatar.setVelocity(Vector2.ZERO);
        }
    }

    private boolean checkTagForDelete(GameObject gameObject) {
        return ((!gameObject.getTag().equals("Night")) && (!gameObject.getTag().equals("Sun"))
                && (!gameObject.getTag().equals("SunHalo")) && (!gameObject.getTag().equals("Sky")));
    }




    public static void main(String[] args) {
        new PepseGameManager().run();;
    }
}
