package se.liu.ida.paperio;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

class Painter extends JPanel {

    private int width;
    private int height;
    private int scale;
    private List<Player> players;
    private Player humanPlayer;
    private Board board;

    Painter(int width, int height, int scale, Board board, Player humanPlayer, List<Player> players){
       this.width = width;
       this.height = height;
       this.scale = scale;
       this.board = board;
       this.players = players;
       this.humanPlayer = humanPlayer;
    }

     void draw(Graphics g){
        width = board.getWidth();
        height = board.getHeight();
        drawGameArea(g);
        drawPlayers(g);
        drawScoreboard(g);
    }

    // TODO Print name under player
    // TODO Only interpolate drawPlayers and not gameArea (Optimize)
    // TODO interpolate non-human players
    /**
     * Draws all players on the map with corresponding colors. Doesn't draw players not seen by player.
     * @param g Graphics object recieved as argument in paintComponent method
     */
    private void drawPlayers(Graphics g){
        int drawX;
        int drawY;

        for (Player player : players) {
            drawX = (player.getX() - humanPlayer.getX()) * scale + ((width - scale) / 2);
            drawY = (player.getY() - humanPlayer.getY()) * scale + ((height - scale) / 2);
            if (player != humanPlayer) {
                drawX += (player.getDx() - humanPlayer.getDx() * scale
                        * ((board.getTickCounter() + 1) / (double) board.getTickReset()));
                drawY += (player.getDy() - humanPlayer.getDy() * scale
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
     * @param g Graphics object recieved as argument in paintComponent method
     */
    private void drawGameArea(Graphics g) {
        int drawX;
        int drawY;

        for (int i = 0; i < board.getGameArea().length; i++) {
            for (int j = 0; j < board.getGameArea()[i].length; j++) {
                drawX = (i - humanPlayer.getX()) * scale + ((width - scale) / 2)
                        + (int) ((-humanPlayer.getDx()) * scale * ((board.getTickCounter() + 1) / (double) board.getTickReset()));
                drawY = (j - humanPlayer.getY()) * scale + ((height - scale) / 2)
                        + (int) ((-humanPlayer.getDy()) * scale * ((board.getTickCounter() + 1) / (double) board.getTickReset()));

                if (!(drawX + scale < 0 || drawX > width || drawY + scale < 0 || drawY > height)) {
                    g.setColor(Color.white);
                    g.fillRect(drawX, drawY, scale, scale);

                    g.setColor(board.getGameArea()[i][j].getColor());
                    g.fillRect(drawX, drawY, scale, scale);
                }
            }
        }
    }

    /**
     * Draws the live scoreboard up in the rightmost corner
     * @param g Graphics object received as argument in paintComponent method
     */
    private void drawScoreboard(Graphics g) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int barWidth;
        int barHeight = fontHeight + 4;

        Player player;
        String string;
        Color color;

        double highestPercentOwned = players.get(0).getPercentOwned();
        Collections.sort(players);
        for(int i = 0; i < Integer.min(5, players.size()); i++){
            player = players.get(i);
            string = String.format("%.2f%% - " + player.getName(), player.getPercentOwned());
            color = player.getColor();

            barWidth = (int)((player.getPercentOwned() / highestPercentOwned)*(width/4));
            g.setColor(player.getColor());
            g.fillRect(width - barWidth,  barHeight*i, barWidth,barHeight);
            // If color is perceived as dark set the font color to white, else black
            if(0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue() < 127){
                g.setColor(Color.WHITE);
            }else{
                g.setColor(Color.BLACK);
            }
            g.drawString(string, 2+width -  barWidth,  barHeight*i + fontHeight);
        }
    }

}
