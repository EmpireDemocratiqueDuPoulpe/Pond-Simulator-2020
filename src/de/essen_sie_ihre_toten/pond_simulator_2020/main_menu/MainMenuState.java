package de.essen_sie_ihre_toten.pond_simulator_2020.main_menu;

import de.essen_sie_ihre_toten.pond_simulator_2020.EditorScreenState;
import de.essen_sie_ihre_toten.pond_simulator_2020.pond.PondState;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenuState extends BasicGameState implements ComponentListener {
    // Attributes
    public static final int ID = 2;
    private GameContainer container;
    private StateBasedGame game;

    private Image background;
    private Image gameName;
    private Image editor;
    private Image seal;
    private Image buttonNormal;
    private Image buttonHover;
    private Image buttonPressed;

    private MouseOverArea[] buttons;
    private String[] buttonsActions;

    public static TrueTypeFont buttonTtf;
    public static TrueTypeFont hudTtf;
    public static TrueTypeFont debugTtf;
    public static UnicodeFont endTtf;

    // Getters
    public int getID() { return ID; }

    // Methods
    @Override
    public void init(GameContainer container, StateBasedGame game) {
        this.container = container;
        this.game = game;

        // Load images and fonts
        try {
            loadImages();
            loadFonts();
        } catch (FontFormatException | IOException | SlickException e) {
            e.printStackTrace();
        }

        // MouseOverAreas
        this.buttons = new MouseOverArea[2];
        this.buttonsActions = new String[]{ "Contempler", "Quitter" };

        int moaX = (int) ((container.getWidth() / 2.0f) - (this.buttonNormal.getWidth() / 2.0f));
        int moaY = this.gameName.getHeight() + 115;
        int moaSpacing = this.buttonNormal.getHeight() + 20;

        for (int i = 0; i < buttons.length; i++) {
            this.buttons[i] = new MouseOverArea(container, this.buttonNormal, moaX, moaY, this);
            this.buttons[i].setMouseOverImage(this.buttonHover);
            this.buttons[i].setMouseDownImage(this.buttonPressed);

            moaY += moaSpacing;
        }
    }

    // Rendering
    private void loadImages() throws SlickException {
        this.background = new Image("resources/ui/mainMenuBg.png");
        this.gameName = new Image("resources/ui/gameName.png");
        this.editor = new Image("resources/ui/editor.png");
        this.seal = new Image("resources/ui/seal.png");
        this.buttonNormal = new Image("resources/ui/buttons/button.png");
        this.buttonHover = new Image("resources/ui/buttons/buttonHover.png");
        this.buttonPressed = new Image("resources/ui/buttons/buttonPressed.png");
    }

    private void loadFonts() throws FontFormatException, IOException, SlickException {
        java.awt.Font debugFont = new java.awt.Font("Verdana", java.awt.Font.PLAIN, 14);
        debugTtf = new TrueTypeFont(debugFont, true);

        java.awt.Font hudFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("./src/resources/fonts/game_over_cre.ttf"));

        hudFont = hudFont.deriveFont(java.awt.Font.PLAIN, 14);
        java.awt.Font buttonFont = hudFont.deriveFont(java.awt.Font.PLAIN, 35);
        java.awt.Font endFont = hudFont.deriveFont(java.awt.Font.PLAIN, 48);

        hudTtf = new TrueTypeFont(hudFont, true);
        endTtf = new UnicodeFont(endFont);
        buttonTtf = new TrueTypeFont(buttonFont, true);

        endTtf.addAsciiGlyphs();
        endTtf.getEffects().add(new ColorEffect(java.awt.Color.white));
        endTtf.addAsciiGlyphs();
        endTtf.loadGlyphs();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) {
        float winWidth = container.getWidth();
        float winHeight = container.getHeight();
        float winCenterX = winWidth / 2.0f;
        float winCenterY = winHeight / 2.0f;
        float margin = 15;

        // Background
        graphics.drawImage(this.background, 0, 0);

        // Game name, editor and seal
        graphics.drawImage(this.gameName, (winCenterX - (this.gameName.getWidth() / 2.0f)), margin);
        graphics.drawImage(this.editor, margin, winHeight - this.editor.getHeight() - margin);
        graphics.drawImage(this.seal, winWidth - this.seal.getWidth() - margin, winHeight - this.seal.getHeight() - margin);

        // Buttons
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i].render(container, graphics);

            buttonTtf.drawString(
                    this.buttons[i].getX() + (this.buttons[i].getWidth() / 2.0f) - (buttonTtf.getWidth(this.buttonsActions[i]) / 2.0f),
                    this.buttons[i].getY() + (this.buttons[i].getHeight() / 2.0f) - (buttonTtf.getHeight(this.buttonsActions[i]) / 2.0f),
                    this.buttonsActions[i]
            );
        }
    }

    // Update
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        EditorScreenState.nextMusic();
    }

    // Events
    @Override
    public void keyReleased(int key, char c) {
        // Exit game with ESC
        if (Input.KEY_ESCAPE == key) { this.container.exit(); }
    }

    @Override
    public void componentActivated(AbstractComponent abstractComponent) {
        for (int i = 0; i < this.buttons.length; i++) {
            if (this.buttons[i] == abstractComponent) {
                String action = this.buttonsActions[i];

                // Actions
                if (action.equals("Contempler"))
                    this.game.enterState(PondState.ID);

                else if (action.equals("Quitter"))
                    this.container.exit();
            }
        }
    }
}
