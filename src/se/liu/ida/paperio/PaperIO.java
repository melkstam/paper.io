package se.liu.ida.paperio;

// TODO Cleanup imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaperIO extends JFrame implements ActionListener{

    private Board board;
    private Menu menu;
    private JPanel cards;

    private STATE state;

    private PaperIO(){
        initUI();
    }

    // TODO Add possibility for local multiplayer (split screen)
    private void initUI(){

        setSize(1000, 1000);
        setResizable(false);
        setVisible(true);
        setTitle("paper.io");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //board = new Board(this);
        menu = new Menu(this);
        cards = new JPanel(new CardLayout());
        cards.add(menu, "menu");

        add(cards);
    }

    /**
     * Enum with all possible game states
     */
    private enum STATE{
        GAME,
        MENU
    }

    /**
     * Sets game state to specified state
     * @param s STATE game should be set to
     */
    private void setState(STATE s){
        CardLayout cardLayout = (CardLayout) cards.getLayout();
        if(s == STATE.GAME){
            cardLayout.show(cards, "board");
            board.setPaused(false);
        }else if(s == STATE.MENU){
            cardLayout.show(cards, "menu");
            board.setPaused(true);
        }
    }

    /**
     * Reacts to game actions such as game start and game paused
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Play Singleplayer")) {
            board = new Board(this, menu.getP1Name());
            cards.add(board, "board");
            setState(STATE.GAME);
        }else if(e.getActionCommand().equals("Play Multiplayer")){
            board = new Board(this, menu.getP1Name(), menu.getP2Name());
            cards.add(board, "board");
            setState(STATE.GAME);
        }else if(e.getActionCommand().equals("pause")){
            setState(STATE.MENU);
        }
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "True");

        EventQueue.invokeLater(() -> {
            new PaperIO();
        });
    }

}
