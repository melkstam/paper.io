package se.liu.ida.paperio;

import java.awt.*;
import java.util.ArrayList;

public class BotPlayer extends Player{

    public BotPlayer(int height, int width){
        super(height, width);
    }


    /**
     * Decides where the bot shall move and moves accordingly
     */
    @Override
    public void move() {
        double rand = Math.random();
        if (rand < 0.05) {
            dx = 1;
            dy = 0;
        } else if (rand < 0.1) {
            dx = -1;
            dy = 0;
        } else if (rand < 0.15) {
            dx = 0;
            dy = 1;
        } else if (rand < 0.2) {
            dx = 0;
            dy = -1;
        }
        avoidOutOfBounds();
        x += dx;
        y += dy;

    }
    // TODO Make bots not go in the opposite direction (into their own trail)
    public void avoidOutOfBounds(){
        if(x == 0){
            dx = 1;
            dy = 0;
            System.out.println("Avoided Out of bounds");
        }else if(x == width -1 ){
            dx = -1;
            dy = 0;
            System.out.println("Avoided Out of bounds");
        }else if(y == 0){
            dx = 0;
            dy = 1;
            System.out.println("Avoided Out of bounds");
        }else if(y == height -1){
            dx = 0;
            dy = -1;
            System.out.println("Avoided Out of bounds");
        }
    }
}
