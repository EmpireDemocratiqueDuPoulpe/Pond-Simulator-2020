package de.essen_sie_ihre_toten.pond_simulator_2020;

import de.essen_sie_ihre_toten.pond_simulator_2020.main_menu.MainMenuState;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditorScreenState extends BasicGameState {
    // Attributes
    public static final int ID = 1;
    private GameContainer container;
    private StateBasedGame game;

    private Image editor;
    private float editorAlpha;
    private float alphaStep;
    private boolean decreaseAlpha;
    private float delayBeforeDecrease;

    private static List<Music> bgMusics;

    // Getters
    public int getID() { return ID; }

    // Methods
    @Override
    public void init(GameContainer container, StateBasedGame game) {
        this.container = container;
        this.game = game;

        this.container.setShowFPS(false);

        // Load images and musics
        bgMusics = new ArrayList<>();

        try {
            loadImages();
            loadMusics(new File("./src/resources/musics"));
        } catch (SlickException e) {
            e.printStackTrace();
        }

        this.editorAlpha = .0f;
        this.alphaStep = .0006f;
        this.decreaseAlpha = false;
        this.delayBeforeDecrease = 15;
    }

    // Rendering
    private void loadImages() throws SlickException {
        this.container.setIcon("resources/hud/ducksCount.png");
        this.editor = new Image("resources/ui/editorBig.png");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) {
        float winWidth = container.getWidth();
        float winHeight = container.getHeight();

        // Background
        graphics.setColor(new Color(255, 255, 255));
        graphics.fillRect(0, 0, winWidth, winHeight);

        // Editor
        this.editor.setAlpha(editorAlpha);
        this.editor.drawCentered((winWidth / 2), (winHeight / 2));
    }

    // Update
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            skipEditor();
        }

        nextMusic();

        // Decrease alpha
        if (this.decreaseAlpha && this.editorAlpha > 0) {
            this.editorAlpha = Math.max((this.editorAlpha - (this.alphaStep * delta)), 0);
        }

        // Increase alpha
        else if (!this.decreaseAlpha && this.editorAlpha < 1) {
            this.editorAlpha = Math.min((this.editorAlpha + (this.alphaStep * delta)), 1);

        // Wait a bit
        } else if (this.delayBeforeDecrease > 0) {
            this.delayBeforeDecrease -= .01f * delta;

        // Time to decrease alpha
        } else if (this.delayBeforeDecrease <= 0 && !this.decreaseAlpha) {
            this.decreaseAlpha = true;

        // Switch to next state
        } else {
            this.game.enterState(MainMenuState.ID);
        }
    }

    // Events
    @Override
    public void keyReleased(int key, char c) {
        // Skip editor with ESC
        if (Input.KEY_ESCAPE == key) { skipEditor(); }
    }

    private void skipEditor() {
        this.game.enterState(MainMenuState.ID);
    }

    // Musics
    private void loadMusics(File folder) throws SlickException {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    loadMusics(file);
                } else if (file.getName().endsWith(".ogg")) {
                    bgMusics.add(new Music(file.getPath()));
                }
            }
        }
    }

    public static void nextMusic() {
        for (Music music : bgMusics) {
            if (music.playing()) return;
        }

        int r = (int) (Math.random() * (bgMusics.size()));
        bgMusics.get(r).play(1.0f, .25f);
    }
}
