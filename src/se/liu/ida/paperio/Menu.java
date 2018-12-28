package se.liu.ida.paperio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Menu extends JPanel{

    private GridLayout gridLayout;
    private JButton playBtn;
    private JButton playMultiBtn;
    private JTextField p1NameFld;
    private JTextField p2NameFld;
    private ActionListener actionListener;


    Menu(ActionListener actionListener){
        this.actionListener = actionListener;
        setBackground(Color.BLACK);

        gridLayout = new GridLayout(5, 2);
        setLayout(gridLayout);

        addComponents();

    }

    private void addComponents(){
        add(new JLabel(" "));
        add(new JLabel(" "));

        playBtn = new JButton("Play Singleplayer");
        playMultiBtn = new JButton("Play Multiplayer");

        JButton[] buttons = {playBtn, playMultiBtn};

        for(JButton button : buttons){
            button.setSize(100, 26);
            button.addActionListener(actionListener);
            button.setFont(new Font("Monospaced", Font.PLAIN, 40));
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);

            add(button);
        }

        JLabel p1name = new JLabel("Player 1 name:");
        JLabel p2name = new JLabel("Player 2 name:");

        JLabel[] labels = {p1name, p2name};

        for(JLabel label : labels){
            label.setFont(new Font("Monospaced", Font.PLAIN, 24));
            label.setForeground(Color.WHITE);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.BOTTOM);
            add(label);
        }

        p1NameFld = new JTextField("Player 1 name…");
        p2NameFld = new JTextField("Player 2 name…");

        JTextField[] textFields = {p1NameFld, p2NameFld};

        for(JTextField textField : textFields){
            textField.setFont(new Font("Monospaced", Font.PLAIN, 40));
            textField.setBackground(Color.BLACK);
            textField.setForeground(Color.gray.brighter());
            textField.addMouseListener(new FieldMouseListener(textField));
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setCaretColor(Color.WHITE);
            add(textField);
        }


    }

    public String getP1Name() {
        return p1NameFld.getText();
    }

    public String getP2Name() {
        return p2NameFld.getText();
    }

    class FieldMouseListener implements MouseListener {

        private boolean changed = false;
        private JTextField textField;

        public FieldMouseListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(!changed) {
                textField.setText("");
                textField.setForeground(Color.WHITE);
            }
            changed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }


}
