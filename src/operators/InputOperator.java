package operators;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class InputOperator implements KeyListener, MouseListener{

    private Main main;

    InputOperator(Main main) {
        this.main = main;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        StatusList status = main.getGameStatus();
        ButtonActionList currentAction = ButtonActionList.NO_ACTION;

        if (keyCode == KeyEvent.VK_UP) {
            if(status == StatusList.START_SCREEN || status == StatusList.MAP_SELECTION)
                currentAction = ButtonActionList.GO_UP;
            else
                currentAction = ButtonActionList.JUMP;
        }
        else if(keyCode == KeyEvent.VK_DOWN){
            if(status == StatusList.START_SCREEN || status == StatusList.MAP_SELECTION)
                currentAction = ButtonActionList.GO_DOWN;
        }
        else if (keyCode == KeyEvent.VK_RIGHT) {
            currentAction = ButtonActionList.M_RIGHT;
        }
        else if (keyCode == KeyEvent.VK_LEFT) {
            currentAction = ButtonActionList.M_LEFT;
        }
        else if (keyCode == KeyEvent.VK_ENTER) {
            currentAction = ButtonActionList.SELECT;
        }
        else if (keyCode == KeyEvent.VK_ESCAPE) {
            if(status == StatusList.RUNNING || status == StatusList.PAUSED )
                currentAction = ButtonActionList.PAUSE_RESUME;
            else
                currentAction = ButtonActionList.GO_TO_START_SCREEN;

        }
        else if (keyCode == KeyEvent.VK_SPACE){
            currentAction = ButtonActionList.FIRE;
        }


        notifyInput(currentAction);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(main.getGameStatus() == StatusList.MAP_SELECTION){
            main.selectMapViaMouse();
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_LEFT)
            notifyInput(ButtonActionList.ACTION_COMPLETED);
    }

    private void notifyInput(ButtonActionList action) {
        if(action != ButtonActionList.NO_ACTION)
            main.receiveInput(action);
    }

    @Override
    public void keyTyped(KeyEvent arg0) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
