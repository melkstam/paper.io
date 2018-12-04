package se.liu.ida.paperio;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HumanPlayer implements Player {

    private int x;
    private int y;
    private int dx;
    private int dy;
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
        x += dx;
        y += dy;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
            dy = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
            dy = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -2;
            dx = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 2;
            dx = 0;
        }
    }
}
