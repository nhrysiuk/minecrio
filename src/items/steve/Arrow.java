package items.steve;


import items.GameObject;

import java.awt.image.BufferedImage;

public class Arrow extends GameObject {

    public Arrow(double x, double y, BufferedImage style, boolean toRight) {
        super(x, y, style);
        setDimension(24, 24);
        setFalling(false);
        setJumping(false);
        setVelX(10);

        if(!toRight)
            setVelX(-5);
    }
}
