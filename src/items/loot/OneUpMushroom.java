package items.loot;

import items.steve.Steve;
import operators.Main;

import java.awt.image.BufferedImage;

public class OneUpMushroom extends Booster{

    public OneUpMushroom(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(200);
    }

    @Override
    public void onTouch(Steve mario, Main engine) {
        mario.acquirePoints(getPoint());
        mario.setRemainingLives(mario.getRemainingLives() + 1);
        engine.playOneUp();
    }
}
