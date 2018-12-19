package se.liu.ida.paperio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Menu extends JPanel{

    private JButton playBtn;

    public Menu(ActionListener actionListener){
        playBtn = new JButton("Play");
        playBtn.setSize(100, 26);
        playBtn.addActionListener(actionListener);
        setBackground(Color.WHITE);
        add(playBtn);
    }

}
