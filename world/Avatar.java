//package pepse.world;
//
//import com.sun.jdi.connect.spi.TransportService;
//import danogl.GameObject;
//import danogl.collisions.Collision;
//import danogl.collisions.GameObjectCollection;
//import danogl.components.GameObjectPhysics;
//import danogl.gui.ImageReader;
//import danogl.gui.UserInputListener;
//import danogl.gui.rendering.AnimationRenderable;
//import danogl.gui.rendering.Renderable;
//import danogl.util.Vector2;
//import jdk.jshell.PersistentSnippet;
//import pepse.PepseGameManager;
//
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//
//public class Avatar extends GameObject {
//
//    private static UserInputListener inputListener;
//    private static ImageReader imageReader;
//    private int step;
//    private boolean isRight;
//    private int velocityHorizontal;
//    private boolean isPressJump;
//    private float startedJumpAt;
//    private boolean isDowning;
//    private boolean isJumping;
//    private boolean isLeft;
//    private boolean isFlying;
//
//    /**
//     * Construct a new GameObject instance.
//     *
//     * @param topLeftCorner Position of the object, in window coordinates (pixels).
//     *                      Note that (0,0) is the top-left corner of the window.
//     * @param dimensions    Width and height in window coordinates.
//     * @param renderable    The renderable representing the object. Can be null, in which case
//     *                      the GameObject will not be rendered.
//     */
//    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
//        super(topLeftCorner, dimensions, renderable);
//        velocityHorizontal = 10;
//        isRight = true;
//        step = 1;
//    }
//    public static Avatar create(GameObjectCollection gameObjects,
//                                int layer, Vector2 topLeftCorner,
//                                UserInputListener inputListener,
//                                ImageReader imageReader){
//        Avatar.imageReader = imageReader;
//        Avatar.inputListener = inputListener;
//        Avatar avatar = new Avatar(topLeftCorner, new Vector2(60,80),imageReader.readImage("Run (1).png",true));
//        avatar.physics().preventIntersectionsFromDirection(Vector2.DOWN);
//        gameObjects.addGameObject(avatar,layer);
//        return avatar;
//    }
//
//    @Override
//    public void update(float deltaTime) {
//        super.update(deltaTime);
//        if (isFlying){
//            if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
//                if (getTopLeftCorner().x() < PepseGameManager.windowDimensions.x()){
//                    setVelocity(getVelocity().add(Vector2.RIGHT.mult(50)));
//                }
//                else {
//                    setVelocity(new Vector2(0,getVelocity().y()));
//                }
//            }
//            if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
//                if (getTopLeftCorner().x() > 0){
//                    setVelocity(getVelocity().add(Vector2.LEFT.mult(50)));
//                }
//                else {
//                    setVelocity(new Vector2(0,getVelocity().y()));
//                }
//            }
//            if (inputListener.isKeyPressed(KeyEvent.VK_DOWN)){
//                if (getTopLeftCorner().y() < PepseGameManager.windowDimensions.y()){
//                    setVelocity(getVelocity().add(Vector2.DOWN.mult(50)));
//                }
//                else{
//                    setVelocity(new Vector2(getVelocity().x(),0));
//                }
//            }
//            if (inputListener.isKeyPressed(KeyEvent.VK_UP)){
//                if (getTopLeftCorner().y() > 0){
//                    setVelocity(getVelocity().add(Vector2.UP.mult(50)));
//                }
//                else{
//                    setVelocity(new Vector2(getVelocity().x(),0));
//                }
//            }
////            this.setTopLeftCorner(new Vector2(getTopLeftCorner().x() ,-80 + Terrain.groundHeightAtHashMap.get((int)Math.floor(getCenter().x()/30)*30)));
//        }
//        else if (!isPressJump && !isJumping && !isDowning){
//            if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)){
//                isFlying = true;
//            }
//            else if (getVelocity().x() != 0){
//                step = Math.max(1,(step + 1) % 11);
//                this.renderer().setRenderable(imageReader.readImage("Run (" + Integer.toString(step) + ").png",true ));
//            }
//            else{
//                step = 0;
//                this.renderer().setRenderable(imageReader.readImage("Run (1).png",true ));
//            }
//
//            if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
//                if (isLeft){
//                    this.renderer().setIsFlippedHorizontally(false);
//                    isRight = true;
//                    isLeft = false;
//                    step = 0;
//                }
//                setVelocity(getVelocity().add(Vector2.RIGHT.mult(30)));
//            }
//            if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
//                if (isRight){
//                    this.renderer().setIsFlippedHorizontally(true);
//                    isRight = false;
//                    isLeft = true;
//                    step = 0;
//                }
//                setVelocity(getVelocity().add(Vector2.LEFT.mult(30)));
//            }
//            if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)){
//                isPressJump = true;
//                startedJumpAt = getTopLeftCorner().y();
//            }
//            this.setTopLeftCorner(new Vector2(getTopLeftCorner().x() ,-80 + Terrain.groundHeightAtHashMap.get((int)Math.floor(getCenter().x()/30)*30)));
//        }
//        else if (isPressJump && !isJumping) {
//            isJumping = true;
//            isPressJump = false;
//            this.setTopLeftCorner(getTopLeftCorner().add(Vector2.UP.mult(10)));
//        }
//        else if (isJumping) {
//            if (getTopLeftCorner().y() < startedJumpAt - 100){
//                isJumping = false;
//                isDowning = true;
//                setTopLeftCorner(getTopLeftCorner().add(Vector2.DOWN.mult(20)));
//            }
//            else{
//                setTopLeftCorner(getTopLeftCorner().add(Vector2.UP.mult(10)));
//            }
//        }
//        else if (isDowning) {
//            if (getTopLeftCorner().y() > - 80 + Terrain.groundHeightAtHashMap.get((int)Math.floor(getTopLeftCorner().x()/30)*30)){
//                isDowning = false;
//                this.setTopLeftCorner(new Vector2(getTopLeftCorner().x() ,-80 + Terrain.groundHeightAtHashMap.get((int)Math.floor(getCenter().x()/30)*30)));
//            }
//            else {
//                setTopLeftCorner(getTopLeftCorner().add(Vector2.DOWN.mult(20)));
//            }
//        }
//    }
//
//    @Override
//    public void onCollisionEnter(GameObject other, Collision collision) {
//        super.onCollisionEnter(other, collision);
//        setVelocity(new Vector2(getVelocity().x(),0));
//    }
//}
//
package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.event.KeyEvent;

