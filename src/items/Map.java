package items;

import items.block.Block;
import items.block.OrdinaryBlock;
import items.enemy.Enemy;
import items.steve.Arrow;
import items.steve.Steve;
import items.loot.Booster;
import items.loot.Diamond;
import items.loot.Loot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Map {

    private double remainingTime;
    private Steve steve;
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Block> groundBlocks = new ArrayList<>();
    private ArrayList<Loot> revealedLoot = new ArrayList<>();
    private ArrayList<Block> revealedBlocks = new ArrayList<>();
    private ArrayList<Arrow> arrows = new ArrayList<>();
    private FinishPoint finishPoint;
    private BufferedImage backgroundImage;
    private double bottomBorder = 720 - 96;
    private String path;


    public Map(double remainingTime, BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
        this.remainingTime = remainingTime;
    }


    public Steve getSteve() {
        return steve;
    }

    public void setSteve(Steve mario) {
        this.steve = mario;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    public ArrayList<Loot> getRevealedPrizes() {
        return revealedLoot;
    }

    public ArrayList<Block> getAllBlocks() {
        ArrayList<Block> allBricks = new ArrayList<>();

        allBricks.addAll(blocks);
        allBricks.addAll(groundBlocks);

        return allBricks;
    }

    public void addBlock(Block brick) {
        this.blocks.add(brick);
    }

    public void addGroundBlock(Block brick) {
        this.groundBlocks.add(brick);
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void drawMap(Graphics2D g2){
        drawBackground(g2);
        drawLoot(g2);
        drawBlocks(g2);
        drawEnemies(g2);
        drawArrows(g2);
        drawSteve(g2);
        finishPoint.draw(g2);
    }

    private void drawArrows(Graphics2D g2) {
        for(Arrow fireball : arrows){
            fireball.draw(g2);
        }
    }

    private void drawLoot(Graphics2D g2) {
        for(Loot prize : revealedLoot){
            if(prize instanceof Diamond){
                ((Diamond) prize).draw(g2);
            }
            else if(prize instanceof  Booster){
                ((Booster) prize).draw(g2);
            }
        }
    }

    private void drawBackground(Graphics2D g2){
        g2.drawImage(backgroundImage, 0, 0, null);
    }

    private void drawBlocks(Graphics2D g2) {
        for(Block block : blocks){
            if(block != null)
                block.draw(g2);
        }

        for(Block block : groundBlocks){
            block.draw(g2);
        }
    }

    private void drawEnemies(Graphics2D g2) {
        for(Enemy enemy : enemies){
            if(enemy != null)
                enemy.draw(g2);
        }
    }

    private void drawSteve(Graphics2D g2) {
        steve.draw(g2);
    }

    public void updateLocations() {
        steve.updateLocation();
        for(Enemy enemy : enemies){
            enemy.updateLocation();
        }

        for(Iterator<Loot> prizeIterator = revealedLoot.iterator(); prizeIterator.hasNext();){
            Loot prize = prizeIterator.next();
            if(prize instanceof Diamond){
                ((Diamond) prize).updateLocation();
                if(((Diamond) prize).getRevealBoundary() > ((Diamond) prize).getY()){
                    prizeIterator.remove();
                }
            }
            else if(prize instanceof Booster){
                ((Booster) prize).updateLocation();
            }
        }

        for (Arrow fireball: arrows) {
            fireball.updateLocation();
        }

        for(Iterator<Block> blockIterator = revealedBlocks.iterator(); blockIterator.hasNext();){
            OrdinaryBlock brick = (OrdinaryBlock)blockIterator.next();
            brick.animate();
            if(brick.getFrames() < 0){
                blocks.remove(brick);
                blockIterator.remove();
            }
        }

        finishPoint.updateLocation();
    }

    public double getBottomBorder() {
        return bottomBorder;
    }

    public void addRevealedPrize(Loot prize) {
        revealedLoot.add(prize);
    }

    public void addArrow(Arrow fireball) {
        arrows.add(fireball);
    }

    public void setFinishPoint(FinishPoint endPoint) {
        this.finishPoint = endPoint;
    }

    public FinishPoint getFinishPoint() {
        return finishPoint;
    }

    public void addRevealedBlock(OrdinaryBlock ordinaryBrick) {
        revealedBlocks.add(ordinaryBrick);
    }

    public void removeArrow(Arrow object) {
        arrows.remove(object);
    }

    public void removeEnemy(Enemy object) {
        enemies.remove(object);
    }

    public void removeLoot(Loot object) {
        revealedLoot.remove(object);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void updateTime(double passed){
        remainingTime = remainingTime - passed;
    }

    public boolean isTimeOver(){
        return remainingTime <= 0;
    }

    public double getRemainingTime() {
        return remainingTime;
    }
}
