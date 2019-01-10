package se.liu.ida.paperio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Timer;
import java.util.*;

/**
 * The board class is the class responsible for main game logic. This class initializes the tile grid, players and
 * keeps track of them. Board is also specifies key bindings, fills enclosed areas and keeps track of a timer to tick
 * through game logic. Board draws the live scoreboard but uses one or two Painter:s to draw the game area and players
 * on it.
 */
public class Board extends JPanel {

    //TODO Add end game logic

    private final int areaHeight;
    private final int areaWidth;
    private Tile[][] gameArea;
    private final int scale = 10;

    private int botNumber;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<HumanPlayer> humanPlayers = new ArrayList<>();
    private HashMap<Tile, Player> tilePlayerMap = new HashMap<>();

    private int tickCounter = 0;
    private final int tickReset;

    private ArrayList<Player> deadBots = new ArrayList<>();
    private boolean paused = true;
    private ActionListener actionListener;

    private ArrayList<Painter> painters = new ArrayList<>();

    private List<Color> colorList = new ArrayList<>(Arrays.asList(Color.magenta, Color.green, Color.red,
            Color.blue, Color.orange, Color.yellow, Color.pink, new Color(142,12,255),
            new Color(255,43,119), new Color(100,255,162)));

    /**
     * Creates board for singleplayer
     * @param actionListener listener for key presses and state updates
     * @param p1name name of player
     * @param areaHeight height of game area
     * @param areaWidth width of game area
     * @param gameSpeed game speed between 1 and 5, 5 being the fastest
     * @param botNumber number of bots to have in game
     */
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

    /**
     * Creates board for multiplayer
     * @param actionListener listener for key presses and state updates
     * @param p1name name of player 1
     * @param p2name name of player 2
     * @param areaHeight height of game area
     * @param areaWidth width of game area
     * @param gameSpeed game speed between 1 and 5, 5 being the fastest
     * @param botNumber number of bots to have in game
     */
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

