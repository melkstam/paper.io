package se.liu.ida.paperio;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Player {

    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected Color color;
    protected ArrayList<Tile> tilesOwned = new ArrayList<>();
    protected ArrayList<Tile> tilesContested = new ArrayList<>();
    protected int height;
    protected int width;


    public Player(int height, int width){
        x = (int)(Math.random() * (width - 2) +1);
        y = (int)(Math.random() * (height - 2) +1);
        color = new Color((int)(Math.random() * 0x1000000));
        this.height = height;
        this.width = width;
    }


    /**
     * The x position in the tile system
     * @return      x position in the tile system
     */
    public int getX(){
        return x;
    }

    /**
     * The y position in the tile system
     * @return      y position in the tile system
     */
    public int getY(){
        return y;
    }

    /**
     * @return color of the player
     */
    public Color getColor(){
        return color;
    }


    /**
     * Abstract method to move the player
     */
    public abstract void move();


    /**
     * Add tile to players list of owned tiles
     * @param t Tile to be added to players owned list
     */
    public void setTileOwned(Tile t){
        tilesOwned.add(t);
        t.setOwner(this);
    }

    /**
     * Get tiles owned by player
     * @return Tiles owned by player
     */
    public ArrayList<Tile> getTilesOwned(){
        return tilesOwned;
    }

    /**
     * Add tile to players list of contested tiles
     * @param t Tile to be added to players contested list
     */
    public void setTileContested(Tile t){
        tilesContested.add(t);
        t.setContestedOwner(this);
    }

    /**
     * Get tiles contested by player
     * @return Tiles contested by player
     */
    public ArrayList<Tile> getTilesContested(){
        return tilesContested;
    }

    /**
     * Sets contested tiles to owned by player
     */
    public void contestToOwned(){
        for (Tile t : tilesContested) {
            setTileOwned(t);
        }
        tilesContested.clear();
    }

    public void checkCollision(Tile t){
        if(t.getContestedOwner() != null || t.getContestedOwner() == this){
            //System.out.println(this + "DIEDED");
        }
    }

    public void death(){
        //System.out.println("You diededed");
    }
}
