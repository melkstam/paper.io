package se.liu.ida.paperio;

import java.awt.Color;

public interface Player {

    public int getX();
    public int getY();
    public Color getColor();
    public void move();

    public void setTilesOwned();
    public int[][] getTilesOwned();

    public void setTilesContested();
    public Tile[][] getTilesContested();
}