    /**
     * Initializes necessary variables, timer, players etc required for the board
     */
    private void initBoard(){
        this.gameArea = new Tile[areaHeight][areaWidth];
        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                gameArea[i][j] = new Tile(j,i);
            }
        }

        specifyKeyActions();
        setBackground(Color.BLACK);

        // Adds new bots and give them a color either from colorList or randomized
        for(int i = 0; i < botNumber; i++){
            if(i > 9){
                players.add(new BotPlayer(gameArea.length,gameArea[0].length,
                        new Color((int)(Math.random() * 0x1000000))));
            }else {
                players.add(new BotPlayer(gameArea.length, gameArea[0].length, colorList.get(i)));
            }
        }

        // Gives each player a starting area and makes sure that they don't spawn too close to each other
        for(int i = 0; i < players.size(); i++){
            // If bot is too close to another bot, remove it and create a new one instead
            if(!checkSpawn(players.get(i))){
                players.remove(players.get(i));
                i--;
                if(botNumber > 9){
                    players.add(new BotPlayer(gameArea.length,gameArea[0].length,
                            new Color((int)(Math.random() * 0x1000000))));
                }else {
                    players.add(new BotPlayer(gameArea.length,gameArea[0].length, colorList.get(i)));
                }
                continue;
            }else {
                startingArea(players.get(i));
            }
        }

        // Starts a timer to tick the game logic
        final int INITIAL_DELAY = 0;
        final int PERIOD_INTERVAL = 1000/60;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(),
                INITIAL_DELAY, PERIOD_INTERVAL);
    }


    /**
     * Specifies necessary key bindings and key actions for game
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
     * Marks all tiles in the starting area of a player to owned by player
     * @param player player to generate starting area for
     */
    private void startingArea(Player player){
        int x = player.getX();
        int y = player.getY();
        if(!checkSpawn(player)){
            Player playerCopy = new BotPlayer(gameArea.length,gameArea[0].length, player.getColor());
            startingArea(playerCopy);
        }
        for(int i = x-1; i <= x+1; i++){
            for(int j = y-1; j <= y+1; j++){
                player.setTileOwned(getTile(i,j));
            }
        }
    }

    /**
     * Makes sure that a player doesn't spawn on, or too close to another player. Range is set to ±9 square tiles
     * @param player Player that you want to check surroundings for other players
     * @return  True if nobody is close, False otherwise
     */
    private boolean checkSpawn(Player player){
        int x = player.getX();
        int y = player.getY();
        for(int i = x-3; i <= x+3; i++) {
            for (int j = y - 3; j <= y + 3; j++) {
                if (getTile(i, j).getOwner() != null || getTile(i, j).getContestedOwner() != null ) {
                    System.out.println("TRY AGAIN");
                    return false;
                }
            }
        }
        return true;
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
        try {
            drawScoreboard(g);

        } catch(IndexOutOfBoundsException e){

        }
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
     * Method responsible for main logic of the game. Checks collisions and if enclosures should be filled.
     */
    private void tick(){
        Player player;
        tilePlayerMap.clear();
        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            player.move();
            // Kill player if player moves outside game area
            if(player.getX() < 0 || player.getX() >= areaWidth || player.getY() < 0 || player.getY() >= areaHeight){
                player.die();
            }else{
                Tile tile = getTile(player.getX(), player.getY());
                player.checkCollision(tile);
                player.setCurrentTile(tile);
                findCollision(player, tile);

                // If player is outside their owned territory
                if (tile.getOwner() != player && player.getAlive()) {
                    player.setTileContested(tile);
                    // If player arrives back to an owned tile
                } else if (player.getTilesContested().size() > 0) {
                    player.contestToOwned();
                    fillEnclosure(player);
                }
            }
            // If BotPlayer is killed, add it to deadBots list
            if(player instanceof BotPlayer && !player.getAlive()){
                deadBots.add(player);
            }
        }
        respawnBots();

        // Remove dead players
        players.removeIf(p -> !p.getAlive());

        for(HumanPlayer humanPlayer : humanPlayers){
            humanPlayer.updateD();
        }
    }

    /**
     * Method that respawns dead bots after a set interval
     */
    private void respawnBots(){
        for(int i = 0; i < deadBots.size(); i++){
            if(deadBots.get(i).getAlive()){
                Player player = new BotPlayer(gameArea.length,gameArea[0].length,
                        new Color((int)(Math.random() * 0x1000000)));
                startingArea(player);
                players.add(player);
                deadBots.remove(deadBots.get(i));
            }
        }
    }

    /**
     * Method that detects player-to-player head on collision
     * @param player Player you want to check collision for
     * @param tile   Tile that Player currently is on
     */
    private void findCollision(Player player, Tile tile) {
        // If corresponding tile is found in tilePlayerMap
        if(tilePlayerMap.containsKey(tile)) {

            // Iterate through all entries in tilePlayerMap, if the Tile in entry matches Tile in input,
            // compare sizes between players and destroy one of them. The player with the largest tiles contested
            // survives. If both players have the same amount of tiles contested, the player with the most tiles
            // owned survives. If both players have the same amount of tiles contested and tiles owned,
            // the first player added to Players list dies.
            for(Map.Entry<Tile, Player> entry : tilePlayerMap.entrySet()) {
                if (entry.getKey() == tile) {
                    if (entry.getValue().getTilesContested().size() > player.getTilesContested().size()) {
                        entry.getValue().die();
                    } else if (entry.getValue().getTilesContested().size() < player.getTilesContested().size()) {
                        player.die();
                    } else if (entry.getValue().getTilesContested().size() == player.getTilesContested().size()) {
                        if (entry.getValue().getTilesOwned().size() > player.getTilesOwned().size()) {
                            entry.getValue().die();
                        } else {
                            player.die();
                        }
                    }
                }
            }
        }else { // If no corresponding tile is found, add tile and player to tilePlayerMap
            tilePlayerMap.put(tile, player);
        }
        // Remove dead players
        players.removeIf(p -> !p.getAlive());
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

    /**
     * ScheduleTask is responsible for receiving and responding to timer calls
     */
    private class ScheduleTask extends TimerTask {

        /**
         * Gets called by timer at specified interval. Calls tick at specified rate and repaint each time
         */
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