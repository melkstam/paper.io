package se.liu.ida.paperio;

import java.awt.*;
import java.util.ArrayList;

public class BotPlayer implements Player{

    private int x;
    private int y;
    private int dx;
    private int dy;
    private int height;
    private int width;
    private Color color;
    private ArrayList<Tile> tilesOwned = new ArrayList<Tile>();
    private ArrayList<Tile> tilesContested = new ArrayList<Tile>();


    public BotPlayer(int height, int width){
        this.height = height;
        this.width = width;

        this.dx = 1;
        this.dy = 0;

        x = (int)(Math.random() * (width - 2)) + 1;
        y = (int)(Math.random() * (height - 2)) + 1;
        color = new Color((int)(Math.random() * 0x1000000));
    }


    /**
     * Return the bots x-posistion in the tile system
     * @return      x-posistion in tile system
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * Return the bots y-posistion in the tile system
     * @return      y-posistion in tile system
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * Returns the color of the bot
     * @return      Color of bot player
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Decides where the bot shall move and moves accordingly
     */
    @Override
    public void move() {
        double rand = Math.random();
        if(rand < 0.05){
            dx = 1;
            dy = 0;
        }else if(rand < 0.1){
            dx = -1;
            dy = 0;
        }else if(rand < 0.15){
            dx = 0;
            dy = 1;
        }else if(rand < 0.2){
            dx = 0;
            dy = -1;
        }


        x += dx;
        y += dy;
    }

    @Override
    public void setTilesOwned(Tile t) {
        tilesOwned.add(t);
    }

    @Override
    public ArrayList<Tile> getTilesOwned() {
        return tilesOwned;
    }

    @Override
    public void setTilesContested(Tile t) {
        tilesContested.add(t);
        t.setContestedOwner(this);
    }

    @Override
    public ArrayList<Tile> getTilesContested() {
        return tilesContested;
    }

    @Override
    public void contestToOwned() {
        for (Tile t : tilesContested) {
            t.setOwner(this);
            System.out.println("Added Tile");
        }
        tilesContested.clear();
    }
}
