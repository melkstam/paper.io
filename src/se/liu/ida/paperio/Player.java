package se.liu.ida.paperio;

import java.awt.Color;
import java.util.ArrayList;

abstract class Player {

    int x;
    int y;
    int dx;
    int dy;
    private Color color;
    private ArrayList<Tile> tilesOwned = new ArrayList<>();
    private ArrayList<Tile> tilesContested = new ArrayList<>();
    int height;
    int width;

    // TODO Make sure players start on a non-occupied spot
    Player(int height, int width, Color color){
        x = (int)(Math.random() * (width - 2) +1);
        y = (int)(Math.random() * (height - 2) +1);
        this.color = color;
        this.height = height;
        this.width = width;
    }

    /**
     * The x position in the tile system
     * @return      x position in the tile system
     */
    int getX(){
        return x;
    }

    /**
     * The y position in the tile system
     * @return      y position in the tile system
     */
    int getY(){
        return y;
    }

    /**
     * @return color of the player
     */
    Color getColor(){
        return color;
    }


    /**
     * Abstract method to move the player
     */
    abstract void move();


    /**
     * Add tile to players list of owned tiles
     * @param t Tile to be added to players owned list
     */
    void setTileOwned(Tile t){
        tilesOwned.add(t);
        t.setOwner(this);
    }

    /**
     * Get tiles owned by player
     * @return Tiles owned by player
     */
    ArrayList<Tile> getTilesOwned(){
        return tilesOwned;
    }

    /**
     * Add tile to players list of contested tiles
     * @param t Tile to be added to players contested list
     */
    void setTileContested(Tile t){
        tilesContested.add(t);
        t.setContestedOwner(this);
    }

    /**
     * Get tiles contested by player
     * @return Tiles contested by player
     */
    ArrayList<Tile> getTilesContested(){
        return tilesContested;
    }

    /**
     * Sets contested tiles to owned by player
     */
    void contestToOwned(){
        for (Tile t : tilesContested) {
            setTileOwned(t);
        }
        tilesContested.clear();
    }

    void checkCollision(Tile t){
        if(t.getContestedOwner() != null) {
            //System.out.println("Trail collision detected");
        }
    }

    int getDx() {
        return dx;
    }

    int getDy() {
        return dy;
    }
}
