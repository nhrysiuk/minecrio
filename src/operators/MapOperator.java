package operators;

import appearance.GetImage;
import items.GameObject;
import items.Map;
import items.block.Block;
import items.block.OrdinaryBlock;
import items.enemy.Enemy;
import items.loot.Booster;
import items.loot.Diamond;
import items.loot.Loot;
import items.steve.Arrow;
import items.steve.Steve;

import java.awt.*;
import java.util.ArrayList;

public class MapOperator {

    private Map map;

    public MapOperator() {}

    public void updateLocations() {
        if (map == null)
            return;

        map.updateLocations();
    }

    public void resetCurrentMap(Main engine) {
        Steve steve = getSteve();
        steve.resetLocation();
        engine.resetScreen();
        createMap(engine.getImageLoader(), map.getPath());
        map.setSteve(steve);
    }

    public boolean createMap(GetImage loader, String path) {
        CreateMap mapCreator = new CreateMap(loader);
        map = mapCreator.createMap("/maps/" + path, 400);

        return map != null;
    }

    public void acquirePoints(int point) {
        map.getSteve().acquirePoints(point);
    }

    public Steve getSteve() {
        return map.getSteve();
    }

    public void fire(Main engine) {
        Arrow fireball = getSteve().fire();
        if (fireball != null) {
            map.addArrow(fireball);
            engine.playArrow();
        }
    }

    public boolean isGameOver() {
        return getSteve().getRemainingLives() == 0 || map.isTimeOver();
    }

    public int getScore() {
        return getSteve().getPoints();
    }

    public int getRemainingLives() {
        return getSteve().getRemainingLives();
    }

    public int getDiamonds() {
        return getSteve().getDiamonds();
    }

    public void drawMap(Graphics2D g2) {
        map.drawMap(g2);
    }

    public int passMission() {
        if(getSteve().getX() >= map.getFinishPoint().getX() && !map.getFinishPoint().isTouched()){
            map.getFinishPoint().setTouched(true);
            int height = (int)getSteve().getY();
            return height * 2;
        }
        else
            return -1;
    }

    public boolean endLevel(){
        return getSteve().getX() >= map.getFinishPoint().getX() + 320;
    }

    public void checkCollisions(Main engine) {
        if (map == null) {
            return;
        }

        checkBottomCollisions(engine);
        checkTopCollisions(engine);
        checkSteveHorizontalCollision(engine);
        checkEnemyCollisions();
        checkLootCollision();
        checkLootContact(engine);
        checkArrowContact();
    }

    private void checkBottomCollisions(Main engine) {
        Steve steve = getSteve();
        ArrayList<Block> blocks = map.getAllBlocks();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<GameObject> toBeRemoved = new ArrayList<>();

        Rectangle steveBottomBounds = steve.getBottomBounds();

        if (!steve.isJumping())
            steve.setFalling(true);

        for (Block block : blocks) {
            Rectangle blockTopBounds = block.getTopBounds();
            if (steveBottomBounds.intersects(blockTopBounds)) {
                steve.setY(block.getY() - steve.getDimension().height + 1);
                steve.setFalling(false);
                steve.setVelY(0);
            }
        }

        for (Enemy enemy : enemies) {
            Rectangle enemyTopBounds = enemy.getTopBounds();
            if (steveBottomBounds.intersects(enemyTopBounds)) {
                steve.acquirePoints(100);
                toBeRemoved.add(enemy);
                engine.playStomp();
            }
        }

        if (steve.getY() + steve.getDimension().height >= map.getBottomBorder()) {
            steve.setY(map.getBottomBorder() - steve.getDimension().height);
            steve.setFalling(false);
            steve.setVelY(0);
        }

        removeObjects(toBeRemoved);
    }

    private void checkTopCollisions(Main engine) {
        Steve steve = getSteve();
        ArrayList<Block> blocks = map.getAllBlocks();

        Rectangle steveTopBounds = steve.getTopBounds();
        for (Block block : blocks) {
            Rectangle blockBottomBounds = block.getBottomBounds();
            if (steveTopBounds.intersects(blockBottomBounds)) {
                steve.setVelY(0);
                steve.setY(block.getY() + block.getDimension().height);
                Loot prize = block.reveal(engine);
                if(prize != null)
                    map.addRevealedPrize(prize);
            }
        }
    }

    private void checkSteveHorizontalCollision(Main engine){
        Steve steve = getSteve();
        ArrayList<Block> blocks = map.getAllBlocks();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<GameObject> toBeRemoved = new ArrayList<>();

        boolean steveDies = false;
        boolean toRight = steve.getToRight();

        Rectangle steveBounds = toRight ? steve.getRightBounds() : steve.getLeftBounds();

        for (Block block : blocks) {
            Rectangle blockBounds = !toRight ? block.getRightBounds() : block.getLeftBounds();
            if (steveBounds.intersects(blockBounds)) {
                steve.setVelX(0);
                if(toRight)
                    steve.setX(block.getX() - steve.getDimension().width);
                else
                    steve.setX(block.getX() + block.getDimension().width);
            }
        }

        for(Enemy enemy : enemies){
            Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
            if (steveBounds.intersects(enemyBounds)) {
                steveDies = steve.onTouchEnemy(engine);
                toBeRemoved.add(enemy);
            }
        }
        removeObjects(toBeRemoved);


        if (steve.getX() <= engine.getScreenLocation().getX() && steve.getVelX() < 0) {
            steve.setVelX(0);
            steve.setX(engine.getScreenLocation().getX());
        }

        if(steveDies) {
            resetCurrentMap(engine);
        }
    }

