package se.liu.ida.paperio;

import javax.swing.JPanel;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Color;

import java.util.List;
import java.util.ArrayList;

public class Board extends JPanel {

    Tile[][] gameArea = new Tile[100][100];
    List<HumanPlayer> players = new ArrayList<HumanPlayer>();
    int scale = 10;

    private Timer timer;
    private final int INITIAL_DELAY = 0;
    private final int PERIOD_INTERVAL = 1000/20;

    public Board(){
        initBoard();
    }

    public void initBoard(){
        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                gameArea[i][j] = new Tile();
            }
        }

        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TAdapter());

        players.add(new HumanPlayer(gameArea.length, gameArea[0].length));
        for(HumanPlayer player : players){
            startingArea(player);
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(),
                INITIAL_DELAY, PERIOD_INTERVAL);
    }

    public void checkContest(Player player){

    }

    // Sets starting areas for each player
    public void startingArea(Player player){
        int x = player.getX();
        int y = player.getY();
        gameArea[x-1][y].setOwner(player);
        gameArea[x][y].setOwner(player);
        gameArea[x+1][y].setOwner(player);
        gameArea[x-1][y+1].setOwner(player);
        gameArea[x-1][y-1].setOwner(player);
        gameArea[x][y-1].setOwner(player);
        gameArea[x][y+1].setOwner(player);
        gameArea[x+1][y-1].setOwner(player);
        gameArea[x+1][y+1].setOwner(player);

        for(int i = 0; i < 9; i ++){
            player.setTilesOwned();
        }



        //player.setTilesOwned(9);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);

        Toolkit.getDefaultToolkit().sync();
    }

    public void draw(Graphics g){
        drawGameArea(g);
        drawOwnedTiles(g);
        drawPlayers(g);
        }

    private void drawPlayers(Graphics g){
        for(Player player : players){
            g.setColor(player.getColor());
            //g.drawRect(player.getX(), player.getY(), 10, 10);
            g.fillRect(player.getX() * scale, player.getY() * scale, scale, scale);
        }
    }

    private void drawGameArea(Graphics g){
        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                g.setColor(Color.white);
                //g.drawRect(i * 10, j*10, 10, 10);
                g.fillRect(i * scale, j*scale, scale, scale);
            }
        }
    }

    public void drawOwnedTiles(Graphics g){
        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                g.setColor(gameArea[i][j].getColor());
                g.fillRect(i * scale, j * scale, scale, scale);
            }
        }
    }

    private class ScheduleTask extends TimerTask {

        @Override
        public void run() {
            if(players.size() > 0 ) {
                players.get(0).move();
            }
            for(Player player : players){
                try {
                    if (gameArea[player.getX()][player.getY()].getOwner() != player) {
                        gameArea[player.getX()][player.getY()].setContestedOwner(player);
                        player.setTilesOwned();
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println(e);
                }
            }
            repaint();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if(players.size() > 0) {
                players.get(0).keyPressed(e);
            }
        }

    }
}
