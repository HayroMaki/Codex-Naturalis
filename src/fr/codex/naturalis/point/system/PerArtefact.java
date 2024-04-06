package fr.codex.naturalis.point.system;

import fr.codex.naturalis.corner.Artifact;

import java.util.Objects;

public class PerArtefact implements PointSystem {
    private final Artifact artifact;

    public PerArtefact(Artifact artifact) {
        Objects.requireNonNull(artifact);
        this.artifact = artifact;
    }

    /**
     * calculate the number of points given by the card when placing it, counting the number of matching visible artifacts and multiplying it by the point coeficient.
     *
     * @param point the point coeficient (how much points by artifacts).
     * @return the number of points given by the card.
     */
    public int CalculPoint(int point) {
        return point * artifact.getValue();
    }
}