package fr.codex.naturalis;

import com.github.forax.zen.*;
import com.github.forax.zen.Event;
import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.corner.Ressource;
import fr.codex.naturalis.card.Card;
import fr.codex.naturalis.card.GildingCard;
import fr.codex.naturalis.card.RessourceCard;
import fr.codex.naturalis.card.StartCard;
import fr.codex.naturalis.drawing.CardDrawingSequence;
import fr.codex.naturalis.placing.PlacingCorner;
import fr.codex.naturalis.player.Player;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Game {
    private final int ratio;
    private ArrayList<Card> allCards;
    private final Pile<RessourceCard> ressourceCardPile;
    private final Pile<GildingCard> gildingCardPile;
    private final ArrayList<Card> placedCards;
    private Color backgroundColor;

    public Game(int ratio) {
        ressourceCardPile = new Pile<RessourceCard>();
        gildingCardPile = new Pile<GildingCard>();
        placedCards = new ArrayList<Card>();

        if (ratio < 100) throw new IllegalArgumentException("ratio must be at least of 100.");
        this.ratio = ratio;

        this.backgroundColor = new Color(250, 240, 230);
    }

    private void initRessourcePile() {

    }
    /**
     * Start the game and its graphical interface.
     */
    public void start() {
        // start the application, create a drawing area, full screen
        Application.run(backgroundColor, context -> {
            // get the screen info
            var screenInfo = context.getScreenInfo();
            var width = screenInfo.width();
            var height = screenInfo.height();

            int diff = (int)(ratio/((float) 16/3));

            var cardX = new StartCard(Corner.animal, Corner.plant, Corner.fungi, Corner.insect);
            cardX.changeCoordinates(width/2 - ratio/2, height/2 - ratio/4);

            var cardY = new RessourceCard(Ressource.animal, Corner.animal, Corner.empty, Corner.invisible, Corner.empty, 1);
            cardY.changeCoordinates(width/2 + ratio/2 - diff, height/2 + ratio/4 - diff);

            var cardZ = new GildingCard(Ressource.insect, 2, Corner.empty, Corner.invisible, Corner.insect, Corner.scroll, List.of(Corner.insect,Corner.insect,Corner.plant));
            cardZ.changeCoordinates(cardY.getXCoordinate(), cardX.getYCoordinate() - ratio/8 - diff);

            placedCards.addAll(List.of(cardX,cardY,cardZ));

            for (;;) {
                drawPlacedCards(context);
                Event event = context.pollOrWaitEvent(2147483647); //maximum int value
                if (event != null) {
                    // debug, print the event
                    System.out.println(event);
                    switch (event) {
                        case PointerEvent pointerEvent -> {
                            // if the mouse pointer is up
                            if (pointerEvent.action() == PointerEvent.Action.POINTER_UP) {
                                // close the screen area
                                context.dispose();
                                return;
                            }
                        }
                        case KeyboardEvent keyboardEvent -> {
                            // if the key is "escape"
                            if (keyboardEvent.key() == KeyboardEvent.Key.ESCAPE) {
                                // close the screen area
                                context.dispose();
                                return;
                            }
                        }
                    }
                }
                context.renderFrame(graphics2D -> {
                    graphics2D.setColor(backgroundColor);
                    graphics2D.drawRect(0,0,width,height);
                });
            }
        });
    }

    /**
     * Draw every card in the placedCards List.
     *
     * @param context the ApplicationContext of the actual Application. (insert "context").
     */
    private void drawPlacedCards(ApplicationContext context) {
        Objects.requireNonNull(context);
        context.renderFrame(graphics2D -> {
            var cardDrawingSequence = new CardDrawingSequence(graphics2D, ratio);
            for (Card card : placedCards) {
                cardDrawingSequence.drawCard(card);
            }
        });
    }

    private void startPlacingSequence(ApplicationContext context ,int width, int height, int diff) {
        boolean placedACard = false;
        do {
            var allUnobstructedCorners = PlacingCorner.getCornersList(placedCards,width,height,diff,ratio);
            for (PlacingCorner placingCorner : allUnobstructedCorners) {
                context.renderFrame(placingCorner::drawPlacableCorner);
            }
            Event event = context.pollOrWaitEvent(2147483647);
            if (event != null) {
                switch (event) {
                    case PointerEvent pointerEvent :
                        if (pointerEvent.action() == PointerEvent.Action.POINTER_UP) {

                        }
                        break;
                    default : break;
                }
            }

        } while (placedACard);
    }

    private void placingSequenceLoop() {

    }

    /**
     * Make the player with this player ID pick a RessourceCard from the ressource cards Pile.
     * Adding the picked card into their deck and removing the card from the pile.
     *
     * @param playerID the ID of the player.
     */
    private void PlayerPickRessourcePile(int playerID) {}

    /**
     * Make the player with this player ID pick a GildingCard from the gilding cards Pile.
     * Adding the picked card into their deck and removing the card from the pile.
     *
     * @param playerID the ID of the player.
     */
    private void PlayerPickGildingPile(int playerID) {}
}