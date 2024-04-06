package fr.codex.naturalis.challenge;

import fr.codex.naturalis.card.Card;

import java.util.Objects;

public class ChallengeCard {
    private final Challenge challenge;

    public ChallengeCard(Challenge challenge) {
        Objects.requireNonNull(challenge);
        this.challenge = challenge;
    }
}
