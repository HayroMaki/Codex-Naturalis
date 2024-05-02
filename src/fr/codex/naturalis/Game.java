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
import fr.codex.naturalis.drawing.RessourceDrawingSequence;
import fr.codex.naturalis.placing.PlacingCorner;
import fr.codex.naturalis.player.Player;

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
    private final Color secondaryColor;

    public Game(int ratio) {
        placedCards = new ArrayList<Card>();

        if (ratio < 100) throw new IllegalArgumentException("ratio must be at least of 100.");
        this.ratio = ratio;

        ressourceCardPile = new Pile<RessourceCard>();
        gildingCardPile = new Pile<GildingCard>();
        startCards = new ArrayList<>();
        createPiles();

        this.backgroundColor = new Color(250, 240, 230);
        this.secondaryColor = new Color(200, 170, 140);
    }
    /**
     * Start the game and its graphical interface.
     */
    public void start() {
        // start the application, create a drawing area, full screen
        Application.run(backgroundColor, context -> {
            // Get the screen infos :
            var width = context.getScreenInfo().width();
            var height = context.getScreenInfo().height();
            int diff = (int)(ratio/((float) 16/3));

            // Choose a StartCard :
            startingSequence(context,width,height);
            if (placedCards.isEmpty()) {return;}

            for (;;) {
                drawInformationBanner(context,width,height);
                drawPlacedCards(context);

                Event event = context.pollOrWaitEvent(2147483647); //maximum int value
                if (event != null) {
                    System.out.println(event);
                    switch (event) {
                        case PointerEvent pointerEvent -> {
                            if (pointerEvent.action() == PointerEvent.Action.POINTER_UP) {
                                System.out.println(pointerEvent.location());
                            }
                        }
                        case KeyboardEvent keyboardEvent -> {
                            // if the key is "escape" :
                            if (keyboardEvent.key() == KeyboardEvent.Key.ESCAPE) {
                                // close the screen area.
                                context.dispose();
                                return;
                            }
                        }
                    }
                }
                context.renderFrame(graphics2D -> { graphics2D.clearRect(0,0,width,height);});
            }
        });
    }
    /**
     * The starting sequence that will let the player choose between 2 random StartCard and place the one he chooses.
     * @param context the ApplicationContext.
     * @param width the screen width.
     * @param height the screen height.
     */
    private void startingSequence(ApplicationContext context, int width, int height) {
        Objects.requireNonNull(context);
        StartCard chosenCard = chooseStartingCard(context, width, height);
        chosenCard.place(width/2-ratio/2,height/2-ratio/4);
        context.renderFrame(graphics2D -> {graphics2D.clearRect(0,0,width,height);});
    }
    /**
     * Take 2 random cards and place them in the middle of the screen for the player to choose one.
     * @param context the ApplicationContext.
     * @param width the screen width.
     * @param height the screen height.
     * @return the chosen card.
     */
    private StartCard chooseStartingCard(ApplicationContext context, int width, int height) {
        Objects.requireNonNull(context);
        var x1 = width/2-(int)(1.5*ratio); var y1 = height/2-ratio/4;
        var x2 = width/2+ratio/2;   var y2 = height/2-ratio/4;

        StartCard card1 = randomStartCard();
        startCards.remove(card1);
        card1.changeCoordinates(x1,y1);

        StartCard card2 = randomStartCard();
        startCards.remove(card2);
        card2.changeCoordinates(x2,y2);

        drawStartCardSelection(context, width, height, card1, card2);
        return checkStartCardSelection(context, width, height, x1, y1, card1, x2, y2, card2);
    }
    /**
     * draw the StartCard selection banner and the 2 cards randomly taken.
     * @param context the ApplicationContext.
     * @param width the screen width.
     * @param height the screen height.
     * @param card1 the first card.
     * @param card2 the second card.
     */
    private void drawStartCardSelection(ApplicationContext context, int width, int height, StartCard card1, StartCard card2) {
        context.renderFrame(graphics2D -> {
            graphics2D.setColor(secondaryColor);
            graphics2D.fillRect(0, height /2-ratio/2, width,ratio);
            var cardDrawingSequence = new CardDrawingSequence(graphics2D, ratio);
            cardDrawingSequence.drawCard(card1);
            cardDrawingSequence.drawCard(card2);
        });
    }
    /**
     * Loop until the player chooses one of the two StartCard.
     * @param context the ApplicationContext.
     * @param width the screen width.
     * @param height the screen height.
     * @param x1 the x coordinate of the first card.
     * @param y1 the y coordinate of the first card.
     * @param card1 the first card.
     * @param x2 the x coordinate of the second card.
     * @param y2 the y coordinate of the second card.
     * @param card2 the second card.
     * @return the chosen card.
     */
    private StartCard checkStartCardSelection(ApplicationContext context,int width, int height, int x1, int y1, StartCard card1, int x2, int y2, StartCard card2) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(card1);
        Objects.requireNonNull(card2);
        StartCard chosenCard = null;
        while (true) {
            drawStartCardSelection(context, width, height, card1, card2);
            Event event = context.pollEvent();
            if (event != null) {
                if (event instanceof PointerEvent pointerEvent) {
                    if (pointerEvent.action() == PointerEvent.Action.POINTER_UP) {
                        if ((pointerEvent.location().x() >= x1 && pointerEvent.location().x() <= x1 + ratio) &&
                            (pointerEvent.location().y() >= y1 && pointerEvent.location().y() <= y1 + ratio / 2)) {
                            chosenCard = card1;
                            placedCards.add(card1);
                            startCards.add(card2);
                            return chosenCard;
                        }
                        if ((pointerEvent.location().x() >= x2 && pointerEvent.location().x() <= x2 + ratio) &&
                            (pointerEvent.location().y() >= y2 && pointerEvent.location().y() <= y2 + ratio / 2)) {
                            chosenCard = card2;
                            placedCards.add(card2);
                            startCards.add(card1);
                            return chosenCard;
                        }
                    }
                }
            }
        }
    }
    /**
     * take a random StartCard from the startCards Array, if the array is empty, throw an Exception.
     * @return a random StartCard.
     */
    private StartCard randomStartCard() {
        if (!startCards.isEmpty()) {
            Random rand = new Random();
            return startCards.get(rand.nextInt(startCards.size()));
        }
        throw new EmptyStackException();
    }
    private void playerPickCard(Player player) {

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

    public void drawInformationBanner(ApplicationContext context ,int width, int height) {
        context.renderFrame(graphics2D -> {
            RessourceDrawingSequence ressourceDrawingSequence = new RessourceDrawingSequence(graphics2D,width);
            ressourceDrawingSequence.drawInformationBanner();
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
}