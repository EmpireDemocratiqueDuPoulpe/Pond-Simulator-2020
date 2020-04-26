package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Duck extends BaseDuck {
    // Attributes
    private static Animation[] animations = new Animation[10];

    // Constructors
    public Duck() {
        super();
    }

    public Duck(float x, float y) {
        super(x, y);
    }

    // Getters
    public Animation[] getAnimations() { return animations; }

    // Methods
    // Rendering
    public static void loadSprites() throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("resources/entities/duck/normalDuck.png", 32, 32, 5);

        animations = BaseDuck.loadSprites(spriteSheet);
    }
}
