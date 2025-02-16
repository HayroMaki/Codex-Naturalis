package fr.codex.naturalis.corner;

import java.util.Objects;

public class Empty implements Corner {
    String name;

    public Empty(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }
    /**
     * Return a string version of the Hollow Corner, containing only spaces.
     *
     * @return a string.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Since the Corner is Hollow, doesn't do anything.
     */
    @Override
    public void increase() {
    }

    /**
     * Since the Corner is Hollow, doesn't do anything.
     */
    @Override
    public void decrease() {
    }

    /**
     * returns 0, as the Hollow Corners have no value and are not to be accounted for.
     *
     * @return always 0.
     */
    @Override
    public int getValue() {
        return 0;
    }
}
