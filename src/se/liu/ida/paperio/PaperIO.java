package se.liu.ida.paperio;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class PaperIO extends JFrame {

    public PaperIO(){
        initUI();
    }

    private void initUI(){
        add(new Board());
        setSize(1000, 1000);
        setResizable(false);
        setVisible(true);
        setTitle("paper.io");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "True");

        EventQueue.invokeLater(() -> {
            PaperIO ex = new PaperIO();
            ex.setVisible(true);
        });
    }

}
