package de.essen_sie_ihre_toten.pond_simulator_2020.pond;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.Duck;
import de.essen_sie_ihre_toten.pond_simulator_2020.hud.HUD;

import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PondState extends BasicGameState {
    // Attributes
    public static final int ID = 2;
    private GameContainer container;
    private StateBasedGame game;
    private boolean isEnd;
    private boolean debug;

    private TiledMap map;
    private HUD hud;
    private List<Duck> ducks;
    private static List<Integer> entDeathList;

    private Music bgMusic;

    public static TrueTypeFont hudTtf;
    public static TrueTypeFont debugTtf;
    public static UnicodeFont endTtf;

    // Getters
    public int getID() { return ID; }

    // Methods
    // Slick2D
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        this.game = game;
        this.isEnd = false;
        this.debug = false;

        // Load map
        this.map = new TiledMap("resources/maps/pond.tmx");

        // Load HUD
        this.hud = new HUD();

        // Init ducks
        this.ducks = new ArrayList<>(
            Arrays.asList(
                new Duck(200, 200),
                new Duck(200, 200),
                new Duck(200, 200),
                new Duck(200, 200)
            )
        );

        entDeathList = new ArrayList<>();

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
        } catch (FontFormatException | IOException | SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) {
        if (this.isEnd) {
            graphics.setColor(new Color(0, 0, 0));
            graphics.fillRect(0, 0, container.getWidth(), container.getHeight());

            String message = "C'est la fin";
            String message2 = "Tous les canards sont morts";
            String message3 = "Qu'ils reposent en canards laques ;(";

            endTtf.drawString(
                    ((float) container.getWidth() / 2) - ((float) endTtf.getWidth(message) / 2),
                    (((float) container.getHeight() / 2) - ((float) endTtf.getHeight(message) / 2)) - 50,
                    message
            );

            endTtf.drawString(
                    ((float) container.getWidth() / 2) - ((float) endTtf.getWidth(message2) / 2),
                    ((float) container.getHeight() / 2) - ((float) endTtf.getHeight(message2) / 2),
                    message2
            );

            endTtf.drawString(
                    ((float) container.getWidth() / 2) - ((float) endTtf.getWidth(message3) / 2),
                    (((float) container.getHeight() / 2) - ((float) endTtf.getHeight(message3) / 2)) + 50,
                    message3
            );

            return;
        }

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
        if (this.isEnd) return;

        // Update ducks
        for (Duck duck : this.ducks) {
            duck.update(map, delta);
        }

        // Delete dead entities
        if (entDeathList.size() > 0) {
            this.ducks.removeIf(duck -> entDeathList.contains(duck.getId()));

            // Update ducks count
            int ducksCount = this.ducks.size();

            if (ducksCount == 0)
                this.isEnd = true;
            else
                Duck.setDucksCount(ducksCount);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        // Activate debug
        if (Input.KEY_D == key)             { this.debug = !this.debug; this.container.setShowFPS(this.debug); }
        // Exit game with ESC
        else if (Input.KEY_ESCAPE == key)   { this.container.exit(); }
    }

    // Entities
    public static void addToDeathList(int id) {
        if (!entDeathList.contains(id))
            entDeathList.add(id);
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
            this.bgMusic = new Music("resources/musics/m1.ogg");
            this.bgMusic.loop();
            this.bgMusic.setVolume(0.3f);
        } catch (SlickException sE) {
            sE.printStackTrace();
        }

    }

    // Fonts
    private void loadFonts() throws FontFormatException, IOException, SlickException {
        Font debugFont = new Font("Verdana", Font.PLAIN, 14);
        debugTtf = new TrueTypeFont(debugFont, true);


        Font hudFont = Font.createFont(Font.TRUETYPE_FONT, new File("./src/resources/fonts/game_over_cre.ttf"));

        hudFont = hudFont.deriveFont(Font.PLAIN, 14);
        Font endFont = hudFont.deriveFont(Font.PLAIN, 48);

        hudTtf = new TrueTypeFont(hudFont, true);
        endTtf = new UnicodeFont(endFont);

        endTtf.addAsciiGlyphs();
        endTtf.getEffects().add(new ColorEffect(java.awt.Color.white));
        endTtf.addAsciiGlyphs();
        endTtf.loadGlyphs();
    }
}
