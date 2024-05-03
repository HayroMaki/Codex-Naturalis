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
import fr.codex.naturalis.drawing.DeckDrawingSequence;
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
    private final Color tertiaryColor;

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
        this.tertiaryColor = new Color(160, 130, 70);
    }
    /**
     * Start the game and its graphical interface.
     */
    public void start(Player player) {
        // start the application, create a drawing area, full screen
        Application.run(backgroundColor, context -> {
            // Get the screen infos :
            var width = context.getScreenInfo().width();
            var height = context.getScreenInfo().height();
            int diff = (int)(ratio/((float) 16/3));
            // Choose a StartCard :
            startingSequence(context,width,height);
            if (placedCards.isEmpty()) {return;}
            // Choose 3 cards :
            for (int i = 0; i < 3; i++) { playerPickSequence(context,player,width,height);}
            player.setDeckClosed();
            for (;;) {
                context.renderFrame(graphics2D -> { graphics2D.clearRect(0,0,width,height);});

                //drawInformationBanner(context,width,height);
                drawPlacedCards(context);
                if (player.deckIsOpen()) {
                    drawDeck(context, player, width, height);
                } else {
                    drawHiddenDeck(context, width, height);
                }

                Event event = context.pollOrWaitEvent(2147483647); //maximum int value
                if (event != null) {
                    switch (event) {
                        case PointerEvent pointerEvent -> {
                            if (pointerEvent.action() == PointerEvent.Action.POINTER_UP) {
                                if(clickIsOnLine(event, width, height) && !player.deckIsOpen()) {
                                    player.setDeckOpen();
                                }
                                if (clickIsOnLine(event, width, height - height / 3) && player.deckIsOpen()) {
                                    player.setDeckClosed();
                                }
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
            graphics2D.setColor(tertiaryColor);
            graphics2D.fillRect(0, height /2-ratio/2, width,ratio/10);
            graphics2D.fillRect(0, height /2+ratio/2-ratio/10, width,ratio/10);
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
    private void playerPickSequence(ApplicationContext context, Player player, int width, int height) {
        Objects.requireNonNull(player);
        var x1 = width/2 - (int)(1.5*ratio);
        var x2 = width/2 + ratio/2;
        var y = height/2 - ratio/4 - ratio;

        RessourceCard ressourceCard1 = ressourceCardPile.getTOC1();
        RessourceCard ressourceCard2 = ressourceCardPile.getTOC2();
        RessourceCard ressourceCardFromPile = ressourceCardPile.getFirstOfPile();
        ressourceCardFromPile.changeCoordinates(x1,y);
        ressourceCard1.changeCoordinates(x1,y + ratio);
        ressourceCard2.changeCoordinates(x1,y + 2*ratio);

        GildingCard gildingCard1 = gildingCardPile.getTOC1();
        GildingCard gildingCard2 = gildingCardPile.getTOC2();
        GildingCard gildingCardFromPile = gildingCardPile.getFirstOfPile();
        gildingCardFromPile.changeCoordinates(x2,y);
        gildingCard1.changeCoordinates(x2,y + ratio);
        gildingCard2.changeCoordinates(x2,y + 2*ratio);

        var cardList = List.of(
                ressourceCard1,ressourceCard2,ressourceCardFromPile,
                gildingCard1,gildingCard2,gildingCardFromPile);

        drawPlayerPick(context, cardList, width, height);
        Card chosenCard = choosePickCard(context, cardList, width, height);
        takeCardFromRightPile(player,chosenCard,ressourceCard1,ressourceCard2,ressourceCardFromPile,gildingCard1,gildingCard2,gildingCardFromPile);
        context.renderFrame(graphics2D -> {graphics2D.clearRect(0,0,width,height);});
    }
    private Card choosePickCard(ApplicationContext context, List<Card> cardList, int width, int height) {
        while (true) {
            drawPlayerPick(context, cardList, width, height);
            Event event = context.pollEvent();
            if (event != null) {
                if (event instanceof PointerEvent pointerEvent) {
                    if (pointerEvent.action() == PointerEvent.Action.POINTER_UP) {
                        int eventX = pointerEvent.location().x();
                        int eventY = pointerEvent.location().y();
                        for (Card card:cardList) {
                            int cardX = card.getXCoordinate();
                            int cardY = card.getYCoordinate();
                            if ((eventX >= cardX && eventX <= cardX+ratio) && (eventY >= cardY && eventY <= cardY+ratio/2)) {
                                return card;
    }   }   }   }   }   }   }
    private void drawPlayerPick(ApplicationContext context, List<Card> cardList, int width, int height) {
        context.renderFrame(graphics2D -> {
            graphics2D.setColor(secondaryColor);
            graphics2D.fillRect(0, height /2-ratio/2-ratio, width,3*ratio);
            graphics2D.setColor(tertiaryColor);
            graphics2D.fillRect(0, height /2-ratio/2-ratio, width,ratio/10);
            graphics2D.fillRect(0, height /2+3*(ratio/2)-ratio/10, width,ratio/10);
            var cardDrawingSequence = new CardDrawingSequence(graphics2D, ratio);
            for (Card card : cardList) {
                cardDrawingSequence.drawCard(card);
            }
        });
    }
    private void takeCardFromRightPile(Player player, Card chosenCard,
                                       RessourceCard ressourceCard1, RessourceCard ressourceCard2, RessourceCard ressourceCardFromPile,
                                       GildingCard gildingCard1, GildingCard gildingCard2, GildingCard gildingCardFromPile) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(chosenCard);
        Objects.requireNonNull(ressourceCard1);
        Objects.requireNonNull(ressourceCard2);
        Objects.requireNonNull(ressourceCardFromPile);
        Objects.requireNonNull(gildingCard1);
        Objects.requireNonNull(gildingCard2);
        Objects.requireNonNull(gildingCardFromPile);
        if (chosenCard instanceof RessourceCard) {
            if (chosenCard.equals(ressourceCard1)) { player.addCard(ressourceCardPile.removeTOC1()); return;}
            if (chosenCard.equals(ressourceCard2)) { player.addCard(ressourceCardPile.removeTOC2()); return;}
            if (chosenCard.equals(ressourceCardFromPile)) { player.addCard(ressourceCardPile.removeFromPile());}
        } else if (chosenCard instanceof GildingCard) {
            if (chosenCard.equals(gildingCard1)) { player.addCard(gildingCardPile.removeTOC1()); return;}
            if (chosenCard.equals(gildingCard2)) { player.addCard(gildingCardPile.removeTOC2()); return;}
            if (chosenCard.equals(gildingCardFromPile)) { player.addCard(gildingCardPile.removeFromPile());}
        } else { throw new IllegalArgumentException();}
    }
    private void startPlacingSequence(ApplicationContext context ,int width, int height, int diff) {
        var availableCornerList = PlacingCorner.getCornersList(placedCards,width,height,diff,ratio);
    }
    private void drawAvailableCorners(ApplicationContext context, List<PlacingCorner> availableCornerList, int width, int height, int diff) {
        int size = (height / 2) - (height / 10);
        int arc = (width / 5);
        context.renderFrame(graphics2D -> {
            for (PlacingCorner corner : availableCornerList) {
                graphics2D.setColor(new Color(190, 255, 255));
                graphics2D.drawRoundRect(corner.x(),corner.y(),size,size,arc,arc);
            }
        });
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
            ressourceCardPile.setTOC1(); ressourceCardPile.setTOC2();
            gildingCardPile.setTOC1(); gildingCardPile.setTOC2();
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
        Ressource type = ressourceMap.get("R:"+lineList[7]);
        int point;
        if (lineList[9].equals("None")) {
            point = 0;
        } else {
            point = Integer.parseInt(lineList[9].split(":")[1]);
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
        Ressource type = ressourceMap.get("R:"+lineList[7]);
        int point;
        int len = lineList.length-1;
        if (lineList[len].equals("None")) {
            point = 0;
        } else {
            point = Integer.parseInt(lineList[len].split(":")[1]);
        }
        var cost = new ArrayList<Ressource>();
        for (int i = 9 ; i < (len-1); i++) {
            cost.add(ressourceMap.get("R:"+lineList[i]));
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

    private void drawHiddenDeck(ApplicationContext context, int width, int height) {
        Objects.requireNonNull(context);
        context.renderFrame(graphics2D -> {
            var hiddenDeckDrawingSequence = new DeckDrawingSequence(graphics2D, width, height, secondaryColor, tertiaryColor);
            hiddenDeckDrawingSequence.drawDeckLine(0, height-height/32);
        });
    }

    private void drawDeck(ApplicationContext context, Player player, int width, int height) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(context);
        player.setDeckOpen();
        context.renderFrame(graphics2D -> {
            var deckDrawingSequence = new DeckDrawingSequence(graphics2D, width, height, secondaryColor, tertiaryColor);
            deckDrawingSequence.drawDeck(0, height-height/3);
            var drawingSequence = new CardDrawingSequence(graphics2D, ratio);
            double count = 0.5;
            for(Card card : player.getCards()) {
                //card.changeCoordinates(card.getXCoordinate()count+ratio, height-(height/4));
                card.changeCoordinates((int) (ratio*count), height - (height/4));
                drawingSequence.drawCard(card);
                count+= (double) 5/3;
            }
            drawHiddenDeck(context, width, height-height/3);
        });
    }
    private boolean clickIsOnLine(Event event, int width, int height) {
        if(event == null) {
            return false;
        }
        return event instanceof PointerEvent && ((PointerEvent) event).location().x() >= 0 && ((PointerEvent) event).location().x() <= width && ((PointerEvent) event).location().y() <= height && ((PointerEvent) event).location().y() >= height-height/32;
    }
    private Card playerSelectedACard(Event event, Player player) {
        if (event != null) {
            if(event instanceof PointerEvent pointerEvent) {
                if (pointerEvent.action()== PointerEvent.Action.POINTER_UP) {
                    int x = pointerEvent.location().x();
                    int y = pointerEvent.location().y();
                    for (Card card : player.getCards()) {
                        int cardX = card.getXCoordinate();
                        int cardY = card.getYCoordinate();
                        if(x >= cardX && x <= cardX+ratio && y >= cardY && y <= cardY+ratio/2) {
                            return card;
                        }
                    }
                }
            }
        }
        return null;
    }
}