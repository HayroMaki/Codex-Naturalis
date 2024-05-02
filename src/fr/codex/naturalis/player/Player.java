package fr.codex.naturalis.player;

import fr.codex.naturalis.card.Card;
import fr.codex.naturalis.challenge.Challenge;
import fr.codex.naturalis.challenge.ChallengeCard;

import java.util.Objects;

public class Player {
    private final int playerID;
    private final Deck deck;
    private int score;
    private ChallengeCard challenge;

    public Player(int playerID) {
        this.playerID = playerID;
        this.deck = new Deck();
        this.challenge = null;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerID=" + playerID +
                ", score=" + score +
                ", challenge=" + challenge +
                ", deck=" + deck +
                '}';
    }

    /**
     * Set the challenge card of the player.
     *
     * @param challenge a non-null challenge card.
     */
    public void setChallenge(ChallengeCard challenge) {
        Objects.requireNonNull(challenge);
        this.challenge = challenge;
    }
    /**
     * increase the player's score by the parameter entered.
     *
     * @param point the number of points the player's score will be increased by.
     */
    public void increasePoint(int point) {
        if (point < 0) throw new IllegalArgumentException();
        score += point;
    }
    public void addCard(Card card) {
        Objects.requireNonNull(card);
        deck.add(card);
    }
    public void removeCard(Card card) {
        Objects.requireNonNull(card);
        deck.remove(card);
    }
}
