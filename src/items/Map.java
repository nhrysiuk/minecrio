package items;

import items.block.Block;
import items.enemy.Enemy;
import items.loot.Loot;
import items.steve.Arrow;
import items.steve.Steve;
import operators.MapOperator;

import java.awt.*;
import java.util.ArrayList;

public class Map {
    public void updateLocations() {
    }

    public String getPath() {
        return null;
    }

    public void setSteve(Steve steve) {
    }

    public MapOperator getSteve() {
        return null;
    }

    public void addArrow(Arrow fireball) {
    }

    public boolean isTimeOver() {
        return false;
    }

    public void drawMap(Graphics2D g2) {
    }

    public FinishPoint getFinishPoint() {
        return null;
    }

    public ArrayList<Block> getAllBlocks() {
        return null;
    }

    public ArrayList<Enemy> getEnemies() {
        return null;
    }

    public double getBottomBorder() {
        return 0;
    }

    public void addRevealedPrize(Loot prize) {
    }
}
