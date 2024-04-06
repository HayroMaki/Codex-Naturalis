package fr.codex.naturalis.point.system;

public class PerCorner implements PointSystem {

    /**
     * calculate the number of points given by the card when placing it by multiplying the number of covered corners by the point coeficient.
     *
     * @param point  the point coeficient (how much points by corners).
     * @param corner the number of corners covered.
     * @return the number of points given by the card.
     */
    public int CalculPoint(int point, int corner) {
        return point * corner;
    }
}
