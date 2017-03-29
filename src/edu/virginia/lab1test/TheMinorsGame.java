package edu.virginia.lab1test;

import java.awt.*;
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
public class TheMinorsGame extends Game {

    // GLOBAL CONSTANTS
    public enum GameMode {
        ITEM_SELECTION, ITEM_PLACEMENT, GAMEPLAY, MAIN_MENU
    }

    public GameMode gameMode = GameMode.ITEM_SELECTION;
    public final static int GAME_WIDTH = 1250;
    public final static int GAME_HEIGHT = 700;
    public final static int KEY_UP = 38;
    public final static int KEY_DOWN = 40;
    public final static int KEY_LEFT = 37;
    public final static int KEY_RIGHT = 39;



	// GLOBAL VARIABLES
    public int frameCounter = 0;


	// SET UP SPRITE ASSETS
    // Characters
	public PhysicsSprite mario = new PhysicsSprite("Mario", "SpriteSheet.png");
	// Placeable items
	public Sprite coin = new Sprite("Coin", "Coin.png");
	public Sprite platform = new Sprite("Platform","Brick.png");
	public Sprite platform1 = new Sprite("Platform1", "Brick.png");
	public Sprite platform2 = new Sprite("Platform2", "Brick.png");
	public Sprite platform3 = new Sprite("Platform3", "Brick.png");
	public Sprite spike1 = new Sprite("Spike1", "SpikeRow.png");
	// Backgrounds
    public Sprite selectionBackground = new Sprite("SelectionBackground","item-selection-screen.png");
    // Cursors
    private Sprite cursor = new Sprite("Cursor","cursor.png");
    // Item Lists
    public ArrayList<Sprite> placeableItemList = new ArrayList<>(0);
    public ArrayList<Sprite> placedItemList = new ArrayList<>(0);

	// AUDIO ASSETS
	public SoundManager mySoundManager = SoundManager.getInstance();

	// EVENT MANAGERS
	public QuestManager myQM = new QuestManager();
	public CollisionManager myCM = new CollisionManager();
	public boolean onPlat1 = false;
	public boolean onPlat2 = false;
	public boolean gotCoin = false;
	public String animation = "idle";
	public String prevAnim = "idle";

	// SINGLETON TWEEN JUGGLER
	public TweenJuggler tweenJuggler = new TweenJuggler();

	// tween stuff... not sure how this works  - Jaz
	public TweenTransitions linear = new TweenTransitions("linear");
	public TweenTransitions exponential = new TweenTransitions("exponential");
	public TweenTransitions square = new TweenTransitions("square");
	public Tween marioFade = new Tween(mario, linear);
	public Tween coinx = new Tween(coin, exponential);
	public Tween coiny = new Tween(coin, exponential);
	public Tween coinScaleX = new Tween(coin, linear);
	public Tween coinScaleY = new Tween(coin, linear);
	public Tween coinFade = new Tween(coin, exponential);




	// GAME CLOCKS
    //item selection, item placement, play time
	
	/**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 * */
	public TheMinorsGame() {
		super("The Minors Game", GAME_WIDTH, GAME_HEIGHT);

		// SET SPRITE INITIAL POSITIONS
        cursor.setPosition(GAME_WIDTH/2,GAME_HEIGHT/2);
        cursor.setScale(.5,.5);

        selectionBackground.setPosition(300,80);
        selectionBackground.setScale(1.2,1.3);

        platform.setPosition(50,100);
        platform.setScale(0.7,0.3);

        spike1.setPosition(200,200);
        spike1.setScale(0.3,0.3);

        // BUILD DISPLAY TREES
        selectionBackground.addChild(platform);
        selectionBackground.addChild(spike1);

        // ESTABLISH EVENT LISTENERS


        // SET UP TWEENS

		mario.setxPosition(0);
		mario.setyPosition(130);
		mario.setxScale(3.5);
		mario.setyScale(3.5);
		mario.setAlpha(0);
		mario.setAirborne(true);

		coin.setxPosition(1150);
		coin.setyPosition(290);
		coin.setxScale(.17);
		coin.setyScale(.17);

		platform1.setxPosition(0);
		platform1.setyPosition(350);
		platform1.setxScale(.7);
		platform1.setyScale(.3);

		platform2.setxScale(.7);
		platform2.setyScale(.3);
		platform2.setxPosition(1250 - platform2.getScaledWidth());
		platform2.setyPosition(350);

		coin.addEventListener(myQM, PickedUpEvent.COIN_PICKED_UP);
		mario.addEventListener(myCM, CollisionEvent.COLLISION);
		//mySoundManager.setLoopMusic(true);
		//mySoundManager.playMusic("smb_over.mid");

		marioFade.animate(TweenableParams.ALPHA,0,1,100);
		tweenJuggler.add(marioFade);


        gameMode = GameMode.ITEM_SELECTION;
        placeableItemList.add(platform);
        placeableItemList.add(spike1);
	}
	


