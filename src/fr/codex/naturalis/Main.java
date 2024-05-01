package fr.codex.naturalis;

import fr.codex.naturalis.card.StartCard;
import fr.codex.naturalis.corner.Corner;

public class Main {
    public static void main(String[] args) {
        var cardTest = new StartCard(Corner.insect,Corner.insect,Corner.animal,Corner.animal);
        System.out.println(cardTest);
        var game = new Game(300);
        System.out.println(game.startCards);
        game.start();
    }
}
