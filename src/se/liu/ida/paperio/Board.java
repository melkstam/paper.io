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
    List<Player> players = new ArrayList<>();
    HumanPlayer humanPlayer;
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
                gameArea[i][j] = new Tile(i,j);
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
        gameArea[x-1][y].setOwner(player);
        gameArea[x][y].setOwner(player);
        gameArea[x+1][y].setOwner(player);
        gameArea[x-1][y+1].setOwner(player);
        gameArea[x-1][y-1].setOwner(player);
        gameArea[x][y-1].setOwner(player);
        gameArea[x][y+1].setOwner(player);
        gameArea[x+1][y-1].setOwner(player);
        gameArea[x+1][y+1].setOwner(player);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

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

    private class ScheduleTask extends TimerTask {

        @Override
        public void run() {
            for(Player player : players){
                player.move();
                try {
                    if (gameArea[player.getX()][player.getY()].getOwner() != player) {
                        player.setTilesContested(gameArea[player.getX()][player.getY()]);
                    } else if (gameArea[player.getX()][player.getY()].getOwner() == player){
                        player.contestToOwned();
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
            humanPlayer.keyPressed(e);
        }
    }
}
