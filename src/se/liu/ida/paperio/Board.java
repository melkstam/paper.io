package se.liu.ida.paperio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Timer;
import java.util.*;

// TODO Comment code
public class Board extends JPanel {

    // TODO Fix scope of variables (private, public etc)

    private int areaHeight;
    private int areaWidth;
    private Tile[][] gameArea = new Tile[areaHeight][areaWidth];

    private int botNumber;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<HumanPlayer> humanPlayers = new ArrayList<>();
    private HashMap<Player, Tile> playerCurrentPositions = new HashMap<>();

    private final int scale = 20;
    private int tickCounter = 0;
    private int tickReset;

    private final int INITIAL_DELAY = 0;
    private final int PERIOD_INTERVAL = 1000/60;

    private boolean paused = true;
    private ActionListener actionListener;

    private ArrayList<Painter> painters = new ArrayList<>();

    private List<Color> colorList = new ArrayList<>(Arrays.asList(Color.magenta, Color.green, Color.red,
            Color.blue, Color.orange, Color.yellow, Color.pink, new Color(142,12,255),
            new Color(255,43,119), new Color(100,255,162)));

    Board(ActionListener actionListener, String p1name, int areaHeight, int areaWidth, int gameSpeed, int botNumber){
        this.actionListener = actionListener;
        this.areaHeight = areaHeight;
        this.areaWidth = areaWidth;
        this.botNumber = botNumber;
        int[] speeds = {12, 10, 8, 6, 4};
        tickReset = speeds[gameSpeed - 1];

        players.add(new HumanPlayer(areaHeight, areaWidth, new Color((int)(Math.random() * 0x1000000)), p1name));
        humanPlayers.add((HumanPlayer)players.get(0));

        initBoard();

        painters.add(new Painter(scale, this, humanPlayers.get(0), players));
    }

    Board(ActionListener actionListener, String p1name, String p2name, int areaHeight, int areaWidth, int gameSpeed, int botNumber) {
        this.actionListener = actionListener;
        this.areaHeight = areaHeight;
        this.areaWidth = areaWidth;
        this.botNumber = botNumber;
        int[] speeds = {12, 10, 8, 6, 4};
        tickReset = speeds[gameSpeed - 1];

        players.add(new HumanPlayer(areaHeight, areaWidth, new Color((int)(Math.random() * 0x1000000)), p1name));
        players.add(new HumanPlayer(areaHeight, areaWidth, new Color((int)(Math.random() * 0x1000000)), p2name));
        humanPlayers.add((HumanPlayer)players.get(0));
        humanPlayers.add((HumanPlayer)players.get(1));

        initBoard();

        painters.add(new Painter(scale, this, humanPlayers.get(0), players));
        painters.add(new Painter(scale, this, humanPlayers.get(1), players));
    }

