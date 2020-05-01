package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.Entity;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.EntityMoveable;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.water_lily.WaterLily;
import de.essen_sie_ihre_toten.pond_simulator_2020.hud.Bar;
import de.essen_sie_ihre_toten.pond_simulator_2020.main_menu.MainMenuState;

import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.List;

public abstract class BaseDuck extends Entity implements EntityMoveable {
    // Attributes
    protected float targetX;
    protected float targetY;
    protected float speed;
    protected boolean isMoving;
    protected boolean canGetNewPos;

    protected float hp;
    protected float fp;
    protected int weight;
    protected static boolean canEat = true;
    protected float eatCooldown;
    protected boolean isDead;
    protected boolean isOverweight;
    protected float deathTimer;

    protected static Sound whistle;
    protected static Sound explode;
    protected static Sound dying;
    protected boolean isPlayingSound;

    protected Bar hpBar;
    protected Bar fpBar;
    protected Bar weightBar;
    protected Bar eatCooldownBar;

    // Constructors
    public BaseDuck() {
        super();
        this.width = 32;
        this.height = 32;

        this.targetX = this.x;
        this.targetY = this.y;
        this.speed = 0.2f;
        this.isMoving = false;
        this.canGetNewPos = true;

        this.hp = 100;
        this.fp = 100;
        this.weight = 0;
        this.eatCooldown = .0f;
        this.isDead = false;
        this.isOverweight = false;
        this.deathTimer = 0;

        this.isPlayingSound = false;

        this.hpBar = new Bar(this.x - (this.width / 2), this.y - (this.height / 2) - 22, this.width, 3, this.hp, 100, Bar.darkGreen, Bar.green);
        this.fpBar = new Bar(this.x - (this.width / 2), this.y - (this.height / 2) - 16, this.width, 3, this.fp, 100, Bar.darkOrange, Bar.orange);
        this.weightBar = new Bar(this.x - (this.width / 2), this.y - (this.height / 2) - 10, this.width, 3, this.weight, 30, Bar.grey, Bar.white);
        this.eatCooldownBar = new Bar(this.x - (this.width / 2), this.y - (this.height / 2) - 8, this.width, 2, this.eatCooldown, 10, Bar.darkRed, Bar.red);
    }

    public BaseDuck(float x, float y) {
        super(x, y, 32, 32);

        this.targetX = this.x;
        this.targetY = this.y;
        this.speed = 0.2f;
        this.isMoving = false;
        this.canGetNewPos = true;

        this.hp = 100;
        this.fp = 100;
        this.weight = 0;
        this.eatCooldown = .0f;
        this.isDead = false;
        this.isOverweight = false;
        this.deathTimer = 0;

        this.isPlayingSound = false;

        this.hpBar = new Bar(this.x - (this.width / 2), this.y - (this.height / 2) - 22, this.width, 3, this.hp, 100, Bar.darkGreen, Bar.green);
        this.fpBar = new Bar(this.x - (this.width / 2), this.y - (this.height / 2) - 16, this.width, 3, this.fp, 100, Bar.darkOrange, Bar.orange);
        this.weightBar = new Bar(this.x - (this.width / 2), this.y - (this.height / 2) - 10, this.width, 3, this.weight, 30, Bar.grey, Bar.white);
        this.eatCooldownBar = new Bar(this.x - (this.width / 2), this.y - (this.height / 2) - 8, this.width, 2, this.eatCooldown, 10, Bar.darkRed, Bar.red);
    }

    public BaseDuck(BaseDuck duck) {
        super(duck.getX(), duck.getY(), duck.getWidth(), duck.getHeight(), duck.getDir());

        this.targetX = duck.getTargetX();
        this.targetY = duck.getTargetY();
        this.speed = duck.getSpeed();
        this.isMoving = duck.isMoving();
        this.canGetNewPos = duck.canGetNewPos();

        this.hp = duck.getHp();
        this.fp = duck.getFp();
        this.weight = duck.getWeight();
        this.eatCooldown = duck.getEatCooldown();
        this.isDead = duck.isDead();
        this.isOverweight = duck.isOverweight();
        this.deathTimer = duck.getDeathTimer();

        this.isPlayingSound = duck.isPlayingSound();

        this.hpBar = duck.getHpBar();
        this.fpBar = duck.getFpBar();
        this.weightBar = duck.getWeightBar();
        this.eatCooldownBar = duck.getEatCooldownBar();
    }

