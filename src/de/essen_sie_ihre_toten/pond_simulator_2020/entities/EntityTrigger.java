package de.essen_sie_ihre_toten.pond_simulator_2020.entities;

public interface EntityTrigger {
    // Getters
    float getTriggerRadius();

    // Setters
    void setTriggerRadius(float triggerRadius);

    // Methods
    boolean isInsideRadius(float x, float y);
}
