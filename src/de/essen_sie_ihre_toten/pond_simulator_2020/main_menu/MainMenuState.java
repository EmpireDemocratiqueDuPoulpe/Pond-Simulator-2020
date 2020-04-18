package de.essen_sie_ihre_toten.pond_simulator_2020.main_menu;

import de.essen_sie_ihre_toten.pond_simulator_2020.pond.PondState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicGameState {
    // Attributes
    public static final int ID = 1;
    private StateBasedGame game;

    // Getters
    public int getID() { return ID; }

    // Methods
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.game = game;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }

    @Override
    public void keyReleased(int key, char c) {
        game.enterState(PondState.ID);
    }
}
