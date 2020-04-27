package de.essen_sie_ihre_toten.pond_simulator_2020.entities.WaterLily;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.Entity;

import de.essen_sie_ihre_toten.pond_simulator_2020.hud.Bar;
import de.essen_sie_ihre_toten.pond_simulator_2020.main_menu.MainMenuState;
import org.newdawn.slick.*;

public class WaterLily extends Entity {
    // Attributes
    private float fp;

    private Bar fpBar;

    private static Animation[] animations = new Animation[4];

    // Constructors
    public WaterLily() {
        super();

        this.fp = 90;

        this.fpBar = new Bar(this.x - 32, this.y - 74, 64, 3, this.fp, this.fp, Bar.darkOrange, Bar.orange);
    }

    public WaterLily(float x, float y) {
        super(x, y);

        this.fp = 90;

        this.fpBar = new Bar(this.x - 32, this.y - 74, 64, 3, this.fp, this.fp, Bar.darkOrange, Bar.orange);
    }

    public WaterLily(float x, float y, int dir) {
        super(x, y, dir);

        this.fp = 90;

        this.fpBar = new Bar(this.x - 32, this.y - 74, 64, 3, this.fp, this.fp, Bar.darkOrange, Bar.orange);
    }

    public WaterLily(float x, float y, int dir, float fp) {
        super(x, y, dir);

        this.fp = fp;

        this.fpBar = new Bar(this.x - 32, this.y - 74, 64, 3, this.fp, this.fp, Bar.darkOrange, Bar.orange);
    }

    // Getters
    public float getFp()    { return this.fp; }
    public Bar getFpBar()   { return this.fpBar; }

    // Setters
    public void setFp(float fp)     { this.fp = fp; }
    public void setFpBar(Bar fpBar) { this.fpBar = fpBar; }

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
        graphics.drawAnimation(animations[this.dir], this.x - 32, this.y - 64);
    }

    @Override
    public void renderDebug(Graphics graphics) {
        // Bar
        this.fpBar.draw(graphics);
    }

    @Override
    public void renderSuperDebug(Graphics graphics) {
        // Collider
        graphics.setColor(new Color(255, 0, 0));
        graphics.drawRect(this.x - 32, this.y - 64, 64, 64);

        // Trigger for eating
        graphics.setColor(new Color(255, 255, 255));
        graphics.drawRect(this.x - 32 - 25, this.y - 64 - 25, 64 + 50, 64 + 50);

        // Pos
        graphics.setColor(new Color(0, 0, 0, .5f));

        String pos = Math.round(this.x) + ":" + Math.round(this.y);
        float posW = MainMenuState.debugTtf.getWidth(pos);
        float posH = MainMenuState.debugTtf.getHeight(pos);
        float posTextX = this.x - (posW / 2.0f);
        float posTextY = this.y - (posH / 2.0f);

        graphics.fillRect(posTextX - 1, posTextY - 1, posW + 1, posH + 1);
        MainMenuState.debugTtf.drawString(posTextX, posTextY, pos);
    }

    // Update
    public void update() {
        if (this.fp <= 0) { Entity.addToDeathList(this.id); }
        this.fpBar.setPos(this.x - 32, this.y - 74);
        this.fpBar.setValue(this.fp);
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
