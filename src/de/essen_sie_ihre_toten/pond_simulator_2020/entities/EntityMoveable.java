package de.essen_sie_ihre_toten.pond_simulator_2020.entities;

import org.newdawn.slick.tiled.TiledMap;

public interface EntityMoveable {
    // Getters
    float getTargetX();
    float getTargetY();
    int getDir();

    // Setters
    void setTargetX(float targetX);
    void setTargetY(float targetY);
    void setTarget(float targetX, float targetY);
    void setDir(int dir);

    // Methods
    void move(TiledMap map, int delta);
    void stop();
}
