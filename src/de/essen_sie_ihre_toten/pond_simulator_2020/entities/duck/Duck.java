package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Duck extends BaseDuck {
    // Attributes
    private static int ducksCount;

    private static Animation[] animations = new Animation[10];

    // Constructors
    public Duck() {
        super();
        ducksCount++;
    }

    public Duck(float x, float y) {
        super(x, y);
        ducksCount++;
    }

    // Getters
    public static int getDucksCount()   { return ducksCount; }
    public Animation[] getAnimations()  { return animations; }

    // Setters
    public static void setDucksCount(int count) { ducksCount = count; }

    // Methods
    // Rendering
    public static void loadSprites() throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("resources/entities/duck/normalDuck.png", 32, 32, 5);

        animations = BaseDuck.loadSprites(spriteSheet, 2);
    }
}
