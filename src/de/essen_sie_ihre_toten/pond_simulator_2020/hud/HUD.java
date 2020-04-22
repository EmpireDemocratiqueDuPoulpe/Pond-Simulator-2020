package de.essen_sie_ihre_toten.pond_simulator_2020.hud;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.Duck;
import de.essen_sie_ihre_toten.pond_simulator_2020.pond.PondState;

import org.newdawn.slick.*;

import java.util.*;

public class HUD {
    // Attributes
    private Image ducksCount;

    private HashMap<String, Image> keys;

    // Constructors
    public HUD() throws SlickException {
        this.ducksCount = new Image("./src/resources/hud/ducksCount.png");

        this.keys = new HashMap<>() {
            {
                put("Quitter", new Image("./src/resources/keys/key_esc.png"));
                put("Debug", new Image("./src/resources/keys/key_d.png"));
            }
        };
    }

    // Methods
    // Rendering
    public void render(GameContainer container, Graphics graphics) {
        graphics.resetTransform();

        drawDucksCount(graphics);
        drawKeys(container, graphics);
    }

    private void drawDucksCount(Graphics graphics) {
        graphics.drawImage(this.ducksCount, 10, 10);
        PondState.hudTtf.drawString(12 + ducksCount.getWidth(), 14, ": " + Duck.getDucksCount(), Color.white);
    }

    private void drawKeys(GameContainer container, Graphics graphics) {
        int containerWidth = container.getWidth();
        int keyBoxSize = 100;
        int keyDX = containerWidth - keyBoxSize;
        int keyDY = 10;

        graphics.setColor(new Color(0, 0, 0, .5f));
        graphics.fillRect(keyDX - 10, 0, keyBoxSize + 10, (this.keys.size() * 42) + 8);

        for(Map.Entry<String, Image> key : this.keys.entrySet()) {
            String name = key.getKey();
            Image image = key.getValue();

            graphics.drawImage(image, keyDX, keyDY);
            PondState.hudTtf.drawString(keyDX + 42, keyDY + 8, name, Color.white);

            keyDY += 40;
        }
    }
}
