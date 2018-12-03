package se.liu.ida.paperio;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class App extends JFrame{

    public App(){
        initUI();
    }

    private void initUI(){
        add(new Board());

        setSize(800, 600);
        setResizable(false);
        setTitle("paper.io");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            App ex = new App();
            ex.setVisible(true);
        });
    }

}
