package edu.virginia.lab1test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.virginia.engine.Tween.Tween;
import edu.virginia.engine.Tween.TweenJuggler;
import edu.virginia.engine.Tween.TweenTransitions;
import edu.virginia.engine.Tween.TweenableParams;
import edu.virginia.engine.display.*;
import edu.virginia.engine.event.*;
import edu.virginia.engine.util.SoundManager;

/**
 * Example game that utilizes our engine. We can create a simple prototype game with just a couple lines of code
 * although, for now, it won't be a very fun game :)
 * */
public class LabOneGame extends Game {

	/* Create a sprite object for our game. We'll use mario */
	public int frameCounter = 0;
	public PhysicsSprite mario = new PhysicsSprite("Mario", "SpriteSheet.png");
	public Sprite coin = new Sprite("Coin", "Coin.png");
	public Sprite platform1 = new Sprite("Platform1", "Brick.png");
	public Sprite platform2 = new Sprite("Platform2", "Brick.png");
	public SoundManager mySoundManager = SoundManager.getInstance();
	public QuestManager myQM = new QuestManager();
	public CollisionManager myCM = new CollisionManager();
	public boolean onPlat1 = false;
	public boolean onPlat2 = false;
	public boolean gotCoin = false;
	public String animation = "idle";
	public String prevAnim = "idle";
	public TweenJuggler tweenJuggler = new TweenJuggler();
	public TweenTransitions linear = new TweenTransitions("linear");
	public TweenTransitions exponential = new TweenTransitions("exponential");
	public TweenTransitions square = new TweenTransitions("square");
	public Tween marioFade = new Tween(mario, linear);
	public Tween coinx = new Tween(coin, exponential);
	public Tween coiny = new Tween(coin, exponential);
	public Tween coinScaleX = new Tween(coin, linear);
	public Tween coinScaleY = new Tween(coin, linear);
	public Tween coinFade = new Tween(coin, exponential);

	
	/**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 * */
	public LabOneGame() {
		super("Lab Four Test Game", 1000, 600);
		mario.setxPosition(0);
		mario.setyPosition(507);
		mario.setxScale(3.5);
		mario.setyScale(3.5);
		mario.setAlpha(0);
		coin.setxPosition(800);
		coin.setyPosition(60);
		coin.setxScale(.17);
		coin.setyScale(.17);
		platform1.setxPosition(200);
		platform1.setyPosition(400);
		platform1.setxScale(.7);
		platform1.setyScale(.3);
		platform2.setxPosition(550);
		platform2.setyPosition(250);
		platform2.setxScale(.7);
		platform2.setyScale(.3);
		coin.addEventListener(myQM, PickedUpEvent.COIN_PICKED_UP);
		mario.addEventListener(myCM, CollisionEvent.COLLISION);
		mySoundManager.setLoopMusic(true);
		mySoundManager.playMusic("smb_over.mid");

		marioFade.animate(TweenableParams.ALPHA,0,1,100);
		tweenJuggler.add(marioFade);


	}
	
