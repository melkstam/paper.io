package se.liu.ida.paperio;

import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Color;


public class Board extends JPanel {

    private Timer timer;
    private final int INITIAL_DELAY = 0;
    private final int PERIOD_INTERVAL = 1000/60;

    private HumanPlayer player;

    public Board(){
        initBoard();
    }

    public void initBoard(){

        //setBackground(Color.BLACK);

        player = new HumanPlayer();

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
        g.setColor(player.getColor());
        g.drawRect(player.getX(), player.getY(), player.getSize(), player.getSize());
        g.fillRect(player.getX(), player.getY(), player.getSize(), player.getSize());
    }

    private class ScheduleTask extends TimerTask {

        @Override
        public void run() {
            player.move();
            repaint();
        }
    }

}
