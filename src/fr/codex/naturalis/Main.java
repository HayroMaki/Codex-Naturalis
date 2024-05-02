package fr.codex.naturalis;

import fr.codex.naturalis.card.StartCard;
import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.player.Player;

public class Main {
    public static void main(String[] args) {
        var game = new Game(300);
        Player player = new Player(69);
        game.start(player);
    }
}
