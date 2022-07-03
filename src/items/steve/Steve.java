package items.steve;

import operators.ScreenNow;
import operators.Main;
import appearance.Animation;
import items.GameObject;
import appearance.GetImage;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Steve extends GameObject{

    private int remainingLives;
    private int diamonds;
    private int points;
    private double invincibilityTimer;
    private SteveCondition steveCondition;
    private boolean toRight = true;

    public Steve(double x, double y){
        super(x, y, null);
        setDimension(48,48);

        remainingLives = 3;
        points = 0;
        diamonds = 0;
        invincibilityTimer = 0;

        GetImage imageLoader = new GetImage();
        BufferedImage[] leftFrames = imageLoader.getLeftFrames(SteveCondition.SMALL);
        BufferedImage[] rightFrames = imageLoader.getRightFrames(SteveCondition.SMALL);

        Animation animation = new Animation(leftFrames, rightFrames);
        steveCondition = new SteveCondition(animation, false, false);
        setStyle(steveCondition.getCurrentStyle(toRight, false, false));
    }

    @Override
    public void draw(Graphics g){
        boolean movingInX = (getVelX() != 0);
        boolean movingInY = (getVelY() != 0);

        setStyle(steveCondition.getCurrentStyle(toRight, movingInX, movingInY));

        super.draw(g);
    }

    public void jump(Main engine) {
        if(!isJumping() && !isFalling()){
            setJumping(true);
            setVelY(10);
            engine.playJump();
        }
    }

    public void move(boolean toRight, ScreenNow camera) {
        if(toRight){
            setVelX(5);
        }
        else if(camera.getX() < getX()){
            setVelX(-5);
        }

        this.toRight = toRight;
    }

    public boolean onTouchEnemy(Main engine){

        if(!steveCondition.isSuper() && !steveCondition.isFire()){
            remainingLives--;
            engine.playSteveDies();
            return true;
        }
        else{
            engine.shakeScreen();
            steveCondition = steveCondition.onTouchEnemy(engine.getImageLoader());
            setDimension(48, 48);
            return false;
        }
    }

    public Arrow fire(){
        return steveCondition.fire(toRight, getX(), getY());
    }

    public void acquireDiamond() {
        diamonds++;
    }

    public void acquirePoints(int point){
        points = points + point;
    }

    public int getRemainingLives() {
        return remainingLives;
    }

    public void setRemainingLives(int remainingLives) {
        this.remainingLives = remainingLives;
    }

    public int getPoints() {
        return points;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public SteveCondition getSteveCondition() {
        return steveCondition;
    }

    public void setSteveCondition(SteveCondition marioForm) {
        this.steveCondition = marioForm;
    }

    public boolean isSuper() {
        return steveCondition.isSuper();
    }

    public boolean getToRight() {
        return toRight;
    }

    public void resetLocation() {
        setVelX(0);
        setVelY(0);
        setX(50);
        setJumping(false);
        setFalling(true);
    }
}