	@Override
	public void update(ArrayList<Integer> pressedKeys){
		super.update(pressedKeys);

		switch(gameMode) {
            case ITEM_SELECTION:
                itemSelectionUpdate(pressedKeys);
                break;
            case ITEM_PLACEMENT:
                break;
            case GAMEPLAY:
                break;
            case MAIN_MENU:
                break;
        }

        if(false) {
            frameCounter++;
            if (frameCounter >= 2) {

                mario.update(pressedKeys);
                coin.update(pressedKeys);
                platform1.update(pressedKeys);
                platform2.update(pressedKeys);

                if (mario.getyPosition() >= 608) {
                    mario.setAirborne(false);
                    mario.setyPosition(607);
                }


                if (mario != null) {
                    if (marioFade.isComplete()) {
                        tweenJuggler.remove(marioFade);
                    }

                    if (coinx.isComplete()) {
                        coinFade.animate(TweenableParams.ALPHA, 1, 0, 400);
                        tweenJuggler.add(coinFade);
                    }

                    if (coinFade.isComplete()) {
                        //mySoundManager.setLoopMusic(false);
                        //mySoundManager.stopMusic(mySoundManager.bkgmusic);
                        //mySoundManager.playSound("smb_flag.mid");
                        this.pause();
                    }

                    if (mario.collidesWith(coin) && coin.isVisible() && !gotCoin) {
                        gotCoin = true;
                        coinx.animate(TweenableParams.X, coin.getxPosition(), 450, 100);
                        coiny.animate(TweenableParams.Y, coin.getyPosition(), 200, 100);
                        coinScaleX.animate(TweenableParams.SCALE_X, coin.getxScale(), coin.getxScale() * 3, 100);
                        coinScaleY.animate(TweenableParams.SCALE_Y, coin.getyScale(), coin.getyScale() * 3, 100);
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
                                mario.setyVelocity((int) (mario.getyVelocity() * (-1)));
                                mario.setJump(false);
                            }
                        }
                        if (mario.getHitbox().intersection(platform1.getHitbox()).getWidth() <= mario.getHitbox().intersection(platform1.getHitbox()).getHeight()) {
                            if (mario.getxPosition() < platform1.getxPosition()) {
                                mario.setxPosition(platform1.getxPosition() - mario.getScaledWidth());
                                mario.setxVelocity((int) (mario.getxVelocity() * (-1)));
                            } else {
                                mario.setxPosition(platform1.getxPosition() + platform1.getScaledWidth());
                                mario.setxVelocity((int) (mario.getxVelocity() * (-1)));
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
                                mario.setyVelocity((int) (mario.getyVelocity() * (-1)));
                                mario.setJump(false);
                            }
                        }
                        if (mario.getHitbox().intersection(platform2.getHitbox()).getWidth() <= mario.getHitbox().intersection(platform2.getHitbox()).getHeight()) {
                            if (mario.getxPosition() < platform2.getxPosition()) {
                                mario.setxPosition(platform2.getxPosition() - mario.getScaledWidth());
                                mario.setxVelocity((int) (mario.getxVelocity() * (-1)));
                            } else {
                                mario.setxPosition(platform2.getxPosition() + platform2.getScaledWidth());
                                mario.setxVelocity((int) (mario.getxVelocity() * (-1)));
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

                        if (pressedKeys.contains("right") && ((mario.getxPosition() + mario.getScaledWidth()) < 1250)) {
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
                        if (!mario.isAirborne()) {
                            prevAnim = animation;
                            animation = "idle";
                        }
                    }

                    if (mario.getyVelocity() > 0) {
                        prevAnim = animation;
                        animation = "falling";
                    }

                    if (mario.getyVelocity() < 0) {
                        prevAnim = animation;
                        animation = "jump";
                    }

                    //if(!mySoundManager.bkgmusic.isRunning() && mySoundManager.loopMusic) {
                    //	mySoundManager.playMusic("smb_over.mid");
                    //}

                    mario.animate(animation);

                }
                tweenJuggler.update();
            }
        }
	}

	public void itemSelectionUpdate(ArrayList<Integer> pressedKeys) {
	    if(cursor != null) {
            // SET CURSORS VISIBLE
            cursor.setVisible(true);
            // MOVE CURSOR BASED ON USER INPUT
            if (pressedKeys.contains(KEY_UP)) {
                cursor.setyPosition(cursor.getyPosition() - 5);
            } else if (pressedKeys.contains(KEY_DOWN)) {
                cursor.setyPosition(cursor.getyPosition() + 5);
            }
            if (pressedKeys.contains(KEY_LEFT)) {
                cursor.setxPosition(cursor.getxPosition() - 5);
            } else if (pressedKeys.contains(KEY_RIGHT)) {
                cursor.setxPosition(cursor.getxPosition() + 5);
            }
            // CHECK FOR OVERLAP BETWEEN CURSORS & SELECTABLE ITEMS
            for(Sprite s : placeableItemList) {

            }
            // BASED ON OVERLAPS, HANDLE USER INPUT (SELECTION OF AN ITEM)
            //if colliding and a is pressed
            //give item to player
            //remove from selectable items
            //set visibility

            // CHECK IF SELECTION IS DONE OR TIMED OUT
            //end selection phase and move into item phase
        }
    }


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

        switch(gameMode) {
            case ITEM_SELECTION:
                itemSelectionDraw(g);
                break;
            case ITEM_PLACEMENT:
                break;
            case GAMEPLAY:
                break;
            case MAIN_MENU:
                break;
        }
	}

    public void itemSelectionDraw(Graphics g) {
	    if(cursor != null) { // everything????
            selectionBackground.draw(g);
//            for(Sprite s : placeableItemList) {
//                if(s != null) s.draw(g);
//            }
            cursor.draw(g);
	    }
    }

	public static void main(String[] args) {
		TheMinorsGame game = new TheMinorsGame();
		game.start();

	}
}
