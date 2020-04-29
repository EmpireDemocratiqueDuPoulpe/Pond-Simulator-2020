package de.essen_sie_ihre_toten.pond_simulator_2020.pond;

import de.essen_sie_ihre_toten.pond_simulator_2020.entities.Entity;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.water_lily.WaterLily;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.BaseDuck;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.CaptainDuck;
import de.essen_sie_ihre_toten.pond_simulator_2020.entities.duck.Duck;
import de.essen_sie_ihre_toten.pond_simulator_2020.hud.HUD;

import de.essen_sie_ihre_toten.pond_simulator_2020.main_menu.MainMenuState;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PondState extends BasicGameState {
    // Attributes
    public static final int ID = 2;
    private GameContainer container;
    private StateBasedGame game;
    private boolean isEnd;
    private static boolean debug;
    private static boolean superDebug;

    private TiledMap map;
    private HUD hud;
    private static List<BaseDuck> ducks;
    private List<WaterLily> waterLilies;

    private Music bgMusic;

    // Getters
    public int getID()                          { return ID; }
    public static boolean debugActivated()      { return debug; }
    public static boolean superDebugActivated() { return superDebug; }
    public static List<BaseDuck> getDucks()     { return ducks; }

    // Methods
    // Slick2D
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        this.game = game;
        this.isEnd = false;
        debug = false;
        superDebug = false;

        // Load map
        this.map = new TiledMap("resources/maps/pond.tmx");

        // Load HUD
        this.hud = new HUD();

        // Init entities
        ducks = new ArrayList<>(
            Arrays.asList(
                new Duck(),
                new Duck(),
                new Duck(),
                new Duck()
            )
        );

        this.waterLilies = new ArrayList<>(
            Arrays.asList(
                new WaterLily(),
                new WaterLily(),
                new WaterLily(),
                new WaterLily()
            )
        );

        // Load spritesheets, sounds and fonts
        try {
            // Sprites
            Duck.loadSprites();
            CaptainDuck.loadSprites();

            WaterLily.loadSprites();

            // Sounds
            loadMusics();
            BaseDuck.loadSounds();
        } catch (SlickException sE) {
            sE.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) {
        if (this.isEnd) { renderEnd(graphics); return; }

        // Normal level of the map
        this.map.render(0, 0, this.map.getLayerIndex("Pond"));

        // Entities
        for (WaterLily waterLily : this.waterLilies) {
            waterLily.render(graphics);
        }

        for (BaseDuck duck : ducks) {
            duck.render(graphics);
        }

        // Map layers in front of entities
        this.map.render(0, 0, this.map.getLayerIndex("aboveEntities"));

        // HUD
        this.hud.render(container, graphics);

        // Debug
        if (debug) {
            for (WaterLily waterLily : this.waterLilies) {
                waterLily.renderDebug(graphics);
                if (superDebug) waterLily.renderSuperDebug(graphics);
            }

            for (BaseDuck duck : ducks) {
                duck.renderDebug(graphics);
                if (superDebug) duck.renderSuperDebug(graphics);
            }
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (this.isEnd) return;

        // Add new entities
        if ((Math.random() * (100)) <= .08f)
            this.waterLilies.add(new WaterLily());

        if (Duck.getDucksCount() >= 2)
            if ((Math.random() * (100)) <= .01f)
                ducks.add(new Duck());

        // Update entities
        for (WaterLily waterLily : this.waterLilies) {
            waterLily.update();
        }

        for (BaseDuck duck : ducks) {
            duck.update(map, waterLilies, delta);
        }

        // Convert ducks to CaptainDuck
        ducks = ducks.stream()
            .map(duck -> (
                    (duck.getWeight() >= 10) && // Weight at 10 or more
                    !(duck instanceof CaptainDuck) && // Not a CaptainDuck
                    (CaptainDuck.getDucksCount() < 3)) && // Not already three Captain alive
                    !(duck.isInQueue()) ? new CaptainDuck(duck) : duck) // Not in queue
            .collect(Collectors.toList());

        // Delete dead entities
        /*if (Entity.deadListNotEmpty()) {
            ducks.removeIf(ent -> Entity.getDeathList().contains(ent.getId()));
            this.waterLilies.removeIf(ent -> Entity.getDeathList().contains(ent.getId()));

            // Update ducks count
            int ducksCount = (int) ducks.stream().filter(duck -> duck instanceof Duck).count();
            int captainDucksCount = (int) ducks.stream().filter(duck -> duck instanceof CaptainDuck).count();

            if ((ducksCount + captainDucksCount) == 0)
                this.isEnd = true;
            else {
                Duck.setDucksCount(ducksCount);
                CaptainDuck.setDucksCount(captainDucksCount);
            }
        }*/

        if (Entity.deadListNotEmpty()) {
            // Water lilies
            this.waterLilies.removeIf(ent -> Entity.getDeathList().contains(ent.getId()));

            // Ducks
            List<Integer> ducksIds = new ArrayList<>();

            for (BaseDuck duck : ducks) {
                if (Entity.getDeathList().contains(duck.getId())) {
                    if (duck instanceof CaptainDuck) {
                        ((CaptainDuck) duck).emptyQueue();
                    }

                    ducksIds.add(duck.getId());
                }
            }

            ducks.removeIf(d -> ducksIds.contains(d.getId()));

            // Update ducks count
            int ducksCount = (int) ducks.stream().filter(duck -> duck instanceof Duck).count();
            int captainDucksCount = (int) ducks.stream().filter(duck -> duck instanceof CaptainDuck).count();

            if ((ducksCount + captainDucksCount) == 0)
                this.isEnd = true;
            else {
                Duck.setDucksCount(ducksCount);
                CaptainDuck.setDucksCount(captainDucksCount);
            }
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        // Activate debug
        if (Input.KEY_D == key) {
            if (!debug) {
                debug = true;
            } else if (!superDebug) {
                superDebug = true;
            } else {
                debug = false;
                superDebug = false;
            }

            this.container.setShowFPS(debug);
        }
        // Prevent ducks from eating
        else if (Input.KEY_F == key)        { BaseDuck.setCanEat(!BaseDuck.canEat()); }
        // Exit game with ESC
        else if (Input.KEY_ESCAPE == key)   { this.container.exit(); }
    }

    // Musics
    private void loadMusics() throws SlickException {
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

        // Background music
        this.bgMusic = new Music("resources/musics/m1.ogg");
        this.bgMusic.loop();
        this.bgMusic.setVolume(0.5f);
    }

    // Others
    private void renderEnd(Graphics graphics) {
        graphics.setColor(new Color(0, 0, 0));
        graphics.fillRect(0, 0, container.getWidth(), container.getHeight());

        String message = "C'est la fin";
        String message2 = "Tous les canards sont morts";
        String message3 = "Qu'ils reposent en canards laques ;(";

        MainMenuState.endTtf.drawString(
                ((float) container.getWidth() / 2) - ((float) MainMenuState.endTtf.getWidth(message) / 2),
                (((float) container.getHeight() / 2) - ((float) MainMenuState.endTtf.getHeight(message) / 2)) - 50,
                message
        );

        MainMenuState.endTtf.drawString(
                ((float) container.getWidth() / 2) - ((float) MainMenuState.endTtf.getWidth(message2) / 2),
                ((float) container.getHeight() / 2) - ((float) MainMenuState.endTtf.getHeight(message2) / 2),
                message2
        );

        MainMenuState.endTtf.drawString(
                ((float) container.getWidth() / 2) - ((float) MainMenuState.endTtf.getWidth(message3) / 2),
                (((float) container.getHeight() / 2) - ((float) MainMenuState.endTtf.getHeight(message3) / 2)) + 50,
                message3
        );
    }
}
