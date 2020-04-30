package de.essen_sie_ihre_toten.pond_simulator_2020.entities;

import de.essen_sie_ihre_toten.pond_simulator_2020.main_menu.MainMenuState;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    // Attributes
    protected static int entitiesCount;
    protected final int id;

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected int dir;

    protected boolean isInQueue;

    private static List<Integer> deathList = new ArrayList<>();

    // Constructors
    public Entity() {
        entitiesCount++;
        this.id = entitiesCount;

        this.x = (float) (Math.random() * (864 - 128)) + 128;
        this.y = (float) (Math.random() * (544 - 128)) + 128;
        this.width = 32;
        this.height = 32;
        this.dir = (int) (Math.random() * (4));

        this.isInQueue = false;
    }

    public Entity(float x, float y) {
        entitiesCount++;
        this.id = entitiesCount;

        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 32;
        this.dir = (int) (Math.random() * (4));

        this.isInQueue = false;
    }

    public Entity(float x, float y, float width, float height) {
        entitiesCount++;
        this.id = entitiesCount;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dir = (int) (Math.random() * (4));

        this.isInQueue = false;
    }

    public Entity(float x, float y, float width, float height, int dir) {
        entitiesCount++;
        this.id = entitiesCount;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dir = dir;

        this.isInQueue = false;
    }

    // Getters
    public int getId()                          { return this.id; }
    public float getX()                         { return this.x; }
    public float getY()                         { return this.y; }
    public float getWidth()                     { return this.width; }
    public float getHeight()                    { return this.height; }
    public int getDir()                         { return this.dir; }
    public boolean isInQueue()                  { return this.isInQueue; }
    public static List<Integer> getDeathList()  { return deathList; }
    public static boolean deadListNotEmpty()    { return deathList.size() > 0; }

    // Setters
    public void setX(float x)                   { this.x = x;}
    public void setY(float y)                   { this.y = y;}
    public void setPos(float x, float y)        { this.x = x; this.y = y; }
    public void setWidth(float width)           { this.width = width; }
    public void setHeight(float height)         { this.height = height; }
    public void setDir(int dir)                 { this.dir = dir; }
    public void setInQueue(boolean isInQueue)   { this.isInQueue = isInQueue; }

    // Methods
    // Rendering
    public static void loadSprites() throws SlickException {}
    protected static Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y, int duration) {
        Animation animation = new Animation();

        for (int x = startX; x < endX; x++) {
            animation.addFrame(spriteSheet.getSprite(x, y), duration);
        }

        return animation;
    }

    public abstract void render(Graphics graphics);
    public void renderDebug(Graphics graphics) {
        String[] lines = this.toString().split("\n");
        float textX = this.x - this.width;
        float textY = this.y;

        for (String line : lines) {
            MainMenuState.debugTtf.drawString(textX, textY, line, Color.white);
            textY += 14;
        }
    }
    public void renderSuperDebug(Graphics graphics) {
        // Collider
        graphics.setColor(new Color(255, 0, 0));

        graphics.drawRect(this.x - (this.width / 2), this.y - this.height, this.width, this.height);

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
    public void update() {}

    // Movement
    protected boolean isEnteringCollision(TiledMap map, float nextX, float nextY) {
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

    // Others
    public static void addToDeathList(int id) {
        if (!deathList.contains(id))
            deathList.add(id);
    }

    public String toString() {
        return "Entities n°" + this.id + ":\n" +
                "x: " + Math.round(this.x) + "px\n" +
                "y: " + Math.round(this.y) + "px\n";
    }
}
