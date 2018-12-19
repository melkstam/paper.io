package se.liu.ida.paperio;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.Timer;

// TODO Comment code
public class Board extends JPanel {

    // TODO Fix scope of variables (private, public etc)

    private Tile[][] gameArea = new Tile[100][100];
    private List<Player> players = new ArrayList<>();
    private HumanPlayer humanPlayer;
    private HashMap<Player, int[]> playerPositions = new HashMap<>();


    private final int scale = 20;
    private int tickCounter = 0;
    private final int tickReset = 8;

    private final int INITIAL_DELAY = 0;
    private final int PERIOD_INTERVAL = 1000/60;

    private boolean splitScreen = false;

    private boolean paused = true;

    private int keyToSend;
    private ActionListener actionListener;

    private List<Color> colorList = new ArrayList<>(Arrays.asList(Color.magenta, Color.green, Color.red,
            Color.blue, Color.orange, Color.yellow, Color.pink, new Color(142,12,255),
            new Color(255,43,119), new Color(100,255,162)));

    public Board(ActionListener actionListener){
        this.actionListener = actionListener;
        initBoard();
    }

    private void initBoard(){
        specifyKeyActions();

        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                gameArea[i][j] = new Tile(j,i);
            }
        }

        setBackground(Color.BLACK);

        players.add(new HumanPlayer(gameArea.length, gameArea[0].length, new Color((int)(Math.random() * 0x1000000))));
        humanPlayer = (HumanPlayer)players.get(0);
        for(int i = 0; i < 10; i++){
            if(i > 9){
                players.add(new BotPlayer(gameArea.length,gameArea[0].length,
                        new Color((int)(Math.random() * 0x1000000))));
            }else {
                players.add(new BotPlayer(gameArea.length, gameArea[0].length, colorList.get(i)));
            }
        }
        for(Player player : players){
            startingArea(player);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(),
                INITIAL_DELAY, PERIOD_INTERVAL);
    }

    /**
     * Specifies necessary key bindings and key actions for game to work.
     */
    private void specifyKeyActions(){
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUP");
        am.put("moveUP", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                keyToSend = KeyEvent.VK_UP;
            }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDOWN");
        am.put("moveDOWN", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                keyToSend = KeyEvent.VK_DOWN;
            }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLEFT");
        am.put("moveLEFT", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                keyToSend = KeyEvent.VK_LEFT;
            }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRIGHT");
        am.put("moveRIGHT", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                keyToSend = KeyEvent.VK_RIGHT;
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
        am.put("pause", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                ActionEvent action = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "pause");
                actionListener.actionPerformed(action);
            }
        });
    }

    /**
     * Marks all tiles in the starting area of a player to owned by player.
     * @param player Player
     */
    private void startingArea(Player player){
        int x = player.getX();
        int y = player.getY();
        for(int i = x-1; i <= x+1; i++){
            for(int j = y-1; j <= y+1; j++){
                player.setTileOwned(gameArea[i][j]);
            }
        }
    }

    /**
     * Overrides paintComponent and is called whenever everything should be drawn on the screen
     * @param g Graphics element used to draw elements on screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    // TODO Draw a live scoreboard
    // TODO Ask for name and print name under player
    // TODO Only interpolate drawPlayers and not gameArea (Optimize)
    // TODO Fix right side splitscreen rendering 1/4 of left side
    // TODO Fix right side splitscreen jagged/interpolated(?) movement
    /**
     * Main method responsible for drawing everything to the screen
     * @param g Graphics object gotten as argument in paintComponent method
     */
    private void draw(Graphics g){
        drawGameArea(g);
        drawPlayers(g);
    }

    /**
     * Draws all tiles on the map with colors corresponding to owner and contested owner. Doesn't draw tiles not seen by
     * player.
     * @param g Graphics object gotten as argument in paintComponent method
     */
    private void drawGameArea(Graphics g) {

        int drawX;
        int drawY;
        int drawXSplit;
        int drawYSplit;
        if (!splitScreen){
            for (int i = 0; i < gameArea.length; i++) {
                for (int j = 0; j < gameArea[i].length; j++) {
                    drawX = (i - humanPlayer.getX()) * scale + ((getWidth() - scale) / 2)
                            + (int) ((-humanPlayer.getDx()) * scale * ((tickCounter + 1) / (double) tickReset));
                    drawY = (j - humanPlayer.getY()) * scale + ((getHeight() - scale) / 2)
                            + (int) ((-humanPlayer.getDy()) * scale * ((tickCounter + 1) / (double) tickReset));

                    if (!(drawX + scale < 0 || drawX > getWidth() || drawY + scale < 0 || drawY > getHeight())) {
                        g.setColor(Color.white);
                        g.fillRect(drawX, drawY, scale, scale);

                        g.setColor(gameArea[i][j].getColor());
                        g.fillRect(drawX, drawY, scale, scale);
                    }
                }
            }
        }else {
            for (int i = 0; i < gameArea.length; i++) {
                for (int j = 0; j < gameArea[i].length; j++) {
                    drawX = (i - humanPlayer.getX()) * scale + ((getWidth() - scale) / 4) +
                            (int) ((-humanPlayer.getDx()) * scale * ((tickCounter + 1) / (double) tickReset));
                    drawY = (j - humanPlayer.getY()) * scale + ((getHeight() - scale) / 2)
                            + (int) ((-humanPlayer.getDy()) * scale * ((tickCounter + 1) / (double) tickReset));

                    if (!(drawX + scale < 0 || drawX > (getWidth() / 2) || drawY + scale < 0 || drawY > getHeight())) {
                        g.setColor(Color.white);
                        g.fillRect(drawX, drawY, scale, scale);

                        g.setColor(gameArea[i][j].getColor());
                        g.fillRect(drawX, drawY, scale, scale);
                    }


                    drawXSplit = (i - players.get(1).getX()) * scale + ((getWidth() - scale) / 4)
                            + (getWidth() / 2) + (int) ((-players.get(1).getDx()) * scale
                            * ((tickCounter + 1) / (double) tickReset));

                    drawYSplit = (j - players.get(1).getY()) * scale + ((getHeight() - scale) / 2)
                            + (int) ((-players.get(1).getDy()) * scale * ((tickCounter + 1) / (double) tickReset));

                    if (!(drawXSplit + scale < 0 || drawXSplit > (getWidth())
                            || drawYSplit + scale < 0 || drawYSplit > getHeight())) {
                        g.setColor(Color.white);
                        g.fillRect(drawXSplit, drawYSplit, scale, scale);

                        g.setColor(gameArea[i][j].getColor());
                        g.fillRect(drawXSplit, drawYSplit, scale, scale);
                    }
                }
            }
        }
    }

    /**
     * Draws all players on the map with corresponding colors. Doesn't draw players not seen by player.
     * @param g Graphics object gotten as argument in paintComponent method
     */
    private void drawPlayers(Graphics g) {

        int drawX;
        int drawY;

        int drawXSplit;
        int drawYSplit;

        for (Player player : players) {
            if(!splitScreen) {
                drawX = (player.getX() - humanPlayer.getX()) * scale + ((getWidth() - scale) / 2);
                drawY = (player.getY() - humanPlayer.getY()) * scale + ((getHeight() - scale) / 2);
                if (player != humanPlayer) {
                    drawX += (int) ((player.getDx() - humanPlayer.getDx()) * scale
                            * ((tickCounter + 1) / (double) tickReset));
                    drawY += (int) ((player.getDy() - humanPlayer.getDy()) * scale
                            * ((tickCounter + 1) / (double) tickReset));
                }

                if (!(drawX + scale < 0 || drawX > getWidth() || drawY + scale < 0 || drawY > getHeight())) {
                    g.setColor(player.getColor());
                    g.fillRect(drawX, drawY, scale, scale);
                }

            }else {
                drawX = (player.getX() - humanPlayer.getX()) * scale + ((getWidth() - scale) / 4);
                drawY = (player.getY() - humanPlayer.getY()) * scale + ((getHeight() - scale) / 2);
                if (player != humanPlayer) {
                    drawX += (int) ((player.getDx() - humanPlayer.getDx()) * scale
                            * ((tickCounter + 1) / (double) tickReset));
                    drawY += (int) ((player.getDy() - humanPlayer.getDy()) * scale
                            * ((tickCounter + 1) / (double) tickReset));
                }

                if (!(drawX + scale < 0 || drawX > getWidth() || drawY + scale < 0 || drawY > getHeight())) {
                    g.setColor(player.getColor());
                    g.fillRect(drawX, drawY, scale, scale);
                }

                drawXSplit = (player.getX() - players.get(1).getX()) * scale
                        + (((getWidth() - scale) / 4) + getWidth() / 2);
                drawYSplit = (player.getY() - players.get(1).getY()) * scale + ((getHeight() - scale) / 2);
                if (player != humanPlayer) {
                    drawXSplit += (int) ((player.getDx() - players.get(1).getDx()) * scale
                            * ((tickCounter + 1) / (double) tickReset));
                    drawYSplit += (int) ((player.getDy() - players.get(1).getDy()) * scale
                            * ((tickCounter + 1) / (double) tickReset));
                }

                if (!(drawXSplit + scale < 0 || drawXSplit > getWidth()
                        || drawYSplit + scale < 0 || drawYSplit > getHeight())) {
                    g.setColor(player.getColor());
                    g.fillRect(drawXSplit, drawYSplit, scale, scale);
                }
            }
        }
    }

    /**
     * Method responsible for main logic of the game
     */
    private void tick(){
        for (Player player : players) {
            player.move();
            try {
                // If player is outside their owned territory, check if
                if (gameArea[player.getX()][player.getY()].getOwner() != player) {
                    player.checkCollision(gameArea[player.getX()][player.getY()]);
                    player.setTileContested(gameArea[player.getX()][player.getY()]);
                } else if ((gameArea[player.getX()][player.getY()].getOwner() == player)
                        && (player.getTilesContested().size() > 0)) {
                    player.checkCollision(gameArea[player.getX()][player.getY()]);

                    player.contestToOwned();
                    fillEnclosure(player);

                    playerPositions.put(player, new int[] {player.getX(), player.getY()});

                }
            } catch (ArrayIndexOutOfBoundsException e) {
                //System.out.println(e);
            }
        }

        if(keyToSend != 0){
            humanPlayer.keyPressed(keyToSend);
            keyToSend = 0;
        }
    }

    /**
     * Controls tick counter of game which is needed to make game smooth.
     */
    private void updateTick(){
        tickCounter++;
        tickCounter %= tickReset;
    }

    /**
     * After a player has traveled out to enclose an area the area needs to be filled. This method depends on that the
     * Player.contestedToOwned() method has been called. The method works by doing a depth first search from each tile
     * adjacent to a tile owned by the player sent as parameter. If the DFS algorithm finds a boundary we know it is not
     * enclosed and should not be filled. The boundary is the smallest rectangle surrounding all owned tiles by the
     * player to minimize cost of method. If the DFS can't find the boundary or if the one the DFS starts on we know it
     * should be filled.
     * @param player The player whose enclosure to be filled
     */
    private void fillEnclosure(Player player) {
        // Set boundary
        int maxX = 0;
        int minX = gameArea[0].length;
        int maxY = 0;
        int minY = gameArea.length;
        for (Tile t : player.getTilesOwned()) {
            if(t.getX() > maxX) maxX = t.getX();
            if(t.getX() < minX) minX = t.getX();
            if(t.getY() > maxY) maxY = t.getY();
            if(t.getY() < minY) minY = t.getY();
        }

        // Necessary collections for DFS to work
        ArrayList<Tile> outside = new ArrayList<>();
        ArrayList<Tile> inside  = new ArrayList<>();
        ArrayList<Tile> visited = new ArrayList<>();
        HashSet<Tile> toCheck = new HashSet<>();


        // Add all adjacent tiles
        int y;
        int x;
        for(Tile t : player.getTilesOwned()){
            y = t.getY();
            x = t.getX();
            if(y -1 >= 0) toCheck.add(gameArea[y-1][x]);
            if(y + 1 < gameArea.length) toCheck.add(gameArea[y+1][x]);
            if(x - 1 >= 0) toCheck.add(gameArea[y][x-1]);
            if(x + 1 < gameArea[y].length) toCheck.add(gameArea[y][x+1]);
        }


        // Loop over all tiles to do DFS from
        for(Tile t : toCheck){
            if(!inside.contains(t)){
                Stack<Tile> stack = new Stack<>();
                boolean cont = true;
                Tile v;
                visited.clear();

                // DFS algorithm
                stack.push(t);
                while((!stack.empty()) && cont){
                    v = stack.pop();
                    if(!visited.contains(v) && (v.getOwner() != player)){
                        y = v.getY();
                        x = v.getX();
                        if(outside.contains(v) //If already declared as outside
                        || x < minX || x > maxX || y < minY || y > maxY //If outside of boundary
                        || x == gameArea[0].length -1 || x == 0 || y == 0 || y == gameArea.length -1){ // If it is a edge tile
                            cont = false;
                        }else{
                            visited.add(v);
                            if(y -1 >= 0) stack.push(gameArea[y-1][x]);
                            if(y + 1 < gameArea.length) stack.push(gameArea[y+1][x]);
                            if(x - 1 >= 0) stack.push(gameArea[y][x-1]);
                            if(x + 1 < gameArea[y].length) stack.push(gameArea[y][x+1]);
                        }
                    }
                }
                if(cont){ // If DFS don't find boundary
                    inside.addAll(visited);
                }else{
                    outside.addAll(visited);
                }
            }
        }

        // Set all enclosed tiles to be owned by player
        for(Tile t : inside){
            player.setTileOwned(t);
        }
    }

    public void setPaused(Boolean b){
        paused = b;
    }


    private class ScheduleTask extends TimerTask {

        // TODO make tick separate method
        // TODO Fix player collision detections
        @Override
        public void run() {
            if(!paused) {
                updateTick();
                if (tickCounter == 0) {
                    tick();
                }
                repaint();
            }
        }
    }

}
