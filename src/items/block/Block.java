package items.block;

import operators.Main;
import items.GameObject;
import items.Map;
import items.loot.Loot;

import java.awt.image.BufferedImage;

public abstract class Block extends GameObject{

    private boolean breakable;

    private boolean empty;

    public Block(double x, double y, BufferedImage style){
        super(x, y, style);
        setDimension(48, 48);
    }

    public boolean isBreakable() {
        return breakable;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public Loot reveal(Main engine){ return null;}

    public Loot getPrize() {
        return null;
    }
}
