package se.liu.ida.paperio;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HumanPlayer extends Player {

    private int nextKey;

    HumanPlayer(int height, int width, Color color, String name) {
        super(height, width, color);
        this.name = name;
    }

    /**
     * Set key to change dx and dy in next tick
     * @param nextKey key to change dx and dy in next tick
     */
    void setNextKey(int nextKey) {
        this.nextKey = nextKey;
    }

    /**
     * Moves the player in different directions
     */
    @Override
    public void move(){
        x += dx;
        y += dy;
    }


    /**
     * Updates dx and dy regarding to key sent as input
     */
    void updateD() {
        //Left
        if((nextKey == KeyEvent.VK_LEFT || nextKey == KeyEvent.VK_A) && dx != 1) {
            dx = -1;
            dy = 0;
        }

        //Right
        if((nextKey == KeyEvent.VK_RIGHT || nextKey == KeyEvent.VK_D) && dx != -1) {
            dx = 1;
            dy = 0;
        }

        //Up
        if((nextKey == KeyEvent.VK_UP || nextKey == KeyEvent.VK_W) && dy != 1) {
            dx = 0;
            dy = -1;
        }

        //Down
        if((nextKey == KeyEvent.VK_DOWN || nextKey == KeyEvent.VK_S) && dy != -1) {
            dx = 0;
            dy = 1;
        }
    }
}
