package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.Entity;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.WaterLily.WaterLily;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Duck extends Entity {
    // Attributes
    private static int ducksCount;

    private float targetX;
    private float targetY;
    private float speed;
    private boolean isMoving;

    private float hp;
    private float fp;
    private float eatCooldown;
    private boolean isDead;
    private float deathTimer;

    private static Animation[] animations = new Animation[9];

    // Constructors
    public Duck() {
        super();

        ducksCount++;

        this.targetX = this.x;
        this.targetY = this.y;
        this.speed = 0.2f;
        this.isMoving = false;

        this.hp = 100;
        this.fp = 100;
        this.eatCooldown = .0f;
        this.isDead = false;
        this.deathTimer = 0;
    }

    public Duck(float x, float y) {
        super(x, y);

        ducksCount++;

        this.targetX = this.x;
        this.targetY = this.y;
        this.speed = 0.2f;
        this.isMoving = false;

        this.hp = 100;
        this.fp = 100;
        this.eatCooldown = .0f;
        this.isDead = false;
        this.deathTimer = 0;
    }

    // Getters
    public static int getDucksCount()   { return ducksCount; }
    public float getTargetX()           { return this.targetX; }
    public float getTargetY()           { return this.targetY; }
    public float getSpeed()             { return this.speed; }
    public boolean isMoving()           { return this.isMoving; }
    public float getHp()                { return this.hp; }
    public float getFp()                { return this.fp; }
    public float getEatCooldown()       { return this.eatCooldown; }
    public boolean isDead()             { return this.isDead; }
    public float getDeathTimer()        { return this.deathTimer; }

    // Setters
    public void setTargetX(float targetX)       { this.targetX = targetX; }
    public void setTargetY(float targetY)       { this.targetY = targetY; }
    public void setSpeed(float speed)           { this.speed = speed; }
    public void setMoving(boolean isMoving)     { this.isMoving = isMoving; }
    public void setHp(float hp)                 { this.hp = hp; }
    public void setFp(float fp)                 { this.fp = fp; }
    public void setEatCooldown(float cooldown)  { this.eatCooldown = cooldown; }
    public void setDead(boolean isDead)         { this.isDead = isDead; }
    public void setDeathTimer(float deathTimer) { this.deathTimer = deathTimer; }
    public static void setDucksCount(int count) { ducksCount = count; }

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
        animations[8].setLooping(false);
    }

    public void render(Graphics graphics) {
        if (!this.isDead) {
            // Shadow
            graphics.setColor(new Color(0, 0, 0, .5f));
            graphics.fillOval(this.x - 16, this.y - 8, 32, 16);

            // Duck
            graphics.drawAnimation(animations[this.dir + (this.isMoving ? 4 : 0)], this.x - 16, this.y - 32);
        } else {
            graphics.drawAnimation(animations[8], this.x - 16, this.y - 32);
        }

    }

    // Update
    public void update(TiledMap map, List<WaterLily> waterLilies, int delta) {
        if (!this.isDead) {
            move(map, delta);
            loseFoodPoint(delta);
            eat(waterLilies, delta);
        } else {
            this.deathTimer += .01f * delta;

            if (this.deathTimer > 5) {
                addToDeathList(this.id);

                // Reset death anim
                animations[8].restart();
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

    private void eat(List<WaterLily> waterLilies, int delta) {
        if (this.eatCooldown > .0f) {
            this.eatCooldown -= .01f * delta;
            return;
        }

        for (WaterLily waterLily : waterLilies) {
            float wX = waterLily.getX();
            float wY = waterLily.getY();
            int wWidth = 64;
            int wHeight = 64;
            int margin = 25;

            if (
                    (this.x >= wX - margin && this.x <= wX + wWidth + margin) &&
                    (this.y >= wY - margin && this.y <= wY + wHeight + margin)
            ) {
                // Add food point
                float givenFp = Math.min(30, waterLily.getFp());

                this.fp = Math.min(100, this.fp + givenFp);
                waterLily.setFp(waterLily.getFp() - givenFp);

                // Set cooldown
                this.eatCooldown = 10;
            }
        }
    }

    // Others
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
                "eatCooldown: " + Math.round(this.eatCooldown) + "\n" +
                "isDead: " + this.isDead;
    }
}
