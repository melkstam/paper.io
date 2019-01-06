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

    private int areaHeight = 100;
    private int areaWidth = 100;
    private int width;
    private int height;
    private Tile[][] gameArea = new Tile[areaHeight][areaWidth];
    private List<Player> players = new ArrayList<>();
    private HumanPlayer humanPlayer;
    private HashMap<Player, int[]> playerPositions = new HashMap<>();
    private HashMap<Player, Tile> playerCurrentPositions = new HashMap<>();

    private final int scale = 20;
    private int tickCounter = 0;
    private final int tickReset = 8;

    private final int INITIAL_DELAY = 0;
    private final int PERIOD_INTERVAL = 1000/60;

    private boolean splitScreen = false;
    private boolean paused = true;

    private String p1name;
    private String p2name;

    private int keyToSend;
    private ActionListener actionListener;

    private Painter painter;


    private List<Color> colorList = new ArrayList<>(Arrays.asList(Color.magenta, Color.green, Color.red,
            Color.blue, Color.orange, Color.yellow, Color.pink, new Color(142,12,255),
            new Color(255,43,119), new Color(100,255,162)));

    public Board(ActionListener actionListener, String p1name){
        this.actionListener = actionListener;
        this.p1name = p1name;
        initBoard();
        painter = new Painter(getWidth(), getHeight(), scale, this, humanPlayer, players);
    }

    public Board(ActionListener actionListener, String p1name, String p2name) {
        this.actionListener = actionListener;
        this.p1name = p1name;
        this.p2name = p2name;
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

        players.add(new HumanPlayer(gameArea.length, gameArea[0].length, new Color((int)(Math.random() * 0x1000000)), p1name));
        humanPlayer = (HumanPlayer)players.get(0);
        for(int i = 0; i < 0; i++){
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

    // TODO call paintComponent/repaint from Painter
    /**
     * Overrides paintComponent and is called whenever everything should be drawn on the screen
     * @param g Graphics element used to draw elements on screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        painter.draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Method responsible for main logic of the game
     */
    private void tick(){
        for (Player player : players) {
            if(player.getAlive() == true) {
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

                        playerPositions.put(player, new int[]{player.getX(), player.getY()});

                    }
                    player.setCurrentTile(gameArea[player.getX()][player.getY()]);
                    playerCurrentPositions.put(player, gameArea[player.getX()][player.getY()]);
                    findCollision();
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if(keyToSend != 0){
            humanPlayer.keyPressed(keyToSend);
            keyToSend = 0;
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
    public void setPaused(Boolean b){
        paused = b;
    }

    public Tile[][] getGameArea() {
        return gameArea;
    }

    public int getTickCounter() {
        return tickCounter;
    }

    public int getTickReset() {
        return tickReset;
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