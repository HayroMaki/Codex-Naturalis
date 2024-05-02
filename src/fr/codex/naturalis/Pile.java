package fr.codex.naturalis;

import fr.codex.naturalis.card.Card;
import fr.codex.naturalis.card.RessourceCard;
import fr.codex.naturalis.card.StartCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Pile<Card> {
    private final ArrayList<Card> pile;
    private Card TurnedOverCard1;
    private Card TurnedOverCard2;

    public Pile() {
        this.pile = new ArrayList<Card>();
        this.TurnedOverCard1 = null;
        this.TurnedOverCard2 = null;
    }

    @Override
    public String toString() {
        return "Pile{" +
                ", TurnedOverCard1=" + TurnedOverCard1 +
                ", TurnedOverCard2=" + TurnedOverCard2 +
                ", pile=" + pile +
                '}';
    }
    public Card getFirstOfPile() {
        return pile.getFirst();
    }
    public Card getTOC1() {
        return TurnedOverCard1;
    }
    public Card getTOC2() {
        return TurnedOverCard2;
    }
    /**
     * set the first turned over card to the first card of the pile, removing it from the pile.
     */
    public void setTOC1() {
        if (!pile.isEmpty()) this.TurnedOverCard1 = pile.removeFirst();
        else TurnedOverCard1 = null;
    }
    /**
     * set the second turned over card to the first card of the pile, removing it from the pile.
     */
    public void setTOC2() {
        if (!pile.isEmpty()) this.TurnedOverCard2 = pile.removeFirst();
        else TurnedOverCard2 = null;
    }
    /**
     * Remove the first turned over card and returns it.
     * Then set the first turned over card to the first card of the pile, removing it from the pile.
     *
     * @return the turned over Card that was removed.
     */
    public Card removeTOC1() {
        var card = TurnedOverCard1;
        setTOC1();
        return card;
    }
    /**
     * Remove the second turned over card and returns it.
     * Then set the second turned over card to the first card of the pile, removing it from the pile.
     *
     * @return the turned over Card that was removed.
     */
    public Card removeTOC2() {
        var card = TurnedOverCard2;
        setTOC2();
        return card;
    }
    /**
     * Check if the first turned over card is takeable.
     *
     * @return true if not null, false if it is.
     */
    public boolean isTOC1Takeable() {
        return TurnedOverCard1 != null;
    }
    /**
     * Check if the second turned over card is takeable.
     *
     * @return true if not null, false if it is.
     */
    public boolean isTOC2Takeable() {
        return TurnedOverCard2 != null;
    }
    /**
     * Remove the first card from the pile and returns it.
     *
     * @return the first card of the pile.
     */
    public Card removeFromPile() {
        return pile.removeFirst();
    }

    public void add(Card card) {
        pile.add(card);
    }
}
