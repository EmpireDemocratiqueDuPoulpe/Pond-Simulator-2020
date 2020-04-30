package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.Entity;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.EntityQueue;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.EntityTrigger;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.water_lily.WaterLily;
import de.essen_sie_ihre_toten.pond_simulator_2020.pond.PondState;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.List;

public class CaptainDuck extends BaseDuck implements EntityTrigger {
    // Attributes
    private static int ducksCount;

    private float triggerRadius;
    private EntityQueue queue;

    private static Animation[] animations = new Animation[10];

    // Constructors
    public CaptainDuck() {
        super();
        ducksCount++;

        this.triggerRadius = 200;
        this.queue = new EntityQueue(this);
    }

    public CaptainDuck(float x, float y) {
        super(x, y);
        ducksCount++;

        this.triggerRadius = 200;
        this.queue = new EntityQueue(this);
    }

    public CaptainDuck(BaseDuck duck) {
        super(duck);
        ducksCount++;

        this.triggerRadius = 200;
        this.queue = new EntityQueue(this);
    }

    // Getters
    public static int getDucksCount()   { return ducksCount; }
    public float getTriggerRadius()     { return this.triggerRadius; }
    public EntityQueue getQueue()       { return this.queue; }
    public Animation[] getAnimations()  { return animations; }

    // Setters
    public void setTriggerRadius(float triggerRadius)   { this.triggerRadius = triggerRadius; }
    public void setQueue(EntityQueue queue)             { this.queue = queue; }
    public void addToQueue(Entity entity)               { this.queue.addMember(entity); }
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
        // Update
        this.queue.addLastLeaderPos(this.x, this.y);

        super.update(map, waterLilies, delta);

        this.queue.follow();

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
        this.queue.freeAll();

        // Get ducks around;
        List<BaseDuck> ducks = PondState.getDucks();

        for (BaseDuck duck : ducks) {
            if ((duck instanceof CaptainDuck)) continue;

            if (isInsideRadius(duck.getX(), duck.getY())) {
                this.queue.addMember(duck);
            }
        }
    }
}
