package fr.codex.naturalis.corner;

import java.awt.*;

public interface Corner {
    Ressource animal = new Ressource("animal",
            new Color(120, 201, 204),
            new Color(1, 162, 156),
            new Color(44, 42, 125));
    Ressource plant = new Ressource("plant",
            new Color(107, 188, 117),
            new Color(20, 126, 49),
            new Color(0, 60, 34));
    Ressource fungi = new Ressource("fungi",
            new Color(235, 75, 25),
            new Color(191, 13, 26),
            new Color(87, 15, 16));
    Ressource insect = new Ressource("insect",
            new Color(163, 61, 141),
            new Color(143, 25, 121),
            new Color(40, 29, 79));
    Artifact scroll = new Artifact("scroll");
    Artifact ink = new Artifact("ink");
    Artifact feather = new Artifact("feather");
    Empty empty = new Empty();
    Empty invisible = new Empty();

    /**
     * Return a string version of the Corner, containing its name and value.
     *
     * @return a string.
     */
    public String toString();

    /**
     * increase the Corner's value by 1.
     */
    public void increase();

    /**
     * decrease the Corner's value by 1.
     */
    public void decrease();

    /**
     * returns the value of the Corner, symbolizing the number of visible Corner of this kind.
     *
     * @return the value of the Corner.
     */
    public int getValue();
}
