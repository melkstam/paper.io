package se.liu.ida.paperio;

import java.awt.*;

public class HumanPlayer implements Player {

    private int x;
    private int y;
    private int size;
    private Color color;

    public HumanPlayer(){
        x = 100;
        y = 100;
        size = 100;
        color = Color.PINK;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public void move(){
        x += 2;
    }
}