    private void checkEnemyCollisions() {
        ArrayList<Block> blocks = map.getAllBlocks();
        ArrayList<Enemy> enemies = map.getEnemies();

        for (Enemy enemy : enemies) {
            boolean standsOnBlock = false;

            for (Block block : blocks) {
                Rectangle enemyBounds = enemy.getLeftBounds();
                Rectangle blockBounds = block.getRightBounds();

                Rectangle enemyBottomBounds = enemy.getBottomBounds();
                Rectangle blockTopBounds = block.getTopBounds();

                if (enemy.getVelX() > 0) {
                    enemyBounds = enemy.getRightBounds();
                    blockBounds = block.getLeftBounds();
                }

                if (enemyBounds.intersects(blockBounds)) {
                    enemy.setVelX(-enemy.getVelX());
                }

                if (enemyBottomBounds.intersects(blockTopBounds)){
                    enemy.setFalling(false);
                    enemy.setVelY(0);
                    enemy.setY(block.getY()-enemy.getDimension().height);
                    standsOnBlock = true;
                }
            }

            if(enemy.getY() + enemy.getDimension().height > map.getBottomBorder()){
                enemy.setFalling(false);
                enemy.setVelY(0);
                enemy.setY(map.getBottomBorder()-enemy.getDimension().height);
            }

            if (!standsOnBlock && enemy.getY() < map.getBottomBorder()){
                enemy.setFalling(true);
            }
        }
    }

    private void checkLootCollision() {
        ArrayList<Loot> loots = map.getRevealedPrizes();
        ArrayList<Block> blocks = map.getAllBlocks();

        for (Loot loot : loots) {
            if (loot instanceof Booster) {
                Booster boost = (Booster) loot;
                Rectangle prizeBottomBounds = boost.getBottomBounds();
                Rectangle prizeRightBounds = boost.getRightBounds();
                Rectangle prizeLeftBounds = boost.getLeftBounds();
                boost.setFalling(true);

                for (Block block : blocks) {
                    Rectangle blockBounds;

                    if (boost.isFalling()) {
                        blockBounds = block.getTopBounds();

                        if (blockBounds.intersects(prizeBottomBounds)) {
                            boost.setFalling(false);
                            boost.setVelY(0);
                            boost.setY(block.getY() - boost.getDimension().height + 1);
                            if (boost.getVelX() == 0)
                                boost.setVelX(2);
                        }
                    }

                    if (boost.getVelX() > 0) {
                        blockBounds = block.getLeftBounds();

                        if (blockBounds.intersects(prizeRightBounds)) {
                            boost.setVelX(-boost.getVelX());
                        }
                    } else if (boost.getVelX() < 0) {
                        blockBounds = block.getRightBounds();

                        if (blockBounds.intersects(prizeLeftBounds)) {
                            boost.setVelX(-boost.getVelX());
                        }
                    }
                }

                if (boost.getY() + boost.getDimension().height > map.getBottomBorder()) {
                    boost.setFalling(false);
                    boost.setVelY(0);
                    boost.setY(map.getBottomBorder() - boost.getDimension().height);
                    if (boost.getVelX() == 0)
                        boost.setVelX(2);
                }

            }
        }
    }

    private void checkLootContact(Main engine) {
        ArrayList<Loot> loots = map.getRevealedPrizes();
        ArrayList<GameObject> toBeRemoved = new ArrayList<>();

        Rectangle steveBounds = getSteve().getBounds();
        for(Loot loot : loots){
            Rectangle prizeBounds = loot.getBounds();
            if (prizeBounds.intersects(steveBounds)) {
                loot.onTouch(getSteve(), engine);
                toBeRemoved.add((GameObject) loot);
            } else if(loot instanceof Diamond){
                loot.onTouch(getSteve(), engine);
            }
        }

        removeObjects(toBeRemoved);
    }

    private void checkArrowContact() {
        ArrayList<Arrow> arrows = map.getArrows();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<Block> blocks = map.getAllBlocks();
        ArrayList<GameObject> toBeRemoved = new ArrayList<>();

        for(Arrow arrow : arrows){
            Rectangle arrowBounds = arrow.getBounds();

            for(Enemy enemy : enemies){
                Rectangle enemyBounds = enemy.getBounds();
                if (arrowBounds.intersects(enemyBounds)) {
                    acquirePoints(100);
                    toBeRemoved.add(enemy);
                    toBeRemoved.add(arrow);
                }
            }

            for(Block block : blocks){
                Rectangle blockBounds = block.getBounds();
                if (arrowBounds.intersects(blockBounds)) {
                    toBeRemoved.add(arrow);
                }
            }
        }

        removeObjects(toBeRemoved);
    }

    private void removeObjects(ArrayList<GameObject> list){
        if(list == null)
            return;

        for(GameObject object : list){
            if(object instanceof Arrow){
                map.removeArrow((Arrow) object);
            }
            else if(object instanceof Enemy){
                map.removeEnemy((Enemy)object);
            }
            else if(object instanceof Diamond || object instanceof Booster){
                map.removeLoot((Loot) object);
            }
        }
    }

    public void addRevealedBlock(OrdinaryBlock ordinaryblock) {
        map.addRevealedBlock(ordinaryblock);
    }

    public void updateTime(){
        if(map != null)
            map.updateTime(1);
    }

    public int getRemainingTime() {
        return (int)map.getRemainingTime();
    }
}