    private void initBoard(){
        this.gameArea = new Tile[areaHeight][areaWidth];

        specifyKeyActions();

        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                gameArea[i][j] = new Tile(j,i);
            }
        }

        setBackground(Color.BLACK);

        for(int i = 0; i < botNumber; i++){
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

        if(humanPlayers.size() == 1){
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUP");
            am.put("moveUP", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_UP);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDOWN");
            am.put("moveDOWN", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {humanPlayers.get(0).setNextKey(KeyEvent.VK_DOWN);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLEFT");
            am.put("moveLEFT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_LEFT);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRIGHT");
            am.put("moveRIGHT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_RIGHT);
                }
            });
        }else if(humanPlayers.size() == 2){
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveP1UP");
            am.put("moveP1UP", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(1).setNextKey(KeyEvent.VK_UP);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveP1DOWN");
            am.put("moveP1DOWN", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {humanPlayers.get(1).setNextKey(KeyEvent.VK_DOWN);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveP1LEFT");
            am.put("moveP1LEFT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(1).setNextKey(KeyEvent.VK_LEFT);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveP1RIGHT");
            am.put("moveP1RIGHT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(1).setNextKey(KeyEvent.VK_RIGHT);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveP2UP");
            am.put("moveP2UP", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_W);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "moveP2DOWN");
            am.put("moveP2DOWN", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {humanPlayers.get(0).setNextKey(KeyEvent.VK_S);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveP2LEFT");
            am.put("moveP2LEFT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_A);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveP2RIGHT");
            am.put("moveP2RIGHT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_D);
                }
            });
        }


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
        for(int i = 0; i < painters.size(); i++){
            //Set clipping area for painter
            g.setClip(getWidth()/painters.size() * i,0,getWidth()/painters.size(),getHeight());

            //Move graphics to top-left of clipping area
            g.translate(getWidth()/painters.size() * i,0);

            //Painter paints area
            painters.get(i).draw(g);

            //Move graphics back to top-left of window
            g.translate(-getWidth()/painters.size() * i,0);
        }
        drawScoreboard(g);
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Draws the live scoreboard up in the rightmost corner
     * @param g Graphics object received as argument in paintComponent method
     */
    private void drawScoreboard(Graphics g) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int barWidth;
        int barHeight = fontHeight + 4;

        Player player;
        String string;
        Color color;

        double highestPercentOwned = players.get(0).getPercentOwned();
        Collections.sort(players);
        for(int i = 0; i < Integer.min(5, players.size()); i++){
            player = players.get(i);
            string = String.format("%.2f%% - " + player.getName(), player.getPercentOwned());
            color = player.getColor();

            barWidth = (int)((player.getPercentOwned() / highestPercentOwned)*(getWidth()/4));
            g.setColor(player.getColor());
            g.fillRect(getWidth() - barWidth,  barHeight*i, barWidth,barHeight);
            // If color is perceived as dark set the font color to white, else black
            if(0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue() < 127){
                g.setColor(Color.WHITE);
            }else{
                g.setColor(Color.BLACK);
            }
            g.drawString(string, 2+getWidth() -  barWidth,  barHeight*i + fontHeight);
        }
    }

    /**
     * Method responsible for main logic of the game
     */
    private void tick(){
        for (Player player : players) {
            if(player.getAlive()) {
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

                    }
                    player.setCurrentTile(gameArea[player.getX()][player.getY()]);
                    playerCurrentPositions.put(player, gameArea[player.getX()][player.getY()]);
                    findCollision();
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        for(HumanPlayer humanPlayer : humanPlayers){
            humanPlayer.updateD();
        }
    }

    private void findCollision(){
        HashMap<Tile, Player> reverseMap = new HashMap<>();
        HashMap<Player, Tile> noDuplicateMap = new HashMap<>();
        HashMap<Player, Tile> removedDuplicates = playerCurrentPositions;

        Tile contestedTile = new Tile(0,0);
        Player player1 = new HumanPlayer(0,0, Color.white, "");
        Player player2 = new HumanPlayer(0,0, Color.white, "");

        // Reverses the HashMap and stores it in reverseMap.
        for(Player p : playerCurrentPositions.keySet()){
            for(Tile t : playerCurrentPositions.values()){
                reverseMap.put(t,p);
            }
        }

        // Re-reverses the HashMap back. Player that had a Tile equal to another Player will be removed.
        // Output is stored in noDuplicateMap.
        for(Map.Entry<Tile, Player> entry : reverseMap.entrySet()){
            noDuplicateMap.put(entry.getValue(), entry.getKey());
        }

        // Removes all Players from playerCurrentPositions, except for the duplicate.
        // Result is stored in removedDuplicates
        for(Player p : noDuplicateMap.keySet()){
            removedDuplicates.remove(p);
        }

        // Stores the Tile and Player of the remaining Entry in noDuplicateMap in
        // player1 and contestedTile

        for(Player p : noDuplicateMap.keySet()){
            player1 = p;
            contestedTile = p.getCurrentTile();
        }

        // Finds the Player that has the matching Tile to player1 and stores it in player2
        for(Map.Entry<Player, Tile> entry : noDuplicateMap.entrySet()){
            if(entry.getValue() == contestedTile){
                player2 = entry.getKey();
            }
        }

        if(player1.getTilesContested().size() > player2.getTilesContested().size()){
            player1.death();
            System.out.println("player1 DIED");
        }else if (player1.getTilesContested().size() < player2.getTilesContested().size()){
            player2.death();
            System.out.println("PLAYER 2 die");
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

    /**
     * Set board to paused mode, meaning logic and graphics are not updated
     * @param b True if game should be paused, false otherwise
     */
    void setPaused(Boolean b){
        paused = b;
    }

    /**
     * Get height of game area
     * @return height of game area
     */
    int getAreaHeight() {
        return areaHeight;
    }

    /**
     * Get width of game area
     * @return width of game area
     */
    int getAreaWidth() {
        return areaWidth;
    }

    /**
     * Get current tick counter
     * @return current tick counter
     */
    int getTickCounter() {
        return tickCounter;
    }

    /**
     * Get how often tick is reset, impacting speed of game
     * @return how often tick is reset
     */
    int getTickReset() {
        return tickReset;
    }

    /**
     * Get tile at position (x,y)
     * @param x x position of tile
     * @param y y position of tile
     * @return tile at position (x,y)
     */
    Tile getTile(int x, int y){
        return gameArea[y][x];
    }

    private class ScheduleTask extends TimerTask {

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