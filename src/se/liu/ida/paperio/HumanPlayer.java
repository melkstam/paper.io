package se.liu.ida.paperio;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HumanPlayer extends Player {


    HumanPlayer(int height, int width, Color color, String name) {
        super(height, width, color);
        this.name = name;
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
     * Handles player movement controls
     * @param key Key that is being pressed
     */
    void keyPressed(int key) {

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