import static pepse.PepseGameManager.GROUND_LAYER;
import static pepse.PepseGameManager.TRUNK_LAYER;

/**
 * An avatar can move around the world.
 */
public class Avatar extends GameObject{
    private static final float ZERO_VELOCITY = 0f;
    private static final float GRAVITY = 500f;
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private UserInputListener inputListener;
    AnimationRenderable flying;
    AnimationRenderable walking;
    AnimationRenderable jumping;
    AnimationRenderable landing;
    AnimationRenderable standing;
    private static float energy;
    private static final int AVATAR_WIDTH = 120;
    private static final int AVATAR_HEIGHT = 140; //by the way, the real height of Donald Trump.
    private static final float MAX_ENERGY = 100f;
    private static final float ENERGY_FACTOR = 0.5f;

    private Avatar(Vector2 topLeftCorner, Renderable renderable,
                   GameObjectCollection gameObjects, UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner,new Vector2(AVATAR_WIDTH,AVATAR_HEIGHT) , renderable);
        this.inputListener = inputListener;
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        energy = MAX_ENERGY;
        animationMaker(imageReader);
    }

    /**
     * Called on every frame of a collision with a given object, including the first.
     * @param other The collision partner.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);
        energy += ENERGY_FACTOR; //for landing on ground or tree
    }

    /**
     * this method initializing the avatar animations in the game.
     * @param imageReader Contains a single method: readImage,
     *                     which reads an image from disk. See its documentation for help.
     */
    private void animationMaker(ImageReader imageReader) {
        Renderable standing = imageReader.readImage("pepse/assets/Walk (1).png",true);
        Renderable jumping = imageReader.readImage("pepse/assets/jump.png",true);

        Renderable walking1 = imageReader.readImage("pepse/assets/Walk (1).png",true);
        Renderable walking2 = imageReader.readImage("pepse/assets/Walk (2).png",true);
        Renderable walking3 = imageReader.readImage("pepse/assets/Walk (3).png",true);
        Renderable walking4 = imageReader.readImage("pepse/assets/Walk (4).png",true);
        Renderable walking5 = imageReader.readImage("pepse/assets/Walk (5).png",true);
        Renderable walking6 = imageReader.readImage("pepse/assets/Walk (6).png",true);
        Renderable walking7 = imageReader.readImage("pepse/assets/Walk (7).png",true);

        Renderable landingTrump1 = imageReader.readImage("pepse/assets/Walk (1).png",true);
        Renderable flying2 = imageReader.readImage("pepse/assets/fly (2).png",true);
        Renderable flying3 = imageReader.readImage("pepse/assets/fly (3).png",true);

        this.flying = new AnimationRenderable(new Renderable[]{flying2,flying3}, 0.3);
        this.walking = new AnimationRenderable(new Renderable[]{walking1,walking2,walking3,walking4,walking5,walking6,walking7}, 0.1f);
        this.jumping = new AnimationRenderable(new Renderable[]{jumping}, 0.3f);
        this.landing = new AnimationRenderable(new Renderable[]{landingTrump1}, 1);
        this.standing = new AnimationRenderable(new Renderable[]{standing}, 1);
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera.
     * @param gameObjects The collection of all participating game objects.
     * @param layer The number of the layer to which the created avatar should be added.
     * @param topLeftCorner The location of the top-left corner of the created avatar.
     * @param inputListener Used for reading input from the user.
     * @param imageReader Used for reading images from disk or from within a jar.
     * @return A newly created representing the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader){
        Renderable renderable = imageReader.readImage("pepse/assets/Walk (1).png",true);
        Avatar avatar = new Avatar(topLeftCorner.subtract(new Vector2(0,AVATAR_HEIGHT)), renderable, gameObjects,
                inputListener, imageReader);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.layers().shouldLayersCollide(layer, GROUND_LAYER, true);

        gameObjects.addGameObject(new GameObject(Vector2.ZERO, Vector2.ZERO,null),TRUNK_LAYER);
        gameObjects.layers().shouldLayersCollide(layer, TRUNK_LAYER, true);
//        gameObjects.layers().shouldLayersCollide(PepseGameManager.LEAVES_LAYER, GROUND_LAYER
//                , true);
        gameObjects.addGameObject(avatar,layer);
        avatar.transform().setAccelerationY(GRAVITY);
        return avatar;
    }

    /**
     * Should be called once per frame.
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     *                 Can be used to determine a new position/velocity by multiplying this delta with
     *                 the velocity/acceleration respectively and adding to the position/velocity:
     *                 velocity += deltaTime*acceleration pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = ZERO_VELOCITY;
         if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)){
            if (energy <= 0f){
                return;
            }
            renderer().setRenderable(flying);
            transform().setVelocityY(VELOCITY_Y);
            xVel = changeVelocityX(xVel);
            energy -= ENERGY_FACTOR;

        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == ZERO_VELOCITY) {
            renderer().setRenderable(jumping);
            transform().setVelocityY(VELOCITY_Y);
            xVel = changeVelocityX(xVel);
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            xVel = -VELOCITY_X;
            renderer().setRenderable(walking);
            renderer().setIsFlippedHorizontally(true);

        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            xVel = VELOCITY_X;
            renderer().setRenderable(walking);
            renderer().setIsFlippedHorizontally(false);
        }
        //standing
        else if (getVelocity().x() == ZERO_VELOCITY && getVelocity().y() == ZERO_VELOCITY) {
            renderer().setRenderable(standing);
        }
        transform().setVelocityX(xVel);
    }


    /**
     * this method change the velocity and the direction of the avatar depends on the direction key.
     * @param xVel the velocity.
     * @return the modified velocity.
     */
    private float changeVelocityX(float xVel){
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            renderer().setIsFlippedHorizontally(true);
            return -VELOCITY_X;

        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            renderer().setIsFlippedHorizontally(false);
            return VELOCITY_X;
        }
        return xVel;
    }
}
