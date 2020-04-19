package de.essen_sie_ihre_toten.pond_simulator_2020.hud;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.Duck;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class HUD {
    // Attributes
    private Image ducksCount;
    private final int ducksCountX = 10;
    private final int ducksCountY = 10;

    // Constructors
    public HUD() throws SlickException {
        this.ducksCount = new Image("./src/resources/hud/ducksCount.png");
    }

    // Methods
    // Rendering
    public void render(Graphics graphics) {
        graphics.resetTransform();
        graphics.drawImage(this.ducksCount, this.ducksCountX, this.ducksCountY);
        graphics.drawString(": " + Duck.getDucksCount(), ducksCountX + ducksCount.getWidth(), ducksCountY);
    }
}
