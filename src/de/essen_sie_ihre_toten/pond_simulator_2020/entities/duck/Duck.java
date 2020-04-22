package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import de.essen_sie_ihre_toten.pond_simulator_2020.pond.PondState;

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

    private float hp;
    private float fp;
    private boolean isDead;
    private float deathTimer;

    private static Animation[] animations = new Animation[9];

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

        this.hp = 100;
        this.fp = 100;
        this.isDead = false;
        this.deathTimer = 0;
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

        this.hp = 100;
        this.fp = 100;
        this.isDead = false;
        this.deathTimer = 0;
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
    public float getHp()                { return this.hp; }
    public float getFp()                { return this.fp; }
    public boolean isDead()             { return this.isDead; }
    public float getDeathTimer()        { return this.deathTimer; }
    public Animation[] getAnimations()  { return animations; }

    // Setters
    public static void setDucksCount(int count)         { ducksCount = count; }
    public void setX(float x)                           { this.x = x; }
    public void setY(float y)                           { this.y = y; }
    public void setTargetX(float targetX)               { this.targetX = targetX; }
    public void setTargetY(float targetY)               { this.targetY = targetY; }
    public void setDir(int dir)                         { this.dir = dir; }
    public void setSpeed(float speed)                   { this.speed = speed; }
    public void setMoving(boolean isMoving)             { this.isMoving = isMoving; }
    public void setHp(float hp)                         { this.hp = hp; }
    public void setFp(float fp)                         { this.fp = fp; }
    public void setDead(boolean isDead)                 { this.isDead = isDead; }
    public void setDeathTimer(float deathTimer)         { this.deathTimer = deathTimer; }

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

        // Die
        animations[8] = loadAnimation(spriteSheet, 0, 5, 4);
    }

    private static Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
        Animation animation = new Animation();

        for (int x = startX; x < endX; x++) {
            animation.addFrame(spriteSheet.getSprite(x, y), 100);
        }

        return animation;
    }

    public void render(Graphics graphics) {
        if (!this.isDead) {
            // Shadow
            graphics.setColor(new Color(0, 0, 0, .5f));
            graphics.fillOval(x - 16, y - 8, 32, 16);

            // Duck
            graphics.drawAnimation(animations[this.dir + (this.isMoving ? 4 : 0)], this.x - 16, this.y - 32);
        } else {
            graphics.drawAnimation(animations[8], this.x - 16, this.y - 32);
        }

    }

    public void renderDebug() {
        String[] lines = this.toString().split("\n");
        float textX = this.x - 40;
        float textY = this.y;

        for (String line : lines) {
            PondState.debugTtf.drawString(textX, textY, line, Color.white);
            textY += 14;
        }
    }

    // Update
    public void update(TiledMap map, int delta) {
        if (!this.isDead) {
            move(map, delta);
            loseFoodPoint(delta);
        } else {
            this.deathTimer += .01f * delta;

            if (this.deathTimer > 5) {
                PondState.addToDeathList(this.id);
            }
        }
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

        float nextX = (float) (this.x + (this.speed * Math.cos(angle) * delta));
        float nextY = (float) (this.y + (this.speed * Math.sin(angle) * delta));

        // Collisions
        if (isEnteringCollision(map, nextX, nextY)) { this.isMoving = false; }
        // Move
        else {
            this.x = nextX;
            this.y = nextY;

            getMoveDir();
        }
    }

    private void getNewTarget() {
        int min = -300;
        int max = +300;

        this.targetX = this.x + ThreadLocalRandom.current().nextInt(min, max + 1);
        this.targetY = this.y + ThreadLocalRandom.current().nextInt(min, max + 1);

        this.isMoving = true;
    }

    private boolean isEnteringCollision(TiledMap map, float nextX, float nextY) {
        Image solidTile = map.getTileImage(
                (int) nextX / map.getTileWidth(),
                (int) nextY / map.getTileHeight(),
                map.getLayerIndex("Collisions")
        );

        boolean collision = solidTile != null;

        if (collision) {

            // Still collide if this is not a transparent pixel
            Color color = solidTile.getColor(
                    (int) nextX % map.getTileWidth(),
                    (int) nextY % map.getTileHeight()
            );

            collision = color.getAlpha() > 0;
        }

        return collision;
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

    // Food
    private void loseFoodPoint(int delta) {
        // Remove food point or health point
        if (this.fp > 0) {
            this.fp -= .01f * delta;
        } else {
            this.fp = 0;
            this.hp -= .01f * delta;
        }

        // Check if the duck is dead
        if (this.hp <= 0) {
            this.hp = 0;
            this.isDead = true;
        }
    }

    // Others
    @Override
    public String toString() {
        return "Duck n°" + this.id + ":\n" +
                "x: " + Math.round(this.x) + "px\n" +
                "y: " + Math.round(this.y) + "px\n" +
                "targetX: " + Math.round(this.targetX) + "px\n" +
                "targetY: " + Math.round(this.targetY) + "px\n" +
                "dir: " + this.dir + "\n" +
                "isMoving: " + this.isMoving + "\n" +
                "HP: " + Math.round(this.hp) + "\n" +
                "FP: " + Math.round(this.fp) + "\n" +
                "isDead: " + this.isDead;
    }
}
