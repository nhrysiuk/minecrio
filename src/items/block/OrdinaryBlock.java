package items.block;

import operators.Main;
import operators.MapOperator;
import items.Map;
import items.loot.Loot;
import appearance.Animation;
import appearance.GetImage;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OrdinaryBlock extends Block {

    private Animation animation;
    private boolean breaking;
    private int frames;

    public OrdinaryBlock(double x, double y, BufferedImage style){
        super(x, y, style);
        setBreakable(true);
        setEmpty(true);

        setAnimation();
        breaking = false;
        frames = animation.getLeftFrames().length;
    }

    private void setAnimation(){
        GetImage imageLoader = new GetImage();
        BufferedImage[] leftFrames = imageLoader.getBrickFrames();

        animation = new Animation(leftFrames, leftFrames);
    }

    @Override
    public Loot reveal(Main engine){
        MapOperator manager = engine.getMapManager();
        if(!manager.getSteve().isSuper())
            return null;

        breaking = true;
        manager.addRevealedBlock(this);

        double newX = getX() - 27, newY = getY() - 27;
        setLocation(newX, newY);

        return null;
    }

    public int getFrames(){
        return frames;
    }

    public void animate(){
        if(breaking){
            setStyle(animation.animate(3, true));
            frames--;
        }
    }
}
