package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.Entity;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.WaterLily.WaterLily;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseDuck extends Entity {
    // Attributes
    private float targetX;
    private float targetY;
    private float speed;
    private boolean isMoving;

    private float hp;
    private float fp;
    private int weight;
    private static boolean canEat = true;
    private float eatCooldown;
    private boolean isDead;
    private boolean isOverweight;
    private float deathTimer;

    private static Sound explode;
    private static Sound dying;
    private boolean isPlayingSound;

    // Constructors
    public BaseDuck() {
        super();

        this.targetX = this.x;
        this.targetY = this.y;
        this.speed = 0.2f;
        this.isMoving = false;

        this.hp = 100;
        this.fp = 100;
        this.weight = 0;
        this.eatCooldown = .0f;
        this.isDead = false;
        this.isOverweight = false;
        this.deathTimer = 0;

        this.isPlayingSound = false;
    }

    public BaseDuck(float x, float y) {
        super(x, y);

        this.targetX = this.x;
        this.targetY = this.y;
        this.speed = 0.2f;
        this.isMoving = false;

        this.hp = 100;
        this.fp = 100;
        this.weight = 0;
        this.eatCooldown = .0f;
        this.isDead = false;
        this.isOverweight = false;
        this.deathTimer = 0;

        this.isPlayingSound = false;
    }

    public BaseDuck(BaseDuck duck) {
        super(duck.getX(), duck.getY());

        this.targetX = duck.getTargetX();
        this.targetY = duck.getTargetY();
        this.speed = duck.getSpeed();
        this.isMoving = duck.isMoving();

        this.hp = duck.getHp();
        this.fp = duck.getFp();
        this.weight = duck.getWeight();
        this.eatCooldown = duck.getEatCooldown();
        this.isDead = duck.isDead();
        this.isOverweight = duck.isOverweight();
        this.deathTimer = duck.getDeathTimer();

        this.isPlayingSound = duck.isPlayingSound();
    }

    // Getters
    public float getTargetX()           { return this.targetX; }
    public float getTargetY()           { return this.targetY; }
    public float getSpeed()             { return this.speed; }
    public boolean isMoving()           { return this.isMoving; }
    public float getHp()                { return this.hp; }
    public float getFp()                { return this.fp; }
    public int getWeight()              { return this.weight; }
    public static boolean canEat()      { return canEat; }
    public float getEatCooldown()       { return this.eatCooldown; }
    public boolean isDead()             { return this.isDead; }
    public boolean isOverweight()       { return this.isOverweight; }
    public float getDeathTimer()        { return this.deathTimer; }
    public boolean isPlayingSound()     { return this.isPlayingSound; }
    public abstract Animation[] getAnimations();

    // Setters
    public void setTargetX(float targetX)           { this.targetX = targetX; }
    public void setTargetY(float targetY)           { this.targetY = targetY; }
    public void setSpeed(float speed)               { this.speed = speed; }
    public void setMoving(boolean isMoving)         { this.isMoving = isMoving; }
    public void setHp(float hp)                     { this.hp = Math.min(100, hp); }
    public void setFp(float fp)                     { this.fp = Math.min(100, fp); }
    public void setWeight(int weight)               { this.weight = weight; }
    public static void setCanEat(boolean eat)       { canEat = eat; }
    public void setEatCooldown(float cooldown)      { this.eatCooldown = cooldown; }
    public void setDead(boolean isDead)             { this.isDead = isDead; }
    public void setOverweight(boolean isOverweight) { this.isOverweight = isOverweight; }
    public void setDeathTimer(float deathTimer)     { this.deathTimer = deathTimer; }
    public void setPlayingSound(boolean isPlaying)  { this.isPlayingSound = isPlaying; }

    // Methods
    // Rendering
    public static Animation[] loadSprites(SpriteSheet spriteSheet) {
        Animation[] animations = new Animation[10];

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

        // Explosion
        animations[9] = loadAnimation(spriteSheet, 0, 6, 5);
        animations[9].setLooping(false);

        return animations;
    }

    public void render(Graphics graphics) {
        float originX = this.x - 16;
        float originY = this.y - 32;

        float growRatio = (float) (1.6 * this.weight);

        float size = 32 + growRatio;

        if (!this.isDead && !this.isOverweight) {
            // Shadow
            graphics.setColor(new Color(0, 0, 0, .5f));
            graphics.fillOval(originX, originY + 24, size, 16 + growRatio);

            // Duck
            //graphics.drawAnimation(getAnimations()[this.dir + (this.isMoving ? 4 : 0)], this.x - 16, this.y - 32);
            getAnimations()[this.dir + (this.isMoving ? 4 : 0)].draw(originX, originY, size, size);
        } else if (this.isOverweight) {
            //graphics.drawAnimation(getAnimations()[8], originX, originY);
            getAnimations()[9].draw(originX, originY, size, size);
        } else {
            getAnimations()[8].draw(originX, originY, size, size);
        }

    }

    // Update
    public void update(TiledMap map, List<WaterLily> waterLilies, int delta) {
        if (!this.isDead && !this.isOverweight) {
            move(map, delta);
            loseFoodPoint(delta);
            if (canEat) eat(waterLilies, delta);
        } else {
            this.deathTimer += .01f * delta;

            if (this.deathTimer > 5) {
                addToDeathList(this.id);

                // Reset death anim
                getAnimations()[8].restart();
                getAnimations()[9].restart();
            }
        }

        playSounds();
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

        // Check if the duck is overweight
        if (this.weight >= 30) {
            this.isOverweight = true;
        }

        // Check if the duck is dead
        if (this.hp <= 0) {
            this.hp = 0;
            if (!this.isOverweight) this.isDead = true;
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
                float givenFp = Math.min(30, waterLily.getFp());

                // Add food point
                this.fp = Math.min(100, this.fp + givenFp);
                this.weight += 1;

                waterLily.setFp(waterLily.getFp() - givenFp);

                // Set cooldown
                this.eatCooldown = 10;
            }
        }
    }

    // Sounds
    public static void loadSounds() throws SlickException {
        explode = new Sound("resources/sounds/duck_explode.ogg");
        dying = new Sound("resources/sounds/duck_drowning.ogg");
    }

    public void playSounds() {
        // Play sound
        if (!this.isPlayingSound) {
            if (this.isOverweight) { explode.play(1.0f, 0.2f); this.isPlayingSound = true; } // Explode (overweight)
            else if (this.isDead) { dying.play(1.0f, 0.2f);  this.isPlayingSound = true; } // Die (drowning)
        // Check if there's no sound playing
        } else if (!explode.playing() && !dying.playing()) {
            this.isPlayingSound = false;
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
                "weight: " + this.weight + "\n" +
                "eatCooldown: " + Math.round(this.eatCooldown) + "\n" +
                "isDead: " + this.isDead + "\n" +
                "isOverweight: " + this.isOverweight;
    }
}
