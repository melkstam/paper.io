package se.liu.ida.paperio;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class HumanPlayer implements Player {

    private int x;
    private int y;
    private int dx;
    private int dy;
    private int height;
    private int width;
    private Color color;
    private ArrayList<Tile> tilesOwned = new ArrayList<Tile>();
    private ArrayList<Tile> tilesContested = new ArrayList<Tile>();

    public HumanPlayer(int height, int width){
        this.height = height;
        this.width = width;

        x = (int)(Math.random() * width);
        y = (int)(Math.random() * height);
        color = new Color(255,105,180, 255);
    }

    /**
     * @return X position on the map
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * @return Y position on the map
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * @return Player's color
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Moves the player in different directions
     */
    public void move(){
        x += dx;
        y += dy;
    }

    /**
     * @param t Sets tile as owned
     */
    @Override
    public void setTilesOwned(Tile t) {
        tilesOwned.add(t);
    }

    /**
     * @return List of all Tiles the player owns
     */
    @Override
    public ArrayList<Tile> getTilesOwned() {
        return tilesOwned;
    }

    /**
     * @param t Tile that is being contested
     */
    @Override
    public void setTilesContested(Tile t) {
        tilesContested.add(t);
        t.setContestedOwner(this);
        System.out.println(tilesContested.size());
    }

    @Override
    public void contestToOwned() {
        for (Tile t : tilesContested) {
            t.setOwner(this);
            System.out.println("Added Tile");
        }
        tilesContested.clear();
    }

    /**
     * @return List of all Tiles the player is contesting
     */
    @Override
    public ArrayList<Tile> getTilesContested() {
        return tilesContested;
    }

    /** Handles player movement controls
     * @param e Key that is being pressed
     */
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && dx != 1) {
            dx = -1;
            dy = 0;
        }

        if (key == KeyEvent.VK_RIGHT && dx != -1) {
            dx = 1;
            dy = 0;
        }

        if (key == KeyEvent.VK_UP && dy != 1) {
            dx = 0;
            dy = -1;
        }

        if (key == KeyEvent.VK_DOWN && dy != -1) {
            dx = 0;
            dy = 1;
        }
    }
}
