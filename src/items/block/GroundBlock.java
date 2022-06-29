package items.block;

import java.awt.image.BufferedImage;

public class GroundBlock extends Block{

    public GroundBlock(double x, double y, BufferedImage style){
        super(x, y, style);
        setBreakable(false);
        setEmpty(true);
    }

}
