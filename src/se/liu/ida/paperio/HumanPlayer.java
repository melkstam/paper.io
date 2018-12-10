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
    private int[][] tilesOwned;
    private Tile[][] tilesContested;

    public HumanPlayer(int height, int width){
        this.height = height;
        this.width = width;
        x = (int)(Math.random() * width);
        y = (int)(Math.random() * height);
        color = new Color(255,105,180, 255);
        tilesOwned = new int[100][100];
        tilesContested = new Tile[100][100];
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

    @Override
    public int[][] getTilesOwned() {
        return this.tilesOwned;
    }

    @Override
    public void setTilesOwned() {
        tilesOwned[getX()][getY()] = 1;
        int counter = 0;

        for (int i = 0; i < tilesOwned.length; i++){
            for(int j = 0; j < tilesOwned[i].length; j++){
                if(tilesOwned[i][j] == 1){
                    counter++;
                    System.out.println(counter);
                }
            }
        }
    }

    @Override
    public Tile[][] getTilesContested() {
        return this.tilesContested;
    }

    @Override
    public void setTilesContested() {
    }

    public void move(){
        x += dx;
        y += dy;
    }

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
