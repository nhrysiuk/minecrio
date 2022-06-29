package items.loot;

import operators.Main;
import items.steve.Steve;
import items.steve.SteveCondition;
import appearance.Animation;
import appearance.GetImage;

import java.awt.image.BufferedImage;

public class Rose extends Booster {

    public Rose(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(150);
    }

    @Override
    public void onTouch(Steve mario, Main engine) {
        mario.acquirePoints(getPoint());

        GetImage imageLoader = new GetImage();

        if(!mario.getSteveCondition().isFire()){
            BufferedImage[] leftFrames = imageLoader.getLeftFrames(SteveCondition.FIRE);
            BufferedImage[] rightFrames = imageLoader.getRightFrames(SteveCondition.FIRE);

            Animation animation = new Animation(leftFrames, rightFrames);
            SteveCondition newForm = new SteveCondition(animation, true, true);
            mario.setSteveCondition(newForm);
            mario.setDimension(48, 96);

            engine.playRose();
        }
    }

    @Override
    public void updateLocation(){}

}
