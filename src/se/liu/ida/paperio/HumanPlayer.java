package se.liu.ida.paperio;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class HumanPlayer extends Player {


    public HumanPlayer(int height, int width){
        super(height, width);
    }


    /**
     * Moves the player in different directions
     */
    @Override
    public void move(){
        x += dx;
        y += dy;
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
