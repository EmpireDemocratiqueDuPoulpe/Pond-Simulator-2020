package de.essen_sie_ihre_toten.pond_simulator_2020.entities.water_lily;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.Entity;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.EntityTrigger;
import de.essen_sie_ihre_toten.pond_simulator_2020.hud.Bar;
import org.newdawn.slick.*;

public class WaterLily extends Entity implements EntityTrigger {
    // Attributes
    private float fp;
    private float triggerRadius;

    private Bar fpBar;

    private static Animation[] animations = new Animation[4];

    // Constructors
    public WaterLily() {
        super();
        this.width = 64;
        this.height = 64;

        this.fp = 90;
        this.triggerRadius = (this.width / 2) + 25;

        this.fpBar = new Bar(this.x - (this.width / 2), this.y - this.height - 10, this.width, 3, this.fp, this.fp, Bar.darkOrange, Bar.orange);
    }

    public WaterLily(float x, float y) {
        super(x, y, 64, 64);

        this.fp = 90;

        this.fpBar = new Bar(this.x - (this.width / 2), this.y - this.height - 10, this.width, 3, this.fp, this.fp, Bar.darkOrange, Bar.orange);
    }

    public WaterLily(float x, float y, int dir) {
        super(x, y, 64, 64, dir);

        this.fp = 90;

        this.fpBar = new Bar(this.x - (this.width / 2), this.y - this.height - 10, this.width, 3, this.fp, this.fp, Bar.darkOrange, Bar.orange);
    }

    public WaterLily(float x, float y, int dir, float fp) {
        super(x, y, 64, 64, dir);

        this.fp = fp;

        this.fpBar = new Bar(this.x - (this.width / 2), this.y - this.height - 10, this.width, 3, this.fp, this.fp, Bar.darkOrange, Bar.orange);
    }

    // Getters
    public float getFp()            { return this.fp; }
    public float getTriggerRadius() { return this.triggerRadius; }
    public Bar getFpBar()           { return this.fpBar; }

    // Setters
    public void setFp(float fp)                         { this.fp = fp; }
    public void setTriggerRadius(float triggerRadius)   { this.triggerRadius = triggerRadius; }
    public void setFpBar(Bar fpBar)                     { this.fpBar = fpBar; }

    // Methods
    // Rendering
    public static void loadSprites() throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("resources/entities/waterLily/waterLily.png", 64, 64, 5);

        // Idle
        animations[0] = loadAnimation(spriteSheet, 0, 2, 0, 1000); // Up
        animations[1] = loadAnimation(spriteSheet, 0, 2, 1, 1000); // Left
        animations[2] = loadAnimation(spriteSheet, 0, 2, 2, 1000); // Bottom
        animations[3] = loadAnimation(spriteSheet, 0, 2, 3, 1000); // Right
    }

    public void render(Graphics graphics) {
        graphics.drawAnimation(animations[this.dir], this.x - (this.width / 2), this.y - this.height);
    }

    @Override
    public void renderDebug(Graphics graphics) {
        // Bar
        this.fpBar.draw(graphics);
    }

    @Override
    public void renderSuperDebug(Graphics graphics) {
        super.renderSuperDebug(graphics);

        // Trigger for eating
        graphics.setColor(new Color(255, 255, 255));
        graphics.drawOval(
            this.x - this.triggerRadius,
            this.y - (this.height / 2) - this.triggerRadius,
            this.triggerRadius * 2,
            this.triggerRadius * 2
        );
    }

    // Update
    public void update() {
        if (this.fp <= 0) { Entity.addToDeathList(this.id); }
        this.fpBar.setPos(this.x - (this.width / 2), this.y - this.height - 10);
        this.fpBar.setValue(this.fp);
    }

    public boolean isInsideRadius(float x, float y) {
        return (Math.pow((x - this.x), 2) + Math.pow((y - (this.y - (this.height / 2))), 2)) <= Math.pow(this.triggerRadius, 2);
    }

    // Others
    @Override
    public String toString() {
        return "Water lily n°" + this.id + ":\n" +
                "x: " + Math.round(this.x) + "px\n" +
                "y: " + Math.round(this.y) + "px\n" +
                "FP: " + Math.round(this.fp) + "\n";
    }
}
