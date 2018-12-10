package se.liu.ida.paperio;

import java.awt.Color;
import java.util.ArrayList;

public interface Player {

    public int getX();
    public int getY();
    public Color getColor();
    public void move();

    public void setTilesOwned(Tile t);
    public ArrayList<Tile> getTilesOwned();

    public void setTilesContested(Tile t);
    public ArrayList<Tile> getTilesContested();

    public void contestToOwned();
}
