package appearance;

import operators.Main;
import operators.StatusList;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Appearance extends JPanel{

    private Main main;
    private Font font;
    private BufferedImage startScreenImage, aboutScreenImage, navigationScreenImage, gameOverScreen;
    private BufferedImage heartIcon;
    private BufferedImage diamondIcon;
    private BufferedImage selectIcon;
    private GetMap getMap;

    public Appearance(Main main, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));

        this.main = main;
        GetImage loader = main.getImageLoader();

        getMap = new GetMap();

        BufferedImage sprite = loader.loadImage("/sprite.png");
        this.heartIcon = loader.loadImage("/heart-icon.png");
        this.diamondIcon = loader.getSubImage(sprite, 1, 5, 48, 48);
        this.selectIcon = loader.loadImage("/select-icon.png");
        this.startScreenImage = loader.loadImage("/start-screen.png");
        this.navigationScreenImage = loader.loadImage("/navigation-screen.png");
        this.aboutScreenImage = loader.loadImage("/about-screen.png");
        this.gameOverScreen = loader.loadImage("/you-died-screen.png");

        try {
            InputStream in = getClass().getResourceAsStream("/media/font/minecraft-font.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (FontFormatException | IOException e) {
            font = new Font("Verdana", Font.PLAIN, 12);
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        StatusList gameStatus = main.getGameStatus();

        if(gameStatus == StatusList.START_SCREEN){
            drawStartScreen(g2);
        }
        else if(gameStatus == StatusList.MAP_SELECTION){
            drawMapSelectionScreen(g2);
        }
        else if(gameStatus == StatusList.ABOUT_SCREEN){
            drawAboutScreen(g2);
        }
        else if(gameStatus == StatusList.HELP_SCREEN){
            drawNavigationScreen(g2);
        }
        else if(gameStatus == StatusList.GAME_OVER){
            drawGameOverScreen(g2);
        }
        else {
            Point camLocation = main.getScreenLocation();
            g2.translate(-camLocation.x, -camLocation.y);
            main.drawMap(g2);
            g2.translate(camLocation.x, camLocation.y);

            drawPoints(g2);
            drawRemainingLives(g2);
            drawAcquiredDiamonds(g2);
            drawRemainingTime(g2);

            if(gameStatus == StatusList.PAUSED){
                drawPauseScreen(g2);
            }
            else if(gameStatus == StatusList.MISSION_PASSED){
                drawVictoryScreen(g2);
            }
        }

        g2.dispose();
    }

    private void drawRemainingTime(Graphics2D g2) {
        g2.setFont(font.deriveFont(25f));
        g2.setColor(Color.WHITE);
        String displayedStr = "TIME: " + main.getRemainingTime();
        g2.drawString(displayedStr, 750, 50);
    }

    private void drawVictoryScreen(Graphics2D g2) {
        g2.setFont(font.deriveFont(50f));
        g2.setColor(Color.WHITE);
        String displayedStr = "YOU WON!";
        int stringLength = g2.getFontMetrics().stringWidth(displayedStr);
        g2.drawString(displayedStr, (getWidth()-stringLength)/2, getHeight()/2);
    }

    private void drawNavigationScreen(Graphics2D g2) {
        g2.drawImage(navigationScreenImage, 0, 0, null);
    }

    private void drawAboutScreen(Graphics2D g2) {
        g2.drawImage(aboutScreenImage, 0, 0, null);
    }

    private void drawGameOverScreen(Graphics2D g2) {
        g2.drawImage(gameOverScreen, 0, 0, null);
        g2.setFont(font.deriveFont(50f));
        g2.setColor(new Color(255, 255, 255));
        String acquiredPoints = "Score: " + main.getScore();
        int stringLength = g2.getFontMetrics().stringWidth(acquiredPoints);
        int stringHeight = g2.getFontMetrics().getHeight();
        g2.drawString(acquiredPoints, (getWidth()-stringLength)/2, getHeight()-stringHeight*2);
    }

    private void drawPauseScreen(Graphics2D g2) {
        g2.setFont(font.deriveFont(50f));
        g2.setColor(Color.WHITE);
        String displayedStr = "PAUSED";
        int stringLength = g2.getFontMetrics().stringWidth(displayedStr);
        g2.drawString(displayedStr, (getWidth()-stringLength)/2, getHeight()/2);
    }

    private void drawAcquiredDiamonds(Graphics2D g2) {
        g2.setFont(font.deriveFont(30f));
        g2.setColor(Color.WHITE);
        String displayedStr = "" + main.getDiamonds();
        g2.drawImage(diamondIcon, getWidth()-115, 10, null);
        g2.drawString(displayedStr, getWidth()-65, 50);
    }

    private void drawRemainingLives(Graphics2D g2) {
        g2.setFont(font.deriveFont(30f));
        g2.setColor(Color.WHITE);
        String displayedStr = "" + main.getRemainingLives();
        g2.drawImage(heartIcon, 50, 10, null);
        g2.drawString(displayedStr, 100, 50);
    }

    private void drawPoints(Graphics2D g2){
        g2.setFont(font.deriveFont(25f));
        g2.setColor(Color.WHITE);
        String displayedStr = "Points: " + main.getScore();
        int stringLength = g2.getFontMetrics().stringWidth(displayedStr);
        g2.drawString(displayedStr, 300, 50);
    }

    private void drawStartScreen(Graphics2D g2){
        int row = main.getStartScreenSelection().getLineNumber();
        g2.drawImage(startScreenImage, 0, 0, null);
        g2.drawImage(selectIcon, 375, row * 70 + 440, null);
    }

    private void drawMapSelectionScreen(Graphics2D g2){
        g2.setFont(font.deriveFont(50f));
        g2.setColor(Color.WHITE);
        getMap.draw(g2);
        int row = main.getSelectedMap();
        int y_location = row*100+300-selectIcon.getHeight();
        g2.drawImage(selectIcon, 375, y_location, null);
    }

    public String selectMapViaMouse(Point mouseLocation) {
        return getMap.selectMap(mouseLocation);
    }

    public String selectMapViaKeyboard(int index){
        return getMap.selectMap(index);
    }

    public int changeSelectedMap(int index, boolean up){
        return getMap.changeSelectedMap(index, up);
    }
}