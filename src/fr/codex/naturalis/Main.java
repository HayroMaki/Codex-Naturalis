package fr.codex.naturalis;

public class Main {
    public static void main(String[] args) {
        var game = new Game(300);
        System.out.println(game.startCards);
        game.start();
    }
}
