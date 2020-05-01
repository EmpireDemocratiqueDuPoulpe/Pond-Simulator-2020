package de.essen_sie_ihre_toten.pond_simulator_2020.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Bar {
    // Attributes
    private float x;
    private float y;
    private float width;
    private float height;

    private float value;
    private float maxValue;

    private Color backgroundColor;
    private Color barColor;

    public static Color darkRed = new Color(66, 19, 16);
    public static Color red = new Color(235, 64, 52);
    public static Color darkOrange = new Color(36, 29, 8);
    public static Color orange = new Color(209, 152, 44);
    public static Color darkGreen = new Color(8, 36, 16);
    public static Color green = new Color(46, 209, 95);
    public static Color black = new Color(0, 0, 0);
    public static Color grey = new Color(46, 46, 46);
    public static Color white = new Color(255, 255, 255);

    // Constructors
    public Bar() {
        this.x = 0;
        this.y = 0;
        this.width = 30;
        this.height = 5;

        this.value = 0;
        this.maxValue = 100;

        this.backgroundColor = new Color(0, 0, 0);
        this.barColor = new Color(255, 255, 255);
    }

    public Bar(float x, float y) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 5;

        this.value = 0;
        this.maxValue = 100;

        this.backgroundColor = new Color(0, 0, 0);
        this.barColor = new Color(255, 255, 255);
    }

    public Bar(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.value = 0;
        this.maxValue = 100;

        this.backgroundColor = new Color(0, 0, 0);
        this.barColor = new Color(255, 255, 255);
    }

    public Bar(float x, float y, float width, float height, float value, float maxValue) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.value = value;
        this.maxValue = maxValue;

        this.backgroundColor = new Color(0, 0, 0);
        this.barColor = new Color(255, 255, 255);
    }

    public Bar(float x, float y, float width, float height, float value, float maxValue, Color backgroundColor, Color barColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.value = value;
        this.maxValue = maxValue;

        this.backgroundColor = backgroundColor;
        this.barColor = barColor;
    }

    // Getters
    public float getX()                 { return this.x; }
    public float getY()                 { return this.y; }
    public float getWidth()             { return this.width; }
    public float getHeight()            { return this.height; }
    public float getValue()             { return this.value; }
    public float getMaxValue()          { return this.maxValue; }
    public Color getBackgroundColor()   { return this.backgroundColor; }
    public Color getBarColor()          { return this.barColor; }

    // Setters
    public void setX(float x)                               { this.x = x; }
    public void setY(float y)                               { this.y = y; }
    public void setPos(float x, float y)                    { this.x = x; this.y = y; }
    public void setWidth(float width)                       { this.width = width; }
    public void setHeight(float height)                     { this.height = height; }
    public void setValue(float value)                       { this.value = value; }
    public void setMaxValue(float maxValue)                 { this.maxValue = maxValue; }
    public void setBackgroundColor(Color backgroundColor)   { this.backgroundColor = backgroundColor; }
    public void setBarColor(Color barColor)                 { this.barColor = barColor; }

    // Methods
    public void draw(Graphics graphics) {

        // Background
        graphics.setColor(this.backgroundColor);
        graphics.fillRect(this.x, this.y, this.width, this.height);

        // Bar
        graphics.setColor(this.barColor);
        graphics.fillRect(this.x, this.y, (this.value * this.width) / this.maxValue, this.height);
    }

    public void draw(Graphics graphics, int markerValue) {

        // Background and bar
        draw(graphics);

        // Marker value
        float markerX = this.x + ((this.width * markerValue) / this.maxValue);
        graphics.setColor(red);
        graphics.drawLine(markerX, this.y - 1, markerX, this.y + this.height + 1);
    }
}