    // Getters
    public float getTargetX()           { return this.targetX; }
    public float getTargetY()           { return this.targetY; }
    public float getSpeed()             { return this.speed; }
    public boolean isMoving()           { return this.isMoving; }
    public boolean canGetNewPos()       { return this.canGetNewPos; }
    public float getHp()                { return this.hp; }
    public float getFp()                { return this.fp; }
    public int getWeight()              { return this.weight; }
    public static boolean canEat()      { return canEat; }
    public float getEatCooldown()       { return this.eatCooldown; }
    public boolean isDead()             { return this.isDead; }
    public boolean isOverweight()       { return this.isOverweight; }
    public float getDeathTimer()        { return this.deathTimer; }
    public boolean isPlayingSound()     { return this.isPlayingSound; }
    public Bar getHpBar()               { return this.hpBar; }
    public Bar getFpBar()               { return this.fpBar; }
    public Bar getWeightBar()           { return this.weightBar; }
    public Bar getEatCooldownBar()      { return this.eatCooldownBar; }
    public abstract Animation[] getAnimations();

    // Setters
    public void setTargetX(float targetX)               { this.targetX = targetX; }
    public void setTargetY(float targetY)               { this.targetY = targetY; }
    public void setTarget(float targetX, float targetY) { this.targetX = targetX; this.targetY = targetY; }
    public void setSpeed(float speed)                   { this.speed = speed; }
    public void setMoving(boolean isMoving)             { this.isMoving = isMoving; }
    public void setCanGetNewPos(boolean canGetNewPos)   { this.canGetNewPos = canGetNewPos; }
    public void setHp(float hp)                         { this.hp = Math.min(100, hp); }
    public void setFp(float fp)                         { this.fp = Math.min(100, fp); }
    public void setWeight(int weight)                   { this.weight = weight; }
    public static void setCanEat(boolean eat)           { canEat = eat; }
    public void setEatCooldown(float cooldown)          { this.eatCooldown = cooldown; }
    public void setDead(boolean isDead)                 { this.isDead = isDead; }
    public void setOverweight(boolean isOverweight)     { this.isOverweight = isOverweight; }
    public void setDeathTimer(float deathTimer)         { this.deathTimer = deathTimer; }
    public void setPlayingSound(boolean isPlaying)      { this.isPlayingSound = isPlaying; }
    public void setHpBar(Bar hpBar)                     { this.hpBar = hpBar; }
    public void setFpBar(Bar fpBar)                     { this.fpBar = fpBar; }
    public void setWeightBar(Bar weightBar)             { this.weightBar = weightBar; }
    public void setEatCooldownBar(Bar eatCooldownBar)   { this.eatCooldownBar = eatCooldownBar; }

    // Methods
    // Rendering
    public static Animation[] loadSprites(SpriteSheet spriteSheet, int swimFramesNb) {
        Animation[] animations = new Animation[10];

        // Idle
        animations[0] = loadAnimation(spriteSheet, 0, 1, 0, 300); // Up
        animations[1] = loadAnimation(spriteSheet, 0, 1, 1, 300); // Left
        animations[2] = loadAnimation(spriteSheet, 0, 1, 2, 300); // Bottom
        animations[3] = loadAnimation(spriteSheet, 0, 1, 3, 300); // Right

        // Swim
        animations[4] = loadAnimation(spriteSheet, 0, swimFramesNb, 0, 300); // Up
        animations[5] = loadAnimation(spriteSheet, 0, swimFramesNb, 1, 300); // Left
        animations[6] = loadAnimation(spriteSheet, 0, swimFramesNb, 2, 300); // Bottom
        animations[7] = loadAnimation(spriteSheet, 0, swimFramesNb, 3, 300); // Right

        // Die
        animations[8] = loadAnimation(spriteSheet, 0, 5, 4, 100);
        animations[8].setLooping(false);

        // Explosion
        animations[9] = loadAnimation(spriteSheet, 0, 6, 5, 100);
        animations[9].setLooping(false);

        return animations;
    }

    public void render(Graphics graphics) {
        float originX = this.x - (this.width / 2);
        float originY = this.y - this.height;

        if (!this.isDead && !this.isOverweight) {
            // Shadow
            float ovalHeight = (this.height / 2);

            graphics.setColor(new Color(0, 0, 0, .5f));
            graphics.fillOval(originX, this.y - (ovalHeight / 2), this.width, ovalHeight);

            // Duck
            getAnimations()[this.dir + (this.isMoving ? 4 : 0)].draw(originX, originY, this.width, this.height);
        } else if (this.isOverweight) {
            getAnimations()[9].draw(originX, originY, this.width, this.height);
        } else {
            getAnimations()[8].draw(originX, originY, this.width, this.height);
        }

    }

