package operators;

import items.FinishPoint;
import items.block.*;
import items.loot.*;
import appearance.GetImage;
import items.Map;
import items.enemy.Enemy;
import items.enemy.Skeleton;
import items.enemy.Zombie;
import items.steve.Steve;

import java.awt.*;
import java.awt.image.BufferedImage;

class CreateMap {

    private GetImage imageLoader;

    private BufferedImage backgroundImage;
    private BufferedImage superMushroom, oneUpMushroom, rose, diamond;
    private BufferedImage ordinaryBlock, surpriseBlock, groundBlock, chest;
    private BufferedImage skeletonLeft, skeletonRight, zombieLeft, zombieRight, finishPoint;


    CreateMap(GetImage imageLoader) {

        this.imageLoader = imageLoader;
        BufferedImage sprite = imageLoader.loadImage("/sprite.png");

        this.backgroundImage = imageLoader.loadImage("/background.png");
        this.superMushroom = imageLoader.getSubImage(sprite, 2, 5, 48, 48);
        this.oneUpMushroom= imageLoader.getSubImage(sprite, 3, 5, 48, 48);
        this.rose= imageLoader.getSubImage(sprite, 4, 5, 48, 48);
        this.diamond = imageLoader.getSubImage(sprite, 1, 5, 48, 48);
        this.ordinaryBlock = imageLoader.getSubImage(sprite, 1, 1, 48, 48);
        this.surpriseBlock = imageLoader.getSubImage(sprite, 2, 1, 48, 48);
        this.groundBlock = imageLoader.getSubImage(sprite, 2, 2, 48, 48);
        this.chest = imageLoader.getSubImage(sprite, 3, 1, 96, 96);
        this.skeletonLeft = imageLoader.getSubImage(sprite, 2, 3, 48, 96);
        this.skeletonRight = imageLoader.getSubImage(sprite, 5, 3, 48, 96);
        this.zombieLeft = imageLoader.getSubImage(sprite, 1, 3, 48, 64);
        this.zombieRight = imageLoader.getSubImage(sprite, 4, 3, 48, 64);
        this.finishPoint = imageLoader.getSubImage(sprite, 5, 1, 48, 48);

    }

    Map createMap(String mapPath, double timeLimit) {
        BufferedImage mapImage = imageLoader.loadImage(mapPath);

        if (mapImage == null) {
            System.out.println("Given path is invalid...");
            return null;
        }

        Map createdMap = new Map(timeLimit, backgroundImage);
        String[] paths = mapPath.split("/");
        createdMap.setPath(paths[paths.length-1]);

        int pixelMultiplier = 48;

        int steve = new Color(160, 160, 160).getRGB();
        int ordinaryBlock = new Color(0, 0, 255).getRGB();
        int surpriseBlock = new Color(255, 255, 0).getRGB();
        int groundBlock = new Color(255, 0, 0).getRGB();
        int chest = new Color(0, 255, 0).getRGB();
        int skeleton = new Color(0, 255, 255).getRGB();
        int zombie = new Color(255, 0, 255).getRGB();
        int finish = new Color(160, 0, 160).getRGB();

        for (int x = 0; x < mapImage.getWidth(); x++) {
            for (int y = 0; y < mapImage.getHeight(); y++) {

                int currentPixel = mapImage.getRGB(x, y);
                int xLocation = x*pixelMultiplier;
                int yLocation = y*pixelMultiplier;

                if (currentPixel == ordinaryBlock) {
                    Block brick = new OrdinaryBlock(xLocation, yLocation, this.ordinaryBlock);
                    createdMap.addBlock(brick);
                }
                else if (currentPixel == surpriseBlock) {
                    Loot prize = generateRandomPrize(xLocation, yLocation);
                    Block brick = new SurpriseBlock(xLocation, yLocation, this.surpriseBlock, prize);
                    createdMap.addBlock(brick);
                }
                else if (currentPixel == chest) {
                    Block brick = new Chest(xLocation, yLocation, this.chest);
                    createdMap.addGroundBlock(brick);
                }
                else if (currentPixel == groundBlock) {
                    Block brick = new GroundBlock(xLocation, yLocation, this.groundBlock);
                    createdMap.addGroundBlock(brick);
                }
                else if (currentPixel == skeleton) {
                    Enemy enemy = new Skeleton(xLocation, yLocation, this.skeletonLeft);
                    ((Skeleton)enemy).setRightImage(skeletonRight);
                    createdMap.addEnemy(enemy);
                }
                else if (currentPixel == zombie) {
                    Enemy enemy = new Zombie(xLocation, yLocation, this.zombieLeft);
                    ((Zombie)enemy).setRightImage(zombieRight);
                    createdMap.addEnemy(enemy);
                }
                else if (currentPixel == steve) {
                    Steve steveObject = new Steve(xLocation, yLocation);
                    createdMap.setSteve(steveObject);
                }
                else if(currentPixel == finish){
                    FinishPoint endPoint= new FinishPoint(xLocation+24, yLocation, finishPoint);
                    createdMap.setFinishPoint(endPoint);
                }
            }
        }
        return createdMap;
    }

    private Loot generateRandomPrize(double x, double y){
        Loot generated;
        int random = (int)(Math.random() * 12);

        if(random == 0){
            generated = new SuperMushroom(x, y, this.superMushroom);
        }
        else if(random == 1){
            generated = new Rose(x, y, this.rose);
        }
        else if(random == 2){
            generated = new OneUpMushroom(x, y, this.oneUpMushroom);
        }
        else{
            generated = new Diamond(x, y, this.diamond, 50);
        }

        return generated;
    }


}
