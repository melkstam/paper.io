package se.liu.ida.paperio;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HumanPlayer implements Player {

    private int x;
    private int y;
    private int dx;
    private int dy;
    private int height;
    private int width;
    private Color color;

    public HumanPlayer(int height, int width){
        this.height = height;
        this.width = width;
        x = (int)(Math.random() * width);
        y = (int)(Math.random() * height);
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
            dx = -1;
            dy = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
            dy = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -1;
            dx = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 1;
            dx = 0;
        }
    }
}