    @Override
    public void renderDebug(Graphics graphics) {
        // Direction
        if (this.isInQueue) { graphics.setColor(new Color(255, 0, 0)); }
        else                { graphics.setColor(new Color(0, 255, 0)); }
        graphics.drawLine(this.x, this.y, this.targetX, this.targetY);

        // Bars
        this.hpBar.draw(graphics);
        this.fpBar.draw(graphics);
        this.weightBar.draw(graphics, 10);
        this.eatCooldownBar.draw(graphics);
    }

    @Override
    public void renderSuperDebug(Graphics graphics) {
        super.renderSuperDebug(graphics);

        // Target pos
        String targetPos = Math.round(this.targetX) + ":" + Math.round(this.targetY);
        float targetPosW = MainMenuState.debugTtf.getWidth(targetPos);
        float targetPosH = MainMenuState.debugTtf.getHeight(targetPos);
        float targetPosTextX = this.targetX - (targetPosW / 2.0f);
        float targetPosTextY = this.targetY - (targetPosH / 2.0f);

        graphics.fillRect(targetPosTextX - 1, targetPosTextY - 1, targetPosW + 1, targetPosH + 1);
        MainMenuState.debugTtf.drawString(targetPosTextX, targetPosTextY, targetPos);
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
        if (!this.isMoving && this.canGetNewPos) { getNewTarget(); }

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

            // Update bars
            this.hpBar.setPos(this.x - (this.width / 2), this.y - this.height - 22);
            this.fpBar.setPos(this.x - (this.width / 2), this.y - this.height - 16);
            this.weightBar.setPos(this.x - (this.width / 2), this.y - this.height - 10);
            this.eatCooldownBar.setPos(this.x - (this.width / 2), this.y - this.height - 8);

            getMoveDir();
        }
    }

    protected void getNewTarget() {
        this.targetX = (float) (Math.random() * (864 - 128)) + 128;
        this.targetY = (float) (Math.random() * (544 - 128)) + 128;

        this.isMoving = true;
    }

    protected void getMoveDir() {
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

    public void stop() {
        this.targetX = this.x;
        this.targetY = this.y;
    }

    // Food
    protected void loseFoodPoint(int delta) {
        // Remove food point or health point
        if (this.fp > 0) {
            this.fp -= .01f * delta;
        } else {
            this.fp = 0;
            this.hp -= .01f * delta;
        }

        // Update bars
        this.hpBar.setValue(this.hp);
        this.fpBar.setValue(this.fp);
        this.weightBar.setValue(this.weight);

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

    protected void eat(List<WaterLily> waterLilies, int delta) {
        if (this.eatCooldown > .0f) {
            this.eatCooldown -= .01f * delta;
            this.eatCooldownBar.setValue(this.eatCooldown);
            return;
        }

        for (WaterLily waterLily : waterLilies) {
            // Add food point if inside
            if (waterLily.isInsideRadius(this.x, this.y)) {
                float givenFp = Math.min(30, waterLily.getFp());

                // Add food point
                this.fp = Math.min(100, this.fp + givenFp);
                this.weight += 1;

                float growRatio = (float) (1.6 * this.weight);

                this.width = 32 + growRatio;
                this.height = 32 + growRatio;

                waterLily.setFp(waterLily.getFp() - givenFp);

                // Set cooldown
                this.eatCooldown = 10;
                this.eatCooldownBar.setValue(this.eatCooldown);
            }
        }
    }

    // Sounds
    public static void loadSounds() throws SlickException {
        whistle = new Sound("resources/sounds/duck_whistle.ogg");
        explode = new Sound("resources/sounds/duck_explode.ogg");
        dying = new Sound("resources/sounds/duck_drowning.ogg");
    }

    public void playSounds() {
        // Play sound
        if (!this.isPlayingSound) {
            if (this.isOverweight) { explode.play(1.0f, 0.2f); this.isPlayingSound = true; } // Explode (overweight)
            else if (this.isDead) { dying.play(1.0f, 0.1f);  this.isPlayingSound = true; } // Die (drowning)
        // Check if there's no sound playing
        } else if (!explode.playing() && !dying.playing()) {
            this.isPlayingSound = false;
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
                "weight: " + this.weight + "\n" +
                "eatCooldown: " + Math.round(this.eatCooldown) + "\n" +
                "isDead: " + this.isDead + "\n" +
                "isOverweight: " + this.isOverweight;
    }
}
