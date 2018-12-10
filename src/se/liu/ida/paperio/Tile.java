package se.liu.ida.paperio;

import java.awt.Color;

public class Tile {

    private Player owner;
    //private Color color = new Color((int)(Math.random() * 0x1000000));
    private Color color = Color.white;
    private Player contestedOwner;

    public Tile(){}

    public Color getColor(){
        if(owner != null && contestedOwner == null) {
            return owner.getColor().darker();
        }else if (owner == null && contestedOwner != null) {
            Color clr = new Color(contestedOwner.getColor().getRed(), contestedOwner.getColor().getGreen(),
                    contestedOwner.getColor().getBlue(), 100);

            return clr;
        }else if (owner != null && contestedOwner != owner){
            Color clr = new Color(contestedOwner.getColor().getRed(), contestedOwner.getColor().getBlue(),
                    contestedOwner.getColor().getGreen(), 100);
            return clr;

        }else{
            return color;
        }
    }

    public Player getContestedOwner() {
        return contestedOwner;
    }

    public void setContestedOwner(Player contestedOwner) {
        this.contestedOwner = contestedOwner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
        owner.setTilesOwned();
    }





}
