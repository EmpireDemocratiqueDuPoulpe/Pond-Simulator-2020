package de.essen_sie_ihre_toten.pond_simulator_2020.hud;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.BaseDuck;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.CaptainDuck;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.Duck;
import de.essen_sie_ihre_toten.pond_simulator_2020.main_menu.MainMenuState;
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
                put("No food", new Image("./src/resources/keys/key_f.png"));
                put("Quitter", new Image("./src/resources/keys/key_esc.png"));
                put("Debug", new Image("./src/resources/keys/key_d.png"));
                put("Pause", new Image("./src/resources/keys/key_p.png"));
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
        MainMenuState.hudTtf.drawString(12 + ducksCount.getWidth(), 14, ": " + (Duck.getDucksCount() + CaptainDuck.getDucksCount()), Color.white);
    }

    private void drawKeys(GameContainer container, Graphics graphics) {
        int containerWidth = container.getWidth();
        int keyBoxSize = 100;
        int keyDX = containerWidth - keyBoxSize;
        int keyDY = 10;

        // Black box behind
        graphics.setColor(new Color(0, 0, 0, .5f));
        graphics.fillRect(keyDX - 10, 0, keyBoxSize + 10, (this.keys.size() * 42) + 8);

        for(Map.Entry<String, Image> key : this.keys.entrySet()) {
            String name = key.getKey();
            Image image = key.getValue();
            Color color = Color.white;

            // Change color if the key is activated
            if (name.equals("Debug") && PondState.debugActivated()) {
                if (PondState.superDebugActivated())
                    color = Color.red;

                else
                    color = Color.yellow;
            }
            else if (name.equals("No food") && !BaseDuck.canEat())  { color = Color.yellow; }
            else if (name.equals("Pause") && container.isPaused())  { color = Color.yellow; }

            // Draw the key
            graphics.drawImage(image, keyDX, keyDY);
            MainMenuState.hudTtf.drawString(keyDX + 42, keyDY + 8, name, color);

            keyDY += 40;
        }
    }
}
