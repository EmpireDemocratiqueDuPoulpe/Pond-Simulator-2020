package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import org.newdawn.slick.*;

public class Duck {
    // Attributes
    private static int ducksCount;
    private int id;

    private float x;
    private float y;
    private int dir;
    private boolean isMoving;

    private static Animation[] animations = new Animation[8];

    // Constructors
    public Duck() {
        ducksCount++;
        this.id = ducksCount;

        this.x = 300;
        this.y = 300;
        this.dir = 1;
        this.isMoving = false;
    }

    public Duck(float x, float y) {
        ducksCount++;
        this.id = ducksCount;

        this.x = x;
        this.y = y;
        this.dir = 1;
        this.isMoving = false;
    }

    // Getters
    public int getDucksCount()          { return ducksCount; }
    public int getId()                  { return this.id; }
    public float getX()                 { return this.x; }
    public float getY()                 { return this.y; }
    public int getDir()                 { return this.dir; }
    public boolean isMoving()           { return this.isMoving; }
    public Animation[] getAnimations()  { return animations; }

    // Setters
    public void setX(float x)                           { this.x = x; }
    public void setY(float y)                           { this.y = y; }
    public void setDir(int dir)                         { this.dir = dir; }
    public void setMoving(boolean isMoving)             { this.isMoving = isMoving; }

    // Methods
    public static void loadSprites() throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("resources/entities/duck/duck.png", 32, 32, 5);

        // Idle
        animations[0] = loadAnimation(spriteSheet, 0, 1, 0); // Up
        animations[1] = loadAnimation(spriteSheet, 0, 1, 1); // Left
        animations[2] = loadAnimation(spriteSheet, 0, 1, 2); // Bottom
        animations[3] = loadAnimation(spriteSheet, 0, 1, 3); // Right

        // Swim
        animations[4] = loadAnimation(spriteSheet, 1, 3, 0); // Up
        animations[5] = loadAnimation(spriteSheet, 1, 3, 1); // Left
        animations[6] = loadAnimation(spriteSheet, 1, 3, 2); // Bottom
        animations[7] = loadAnimation(spriteSheet, 1, 3, 3); // Right
    }

    private static Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
        Animation animation = new Animation();

        for (int x = startX; x < endX; x++) {
            animation.addFrame(spriteSheet.getSprite(x, y), 100);
        }

        return animation;
    }

    public void render(Graphics graphics) {
        // Shadow
        graphics.setColor(new Color(0, 0, 0, .5f));
        graphics.fillOval(x - 16, y - 8, 32, 16);

        // Duck
        graphics.drawAnimation(animations[this.dir + (this.isMoving ? 4 : 0)], this.x - 16, this.y - 32);
    }

    @Override
    public String toString() {
        return "Duck n°" + this.id + ":\n" +
                "x: " + this.x + "px\n" +
                "y: " + this.y + "px\n" +
                "dir: " + this.dir + "\n" +
                "isMoving: " + this.isMoving + "\n";
    }
}
