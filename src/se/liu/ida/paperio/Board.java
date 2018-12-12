package se.liu.ida.paperio;

import javax.swing.JPanel;
import java.awt.event.*;
import java.sql.SQLOutput;
import java.util.*;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Color;

// TODO Comment code
public class Board extends JPanel {

    // TODO Fix scope of variables (private, public etc)

    Tile[][] gameArea = new Tile[100][100];
    List<Player> players = new ArrayList<>();
    HumanPlayer humanPlayer;
    int scale = 25;

    private Timer timer;
    private final int INITIAL_DELAY = 0;
    private final int PERIOD_INTERVAL = 1000/20;
    private KeyEvent key;

    public Board(){
        initBoard();
    }

    public void initBoard(){
        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                gameArea[i][j] = new Tile(j,i);
            }
        }

        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TAdapter());


        players.add(new HumanPlayer(gameArea.length, gameArea[0].length));
        humanPlayer = (HumanPlayer)players.get(0);
        for(int i = 0; i < 0; i++){
            players.add(new BotPlayer(gameArea.length, gameArea[0].length));
        }
        for(Player player : players){
            startingArea(player);
        }


        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(),
                INITIAL_DELAY, PERIOD_INTERVAL);
    }



    /**
     * Marks all tiles in the starting area of a player to owned by player.
     * @param player Player
     */
    public void startingArea(Player player){
        int x = player.getX();
        int y = player.getY();
        for(int i = x-1; i <= x+1; i++){
            for(int j = y-1; j <= y+1; j++){
                player.setTileOwned(gameArea[i][j]);
            }
        }
    }

    /**
     * Overrides paintComponent and is called whenever everything should be drawn on the screen
     * @param g Graphics element used to draw elements on screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    // TODO Draw a live scoreboard
    // TODO Ask for name and print name under player
    // TODO Make movement smooth (several ticks between getting from one tile to the next)
    /**
     * Main method responsible for drawing everything to the screen
     * @param g Graphics object gotten as argument in paintComponent method
     */
    public void draw(Graphics g){
        drawGameArea(g);
        drawPlayers(g);
    }

    /**
     * Draws all players on the map with corresponding colors. Doesn't draw players seen by player.
     * @param g Graphics object gotten as argument in paintComponent method
     */
    private void drawPlayers(Graphics g){

        int drawX;
        int drawY;

        for(Player player : players){

            drawX = (player.getX() - humanPlayer.getX())*scale + ((getWidth()-scale)/2);
            drawY = (player.getY() - humanPlayer.getY())*scale + ((getHeight()-scale)/2);

            if(!(drawX + scale < 0 || drawX > getWidth() || drawY + scale < 0 || drawY > getHeight())) {
                g.setColor(player.getColor());
                g.fillRect(drawX, drawY, scale, scale);
            }

        }
    }

    /**
     * Draws all tiles on the map with colors corresponding to owner, contested owner. Doesn't draw tiles not seen by
     * player.
     * @param g Graphics object gotten as argument in paintComponent method
     */
    private void drawGameArea(Graphics g){

        int drawX;
        int drawY;

        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){

                drawX = (i - humanPlayer.getX())*scale + ((getWidth()-scale)/2);
                drawY = (j - humanPlayer.getY())*scale + ((getHeight()-scale)/2);

                if(!(drawX + scale < 0 || drawX > getWidth() || drawY + scale < 0 || drawY > getHeight())) {
                    g.setColor(Color.white);
                    g.fillRect(drawX, drawY, scale, scale);

                    g.setColor(gameArea[i][j].getColor());
                    g.fillRect(drawX, drawY, scale, scale);
                }
            }
        }
    }


    /**
     * After a player has traveled out to enclose an area the area needs to be filled. This method depends on that the
     * Player.contestedToOwned() method has been called. The method works by doing a depth first search from each tile
     * adjacent to a tile owned by the player sent as parameter. If the DFS algorithm finds a boundary we know it is not
     * enclosed and should not be filled. The boundary is the smallest rectangle surrounding all owned tiles by the
     * player to minimize cost of method. If the DFS can't find the boundary we know it should be filled.
     * @param player The player whose enclosure to be filled
     */
    private void fillEnclosure(Player player) {
        // Set boundary
        int maxX = 0;
        int minX = gameArea[0].length;
        int maxY = 0;
        int minY = gameArea.length;
        for (Tile t : player.getTilesOwned()) {
            if(t.getX() > maxX) maxX = t.getX();
            if(t.getX() < minX) minX = t.getX();
            if(t.getY() > maxY) maxY = t.getY();
            if(t.getY() < minY) minY = t.getY();
        }


        // Necessary collections for DFS to work
        ArrayList<Tile> outside = new ArrayList<>();
        ArrayList<Tile> inside  = new ArrayList<>();
        ArrayList<Tile> visited = new ArrayList<>();
        HashSet<Tile> toCheck = new HashSet<>();

        // Add all adjacent tiles
        for(Tile t : player.getTilesOwned()){
            toCheck.add(gameArea[t.getY()-1][t.getX()]);
            toCheck.add(gameArea[t.getY()+1][t.getX()]);
            toCheck.add(gameArea[t.getY()][t.getX()-1]);
            toCheck.add(gameArea[t.getY()][t.getX()+1]);
        }


        // Loop over all tiles to do DFS from
        for(Tile t : toCheck){
            Stack<Tile> stack = new Stack<>();
            boolean cont = true;
            Tile v;
            visited.clear();

            // DFS algorithm
            stack.push(t);
            while((!stack.empty()) && cont){
                v = stack.pop();
                if(!visited.contains(v) && (v.getOwner() != player)){
                    if(v.getX() < minX || v.getX() > maxX || v.getY() < minY || v.getY() > maxY){
                        cont = false;
                    }else{
                        visited.add(v);
                        stack.push(gameArea[v.getY()-1][v.getX()]);
                        stack.push(gameArea[v.getY()+1][v.getX()]);
                        stack.push(gameArea[v.getY()][v.getX()-1]);
                        stack.push(gameArea[v.getY()][v.getX()+1]);
                    }
                }
            }
            if(cont){ // If DFS don't find boundary
                inside.addAll(visited);
            }else{
                outside.addAll(visited);
            }

        }

        // Set all enclosed tiles to be owned by player
        for(Tile t : inside){
            player.setTileOwned(t);
        }

    }


    private class ScheduleTask extends TimerTask {

        // TODO make tick separate method
        // TODO Fix all collision detections
        @Override
        public void run() {
            for(Player player : players){
                player.move();
                try {
                    if (gameArea[player.getX()][player.getY()].getOwner() != player) {
                        player.checkCollision(gameArea[player.getX()][player.getY()]);
                        player.setTileContested(gameArea[player.getX()][player.getY()]);
                    } else if ((gameArea[player.getX()][player.getY()].getOwner() == player) && (player.getTilesContested().size() > 0)){
                        player.contestToOwned();
                        fillEnclosure(player);
                    }

                } catch (ArrayIndexOutOfBoundsException e){
                    //System.out.println(e);
                    player.death();
                }
            }
            repaint();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            humanPlayer.keyPressed(e);
        }
    }
}
