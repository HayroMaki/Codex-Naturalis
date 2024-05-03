package fr.codex.naturalis.drawing;

import fr.codex.naturalis.card.Card;
import fr.codex.naturalis.card.GildingCard;
import fr.codex.naturalis.card.RessourceCard;
import fr.codex.naturalis.card.StartCard;
import fr.codex.naturalis.corner.Artifact;
import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.corner.Ressource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardDrawingSequence {
    private final Graphics2D graphics2D;
    int width;
    int height;
    int cornerSize;
    int arcDiameter;
    public CardDrawingSequence(Graphics2D graphics2D, int width) {
        Objects.requireNonNull(graphics2D);
        this.graphics2D = graphics2D;
        this.width = width;
        height = width / 2;
        cornerSize = (height / 2) - (height / 10);
        arcDiameter = (width / 5);
    }
    /**
     * Draw a card in the graphical interface.
     *
     * @param card the card to draw.
     */
    public void drawCard(Card card) {
        Objects.requireNonNull(card);
        int x = card.getXCoordinate();
        int y = card.getYCoordinate();
        // Draw the card's body.
        graphics2D.setColor(card.getColor());
        graphics2D.fillRoundRect(x, y, width, height, arcDiameter, arcDiameter);

        //Draw the card's corners.
        if (!card.isVerso()) {
            if (card instanceof StartCard) drawStartRecto(card, x, y);
            else if (card instanceof RessourceCard) drawRessourceCardRecto(card, x, y);
            else if (card instanceof GildingCard) drawGildingCardRecto(card, x, y);

        } else if (card.isVerso()) {
            if (card instanceof StartCard); //drawStartVerso(card);
            else {
                drawNormalVerso(card);
            }
        }
    }

    public void drawNormalVerso(Card card) {
        Objects.requireNonNull(card);
        int x = card.getXCoordinate();
        int y = card.getYCoordinate();

        // Draws the card's shape and fills it.
        graphics2D.setColor(card.getColor());
        graphics2D.fillRoundRect(x, y, width, height, arcDiameter, arcDiameter);
        if(card instanceof RessourceCard) {
            ((RessourceCard) card).verso(this, width, cornerSize);
        }
        if (card instanceof GildingCard) {
            ((GildingCard) card).verso(this, width, cornerSize);
        }
    }

    /**
     * Draw the recto side of a RessourceCard.
     *
     * @param card the card.
     * @param x
     * @param y
     */
    private void drawRessourceCardRecto(Card card, int x, int y) {
        drawEveryCorners(card, x, y);
        if (((RessourceCard)card).getPoint() > 0) {
            drawPointBanner(((RessourceCard)card).getPoint(), x + width/2, y);
        }
    }
    /**
     * Draw the recto side of a GildingCard.
     *
     * @param card the card.
     * @param x
     * @param y
     */
    private void drawGildingCardRecto(Card card, int x, int y) {
        drawEveryCorners(card, x, y);
        drawPointBanner(((GildingCard)card).getPoint(), x + width/2, y);
    }
    /**
     * Draw the recto side of a StartCard.
     *
     * @param card the card.
     * @param x
     * @param y
     */
    private void drawStartRecto(Card card, int x, int y) {
        drawEveryCorners(card, x, y);

        int centerX = x + width/2 - width/16; int centerY = y + width/4 - width/16;

        draw4EmptyCircles(centerX, centerY);
        //draw4CirclesOutlines(centerX, centerY);
    }
    /**
     * Draw the 4 circles in the middle of a StartCard's recto (used for drawStartCircles).
     *
     * @param centerX
     * @param centerY
     */
    private void draw4EmptyCircles(int centerX, int centerY) {
        graphics2D.setColor(new Color(176, 159, 34));

        graphics2D.fillOval(centerX - width /10, centerY, width /8, width /8);
        graphics2D.fillOval(centerX + width /10, centerY, width /8, width /8);
        graphics2D.fillOval(centerX, centerY + width /10, width /8, width /8);
        graphics2D.fillOval(centerX, centerY - width /10, width /8, width /8);
    }
    /**
     * Draw the outlines of the 4 circles in the middle of a StartCard's recto (used for drawStartCircles).
     *
     * @param centerX
     * @param centerY
     */
    private void draw4CirclesOutlines(int centerX, int centerY) {
        graphics2D.setColor(new Color(87, 66, 11));

        graphics2D.drawOval(centerX - width /10, centerY, width /8, width /8);
        graphics2D.drawOval(centerX + width /10, centerY, width /8, width /8);
        graphics2D.drawOval(centerX, centerY + width /10, width /8, width /8);
        graphics2D.drawOval(centerX, centerY - width /10, width /8, width /8);

        graphics2D.drawOval(centerX - width /10 - width /109, centerY - width /109, width /7, width /7);
        graphics2D.drawOval(centerX + width /10 - width /109, centerY - width /109, width /7, width /7);
        graphics2D.drawOval(centerX - width /109, centerY - width /10 - width /109, width /7, width /7);
        graphics2D.drawOval(centerX - width /109, centerY + width /10 - width /109, width /7, width /7);
    }

    private void drawVerso() {}

    /**
     * Draw every corner of a card.
     *
     * @param card       the card (to know the type).
     * @param x          the x coordinate of where to print the corner.
     * @param y          the y coordinate of where to print the corner.
     */
    private void drawEveryCorners(Card card, int x, int y) {
        List<Corner> corners = card.getAllCorners();
        for (int i = 0; i <= 3; i++)
            drawCorner(card, corners.get(i),x, y, i);
    }
    /**
     * Draw the Corner, and, if there is one, it's logo (used for the drawEveryCorners method).
     *
     * @param card       the card (to know the type).
     * @param corner     the corner to print.
     * @param x          the x coordinate of where to print the corner.
     * @param y          the y coordinate of where to print the corner.
     * @param whichCorner an int symbolizing which corner we're treating (0=topLeft, 1=topRight, 2=bottomLeft, 3=bottomRight).
     */
    private void drawCorner(Card card, Corner corner, int x, int y, int whichCorner) {
        int differX = 0; int differY = 0;
        switch (whichCorner) {
            case 0:
                break;
            case 1:
                x += width - cornerSize; differX = width/80;
                break;
            case 2:
                y += width/2 - cornerSize; differY = width/80;
                break;
            case 3:
                x += width - cornerSize; y += width/2 - cornerSize;
                differX = width/80; differY = width/80;
                break;
            default:
                throw new AssertionError();
        }
        if (corner != Corner.invisible) {
            drawBasicCorners(card, x, y, whichCorner, differX, differY);

            if (corner instanceof Ressource) {
                drawRessource((Ressource) corner, x+differX, y+differY);
            } else if (corner instanceof Artifact) {
                drawArtifact((Artifact) corner, x+differX, y+differY);
            }
        }
    }
    /**
     *
     *
     * @param card
     * @param x
     * @param y
     * @param whichCorner
     * @param differX
     * @param differY
     */
    private void drawBasicCorners(Card card, int x, int y, int whichCorner, int differX, int differY) {
        int arc = width / 5;
        //Draw the outline.
        setColorFromCardType(card);
        graphics2D.fillRoundRect(x, y, cornerSize, cornerSize, arc, arc);
        drawCornersAngles(x, y, whichCorner);

        //Draw the inside.
        graphics2D.setColor(new Color(227, 219, 182));
        graphics2D.fillRoundRect((x + differX), (y + differY), cornerSize - width /80, cornerSize - width /80, arc, arc);
        drawEmptyCornersAngles((x + differX), (y + differY), whichCorner);
    }
    /**
     * Set the graphics2D color depending on the card.
     *
     * @param card       the card (to know the type).
     */
    private void setColorFromCardType(Card card) {
        if (card instanceof RessourceCard)
            graphics2D.setColor(((RessourceCard) card).getSecondaryColor());
        else if (card instanceof GildingCard || card instanceof StartCard)
            graphics2D.setColor(new Color(176, 159, 34));
    }
    /**
     * Draw the 90° Angles of the corner depending on th side.
     *
     * @param x          the x coordinate of where to print the Angle.
     * @param y          the y coordinate of where to print the Angle.
     * @param whichCorner an int symbolizing which corner we're treating (0=topLeft, 1=topRight, 2=bottomLeft, 3=bottomRight).
     */
    private void drawCornersAngles(int x, int y, int whichCorner) {
        switch (whichCorner) {
            case 0, 3:
                graphics2D.fillRect(x,(y + cornerSize - arcDiameter /2),(arcDiameter /2),(arcDiameter /2));
                graphics2D.fillRect((x + cornerSize - arcDiameter /2), y,(arcDiameter /2),(arcDiameter /2));
                break;
            case 1, 2:
                graphics2D.fillRect(x, y,(arcDiameter /2),(arcDiameter /2));
                graphics2D.fillRect((x + cornerSize - arcDiameter /2),(y + cornerSize - arcDiameter /2),(arcDiameter /2),(arcDiameter /2));
                break;
        }
    }
    /**
     * Draw the 90° Angles of the corner depending on th side.
     *
     * @param x          the x coordinate of where to print the Angle.
     * @param y          the y coordinate of where to print the Angle.
     * @param whichCorner an int symbolizing which corner we're treating (0=topLeft, 1=topRight, 2=bottomLeft, 3=bottomRight).
     */
    private void drawEmptyCornersAngles(int x, int y, int whichCorner) {
        int CalculatedY = y + cornerSize - arcDiameter / 2 - width / 80;
        int CalculatedX = x + cornerSize - arcDiameter / 2 - width / 80;
        switch (whichCorner) {
            case 0, 3:
                graphics2D.fillRect(x, CalculatedY,(arcDiameter /2),(arcDiameter /2));
                graphics2D.fillRect(CalculatedX, y,(arcDiameter /2),(arcDiameter /2));
                break;
            case 1, 2:
                graphics2D.fillRect(x, y,(arcDiameter /2),(arcDiameter /2));
                graphics2D.fillRect(CalculatedX, CalculatedY,(arcDiameter /2),(arcDiameter /2));
                break;
        }
    }

    /**
     *
     *
     * @param ressource
     * @param x
     * @param y
     */
    public void drawRessource(Ressource ressource, int x, int y) {
        if (ressource.equals(Corner.animal)) {
            drawAnimal(ressource, x, y);
        } else if (ressource.equals(Corner.fungi)) {
            drawFungi(ressource, x, y);
        } else if (ressource.equals(Corner.insect)) {
            drawInsect(ressource, x, y);
        } else if (ressource.equals(Corner.plant)) {
            drawPlant(ressource, x, y);
        }
    }

    /**
     *
     *
     * @param artifact
     * @param x
     * @param y
     */
    private void drawArtifact(Artifact artifact, int x, int y) {
        if (artifact.equals(Corner.scroll)) {
            drawScroll(artifact, x, y);
        } else if (artifact.equals(Corner.ink)) {
            drawInk(artifact, x, y);
        } else if (artifact.equals(Corner.feather)) {
            drawFeather(artifact, x, y);
        }
    }

    /**
     * Draw the card's point banner and the number of point inside of it.
     *
     * @param point the point value of the card.
     * @param x the x coordinate at which the banner will be drawn (center of the banner).
     * @param y the y coordinate at which the banner will be drawn (top of the banner).
     */
    private void drawPointBanner(int point, int x, int y) {
        //create the polygon
        var polygon = new Polygon();
        char chrPoint = (char)('0'+point);
        char[] strPoint = {chrPoint};
        polygon.addPoint(x - width/20, y);
        polygon.addPoint(x + width/20, y);
        polygon.addPoint(x + width/20, y + width/15);
        polygon.addPoint(x, y + width/10);
        polygon.addPoint(x - width/20, y + width/15);
        //draw the polygon
        graphics2D.setColor(new Color(176, 159, 34));
        graphics2D.fillPolygon(polygon);
        graphics2D.setColor(new Color(87, 66, 11));
        // graphics2D.drawPolygon(polygon); //outline
        graphics2D.setFont(new Font("grand", Font.BOLD,width/12));
        graphics2D.drawChars(strPoint,0,1,x-width/38, y + width/14);
    }

    private void drawAnimal(Ressource ressource, int x, int y) {
        graphics2D.setColor(ressource.getSecondaryColor());
        graphics2D.fillRect(x+cornerSize/5, y+cornerSize/5, cornerSize/2, cornerSize/2);
    }

    private void drawFungi(Ressource ressource, int x, int y) {
        graphics2D.setColor(ressource.getSecondaryColor());
        graphics2D.fillRect(x+cornerSize/5, y+cornerSize/5, cornerSize/2, cornerSize/2);
    }

    private void drawInsect(Ressource ressource, int x, int y) {
        graphics2D.setColor(ressource.getSecondaryColor());
        graphics2D.fillRect(x+cornerSize/5, y+cornerSize/5, cornerSize/2, cornerSize/2);
    }

    private void drawPlant(Ressource ressource, int x, int y) {
        graphics2D.setColor(ressource.getSecondaryColor());
        graphics2D.fillRect(x+cornerSize/5, y+cornerSize/5, cornerSize/2, cornerSize/2);
    }

    private void drawScroll(Artifact artifact, int x, int y) {
        graphics2D.setColor(artifact.getMainColor());
        graphics2D.fillRect(x+cornerSize/4, y+cornerSize/4, cornerSize/2, cornerSize/2);
    }

    private void drawInk(Artifact artifact, int x, int y) {
        graphics2D.setColor(artifact.getMainColor());
        graphics2D.fillRect(x+cornerSize/4, y+cornerSize/4, cornerSize/2, cornerSize/2);
    }

    private void drawFeather(Artifact artifact, int x, int y) {
        graphics2D.setColor(artifact.getMainColor());
        graphics2D.fillRect(x+cornerSize/4, y+cornerSize/4, cornerSize/2, cornerSize/2);
    }
}


