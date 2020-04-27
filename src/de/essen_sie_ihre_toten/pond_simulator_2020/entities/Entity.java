package de.essen_sie_ihre_toten.pond_simulator_2020.entities;

import de.essen_sie_ihre_toten.pond_simulator_2020.main_menu.MainMenuState;
import de.essen_sie_ihre_toten.pond_simulator_2020.pond.PondState;
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
    protected int dir;

    private static List<Integer> deathList;

    // Constructors
    public Entity() {
        entitiesCount++;
        this.id = entitiesCount;

        this.x = (int) (Math.random() * (864 - 128)) + 128;
        this.y = (int) (Math.random() * (544 - 128)) + 128;
        this.dir = (int) (Math.random() * (4));

        deathList = new ArrayList<>();
    }

    public Entity(float x, float y) {
        entitiesCount++;
        this.id = entitiesCount;

        this.x = x;
        this.y = y;
        this.dir = (int) (Math.random() * (4));

        deathList = new ArrayList<>();
    }

    public Entity(float x, float y, int dir) {
        entitiesCount++;
        this.id = entitiesCount;

        this.x = x;
        this.y = y;
        this.dir = dir;

        deathList = new ArrayList<>();
    }

    // Getters
    public int getId()                          { return this.id; }
    public float getX()                         { return this.x; }
    public float getY()                         { return this.y; }
    public int getDir()                         { return this.dir; }
    public static List<Integer> getDeathList()  { return deathList; }
    public static boolean deadListNotEmpty()    { return deathList.size() > 0; }

    // Setters
    public void setX(float x)   { this.x = x;}
    public void setY(float y)   { this.y = y;}
    public void setDir(int dir) { this.dir = dir; }

    // Methods
    // Rendering
    public static void loadSprites() throws SlickException {}
    protected static Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
        Animation animation = new Animation();

        for (int x = startX; x < endX; x++) {
            animation.addFrame(spriteSheet.getSprite(x, y), 100);
        }

        return animation;
    }

    public void render() {}
    public void renderDebug() {
        String[] lines = this.toString().split("\n");
        float textX = this.x - 40;
        float textY = this.y;

        for (String line : lines) {
            MainMenuState.debugTtf.drawString(textX, textY, line, Color.white);
            textY += 14;
        }
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
