package items.loot;

import appearance.Animation;
import appearance.GetImage;
import items.steve.SteveCondition;
import operators.Main;

import java.awt.image.BufferedImage;

public class SuperMushroom extends Booster {

    public SuperMushroom(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(125);
    }

    @Override
    public void onTouch(Steve steve, Main engine) {
        steve.acquirePoints(getPoint());

        GetImage imageLoader = new GetImage();

        if(!steve.getSteveCondition().isSuper()){
            BufferedImage[] leftFrames = imageLoader.getLeftFrames(SteveCondition.SUPER);
            BufferedImage[] rightFrames = imageLoader.getRightFrames(SteveCondition.SUPER);

            Animation animation = new Animation(leftFrames, rightFrames);
            SteveCondition newForm = new SteveCondition(animation, true, false);
            steve.setSteveCondition(newForm);
            steve.setDimension(48, 96);

            engine.playSuperMushroom();
        }
    }
}
