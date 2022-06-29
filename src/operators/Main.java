package operators;

import items.steve.Steve;
import appearance.GetImage;
import appearance.GetMenu;
import appearance.Appearance;

import javax.swing.*;
import java.awt.*;

public class Main implements Runnable {

    private final static int WIDTH = 1268, HEIGHT = 708;

    private MapOperator mapOperator;
    private Appearance appearance;
    private SoundSupport soundSupport;
    private StatusList statusList;
    private boolean isRunning;
    private ScreenNow screen;
    private GetImage getImage;
    private Thread thread;
    private GetMenu getMenu = GetMenu.START_GAME;
    private int selectedMap = 0;

    public static void main(String... args) {
        new Main();
    }

    private Main() {
        init();
    }

    private void init() {
        getImage = new GetImage();
        InputOperator inputOperator = new InputOperator(this);
        statusList = StatusList.START_SCREEN;
        screen = new ScreenNow();
        appearance = new Appearance(this, WIDTH, HEIGHT);
        soundSupport = new SoundSupport();
        mapOperator = new MapOperator();

        JFrame frame = new JFrame("Minecrio");
        frame.add(appearance);
        frame.addKeyListener(inputOperator);
        frame.addMouseListener(inputOperator);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        start();
    }

    private synchronized void start() {
        if (isRunning)
            return;

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void reset(){
        resetScreen();
        setGameStatus(StatusList.START_SCREEN);
    }

    public void resetScreen(){
        screen = new ScreenNow();
        soundSupport.restartBackground();
    }

    public void selectMapViaMouse() {
        String path = appearance.selectMapViaMouse(appearance.getMousePosition());
        if (path != null) {
            createMap(path);
        }
    }

    public void selectMapViaKeyboard(){
        String path = appearance.selectMapViaKeyboard(selectedMap);
        if (path != null) {
            createMap(path);
        }
    }

    public void changeSelectedMap(boolean up){
        selectedMap = appearance.changeSelectedMap(selectedMap, up);
    }

    private void createMap(String path) {
        boolean loaded = mapOperator.createMap(getImage, path);
        if(loaded){
            setGameStatus(StatusList.RUNNING);
            soundSupport.restartBackground();
        }

        else
            setGameStatus(StatusList.START_SCREEN);
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (isRunning && !thread.isInterrupted()) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                if (statusList == StatusList.RUNNING) {
                    gameLoop();
                }
                delta--;
            }
            render();

            if(statusList != StatusList.RUNNING) {
                timer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                mapOperator.updateTime();
            }
        }
    }

    private void render() {
        appearance.repaint();
    }

    private void gameLoop() {
        updateLocations();
        checkCollisions();
        updateScreen();

        if (isGameOver()) {
            setGameStatus(StatusList.GAME_OVER);
        }

        int missionPassed = passMission();
        if(missionPassed > -1){
            mapOperator.acquirePoints(missionPassed);
        } else if(mapOperator.endLevel())
            setGameStatus(StatusList.MISSION_PASSED);
    }

    private void updateScreen() {
        Steve steve = mapOperator.getSteve();
        double steveVelocityX = steve.getVelX();
        double shiftAmount = 0;

        if (steveVelocityX > 0 && steve.getX() - 600 > screen.getX()) {
            shiftAmount = steveVelocityX;
        }

        screen.moveScreen(shiftAmount, 0);
    }

    private void updateLocations() {
        mapOperator.updateLocations();
    }

    private void checkCollisions() {
        mapOperator.checkCollisions(this);
    }

    public void receiveInput(ButtonActionList input) {

        if (statusList == StatusList.START_SCREEN) {
            if (input == ButtonActionList.SELECT && getMenu == GetMenu.START_GAME) {
                startGame();
            } else if (input == ButtonActionList.SELECT && getMenu == GetMenu.VIEW_ABOUT) {
                setGameStatus(StatusList.ABOUT_SCREEN);
            } else if (input == ButtonActionList.SELECT && getMenu == GetMenu.VIEW_HELP) {
                setGameStatus(StatusList.HELP_SCREEN);
            } else if (input == ButtonActionList.GO_UP) {
                selectOption(true);
            } else if (input == ButtonActionList.GO_DOWN) {
                selectOption(false);
            }
        }
        else if(statusList == StatusList.MAP_SELECTION){
            if(input == ButtonActionList.SELECT){
                selectMapViaKeyboard();
            }
            else if(input == ButtonActionList.GO_UP){
                changeSelectedMap(true);
            }
            else if(input == ButtonActionList.GO_DOWN){
                changeSelectedMap(false);
            }
        } else if (statusList == StatusList.RUNNING) {
            Steve steve = mapOperator.getSteve();
            if (input == ButtonActionList.JUMP) {
                steve.jump(this);
            } else if (input == ButtonActionList.M_RIGHT) {
                steve.move(true, screen);
            } else if (input == ButtonActionList.M_LEFT) {
                steve.move(false, screen);
            } else if (input == ButtonActionList.ACTION_COMPLETED) {
                steve.setVelX(0);
            } else if (input == ButtonActionList.FIRE) {
                mapOperator.fire(this);
            } else if (input == ButtonActionList.PAUSE_RESUME) {
                pauseGame();
            }
        } else if (statusList == StatusList.PAUSED) {
            if (input == ButtonActionList.PAUSE_RESUME) {
                pauseGame();
            }
        } else if(statusList == StatusList.GAME_OVER && input == ButtonActionList.GO_TO_START_SCREEN){
            reset();
        } else if(statusList == StatusList.MISSION_PASSED && input == ButtonActionList.GO_TO_START_SCREEN){
            reset();
        }

        if(input == ButtonActionList.GO_TO_START_SCREEN){
            setGameStatus(StatusList.START_SCREEN);
        }
    }

    private void selectOption(boolean selectUp) {
        getMenu= getMenu.select(selectUp);
    }

    private void startGame() {
        if (statusList != StatusList.GAME_OVER) {
            setGameStatus(StatusList.MAP_SELECTION);
        }
    }

    private void pauseGame() {
        if (statusList == StatusList.RUNNING) {
            setGameStatus(StatusList.PAUSED);
            soundSupport.pauseBackground();
        } else if (statusList == StatusList.PAUSED) {
            setGameStatus(StatusList.RUNNING);
            soundSupport.resumeBackground();
        }
    }

    public void shakeScreen(){
        screen.shakeScreen();
    }

    private boolean isGameOver() {
        if(statusList == StatusList.RUNNING)
            return mapOperator.isGameOver();
        return false;
    }

    public GetImage getImageLoader() {
        return getImage;
    }

    public StatusList getGameStatus() {
        return statusList;
    }

    public GetMenu getStartScreenSelection() {
        return getMenu;
    }

    public void setGameStatus(StatusList gameStatus) {
        this.statusList = gameStatus;
    }

    public int getScore() {
        return mapOperator.getScore();
    }

    public int getRemainingLives() {
        return mapOperator.getRemainingLives();
    }

    public int getDiamonds() {
        return mapOperator.getDiamonds();
    }

    public int getSelectedMap() {
        return selectedMap;
    }

    public void drawMap(Graphics2D g2) {
        mapOperator.drawMap(g2);
    }

    public Point getScreenLocation() {
        return new Point((int)screen.getX(), (int)screen.getY());
    }

    private int passMission(){
        return mapOperator.passMission();
    }

    public void playDiamond() {
        soundSupport.playDiamond();
    }

    public void playOneUp() {
        soundSupport.playOneUp();
    }

    public void playSuperMushroom() {soundSupport.playSuperMushroom();}

    public void playSteveDies() {
        soundSupport.playSteveDies();
    }

    public void playJump() {
        soundSupport.playJump();
    }

    public void playRose() {
        soundSupport.playRose();
    }

    public void playArrow() {
        soundSupport.playArrow();
    }

    public void playStomp() {
        soundSupport.playStomp();
    }

    public MapOperator getMapManager() {
        return mapOperator;
    }

    public int getRemainingTime() {
        return mapOperator.getRemainingTime();
    }
}
