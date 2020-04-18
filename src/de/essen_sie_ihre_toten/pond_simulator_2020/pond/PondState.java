package de.essen_sie_ihre_toten.pond_simulator_2020.pond;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.Duck;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

public class PondState extends BasicGameState {
    // Attributes
    public static final int ID = 2;
    private GameContainer container;
    private StateBasedGame game;

    private TiledMap map;
    private Duck[] ducks;

    // Getters
    public int getID() { return ID; }

    // Methods
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        this.game = game;

        // Load map
        this.map = new TiledMap("resources/maps/pond.tmx");

        // Init ducks
        this.ducks = new Duck[]{
                new Duck(200, 200),
                new Duck(200, 200),
                new Duck(200, 200),
                new Duck(200, 200),
        };

        // Load spritesheets
        try {
            Duck.loadSprites();
        } catch (SlickException sE) {
            System.err.println("Error while loading sprites:\n" + sE.getMessage() + "\n" + sE.getCause());
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) {
        // Map
        this.map.render(0, 0, this.map.getLayerIndex("Pond"));

        // Ducks
        for (Duck duck : this.ducks) {
            duck.render(graphics);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        for (Duck duck : this.ducks) {
            duck.move(map, delta);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        // Exit game with ESC
        if (Input.KEY_ESCAPE == key) { this.container.exit(); }
    }
}
