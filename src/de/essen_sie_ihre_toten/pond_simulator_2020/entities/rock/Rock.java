package de.essen_sie_ihre_toten.pond_simulator_2020.entities.rock;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.Entity;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Rock extends Entity {
    // Attributes
    private static Animation[] animations = new Animation[4];

    // Constructors
    public Rock() {
        super();

        this.width = 64;
        this.height = 64;
    }

    public Rock(float x, float y) {
        super(x, y, 64, 64);
    }

    // Methods
    // Rendering
    public static void loadSprites() throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("resources/entities/rock/rock.png", 64, 64, 5);

        // Idle
        animations[0] = loadAnimation(spriteSheet, 0, 2, 0, 750); // 1
        animations[1] = loadAnimation(spriteSheet, 0, 2, 1, 750); // 2
        animations[2] = loadAnimation(spriteSheet, 0, 2, 2, 750); // 3
        animations[3] = loadAnimation(spriteSheet, 0, 2, 3, 750); // 4
    }

    public void render(Graphics graphics) {
        graphics.drawAnimation(animations[this.dir], this.x - (this.width / 2), this.y - this.height);
    }

    @Override
    public void renderDebug(Graphics graphics) { }

    // Physic
    public boolean isInside(float x, float y) {
        return (
            (x >= (this.x - (this.width / 2))) && (x <= (this.x + (this.width / 2))) &&
            (y >= (this.y - 30)) && (y <= (this.y - 10))
        );
    }
}
