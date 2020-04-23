package de.essen_sie_ihre_toten.pond_simulator_2020.entities.WaterLily;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.Entity;

import org.newdawn.slick.*;

public class WaterLily extends Entity {
    // Attributes
    private float fp;

    private static Animation[] animations;

    // Constructors
    public WaterLily() {
        super();

        this.fp = 90;

        animations = new Animation[4];
    }

    public WaterLily(float x, float y) {
        super(x, y);

        this.fp = 90;

        animations = new Animation[4];
    }

    public WaterLily(float x, float y, int dir) {
        super(x, y, dir);

        this.fp = 90;

        animations = new Animation[4];
    }

    public WaterLily(float x, float y, int dir, float fp) {
        super(x, y, dir);

        this.fp = fp;

        animations = new Animation[4];
    }

    // Getters
    public float getFp() { return this.fp; }

    // Setters
    public void setFp(float fp) { this.fp = fp; }

    // Methods
    // Rendering
    public static void loadSprites() throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("resources/entities/waterLily/waterLily.png", 64, 64, 5);

        // Idle
        animations[0] = loadAnimation(spriteSheet, 0, 1, 0); // Up
        animations[1] = loadAnimation(spriteSheet, 0, 1, 1); // Left
        animations[2] = loadAnimation(spriteSheet, 0, 1, 2); // Bottom
        animations[3] = loadAnimation(spriteSheet, 0, 1, 3); // Right
    }

    public void render(Graphics graphics) {
        graphics.drawAnimation(animations[this.dir], this.x -16, this.y - 32);
    }

    // Update
    public void update() {
        if (this.fp <= 0) { Entity.addToDeathList(this.id); }
    }

    // Others
    public String toString() {
        return "Water lily n°" + this.id + ":\n" +
                "x: " + Math.round(this.x) + "px\n" +
                "y: " + Math.round(this.y) + "px\n" +
                "FP: " + Math.round(this.fp) + "\n";
    }
}
