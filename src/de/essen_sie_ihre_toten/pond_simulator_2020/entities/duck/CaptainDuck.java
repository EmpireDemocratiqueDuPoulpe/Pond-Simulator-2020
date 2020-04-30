package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.EntityTrigger;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.water_lily.WaterLily;
import de.essen_sie_ihre_toten.pond_simulator_2020.pond.PondState;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.List;

public class CaptainDuck extends BaseDuck implements EntityTrigger {
    // Attributes
    private static int ducksCount;

    private float triggerRadius;
    private List<Integer> queue;

    private static Animation[] animations = new Animation[10];

    // Constructors
    public CaptainDuck() {
        super();
        ducksCount++;

        this.triggerRadius = 200;
        this.queue = new ArrayList<>();
    }

    public CaptainDuck(float x, float y) {
        super(x, y);
        ducksCount++;

        this.triggerRadius = 200;
        this.queue = new ArrayList<>();
    }

    public CaptainDuck(BaseDuck duck) {
        super(duck);
        ducksCount++;

        this.triggerRadius = 200;
        this.queue = new ArrayList<>();
    }

    // Getters
    public static int getDucksCount()   { return ducksCount; }
    public float getTriggerRadius()     { return this.triggerRadius; }
    public List<Integer> getQueue()     { return this.queue; }
    public boolean isQueueEmpty()       { return this.queue.size() == 0; }
    public Animation[] getAnimations()  { return animations; }

    // Setters
    public void setTriggerRadius(float triggerRadius)   { this.triggerRadius = triggerRadius; }
    public void setQueue(List<Integer> queue)           { this.queue = queue; }
    public void addToQueue(int id)                      { if (!isInQueue(id)) this.queue.add(id); }
    public static void setDucksCount(int count)         { ducksCount = count; }

    // Methods
    // Rendering
    public static void loadSprites() throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("resources/entities/duck/captainDuck.png", 32, 32, 5);

        animations = BaseDuck.loadSprites(spriteSheet, 3);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
    }

    @Override
    public void renderSuperDebug(Graphics graphics) {
        super.renderSuperDebug(graphics);

        graphics.setColor(new Color(0, 0, 0));
        graphics.drawOval((this.x - this.triggerRadius), (this.y - this.triggerRadius), (this.triggerRadius * 2), (this.triggerRadius * 2));
    }

    // Update
    @Override
    public void update(TiledMap map, List<WaterLily> waterLilies, int delta) {
        // Prevent CaptainDuck from moving if ducks in queue aren't arrived
        this.canGetNewPos = ducksInQueueArrived();

        // Update
        super.update(map, waterLilies, delta);

        if (!this.isDead && !this.isOverweight) {
            whistle();
        }
    }

    public boolean isInsideRadius(float x, float y) {
        return (Math.pow((x - this.x), 2) + Math.pow((y - (this.y - (this.height / 2))), 2)) <= Math.pow(this.triggerRadius, 2);
    }

    public void whistle() {
        if ((Math.random() * (100)) > .01f) return;

        whistle.play(1.0f, 0.1f);
        emptyQueue();

        // Get ducks around;
        List<BaseDuck> ducks = PondState.getDucks();

        for (BaseDuck duck : ducks) {
            if (duck instanceof CaptainDuck) continue;
            if (duck.isInQueue() || isInQueue(duck.getId())) continue;

            if (isInsideRadius(duck.getX(), duck.getY())) {
                addToQueue(duck.getId());

                duck.setInQueue(true);
                duck.setCanGetNewPos(false);
                duck.stop();
            }
        }
    }

    public boolean isInQueue(int id) {
        return this.queue.contains(id);
    }

    public boolean ducksInQueueArrived() {
        boolean arrived = true;

        if (!isQueueEmpty()) {
            List<BaseDuck> ducks = PondState.getDucks();

            for (BaseDuck duck : ducks) {
                if (!(duck instanceof CaptainDuck)) {
                    if (isInQueue(duck.getId())) {
                        if (duck.isMoving()) {
                            arrived = false;
                            break;
                        }
                    }
                }
            }
        }

        return arrived;
    }

    public void queueNewTarget() {
        List<BaseDuck> ducks = PondState.getDucks();
        float spaceBetween = 30;

        // Get offset between each ducks
        float offsetX = 0;
        float offsetY = 0;

        if (this.x > this.targetX) offsetX = +((this.width / 2) + spaceBetween);
        else if (this.x < this.targetX) offsetX = -((this.width / 2) - spaceBetween);

        if (this.y > this.targetY) offsetY = -(this.height - spaceBetween);
        else if (this.y < this.targetY) offsetY = +(spaceBetween);

        // Set pos for the first duck of the queue
        float dX = this.targetX + offsetX;
        float dY = this.targetY + offsetY;

        for (BaseDuck duck : ducks) {
            if (!this.queue.contains(duck.getId())) continue;

            duck.setTarget(dX, dY);

            dX += offsetX;
            dY += offsetY;
        }
    }

    public void emptyQueue() {
        List<BaseDuck> ducks = PondState.getDucks();

        for (BaseDuck duck : ducks) {
            if (!isInQueue(duck.getId())) continue;

            duck.setInQueue(false);
            duck.setCanGetNewPos(true);
            duck.stop();
        }

        this.queue = new ArrayList<>();
    }
}
