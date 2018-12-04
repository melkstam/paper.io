package se.liu.ida.paperio;

import java.awt.Color;

public interface Player {

    public int getX();
    public int getY();
    public int getSize();
    public Color getColor();
    public void move();

}
