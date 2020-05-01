package de.essen_sie_ihre_toten.pond_simulator_2020;

import de.essen_sie_ihre_toten.pond_simulator_2020.main_menu.MainMenuState;
import de.essen_sie_ihre_toten.pond_simulator_2020.pond.PondState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {
    // Main
    public static void main(String[] args) throws SlickException {
        new AppGameContainer(new Game(), 960, 640, false).start();
    }

    // Constructors
    public Game() {
        super("Pond Simulator 2020");
    }

    // Methods
    @Override
    public void initStatesList(GameContainer container) {
        addState(new EditorScreenState());
        addState(new MainMenuState());
        addState(new PondState());
    }
}
