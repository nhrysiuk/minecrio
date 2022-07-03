package items.loot;

import items.steve.Steve;
import operators.Main;

import java.awt.*;

public interface Loot {
    int getPoint();

    void reveal();

    Rectangle getBounds();

    void onTouch(Steve mario, Main engine);
}
