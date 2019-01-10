package se.liu.ida.paperio;

import java.awt.*;
import java.util.List;

/**
 * A Painter is responsible for drawing the game area. Multiple painters can be used to draw the game area from
 * different players views.
 */
class Painter{

    private int width;
    private int height;
    private final int scale;
    private List<Player> players;
    private Player humanPlayer;
    private Board board;
    private boolean draw = true;

    /**
     * Create a new painter with scale, board which state to be drawn, player to follow and all players
     * @param scale how much a tile should be scaled from one pixel
     * @param board board which state to be drawn
     * @param humanPlayer player to follow from which view the game area and player should be drawn
     * @param players all players that at each time should be drawn
     */
    Painter(int scale, Board board, Player humanPlayer, List<Player> players){
       this.scale = scale;
       this.board = board;
       this.players = players;
       this.humanPlayer = humanPlayer;
    }

    /**
     * Sets whether painter should draw or not
     * @param draw true if painter should continue to draw, false otherwise
     */
    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    /**
     * Method is called from board to initialize a draw with graphics received
     * @param g graphics object used to draw
     */
     void draw(Graphics g){
         if(draw){
             height = g.getClipBounds().height;
             width = g.getClipBounds().width;
             drawGameArea(g);
             drawPlayers(g);
         }
    }

    /**
     * Draws all players and their name on the map with corresponding color. Doesn't draw players not seen by player.
     * @param g Graphics object received as argument in paintComponent method
     */
    private void drawPlayers(Graphics g){
        int drawX;
        int drawY;

        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        FontMetrics fontMetrics = g.getFontMetrics();

        for (Player player : players) {
            // x and y position relative to humanPlayer at which player should be drawn
            drawX = (player.getX() - humanPlayer.getX()) * scale + ((width - scale) / 2);
            drawY = (player.getY() - humanPlayer.getY()) * scale + ((height - scale) / 2);
            if (player != humanPlayer) {
                // For all other players than humanPlayer we need to smooth animations regarding to animation smoothing
                // of humanPlayer
                drawX += ((player.getDx() - humanPlayer.getDx()) * scale
                        * ((board.getTickCounter() + 1) / (double) board.getTickReset()));
                drawY += ((player.getDy() - humanPlayer.getDy()) * scale
                        * ((board.getTickCounter() + 1) / (double) board.getTickReset()));
            }

            g.setColor(Color.BLACK);
            g.drawString(player.getName(),
                    drawX + (scale - fontMetrics.stringWidth(player.getName()))/2, drawY+scale+16);

            // Draw player if visible
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

        for (int y = 0; y < board.getAreaHeight(); y++) {
            for (int x = 0; x < board.getAreaWidth(); x++) {
                // x and y position relative to humanPlayer at which tile should be drawn
                drawX = (x - humanPlayer.getX()) * scale + ((width - scale) / 2)
                        + (int) ((-humanPlayer.getDx()) * scale *
                        ((board.getTickCounter() + 1) / (double) board.getTickReset()));
                drawY = (y - humanPlayer.getY()) * scale + ((height - scale) / 2)
                        + (int) ((-humanPlayer.getDy()) * scale *
                        ((board.getTickCounter() + 1) / (double) board.getTickReset()));

                // If visible, draw first white background and then draw color on top
                if (!(drawX + scale < 0 || drawX > width || drawY + scale < 0 || drawY > height)) {
                    g.setColor(Color.white);
                    g.fillRect(drawX, drawY, scale, scale);

                    g.setColor(board.getTile(x,y).getColor());
                    g.fillRect(drawX, drawY, scale, scale);
                }
            }
        }
    }

}
