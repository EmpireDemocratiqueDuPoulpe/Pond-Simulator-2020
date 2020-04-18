package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.concurrent.ThreadLocalRandom;

public class Duck {
    // Attributes
    private static int ducksCount;
    private int id;

    private float x;
    private float y;
    private float targetX;
    private float targetY;
    private int dir;
    private float speed;
    private boolean isMoving;

    private static Animation[] animations = new Animation[8];

    // Constructors
    public Duck() {
        ducksCount++;
        this.id = ducksCount;

        this.x = 300;
        this.y = 300;
        this.targetX = this.x;
        this.targetY = this.y;
        this.dir = 1;
        this.speed = 0.2f;
        this.isMoving = false;
    }

    public Duck(float x, float y) {
        ducksCount++;
        this.id = ducksCount;

        this.x = x;
        this.y = y;
        this.targetX = this.x;
        this.targetY = this.y;
        this.dir = 1;
        this.speed = 0.2f;
        this.isMoving = false;
    }

    // Getters
    public static int getDucksCount()   { return ducksCount; }
    public int getId()                  { return this.id; }
    public float getX()                 { return this.x; }
    public float getY()                 { return this.y; }
    public float getTargetX()           { return this.targetX; }
    public float getTargetY()           { return this.targetY; }
    public int getDir()                 { return this.dir; }
    public float getSpeed()             { return this.speed; }
    public boolean isMoving()           { return this.isMoving; }
    public Animation[] getAnimations()  { return animations; }

    // Setters
    public void setX(float x)                           { this.x = x; }
    public void setY(float y)                           { this.y = y; }
    public void setTargetX(float targetX)               { this.targetX = targetX; }
    public void setTargetY(float targetY)               { this.targetY = targetY; }
    public void setDir(int dir)                         { this.dir = dir; }
    public void setSpeed(float speed)                   { this.speed = speed; }
    public void setMoving(boolean isMoving)             { this.isMoving = isMoving; }

    // Methods
    // Rendering
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

    // Movement
    public void move(TiledMap map, int delta) {
        // Compute dir
        if (!this.isMoving) { getNewTarget(); }

        // Get the next pos
        double deltaX = this.targetX - this.x;
        double deltaY = this.targetY - this.y;

        // The duck is arrived
        if ((1 > deltaX && deltaX > -1) && (1 > deltaY && deltaY > -1)) {
            this.isMoving = false;
            return;
        }

        double angle = Math.atan2(deltaY, deltaX);

        //boolean movedOnX = false;
        //boolean movedOnY = false;

        /*if (!(1 > deltaX && deltaX >= 0)) {
            this.x = (float) (this.x + (this.speed * Math.cos(angle) * delta));
            movedOnX = true;
        }

        if (!(1 > deltaY && deltaY >= 0))  {
            this.y = (float) (this.y + (this.speed * Math.sin(angle) * delta));
            movedOnY = true;
        }*/

        float nextX = (float) (this.x + (this.speed * Math.cos(angle) * delta));
        float nextY = (float) (this.y + (this.speed * Math.sin(angle) * delta));

        // Collisions
        Image solidTile = map.getTileImage(
            (int) nextX / map.getTileWidth(),
            (int) nextY / map.getTileHeight(),
            map.getLayerIndex("Collisions")
        );

        if (solidTile != null) { this.isMoving = false; }
        // Move
        else {
            this.x = nextX;
            this.y = nextY;

            getMoveDir();
        }

        //if (!movedOnX && !movedOnY) this.isMoving = false;
    }

    private void getNewTarget() {
        int min = -300;
        int max = +300;

        this.targetX = this.x + ThreadLocalRandom.current().nextInt(min, max + 1);
        this.targetY = this.y + ThreadLocalRandom.current().nextInt(min, max + 1);

        this.isMoving = true;
    }

    private void getMoveDir() {
        float absX = Math.abs(this.targetX);
        float absY = Math.abs(this.targetY);

        // Horizontal
        if (absX > absY) {
            this.dir = (this.targetX >= this.x) ? 3 : 1; // 3: Right / 1: Left
        }
        // Vertical
        else {
            this.dir = (this.targetY >= this.y) ? 2 : 0; // 2: Bottom / 0: Top
        }
    }

    // Others
    @Override
    public String toString() {
        return "Duck n°" + this.id + ":\n" +
                "x: " + this.x + "px\n" +
                "y: " + this.y + "px\n" +
                "targetX: " + this.targetX + "px\n" +
                "targetY: " + this.targetY + "px\n" +
                "dir: " + this.dir + "\n" +
                "isMoving: " + this.isMoving + "\n";
    }
}
