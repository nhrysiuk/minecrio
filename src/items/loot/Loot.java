package items.loot;

import operators.Main;
import operators.MapOperator;
import items.steve.Steve;

import java.awt.*;

public interface Loot {

    int getPoint();

    void reveal();

    Rectangle getBounds();

    void onTouch(Steve mario, Main engine);

}
