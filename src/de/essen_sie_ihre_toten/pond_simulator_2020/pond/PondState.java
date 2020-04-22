package de.essen_sie_ihre_toten.pond_simulator_2020.pond;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.Duck;
import de.essen_sie_ihre_toten.pond_simulator_2020.hud.HUD;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

public class PondState extends BasicGameState {
    // Attributes
    public static final int ID = 2;
    private GameContainer container;
    private StateBasedGame game;
    private boolean debug;

    private TiledMap map;
    private HUD hud;
    private Duck[] ducks;

    private Music bgMusic;

    public static TrueTypeFont hudTtf;
    public static TrueTypeFont debugTtf;
    //public static TrueTypeFont hudTtf;

    // Getters
    public int getID() { return ID; }

    // Methods
    // Slick2D
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        this.game = game;
        this.debug = false;

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

        // Load fonts
        try {
            loadFonts();
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
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
        this.hud.render(container, graphics);

        // Debug
        if (this.debug) {
            for (Duck duck : this.ducks) {
                duck.renderDebug();
            }
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
        // Activate debug
        if (Input.KEY_D == key)             { this.debug = !this.debug; }
        // Exit game with ESC
        else if (Input.KEY_ESCAPE == key)   { this.container.exit(); }
    }

    // Musics
    private void loadMusics() {
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

    // Fonts
    private void loadFonts() throws FontFormatException, IOException {
        Font debugFont = new Font("Verdana", Font.PLAIN, 14);
        debugTtf = new TrueTypeFont(debugFont, true);


        Font hudFont = Font.createFont(Font.TRUETYPE_FONT, new File("./src/resources/fonts/game_over_cre.ttf"));
        hudFont = hudFont.deriveFont(Font.PLAIN, 14);

        hudTtf = new TrueTypeFont(hudFont, true);
        /*hudTtf.addAsciiGlyphs();
        hudTtf.getEffects().add(new ColorEffect(java.awt.Color.white));
        hudTtf.addAsciiGlyphs();
        hudTtf.loadGlyphs();*/
    }
}
