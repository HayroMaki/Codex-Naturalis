package fr.codex.naturalis.corner;

import java.awt.*;
import java.util.Objects;

public class Artifact implements Corner {
    private final String name;
    private final Color mainColor;
    private final Color secondaryColor;
    private int value;

    public Artifact(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        this.value = 0;
        this.mainColor = new Color(218, 145, 0);
        this.secondaryColor = new Color(87, 66, 11);
    }

    /**
     * Return a string version of the Artifact, containing its name and value.
     *
     * @return a string.
     */
    @Override
    public String toString() {
        return name + ": " + value;
    }

    /**
     * increase the Artifact's value by 1.
     */
    @Override
    public void increase() {
        value++;
    }

    /**
     * decrease the Artifact's value by 1.
     */
    @Override
    public void decrease() {
        value--;
    }

    /**
     * returns the value of the Artifact, symbolizing the number of visible Artifacts of this kind.
     *
     * @return the value of the Artifact.
     */
    @Override
    public int getValue() {
        return value;
    }

    public Color getMainColor() {
        return this.mainColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }
}
