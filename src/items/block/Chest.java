package items.block;

import java.awt.image.BufferedImage;

public class Chest extends Block{

    public Chest(double x, double y, BufferedImage style){
        super(x, y, style);
        setBreakable(false);
        setEmpty(true);
        setDimension(96, 96);
    }
}
