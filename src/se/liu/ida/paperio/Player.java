package se.liu.ida.paperio;

import java.awt.*;
import java.util.ArrayList;

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

    void death() {
        isAlive = false;
        ArrayList<Tile> ownedTilesCopy = (ArrayList<Tile>) tilesOwned.clone();
        for(int i = 0; i < ownedTilesCopy.size(); i++){
            ownedTilesCopy.get(i).setOwner(null);
        }

        tilesOwned = ownedTilesCopy;
        System.out.println(tilesOwned.size());
        for(int i = 0; i < tilesContested.size(); i++){
            tilesContested.get(i).setOwner(null);
        }
        tilesOwned.clear();
        tilesContested.clear();
        currentTile = null;
        System.out.println(name + " died.");
    }

    /**
     * Add tile to players list of owned tiles
     * @param t Tile to be added to players owned list
     */
    void setTileOwned(Tile t){
        tilesOwned.add(t);
        t.setOwner(this);
    }

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

    public void setTilesOwned(ArrayList<Tile> tilesOwned) {
        this.tilesOwned = tilesOwned;
    }

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

    void checkCollision(Tile t){
        if(t.getContestedOwner() != null) {
            t.getContestedOwner().death();
        }
    }


    Tile getCurrentTile() {
        return currentTile;
    }

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

    Boolean getAlive() {
        return isAlive;
    }

    void setAlive(Boolean alive) {
        isAlive = alive;
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
