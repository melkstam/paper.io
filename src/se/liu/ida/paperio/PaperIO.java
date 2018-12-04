package se.liu.ida.paperio;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class PaperIO extends JFrame {

    public PaperIO(){
        initUI();
    }

    private void initUI(){
        add(new Board());

        setSize(1280, 720);
        setResizable(false);
        setTitle("paper.io");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            PaperIO ex = new PaperIO();
            ex.setVisible(true);
        });
    }

}
