package se.liu.ida.paperio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class Painter extends JPanel {

    private int width;
    private int height;
    private int scale;
    private List<Player> players;
    private Player humanPlayer;
    private Board board;

    Painter(int scale, Board board, Player humanPlayer, List<Player> players){
       this.scale = scale;
       this.board = board;
       this.players = players;
       this.humanPlayer = humanPlayer;
    }

     void draw(Graphics g){
        height = g.getClipBounds().height;
        width = g.getClipBounds().width;
        drawGameArea(g);
        drawPlayers(g);
    }

    // TODO Print name under player
    // TODO Only interpolate drawPlayers and not gameArea (Optimize)
    // TODO interpolate non-human players
    /**
     * Draws all players on the map with corresponding colors. Doesn't draw players not seen by player.
     * @param g Graphics object received as argument in paintComponent method
     */
    private void drawPlayers(Graphics g){
        int drawX;
        int drawY;

        for (Player player : players) {
            drawX = (player.getX() - humanPlayer.getX()) * scale + ((width - scale) / 2);
            drawY = (player.getY() - humanPlayer.getY()) * scale + ((height - scale) / 2);
            if (player != humanPlayer) {
                drawX += ((player.getDx() - humanPlayer.getDx()) * scale
                        * ((board.getTickCounter() + 1) / (double) board.getTickReset()));
                drawY += ((player.getDy() - humanPlayer.getDy()) * scale
                        * ((board.getTickCounter() + 1) / (double) board.getTickReset()));
            }

            if (!(drawX + scale < 0 || drawX > width || drawY + scale < 0 || drawY > height)) {
                g.setColor(player.getColor());
                g.fillRect(drawX, drawY, scale, scale);
            }
        }
    }

    /**
     * Draws all tiles on the map with colors corresponding to owner and contested owner. Doesn't draw tiles not seen by
     * player.
     * @param g Graphics object received as argument in paintComponent method
     */
    private void drawGameArea(Graphics g) {
        int drawX;
        int drawY;

        for (int i = 0; i < board.getAreaHeight(); i++) {
            for (int j = 0; j < board.getAreaWidth(); j++) {
                drawX = (i - humanPlayer.getX()) * scale + ((width - scale) / 2)
                        + (int) ((-humanPlayer.getDx()) * scale *
                        ((board.getTickCounter() + 1) / (double) board.getTickReset()));
                drawY = (j - humanPlayer.getY()) * scale + ((height - scale) / 2)
                        + (int) ((-humanPlayer.getDy()) * scale *
                        ((board.getTickCounter() + 1) / (double) board.getTickReset()));

                if (!(drawX + scale < 0 || drawX > width || drawY + scale < 0 || drawY > height)) {
                    g.setColor(Color.white);
                    g.fillRect(drawX, drawY, scale, scale);

                    g.setColor(board.getTile(j,i).getColor());
                    g.fillRect(drawX, drawY, scale, scale);
                }
            }
        }
    }


}
