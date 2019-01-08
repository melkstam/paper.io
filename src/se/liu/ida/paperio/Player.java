package se.liu.ida.paperio;

import java.awt.*;
import java.util.ArrayList;

/**
 * An abstract class for a general player in the game. Human player and bot player differs a bit but their common logic
 * is specified here. It keeps track of players position, speed, color, owned and contested tiles and name. Two players
 * can also be compared that compares number of owned tiles of the player.
 */
abstract class Player implements Comparable<Player> {

    int x;
    int y;
    int dx;
    int dy;
    private Color color;
    private ArrayList<Tile> tilesOwned = new ArrayList<>();
    private ArrayList<Tile> tilesContested = new ArrayList<>();
    int height;
    int width;
    String name;

    private Boolean isAlive = true;

    private Tile currentTile;

    // TODO Make sure players start on a non-occupied spot

    /**
     * Initializes a player on a random spot on the game area with specified color
     * @param height height of game area player is constructed in
     * @param width width of game area player is constructed in
     * @param color the color of the player
     */
    Player(int height, int width, Color color){
        x = (int)(Math.random() * (width - 2) +1);
        y = (int)(Math.random() * (height - 2) +1);
        this.color = color;
        this.height = height;
        this.width = width;

        double rand = Math.random();
        if (rand < 0.25) {
            dx = 1;
            dy = 0;
        } else if (rand < 5) {
            dx = -1;
            dy = 0;
        } else if (rand < 0.75) {
            dx = 0;
            dy = 1;
        } else {
            dx = 0;
            dy = -1;
        }
    }

    /**
     * The x position in the tile system
     * @return x position in the tile system
     */
    int getX(){
        return x;
    }

    /**
     * The y position in the tile system
     * @return y position in the tile system
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
     * Logic for when player gets killed
     */
    void die() {
        isAlive = false;
        ArrayList<Tile> ownedTilesCopy = (ArrayList<Tile>)tilesOwned.clone();
        ArrayList<Tile> contestedTilesCopy = (ArrayList<Tile>)tilesContested.clone();
        for(int i = 0; i < ownedTilesCopy.size(); i++){
            ownedTilesCopy.get(i).setOwner(null);
        }

        for(int i = 0; i < contestedTilesCopy.size(); i++){
            contestedTilesCopy.get(i).setContestedOwner(null);
        }
        tilesOwned.clear();
        tilesContested.clear();
        currentTile = null;

    }

    /**
     * Add tile to players list of owned tiles
     * @param t Tile to be added to players owned list
     */
    void setTileOwned(Tile t){
        tilesOwned.add(t);
        t.setOwner(this);
        t.setContestedOwner(null);
    }

    /**
     * Remove a tile from owned
     * @param t tile to be removed from owned
     */
    void removeTileOwned(Tile t){
        tilesOwned.remove(t);
    }

    /**
     * Get tiles owned by player
     * @return Tiles owned by player
     */
    ArrayList<Tile> getTilesOwned(){
        return tilesOwned;
    }

    /**
     * Get as a percentage how much of the total game area a player owns
     * @return percentage of how much of the total game area a player owns
     */
    double getPercentOwned(){
        return 100 * getTilesOwned().size() / (double)(height*width);
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

    /**
     * Kills the player contesting a tile when travelling on it
     * @param t tile which contested owner should get killed
     */
    void checkCollision(Tile t){
        if(t.getContestedOwner() != null) {
            t.getContestedOwner().die();
        }
    }

    /**
     * Get current tile player is on
     * @return Tile player is on currently
     */
    Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * Set tile to be current tile
     * @param currentTile tile to be set as current tile
     */
    void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    /**
     * Get the players speed in x direction
     * @return Players speed in x direction
     */
    int getDx() {
        return dx;
    }

    /**
     * Get the players speed in y direction
     * @return Players speed in y direction
     */
    int getDy() {
        return dy;
    }

    /**
     * Get name of player
     * @return Name of player
     */
    String getName() {
        return name;
    }

    /**
     * Get alive state of player
     * @return alive state of player
     */
    Boolean getAlive() {
        return isAlive;
    }

    /**
     * Compares two players by the number of tiles owned.
     * @param player Player to compare this to
     * @return 1 if this owns more tiles than player, -1 if player owns more tiles than this or 0 otherwise
     */
    public int compareTo(Player player){
        return Integer.compare(player.getTilesOwned().size(), tilesOwned.size());
    }
}
