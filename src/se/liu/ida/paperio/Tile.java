package se.liu.ida.paperio;

import java.awt.Color;

public class Tile {

    private Player owner;
    private Color color = new Color((int)(Math.random() * 0x1000000));

    public Tile(){

    }

    public Color getColor(){
        if(owner != null) {
            return owner.getColor();
        }else{
            return color;
        }
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }



}
