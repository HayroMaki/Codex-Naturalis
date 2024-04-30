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

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

public class Game {
    private final int ratio;
    private ArrayList<Card> allCards;
    public final Pile<RessourceCard> ressourceCardPile;
    public final Pile<GildingCard> gildingCardPile;
    private final ArrayList<Card> placedCards;
    public final ArrayList<StartCard> startCards;
    private final Color backgroundColor;

    public Game(int ratio) {
        placedCards = new ArrayList<Card>();

        if (ratio < 100) throw new IllegalArgumentException("ratio must be at least of 100.");
        this.ratio = ratio;

        ressourceCardPile = new Pile<RessourceCard>();
        gildingCardPile = new Pile<GildingCard>();
        startCards = new ArrayList<>();
        createPiles();

        this.backgroundColor = new Color(250, 240, 230);
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

    private void StartingSequence(ApplicationContext context) {
        Objects.requireNonNull(context);

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
    private void createPiles() {
        Map<String,Corner> cornerMap = Map.of(
                "Empty", Corner.empty,
                "Invisible",Corner.invisible,
                "R:Insect",Corner.insect,
                "R:Animal",Corner.animal,
                "R:Fungi",Corner.fungi,
                "R:Plant",Corner.plant,
                "A:Quill",Corner.feather,
                "A:Manuscript",Corner.scroll,
                "A:Inkwell",Corner.ink );

        Map<String,Ressource> ressourceMap = Map.of(
                "R:Insect",Corner.insect,
                "R:Animal",Corner.animal,
                "R:Fungi",Corner.fungi,
                "R:Plant",Corner.plant );

        Path path = Path.of("data", "deck.txt");
        try (var reader = Files.newBufferedReader(path, ISO_8859_1)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("StarterCard")) {
                    readStarterCard(line,ressourceMap);
                } else if (line.startsWith("ResourceCard")) {
                    readRessourceCard(line,cornerMap,ressourceMap);
                } else if (line.startsWith("GoldCard")) {
                    readGildingCard(line,cornerMap,ressourceMap);
                } else if (line.startsWith("Objective")) {
                    // ignore for now.
                }
            }
        } catch (Exception e) {
            System.out.println("couldn't read the file "+ "deck.txt" +".");
        }
    }
    private void readStarterCard(String line, Map<String,Ressource> ressourceMap) {
        String[] lineList = line.split(" ");
        Ressource topLeft = ressourceMap.get(lineList[2]);
        Ressource topRight = ressourceMap.get(lineList[3]);
        Ressource botLeft = ressourceMap.get(lineList[4]);
        Ressource botRight = ressourceMap.get(lineList[5]);
        //Verso later.
        StartCard card = new StartCard(topLeft,topRight,botLeft,botRight);
        startCards.add(card);
    }
    private void readRessourceCard(String line, Map<String,Corner> cornerMap, Map<String,Ressource> ressourceMap) {
        String[] lineList = line.split(" ");
        Corner topLeft = cornerMap.get(lineList[2]);
        Corner topRight = cornerMap.get(lineList[3]);
        Corner botLeft = cornerMap.get(lineList[4]);
        Corner botRight = cornerMap.get(lineList[5]);
        Ressource type = ressourceMap.get(lineList[7]);
        int point;
        if (lineList[9].equals("None")) {
            point = 0;
        } else {
            point = (int) lineList[9].charAt(3);
        }
        //Verso later.
        RessourceCard card = new RessourceCard(type,topLeft,topRight,botLeft,botRight,point);
        ressourceCardPile.add(card);
    }
    private void readGildingCard(String line, Map<String,Corner> cornerMap, Map<String,Ressource> ressourceMap) {
        String[] lineList = line.split(" ");
        Corner topLeft = cornerMap.get(lineList[2]);
        Corner topRight = cornerMap.get(lineList[3]);
        Corner botLeft = cornerMap.get(lineList[4]);
        Corner botRight = cornerMap.get(lineList[5]);
        Ressource type = ressourceMap.get(lineList[7]);
        int point;
        int len = lineList.length-1;
        if (lineList[len].equals("None")) {
            point = 0;
        } else {
            point = (int) lineList[len].charAt(3);
        }
        var cost = new ArrayList<Ressource>();
        for (int i = 10 ; i < (len-2); i++) {
            cost.add(ressourceMap.get(lineList[i]));
        }
        //Verso later.
        GildingCard card = new GildingCard(type,point,topLeft,topRight,botLeft,botRight,List.copyOf(cost));
        gildingCardPile.add(card);
    }
}