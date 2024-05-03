package fr.codex.naturalis.player;

import fr.codex.naturalis.challenge.ChallengeCard;
import fr.codex.naturalis.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Deck {
    private final ArrayList<Card> deck;
    public boolean isOpen;

    public Deck() {
        this.deck = new ArrayList<Card>();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + deck +
                '}';
    }
    /**
     * remove a card from the deck.
     *
     * @param card the card to remove.
     */
    public void remove(Card card) {
        Objects.requireNonNull(card);
        deck.remove(card);
    }

    /**
     * add a card to the deck, putting it at the end of the deck.
     *
     * @param card the card to add.
     */
    public void add(Card card) {
        Objects.requireNonNull(card);
        deck.add(card);
    }

    public List<Card> getCards() {
        var l = new ArrayList<Card>(deck);
        return List.copyOf(l);
    }


}

