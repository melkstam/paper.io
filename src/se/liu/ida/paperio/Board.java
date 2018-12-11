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
    int scale = 8;

    private Timer timer;
    private final int INITIAL_DELAY = 0;
    private final int PERIOD_INTERVAL = 1000/5;
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
        for(int i = 0; i < 5; i++){
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    // TODO Only draw visible stuff
    public void draw(Graphics g){
        drawGameArea(g);
        drawPlayers(g);
    }

    private void drawPlayers(Graphics g){
        for(Player player : players){
            g.setColor(player.getColor());
            g.fillRect((player.getX() - humanPlayer.getX())*scale +((getWidth()-scale)/2),
                    (player.getY() - humanPlayer.getY())*scale +((getHeight()-scale)/2), scale, scale);
        }
    }

    private void drawGameArea(Graphics g){
        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                g.setColor(Color.white);
                g.fillRect((i - humanPlayer.getX())*scale +((getWidth()-scale)/2),
                        (j - humanPlayer.getY())*scale +((getHeight()-scale)/2), scale, scale);

                g.setColor(gameArea[i][j].getColor());
                g.fillRect((i - humanPlayer.getX())*scale +((getWidth()-scale)/2),
                        (j - humanPlayer.getY())*scale +((getHeight()-scale)/2), scale, scale);
            }
        }
    }

    private void fillEnclosure(Player player) {
        // Set boundary to check if to fill
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

        ArrayList<Tile> outside = new ArrayList<>();
        ArrayList<Tile> inside  = new ArrayList<>();
        ArrayList<Tile> visited = new ArrayList<>();
        HashSet<Tile> toCheck = new HashSet<>();


        for(Tile t : player.getTilesOwned()){
            toCheck.add(gameArea[t.getY()-1][t.getX()]);
            toCheck.add(gameArea[t.getY()+1][t.getX()]);
            toCheck.add(gameArea[t.getY()][t.getX()-1]);
            toCheck.add(gameArea[t.getY()][t.getX()+1]);
        }



        for(Tile t : toCheck){
            Stack<Tile> stack = new Stack<>();
            boolean cont = true;
            Tile v;
            visited.clear();

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
            if(cont){
                inside.addAll(visited);
            }else{
                outside.addAll(visited);
            }

        }

        for(Tile t : inside){
            player.setTileOwned(t);
        }



    }



    private class ScheduleTask extends TimerTask {

        // TODO make tick separate method
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
