package items.block;

import operators.Main;
import items.Map;
import items.steve.Steve;
import items.loot.Loot;
import appearance.GetImage;

import java.awt.image.BufferedImage;

public class SurpriseBlock extends Block{

    private Loot prize;

    public SurpriseBlock(double x, double y, BufferedImage style, Loot prize) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(false);
        this.prize = prize;
    }

    @Override
    public Loot reveal(Main engine){
        BufferedImage newStyle = engine.getImageLoader().loadImage("/sprite.png");
        newStyle = engine.getImageLoader().getSubImage(newStyle, 1, 2, 48, 48);

        if(prize != null){
            prize.reveal();
        }

        setEmpty(true);
        setStyle(newStyle);

        Loot toReturn = this.prize;
        this.prize = null;
        return toReturn;
    }

    @Override
    public Loot getPrize(){
        return prize;
    }
}
