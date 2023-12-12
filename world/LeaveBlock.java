package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

import static pepse.world.Block.SIZE;

public class LeaveBlock extends GameObject{
    public Vector2 firstTopLeftCorner;
    public Transition<Float> transition;
    public Transition<Float> transition1;

    public LeaveBlock(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        firstTopLeftCorner = topLeftCorner;

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        this.removeComponent(transition);
        this.removeComponent(transition1);
        this.setVelocity(Vector2.ZERO);
    }
}
