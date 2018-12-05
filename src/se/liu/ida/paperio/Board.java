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
    int scale = 20;

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

        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(),
                INITIAL_DELAY, PERIOD_INTERVAL);
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
            //g.drawRect(player.getX(), player.getY(), 10, 10);
            g.fillRect((getWidth()-scale)/2, (getHeight()-scale)/2, scale, scale);
        }
    }

    private void drawGameArea(Graphics g){
        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                g.setColor(gameArea[i][j].getColor());
                //g.drawRect(i * 10, j*10, 10, 10);
                g.fillRect((i - players.get(0).getX())*scale +((getWidth()-scale)/2), (j - players.get(0).getY())*scale +((getHeight()-scale)/2), scale, scale);
            }
        }
    }



    private class ScheduleTask extends TimerTask {

        @Override
        public void run() {
            if(players.size() > 0 ) {
                players.get(0).move();
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