	/**
	 * Engine will automatically call this update method once per frame and pass to us
	 * the set of keys (as strings) that are currently being pressed down
	 * */
	@Override
	public void update(ArrayList<String> pressedKeys){
		super.update(pressedKeys);
		/* Make sure mario is not null. Sometimes Swing can auto cause an extra frame to go before everything is initialized */
		frameCounter++;
		if(frameCounter >= 2) {

			mario.update(pressedKeys);
			coin.update(pressedKeys);
			platform1.update(pressedKeys);
			platform2.update(pressedKeys);

			if (mario.getyPosition() >= 508) {
				mario.setAirborne(false);
				mario.setyPosition(507);
			}


			if (mario != null) {
				if(marioFade.isComplete()) {
					tweenJuggler.remove(marioFade);
				}

				if(coinx.isComplete()) {
					coinFade.animate(TweenableParams.ALPHA,1,0,400);
					tweenJuggler.add(coinFade);
				}

				if(coinFade.isComplete()) {
					mySoundManager.setLoopMusic(false);
					mySoundManager.stopMusic(mySoundManager.bkgmusic);
					mySoundManager.playSound("smb_flag.mid");
					this.pause();
				}

				if (mario.collidesWith(coin) && coin.isVisible() && !gotCoin) {
					gotCoin = true;
					coinx.animate(TweenableParams.X,coin.getxPosition(),450,100);
					coiny.animate(TweenableParams.Y,coin.getyPosition(),200,100);
					coinScaleX.animate(TweenableParams.SCALE_X,coin.getxScale(),coin.getxScale()*3,100);
					coinScaleY.animate(TweenableParams.SCALE_Y,coin.getyScale(),coin.getyScale()*3,100);
					tweenJuggler.add(coinx);
					tweenJuggler.add(coiny);
					tweenJuggler.add(coinScaleX);
					tweenJuggler.add(coinScaleY);
				}

				if (mario.collidesWith(platform1)) {
					if (mario.getHitbox().intersection(platform1.getHitbox()).getWidth() > mario.getHitbox().intersection(platform1.getHitbox()).getHeight()) {
						if (mario.getyPosition() < platform1.getyPosition()) {
							mario.setyPosition(platform1.getyPosition() - mario.getScaledHeight() - 1);
							mario.setAirborne(false);
							mario.setJump(false);
							onPlat1 = true;
						} else {
							mario.setyPosition(platform1.getyPosition() + platform1.getScaledHeight() + 1);
							mario.setyVelocity((int) (mario.getyVelocity() * (-1.5)));
							mario.setJump(false);
						}
					}
					if (mario.getHitbox().intersection(platform1.getHitbox()).getWidth() <= mario.getHitbox().intersection(platform1.getHitbox()).getHeight()) {
						if (mario.getxPosition() < platform1.getxPosition()) {
							mario.setxPosition(platform1.getxPosition() - mario.getScaledWidth());
							mario.setxVelocity((int) (mario.getxVelocity() * (-1.5)));
						} else {
							mario.setxPosition(platform1.getxPosition() + platform1.getScaledWidth());
							mario.setxVelocity((int) (mario.getxVelocity() * (-1.5)));
						}
					}
				}

				if (mario.collidesWith(platform2)) {
					if (mario.getHitbox().intersection(platform2.getHitbox()).getWidth() > mario.getHitbox().intersection(platform2.getHitbox()).getHeight()) {
						if (mario.getyPosition() < platform2.getyPosition()) {
							mario.setyPosition(platform2.getyPosition() - mario.getScaledHeight() - 1);
							mario.setAirborne(false);
							mario.setJump(false);
							onPlat2 = true;
						} else {
							mario.setyPosition(platform2.getyPosition() + platform2.getScaledHeight() + 1);
							mario.setyVelocity((int) (mario.getyVelocity() * (-1.5)));
							mario.setJump(false);
						}
					}
					if (mario.getHitbox().intersection(platform2.getHitbox()).getWidth() <= mario.getHitbox().intersection(platform2.getHitbox()).getHeight()) {
						if (mario.getxPosition() < platform2.getxPosition()) {
							mario.setxPosition(platform2.getxPosition() - mario.getScaledWidth());
							mario.setxVelocity((int) (mario.getxVelocity() * (-1.5)));
						} else {
							mario.setxPosition(platform2.getxPosition() + platform2.getScaledWidth());
							mario.setxVelocity((int) (mario.getxVelocity() * (-1.5)));
						}
					}
				}

				if (onPlat1 && ((mario.getxPosition() >= platform1.getxPosition() + platform1.getScaledWidth()) || (mario.getxPosition() + mario.getScaledWidth() <= platform1.getxPosition()))) {
					onPlat1 = false;
					mario.setAirborne(true);
				}

				if (onPlat2 && ((mario.getxPosition() >= platform2.getxPosition() + platform2.getScaledWidth()) || (mario.getxPosition() + mario.getScaledWidth() <= platform2.getxPosition()))) {
					onPlat2 = false;
					mario.setAirborne(true);
				}


				if (pressedKeys.size() > 0) {

					if (pressedKeys.contains("right") && ((mario.getxPosition() + mario.getScaledWidth()) < 996)) {
						mario.setRight(true);
						mario.setLeft(false);
						prevAnim = animation;
						animation = "walk";
					} else {
						mario.setRight(false);
					}

					if (pressedKeys.contains("left") && (mario.getxPosition() > 0)) {
						mario.setLeft(true);
						mario.setRight(false);
						prevAnim = animation;
						animation = "walk";
					} else {
						mario.setLeft(false);
					}

					if (pressedKeys.contains("up") && (mario.getyPosition() > 10)) {
						if (!mario.isAirborne()) {
							mario.setJump(true);
							mario.setAirborne(true);
							onPlat1 = false;
						} else {
							mario.setJump(false);
						}
					}

				} else {
					mario.setRight(false);
					mario.setLeft(false);
					mario.setJump(false);
					if(!mario.isAirborne()) {
						prevAnim = animation;
						animation = "idle";
					}
				}

				if(mario.getyVelocity() > 0) {
					prevAnim = animation;
					animation = "falling";
				}

				if(mario.getyVelocity() < 0) {
					prevAnim = animation;
					animation = "jump";
				}

				if(!mySoundManager.bkgmusic.isRunning() && mySoundManager.loopMusic) {
					mySoundManager.playMusic("smb_over.mid");
				}

				mario.animate(animation);

			}
			tweenJuggler.update();
		}
	}
	
	/**
	 * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
	 * the screen, we need to make sure to override this method and call mario's draw method.
	 * */
	@Override
	public void draw(Graphics g){

		
		super.draw(g);


		if(mario != null && mario.isVisible()) {
			mario.draw(g);
		}

		if(platform1 != null) {
			platform1.draw(g);
		}

		if(platform2 != null) {
			platform2.draw(g);
		}

		if(coin != null && gotCoin) {

			//coin.setVisible(false);
			g.drawString("Coin Quest Completed!", 425, 20);

			//this.pause();

		}

		if(coin != null && coin.isVisible()) {
			coin.draw(g);
		}
		
		/* Same, just check for null in case a frame gets thrown in before Mario is initialized */

		
		
	}

	/**
	 * Quick main class that simply creates an instance of our game and starts the timer
	 * that calls update() and draw() every frame
	 * */
	public static void main(String[] args) {
		LabOneGame game = new LabOneGame();
		game.start();

	}
}
