package de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class CaptainDuck extends BaseDuck {
    // Attributes
    private static Animation[] animations = new Animation[9];

    // Constructors
    public CaptainDuck() {
        super();
    }

    public CaptainDuck(float x, float y) {
        super(x, y);
    }

    public CaptainDuck(BaseDuck duck) {
        super(
            duck.getX(),
            duck.getY(),
            duck.getTargetX(),
            duck.getTargetY(),
            duck.getSpeed(),
            duck.isMoving(),
            duck.getHp(),
            duck.getFp(),
            duck.getWeight(),
            duck.getEatCooldown(),
            duck.isDead(),
            duck.getDeathTimer()
        );
    }

    // Getters
    public Animation[] getAnimations() { return animations; }

    // Methods
    // Rendering
    public static void loadSprites() throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("resources/entities/duck/captainDuck.png", 32, 32, 5);

        animations = BaseDuck.loadSprites(spriteSheet);
    }
}
