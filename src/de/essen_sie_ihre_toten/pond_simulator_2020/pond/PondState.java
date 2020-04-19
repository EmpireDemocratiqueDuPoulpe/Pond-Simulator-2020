package de.essen_sie_ihre_toten.pond_simulator_2020.pond;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.Duck;

import de.essen_sie_ihre_toten.pond_simulator_2020.hud.HUD;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class PondState extends BasicGameState {
    // Attributes
    public static final int ID = 2;
    private GameContainer container;
    private StateBasedGame game;

    private TiledMap map;
    private HUD hud;
    private Duck[] ducks;

    private Music bgMusic;

    // Getters
    public int getID() { return ID; }

    // Methods
    // Slick2D
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        this.game = game;

        // Load map
        this.map = new TiledMap("resources/maps/pond.tmx");

        // Load HUD
        this.hud = new HUD();

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

        // Load musics
        loadMusics();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) {
        // Normal level of the map
        this.map.render(0, 0, this.map.getLayerIndex("Pond"));

        // Ducks
        for (Duck duck : this.ducks) {
            duck.render(graphics);
        }

        // Map layers in front of ducks
        this.map.render(0, 0, this.map.getLayerIndex("aboveEntities"));

        // HUD
        this.hud.render(graphics);
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

    // Musics
    public void loadMusics() {
        /*File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    loadMusics(file);
                } else if (file.getName().endsWith(".ogg")) {
                    System.out.println(file.getPath());
                }
            }
        }*/

        try {
            this.bgMusic = new Music("./src/resources/musics/m1.ogg");
            this.bgMusic.loop();
            this.bgMusic.setVolume(0.1f);
        } catch (SlickException sE) {
            sE.printStackTrace();
        }

    }
}
