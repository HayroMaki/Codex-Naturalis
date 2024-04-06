package fr.codex.naturalis.corner;

import java.awt.*;
import java.util.Objects;

public class Ressource implements Corner {
    private final String name;
    private final Color mainColor;
    private final Color secondaryColor;
    private final Color tertiaryColor;
    private int value;
    public Ressource(String name, Color mainColor, Color secondaryColor, Color tertiaryColor) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(mainColor);
        Objects.requireNonNull(secondaryColor);
        Objects.requireNonNull(tertiaryColor);
        this.name = name;
        this.value = 0;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
        this.tertiaryColor = tertiaryColor;
    }

    /**
     * Return a string version of the Ressource, containing its name and value.
     *
     * @return a string.
     */
    @Override
    public String toString() {
        return name + ": " + value;
    }

    /**
     * increase the Ressource's value by 1.
     */
    @Override
    public void increase() {
        value++;
    }

    /**
     * decrease the Ressource's value by 1.
     */
    @Override
    public void decrease() {
        value--;
    }

    /**
     * return the Ressource's main color.
     *
     * @return the ressource's color.
     */
    public Color getMainColor() {
        return mainColor;
    }

    /**
     * return the Ressource's secondary color.
     *
     * @return the ressource's color.
     */
    public Color getSecondaryColor() {
        return secondaryColor;
    }

    /**
     * return the Ressource's tertiary color.
     *
     * @return the ressource's color.
     */
    public Color getTertiaryColor() {
        return tertiaryColor;
    }

    /**
     * returns the value of the Ressource, symbolizing the number of visible Ressources of this kind.
     *
     * @return the value of the Ressource.
     */
    @Override
    public int getValue() {
        return value;
    }
}
