package items.enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Skeleton extends Enemy{

    private BufferedImage rightImage;

    public Skeleton(double x, double y, BufferedImage style) {
        super(x, y, style);
        setVelX(3);
    }

    @Override
    public void draw(Graphics g){
        if(getVelX() > 0){
            g.drawImage(rightImage, (int)getX(), (int)getY(), null);
        }
        else
            super.draw(g);
    }

    public void setRightImage(BufferedImage rightImage) {
        this.rightImage = rightImage;
    }
}