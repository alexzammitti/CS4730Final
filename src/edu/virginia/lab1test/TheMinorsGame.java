package edu.virginia.lab1test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import edu.virginia.engine.event.Event;
import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenTransition;
import edu.virginia.engine.tween.TweenableParam;
import edu.virginia.engine.display.*;
import edu.virginia.engine.event.*;
import edu.virginia.engine.util.GameClock;
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
    // keys
    public final static int KEY_DELAY = 200;
    public final static int KEY_UP = 38;
    public final static int KEY_DOWN = 40;
    public final static int KEY_LEFT = 37;
    public final static int KEY_RIGHT = 39;
    public final static int KEY_SPACE = 32;
    public final static int KEY_SHIFT = 16;
    public final static int KEY_R = 82;



    // speeds etc
    public final static int CURSOR_SPEED = 10;



	// GLOBAL VARIABLES
    public int frameCounter = 0;
    public boolean itemSelectionInitialized = false;
    public int placedSpriteCounter = 0;
    public boolean debugHitboxes = true;


	// SET UP SPRITE ASSETS
    // Characters
	public PhysicsSprite mario = new PhysicsSprite("mario", "sprite-sheet.png");
	// Placeable items
	public Sprite coin = new Sprite("coin", "coin.png");
	public Sprite platform = new Sprite("platform","brick.png");
	public Sprite platform1 = new Sprite("platform1", "brick.png");
	public Sprite platform2 = new Sprite("platform2", "brick.png");
	public Sprite platform3 = new Sprite("platform3", "brick.png");
	public Sprite spike1 = new Sprite("spike1", "spikerow.png");
	// Placeholder Sprites for randomly selected placeable items - their images are what will be set later, and their ids updated
    private Sprite item1 = new Sprite("item1");
    private Sprite item2 = new Sprite("item2");
    private Sprite item3 = new Sprite("item3");
    private Sprite item4 = new Sprite("item4");
    private Sprite item5 = new Sprite("item5");
	// Backgrounds
    public Sprite selectionBackground = new Sprite("selectionbackground","item-selection-screen.png");
    // Cursors
    private Sprite cursor = new Sprite("cursor","cursor-orange.png");
    // Item Lists
    public ArrayList<Sprite> placeableItemList = new ArrayList<>(0);
    public ArrayList<Sprite> placedItemList = new ArrayList<>(0);
    // Display Object Containers
    private DisplayObjectContainer levelContainer = new DisplayObjectContainer("level container");

	// AUDIO ASSETS
	public SoundManager mySoundManager = SoundManager.getInstance();

	// EVENT MANAGERS
	public EventManager eventManager = new EventManager();
	//this is just a sanity check to make sure I remember how managers/events work
	//xCoinTween.addEventListener(eventManager, Event.TWEEN_COMPLETE_EVENT);
    // the quest manager listens for events from the xCoinTween


	// These variables should become fields of sprites
	public boolean onPlat1 = false;
	public boolean onPlat2 = false;
	public boolean gotCoin = false;
	public String animation = "idle";
	public String prevAnim = "idle";

	// SINGLETON TWEEN JUGGLER
	public TweenJuggler tweenJuggler = new TweenJuggler();

	// TWEENS


    public Tween selectionBackgroundTween = new Tween(selectionBackground, new TweenTransition(TweenTransition.TransitionType.LINEAR));
    public Tween item1SelectionTween = new Tween(item1, new TweenTransition(TweenTransition.TransitionType.LINEAR));
    public Tween item2SelectionTween = new Tween(item2, new TweenTransition(TweenTransition.TransitionType.LINEAR));
    public Tween item3SelectionTween = new Tween(item3, new TweenTransition(TweenTransition.TransitionType.LINEAR));
    public Tween item4SelectionTween = new Tween(item4, new TweenTransition(TweenTransition.TransitionType.LINEAR));
    public Tween item5SelectionTween = new Tween(item5, new TweenTransition(TweenTransition.TransitionType.LINEAR));


	// GAME CLOCKS
    //item selection, item placement, play time
    public GameClock rKeyClock = new GameClock();
    public GameClock spaceKeyClock = new GameClock();

	
	/**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 * */
	public TheMinorsGame() {
		super("The Minors Game", GAME_WIDTH, GAME_HEIGHT);

        // BUILD DISPLAY TREES

        levelContainer.addChild(platform1);
        levelContainer.addChild(coin);
        levelContainer.addChild(platform2);

        // POPULATE ITEM LISTS
        placeableItemList.add(item1);
        placeableItemList.add(item2);
        placeableItemList.add(item3);
        placeableItemList.add(item4);
        placeableItemList.add(item5);


		// SET SPRITE INITIAL POSITIONS - TODO - may want to move all of this to a method to make it easier to read
        cursor.setPosition(GAME_WIDTH/2,GAME_HEIGHT/2);
        cursor.setScale(.5,.5);

        selectionBackground.setPosition(350,100);        //TODO - fix this stupid bug!
        selectionBackground.setScale(1,1);      //NOTE!!! The children's position within the parent seem to be affected by the parent's scale!!!


        // code from Alex's game
        coin.setxPosition(1150);
        coin.setyPosition(290);
        coin.setxScale(.17);
        coin.setyScale(.17);

        platform1.setxPosition(0);
        platform1.setyPosition(GAME_HEIGHT/2);
        platform1.setxScale(.7);
        platform1.setyScale(.3);

        platform2.setxScale(.7);
        platform2.setyScale(.3);
        platform2.setxPosition(GAME_WIDTH - platform2.getScaledWidth());
        platform2.setyPosition(GAME_HEIGHT/2);

//		mario.setxPosition(0);
//		mario.setyPosition(130);
//		mario.setxScale(3.5);
//		mario.setyScale(3.5);
//		mario.setAlpha(0);
//		mario.setAirborne(true);

        // ESTABLISH EVENT LISTENERS
        item1.addEventListener(eventManager, Event.COLLISION);

        // SET UP TWEENS - TODO - might also be good to methodize
        selectionBackgroundTween.animate(TweenableParam.SCALE_X,0,1.2,100);
        //tweenJuggler.add(selectionBackgroundTween);

        item1SelectionTween.animate(TweenableParam.SCALE_X,platform.getxScale(),platform.getxScale()+.4,50);
        item2SelectionTween.animate(TweenableParam.SCALE_Y,platform.getyScale(),platform.getyScale()+.4,50);
        item1SelectionTween.animate(TweenableParam.SCALE_Y,spike1.getyScale(),spike1.getyScale()+.4,50);
        item2SelectionTween.animate(TweenableParam.SCALE_Y,spike1.getyScale(),spike1.getyScale()+.4,50);

        gameMode = GameMode.ITEM_SELECTION;


        // SET UP PHYSICS - TODO - might also be good to methodize
        //set gravity
	}

	public void itemSelectionInitialize() {
	    selectionBackground.removeAll();
        selectionBackground.addChild(item1);
        selectionBackground.addChild(item2);

        // GIVE ITEMS IMAGES - will be randomized later
        item1.setImage("brick.png");
        item2.setImage("spikerow.png");

        item1.setScale(0.7,0.3);
        item1.alignCenterVertical(selectionBackground);
        item1.alignFractionHorizontal(selectionBackground,7,2);

        item2.setScale(0.3,0.3);
        item2.alignCenterVertical(selectionBackground);
        item2.alignFractionHorizontal(selectionBackground,7,5);

        placeableItemList.clear();
        placeableItemList.add(item1);
        placeableItemList.add(item2);

        itemSelectionInitialized = true;
    }


	@Override
	public void update(ArrayList<Integer> pressedKeys){
		super.update(pressedKeys);
        if(gameMode != null) {
            switch (gameMode) {
                case ITEM_SELECTION:
                    itemSelectionUpdate(pressedKeys);
                    break;
                case ITEM_PLACEMENT:
                    itemPlacementUpdate(pressedKeys);
                    break;
                case GAMEPLAY:
                    break;
                case MAIN_MENU:
                    break;
            }
        }

        frameCounter++;
        if (frameCounter > 4) {
            tweenJuggler.nextFrame();
        }

//        if(false) {
//            frameCounter++;
//            if (frameCounter >= 2) {
//
//                mario.update(pressedKeys);
//                coin.update(pressedKeys);
//                platform1.update(pressedKeys);
//                platform2.update(pressedKeys);
//
//                if (mario.getyPosition() >= 608) {
//                    mario.setAirborne(false);
//                    mario.setyPosition(607);
//                }
//
//
//                if (mario != null) {
////                    if (marioFade.isComplete()) {
////                        tweenJuggler.remove(marioFade);
////                    }
////
////                    if (coinx.isComplete()) {
////                        coinFade.animate(TweenableParams.ALPHA, 1, 0, 400);
////                        tweenJuggler.add(coinFade);
////                    }
////
////                    if (coinFade.isComplete()) {
////                        //mySoundManager.setLoopMusic(false);
////                        //mySoundManager.stopMusic(mySoundManager.bkgmusic);
////                        //mySoundManager.playSound("smb_flag.mid");
////                        this.pause();
////                    }
////
////                    if (mario.collidesWith(coin) && coin.isVisible() && !gotCoin) {
////                        gotCoin = true;
////                        coinx.animate(TweenableParams.X, coin.getxPosition(), 450, 100);
////                        coiny.animate(TweenableParams.Y, coin.getyPosition(), 200, 100);
////                        coinScaleX.animate(TweenableParams.SCALE_X, coin.getxScale(), coin.getxScale() * 3, 100);
////                        coinScaleY.animate(TweenableParams.SCALE_Y, coin.getyScale(), coin.getyScale() * 3, 100);
////                        tweenJuggler.add(coinx);
////                        tweenJuggler.add(coiny);
////                        tweenJuggler.add(coinScaleX);
////                        tweenJuggler.add(coinScaleY);
////                    }
//
//                    if (mario.collidesWith(platform1)) {
//                        if (mario.getHitbox().intersection(platform1.getHitbox()).getWidth() > mario.getHitbox().intersection(platform1.getHitbox()).getHeight()) {
//                            if (mario.getyPosition() < platform1.getyPosition()) {
//                                mario.setyPosition(platform1.getyPosition() - mario.getScaledHeight() - 1);
//                                mario.setAirborne(false);
//                                mario.setJump(false);
//                                onPlat1 = true;
//                            } else {
//                                mario.setyPosition(platform1.getyPosition() + platform1.getScaledHeight() + 1);
//                                mario.setyVelocity((int) (mario.getyVelocity() * (-1)));
//                                mario.setJump(false);
//                            }
//                        }
//                        if (mario.getHitbox().intersection(platform1.getHitbox()).getWidth() <= mario.getHitbox().intersection(platform1.getHitbox()).getHeight()) {
//                            if (mario.getxPosition() < platform1.getxPosition()) {
//                                mario.setxPosition(platform1.getxPosition() - mario.getScaledWidth());
//                                mario.setxVelocity((int) (mario.getxVelocity() * (-1)));
//                            } else {
//                                mario.setxPosition(platform1.getxPosition() + platform1.getScaledWidth());
//                                mario.setxVelocity((int) (mario.getxVelocity() * (-1)));
//                            }
//                        }
//                    }
//
//                    if (mario.collidesWith(platform2)) {
//                        if (mario.getHitbox().intersection(platform2.getHitbox()).getWidth() > mario.getHitbox().intersection(platform2.getHitbox()).getHeight()) {
//                            if (mario.getyPosition() < platform2.getyPosition()) {
//                                mario.setyPosition(platform2.getyPosition() - mario.getScaledHeight() - 1);
//                                mario.setAirborne(false);
//                                mario.setJump(false);
//                                onPlat2 = true;
//                            } else {
//                                mario.setyPosition(platform2.getyPosition() + platform2.getScaledHeight() + 1);
//                                mario.setyVelocity((int) (mario.getyVelocity() * (-1)));
//                                mario.setJump(false);
//                            }
//                        }
//                        if (mario.getHitbox().intersection(platform2.getHitbox()).getWidth() <= mario.getHitbox().intersection(platform2.getHitbox()).getHeight()) {
//                            if (mario.getxPosition() < platform2.getxPosition()) {
//                                mario.setxPosition(platform2.getxPosition() - mario.getScaledWidth());
//                                mario.setxVelocity((int) (mario.getxVelocity() * (-1)));
//                            } else {
//                                mario.setxPosition(platform2.getxPosition() + platform2.getScaledWidth());
//                                mario.setxVelocity((int) (mario.getxVelocity() * (-1)));
//                            }
//                        }
//                    }
//
//                    if (onPlat1 && ((mario.getxPosition() >= platform1.getxPosition() + platform1.getScaledWidth()) || (mario.getxPosition() + mario.getScaledWidth() <= platform1.getxPosition()))) {
//                        onPlat1 = false;
//                        mario.setAirborne(true);
//                    }
//
//                    if (onPlat2 && ((mario.getxPosition() >= platform2.getxPosition() + platform2.getScaledWidth()) || (mario.getxPosition() + mario.getScaledWidth() <= platform2.getxPosition()))) {
//                        onPlat2 = false;
//                        mario.setAirborne(true);
//                    }
//
//
//                    if (pressedKeys.size() > 0) {
//
//                        if (pressedKeys.contains("right") && ((mario.getxPosition() + mario.getScaledWidth()) < 1250)) {
//                            mario.setRight(true);
//                            mario.setLeft(false);
//                            prevAnim = animation;
//                            animation = "walk";
//                        } else {
//                            mario.setRight(false);
//                        }
//
//                        if (pressedKeys.contains("left") && (mario.getxPosition() > 0)) {
//                            mario.setLeft(true);
//                            mario.setRight(false);
//                            prevAnim = animation;
//                            animation = "walk";
//                        } else {
//                            mario.setLeft(false);
//                        }
//
//                        if (pressedKeys.contains("up") && (mario.getyPosition() > 10)) {
//                            if (!mario.isAirborne()) {
//                                mario.setJump(true);
//                                mario.setAirborne(true);
//                                onPlat1 = false;
//                            } else {
//                                mario.setJump(false);
//                            }
//                        }
//
//                    } else {
//                        mario.setRight(false);
//                        mario.setLeft(false);
//                        mario.setJump(false);
//                        if (!mario.isAirborne()) {
//                            prevAnim = animation;
//                            animation = "idle";
//                        }
//                    }
//
//                    if (mario.getyVelocity() > 0) {
//                        prevAnim = animation;
//                        animation = "falling";
//                    }
//
//                    if (mario.getyVelocity() < 0) {
//                        prevAnim = animation;
//                        animation = "jump";
//                    }
//
//                    //if(!mySoundManager.bkgmusic.isRunning() && mySoundManager.loopMusic) {
//                    //	mySoundManager.playMusic("smb_over.mid");
//                    //}
//
//                    mario.animate(animation);
//
//                }
//                //tweenJuggler.nextFrame();
//            }
//        }

	}

	public void itemSelectionUpdate(ArrayList<Integer> pressedKeys) {
	    if(! itemSelectionInitialized && frameCounter > 3) {
	        itemSelectionInitialize();
        }
	    if(cursor != null) {
	        cursor.update(pressedKeys);
	        selectionBackground.update(pressedKeys);

            // SET CURSORS VISIBLE
            cursor.setVisible(true);
            // MOVE CURSOR BASED ON USER INPUT
            handleMoveInput(cursor,CURSOR_SPEED,pressedKeys);
            // CHECK FOR OVERLAP BETWEEN CURSORS & SELECTABLE ITEMS
            for(Iterator<Sprite> iterator = placeableItemList.iterator(); iterator.hasNext();) {
                Sprite s = iterator.next();
                s.update(pressedKeys);
                // if the cursor overlaps with a selectable items
                if(cursor.collidesWith(s)) {
                    // add tween stuff here for polish if desired
                    // and the player presses the select button over it
                    if(pressedKeys.contains(KEY_SPACE) && spaceKeyClock.getElapsedTime() > KEY_DELAY) {
                        String spriteId = "copy" + Integer.toString(placedSpriteCounter);   // we need to make a unique spriteId to make sure that --
                        placedSpriteCounter++;                                              // --> new sprites don't have the same id for checks later
                        Sprite newSprite = new Sprite(spriteId,s.getFileName());          //duplicate the sprite and add it to our level
                        newSprite.setScale(s.getxAbsoluteScale(),s.getyAbsoluteScale());
                        newSprite.setPosition(s.getxAbsolutePosition(),s.getyAbsolutePosition());
                        levelContainer.addChild(newSprite);                                 // the level container will hold everything in the level
                        newSprite.setPivotCenter();                                         // we only want rotation about the center of the sprite
                        iterator.remove();                                                  // the item can no longer be selected
                        //s.setVisible(false);
                        gameMode = GameMode.ITEM_PLACEMENT;     //TODO make this check to see if all players have made a selection before changing mode
                        spaceKeyClock.resetGameClock();         // make sure it doesn't get placed immediately after selection
                    }
                }
            }
            // BASED ON OVERLAPS, HANDLE USER INPUT (SELECTION OF AN ITEM)
            //if colliding and a is pressed
            //create new sprite based on selection
            //give item to player
            //remove from selectable items
            //add item to display tree

            // CHECK IF SELECTION IS DONE OR TIMED OUT
            //end selection phase and move into item phase
        }
    }

    public void itemPlacementUpdate(ArrayList<Integer> pressedKeys) {
	    if(levelContainer != null) {
	        levelContainer.update(pressedKeys);
	        // Move sprite based on user input
            if(!(levelContainer.getLastChild().isPlaced)) {                     //TODO make this give each player the item they chose
                handleMoveInput(levelContainer.getLastChild(), CURSOR_SPEED, pressedKeys);
            }
            // Allow user to rotate image
            if(pressedKeys.contains(KEY_R) && rKeyClock.getElapsedTime() > KEY_DELAY){
                levelContainer.getLastChild().setRotation(levelContainer.getLastChild().getRotation()+Math.PI/2);
                rKeyClock.resetGameClock();
            }
            // Preventing overlaps - image changes to imageName + "-error.png"
            for(DisplayObjectContainer levelItem : levelContainer.getChildren()) {              // iterate over the sprites
                DisplayObjectContainer DOCbeingPlaced = levelContainer.getLastChild();
                if(!levelItem.getId().equals(DOCbeingPlaced.getId())) {                                  // if it's not itself
                    if(DOCbeingPlaced.getFileName().contains("-error") && levelItem.getFileName().contains("-error")) {
                        if(!levelItem.collidesWith(DOCbeingPlaced)){                                     //if there NOT a collision
                            DOCbeingPlaced.setImageNormal();
                            levelItem.setImageNormal();
                            break;
                        }
                    } else {
                        if(levelItem.collidesWith(DOCbeingPlaced)){                                      //if there IS a collision
                            DOCbeingPlaced.setImageError();
                            levelItem.setImageError();
                            break;
                        }
                    }
                }
            }
            if(pressedKeys.contains(KEY_SPACE) && spaceKeyClock.getElapsedTime() > KEY_DELAY) {     //if space is pressed
                if(!levelContainer.getLastChild().getFileName().contains("-error")) {                // and placement is allowed
                    levelContainer.getLastChild().isPlaced = true;
                    gameMode = GameMode.ITEM_SELECTION;
                    itemSelectionInitialized = false;
                    spaceKeyClock.resetGameClock();
                }
            }
        }

    }

    public void handleMoveInput(DisplayObject displayObject, int speed, ArrayList<Integer> pressedKeys) {
        if (pressedKeys.contains(KEY_UP)) {
            displayObject.setyPosition(displayObject.getyPosition() - speed);
        } else if (pressedKeys.contains(KEY_DOWN)) {
            displayObject.setyPosition(displayObject.getyPosition() + speed);
        }
        if (pressedKeys.contains(KEY_LEFT)) {
            displayObject.setxPosition(displayObject.getxPosition() - speed);
        } else if (pressedKeys.contains(KEY_RIGHT)) {
            displayObject.setxPosition(displayObject.getxPosition() + speed);
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
        if(gameMode != null) {
            switch(gameMode) {
                case ITEM_SELECTION:
                    itemSelectionDraw(g);
                    break;
                case ITEM_PLACEMENT:
                    itemPlacementDraw(g);
                    break;
                case GAMEPLAY:
                    break;
                case MAIN_MENU:
                    break;
            }
        }
	}

    public void itemSelectionDraw(Graphics g) {
        if(levelContainer != null) {
            levelContainer.draw(g);
        }
	    if(cursor != null) { // everything????
            selectionBackground.draw(g);
//            for(Sprite s : placeableItemList) {
//                if(s != null) s.draw(g);
//            }
            cursor.draw(g);



            if(debugHitboxes) {
                //Debugging hitboxes
                Rectangle test = item1.getHitbox();
                g.fillRect(test.x, test.y, test.width, test.height);
                test = item2.getHitbox();
                g.fillRect(test.x, test.y, test.width, test.height);
                test = cursor.getHitbox();
                g.fillRect(test.x, test.y, test.width, test.height);
                for(DisplayObjectContainer c : levelContainer.getChildren()) {
                    test = c.getHitbox();
                    g.fillRect(test.x, test.y, test.width, test.height);
                }
            }
	    }
    }

    public void itemPlacementDraw(Graphics g) {
	    if(levelContainer != null) {
	        levelContainer.draw(g);
        }

        if(debugHitboxes) {
            for(DisplayObjectContainer c : levelContainer.getChildren()) {
                Rectangle test = c.getHitbox();
                g.fillRect(test.x, test.y, test.width, test.height);
            }
        }
    }

	public static void main(String[] args) {
		TheMinorsGame game = new TheMinorsGame();
		game.start();

	}
}
