package edu.virginia.lab1test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import edu.virginia.engine.controller.GamePad;
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
        ITEM_SELECTION, ITEM_PLACEMENT, GAMEPLAY, MAIN_MENU, ROUND_COMPLETE;
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
    public final static int KEY_ESC = 27;

    // speeds etc
    public final static int CURSOR_SPEED = 10;
    public final static int PLAYER_SPEED = 5;
    public final static int BEAM_SPEED = 15;



	// GLOBAL VARIABLES
    public int frameCounter = 0;
    public boolean itemSelectionInitialized = false;
    public int placedSpriteCounter = 0;
    public boolean debugHitboxes = false;


	// SET UP SPRITE ASSETS
    // Characters
    private ArrayList<PhysicsSprite> players = new ArrayList<>(0);
	public PhysicsSprite mario = new PhysicsSprite("mario", "sprite-sheet.png");
	// Placeable items
	public Sprite platform1 = new Sprite("platform1", "brick.png");
	public Sprite platform2 = new Sprite("platform2", "brick.png");
	public Sprite portal = new Sprite("portal","portal.png");
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
    public ArrayList<Sprite> laserGunList = new ArrayList<>(0);
    public ArrayList<LaserBeam> laserBeams = new ArrayList<>(0);
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
	//public TweenJuggler tweenJuggler = new TweenJuggler();

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
    public GameClock escKeyClock = new GameClock();

	
    /**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 * */
	public TheMinorsGame() {
		super("The Minors Game", GAME_WIDTH, GAME_HEIGHT);

        // BUILD DISPLAY TREES

        levelContainer.addChild(platform1);
        //levelContainer.addChild(coin);
        levelContainer.addChild(platform2);
        levelContainer.addChild(portal);

        // PLAYER ORGANIZATION
        players.add(mario);

        // POPULATE ITEM LISTS
        placeableItemList.add(item1);
        placeableItemList.add(item2);
        placeableItemList.add(item3);
        placeableItemList.add(item4);
        placeableItemList.add(item5);


		// SET SPRITE INITIAL POSITIONS - TODO - may want to move all of this to a method to make it easier to read
        cursor.setPosition(GAME_WIDTH/2,GAME_HEIGHT/2);
        cursor.setScale(.5,.5);

        selectionBackground.setPosition(350,100);
        selectionBackground.setScale(1,1);


        // code from Alex's game
//        coin.setxPosition(1150);
//        coin.setyPosition(290);
//        coin.setxScale(.17);
//        coin.setyScale(.17);

        platform1.setxPosition(0);
        platform1.setyPosition(GAME_HEIGHT/2);
        platform1.setxScale(.7);
        platform1.setyScale(.3);

        platform2.setxScale(.7);
        platform2.setyScale(.3);
        platform2.setxPosition(GAME_WIDTH - platform2.getScaledWidth());
        platform2.setyPosition(GAME_HEIGHT/2);

        portal.setScale(0.2,0.2);
        portal.setPosition(GAME_WIDTH-portal.getScaledWidth()-20,GAME_HEIGHT/2-120);

		mario.setxPosition(5);
		mario.setyPosition(130);
		mario.setxScale(3.5);
		mario.setyScale(3.5);
		mario.setAlpha(1);

        // ESTABLISH EVENT LISTENERS
        for(PhysicsSprite player : players) {
            player.addEventListener(eventManager, Event.SAFE_COLLISION);
            player.addEventListener(eventManager, Event.UNSAFE_COLLISION);
            player.addEventListener(eventManager, Event.DEATH);
            player.addEventListener(eventManager, Event.GOAL);
        }


        // SET UP TWEENS - TODO - might also be good to methodize
        selectionBackgroundTween.animate(TweenableParam.SCALE_X,0,1.2,100);
        //tweenJuggler.add(selectionBackgroundTween);

        gameMode = GameMode.ITEM_SELECTION;


        // SET UP PHYSICS - TODO - might also be good to methodize
        //set gravity
        mario.setyAcceleration(1);
	}

	public void itemSelectionInitialize() {
	    selectionBackground.removeAll();
        selectionBackground.addChild(item1);
        selectionBackground.addChild(item2);
        selectionBackground.addChild(item3);

        // GIVE ITEMS IMAGES - will be randomized later
        item1.setImage("brick.png");
        item2.setImage("spikerow.png");
        item3.setImage("LaserGun.png");

        item1.setScale(0.7,0.3);
        item1.alignCenterHorizontal(selectionBackground);
        item1.alignFractionVertical(selectionBackground,100,30);

        item2.setScale(0.3,0.3);
        item2.alignCenterHorizontal(selectionBackground);
        item2.alignFractionVertical(selectionBackground,100,52);

        item3.setScale(.5,.5);
        item3.alignCenterHorizontal(selectionBackground);
        item3.alignFractionVertical(selectionBackground,100,75);

        placeableItemList.clear();
        placeableItemList.add(item1);
        placeableItemList.add(item2);
        placeableItemList.add(item3);

        itemSelectionInitialized = true;
    }


	@Override
	public void update(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads){
		super.update(pressedKeys,gamePads);
        if(gameMode != null) {
            switch (gameMode) {
                case ITEM_SELECTION:
                    itemSelectionUpdate(pressedKeys,gamePads);
                    break;
                case ITEM_PLACEMENT:
                    itemPlacementUpdate(pressedKeys,gamePads);
                    break;
                case GAMEPLAY:
                    gameplayUpdate(pressedKeys,gamePads);
                    break;
                case MAIN_MENU:
                    break;
            }
        }

        frameCounter++;
        if (frameCounter > 4) {
            TweenJuggler.getInstance().nextFrame();
        }
	}

	// UPDATE METHODS FOR MODES

	public void itemSelectionUpdate(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
	    if(! itemSelectionInitialized && frameCounter > 3) {
	        itemSelectionInitialize();
        }
	    if(cursor != null) {
	        cursor.update(pressedKeys,gamePads);
	        selectionBackground.update(pressedKeys,gamePads);

            // SET CURSORS VISIBLE
            cursor.setVisible(true);
            // MOVE CURSOR BASED ON USER INPUT
            handleCursorMoveInput(cursor,CURSOR_SPEED,pressedKeys);
            handleGamepadCursorMoveInput(cursor,CURSOR_SPEED,gamePads);
            constrainItemToLevel(cursor);
            // CHECK FOR OVERLAP BETWEEN CURSORS & SELECTABLE ITEMS
            for(Iterator<Sprite> iterator = placeableItemList.iterator(); iterator.hasNext();) {
                Sprite s = iterator.next();
                s.update(pressedKeys,gamePads);
                // if the cursor overlaps with a selectable items
                if(cursor.collidesWith(s)) {
                    // add tween stuff here for polish if desired
                    // and the player presses the select button over it
                    if(pressedKeys.contains(KEY_SPACE) && spaceKeyClock.getElapsedTime() > KEY_DELAY ) {
                        String spriteId = "copy" + Integer.toString(placedSpriteCounter);   // we need to make a unique spriteId to make sure that --
                        placedSpriteCounter++;                                              // --> new sprites don't have the same id for checks later
                        Sprite newSprite = new Sprite(spriteId,s.getFileName());          //duplicate the sprite and add it to our level
                        newSprite.setScale(s.getxAbsoluteScale(),s.getyAbsoluteScale());
                        newSprite.setPosition(s.getxAbsolutePosition(),s.getyAbsolutePosition());
                        levelContainer.addChild(newSprite);                                 // the level container will hold everything in the level
                        if(s.getFileName().contains("Laser")) {
                          laserGunList.add(newSprite);
                        }
                        newSprite.setPivotCenter();                                         // we only want rotation about the center of the sprite
                        newSprite.dangerous = s.getFileName().contains("spike");            // if its spiky, it kills us
                        iterator.remove();                                                  // the item can no longer be selected
                        //s.setVisible(false);
                        gameMode = GameMode.ITEM_PLACEMENT;     //TODO make this check to see if all players have made a selection before changing mode
                        spaceKeyClock.resetGameClock();         // make sure it doesn't get placed immediately after selection
                    }
                    if(gamePads.size()>=1) {
                        if (gamePads.get(0).isButtonPressed(GamePad.BUTTON_CROSS) && spaceKeyClock.getElapsedTime() > KEY_DELAY) {
                            String spriteId = "copy" + Integer.toString(placedSpriteCounter);   // we need to make a unique spriteId to make sure that --
                            placedSpriteCounter++;                                              // --> new sprites don't have the same id for checks later
                            Sprite newSprite = new Sprite(spriteId, s.getFileName());          //duplicate the sprite and add it to our level
                            newSprite.setScale(s.getxAbsoluteScale(), s.getyAbsoluteScale());
                            newSprite.setPosition(s.getxAbsolutePosition(), s.getyAbsolutePosition());
                            levelContainer.addChild(newSprite);                                 // the level container will hold everything in the level
                            if (s.getFileName().contains("Laser")) {
                                laserGunList.add(newSprite);
                            }
                            newSprite.setPivotCenter();                                         // we only want rotation about the center of the sprite
                            newSprite.dangerous = s.getFileName().contains("spike");            // if its spiky, it kills us
                            iterator.remove();                                                  // the item can no longer be selected
                            //s.setVisible(false);
                            gameMode = GameMode.ITEM_PLACEMENT;     //TODO make this check to see if all players have made a selection before changing mode
                            spaceKeyClock.resetGameClock();         // make sure it doesn't get placed immediately after selection
                        }
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
        if(pressedKeys.contains(KEY_ESC) && escKeyClock.getElapsedTime() > KEY_DELAY){
            gameMode = GameMode.GAMEPLAY;
        }
        if( gamePads.size()>=1) {
            if (gamePads.get(0).isButtonPressed(GamePad.BUTTON_START) && escKeyClock.getElapsedTime() > KEY_DELAY) {
                gameMode = GameMode.GAMEPLAY;
            }
        }
    }

    public void itemPlacementUpdate(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
        if (levelContainer != null) {
            levelContainer.update(pressedKeys, gamePads);
            // Move sprite based on user input
            if (!(levelContainer.getLastChild().isPlaced)) {                     //TODO make this give each player the item they chose
                handleCursorMoveInput(levelContainer.getLastChild(), CURSOR_SPEED, pressedKeys);
                handleGamepadCursorMoveInput(levelContainer.getLastChild(),CURSOR_SPEED,gamePads);
            }
            // Allow user to rotate image
            if (pressedKeys.contains(KEY_R) && rKeyClock.getElapsedTime() > KEY_DELAY) {
                if(levelContainer.getLastChild().getRotation() >= 3*Math.PI/2) levelContainer.getLastChild().setRotation(0);    // prevent rotations past 2 PI
                else levelContainer.getLastChild().setRotation(levelContainer.getLastChild().getRotation() + Math.PI / 2);
                rKeyClock.resetGameClock();
            }
            if (gamePads.size() >= 1) {
                if (gamePads.get(0).isButtonPressed(GamePad.RIGHT_TRIGGER) && rKeyClock.getElapsedTime() > KEY_DELAY) {
                    if(levelContainer.getLastChild().getRotation() >= 3*Math.PI/2) levelContainer.getLastChild().setRotation(0);    // prevent rotations past 2 PI
                    else levelContainer.getLastChild().setRotation(levelContainer.getLastChild().getRotation() + Math.PI / 2);
                    rKeyClock.resetGameClock();
                }
            }
            // Preventing overlaps - image changes to imageName + "-error.png"
            for (DisplayObjectContainer levelItem : levelContainer.getChildren()) {              // iterate over the sprites
                DisplayObjectContainer DOCbeingPlaced = levelContainer.getLastChild();
                if (!levelItem.getId().equals(DOCbeingPlaced.getId())) {                                  // if it's not itself
                    if (DOCbeingPlaced.getFileName().contains("-error") && levelItem.getFileName().contains("-error")) {
                        if (!levelItem.collidesWith(DOCbeingPlaced)) {                                     //if there NOT a collision
                            DOCbeingPlaced.setImageNormal();
                            levelItem.setImageNormal();
                            break;
                        }
                    } else {
                        if (levelItem.collidesWith(DOCbeingPlaced)) {                                      //if there IS a collision
                            DOCbeingPlaced.setImageError();
                            levelItem.setImageError();
                            break;
                        }
                    }
                }
            }
            if (pressedKeys.contains(KEY_SPACE) && spaceKeyClock.getElapsedTime() > KEY_DELAY) {     //if space is pressed
                if (!levelContainer.getLastChild().getFileName().contains("-error")) {                // and placement is allowed
                    levelContainer.getLastChild().isPlaced = true;
                    gameMode = GameMode.ITEM_SELECTION;
                    itemSelectionInitialized = false;
                    spaceKeyClock.resetGameClock();
                }
            }
            if (gamePads.size() >= 1) {
                if (gamePads.get(0).isButtonPressed(GamePad.BUTTON_CROSS) && spaceKeyClock.getElapsedTime() > KEY_DELAY) {     //if space is pressed
                    if (!levelContainer.getLastChild().getFileName().contains("-error")) {                // and placement is allowed
                        levelContainer.getLastChild().isPlaced = true;
                        gameMode = GameMode.ITEM_SELECTION;
                        itemSelectionInitialized = false;
                        spaceKeyClock.resetGameClock();
                    }
                }
            }
        }
        if (pressedKeys.contains(KEY_ESC) && escKeyClock.getElapsedTime() > KEY_DELAY) {
            if (!levelContainer.getLastChild().isPlaced) {
                levelContainer.removeChild(levelContainer.getLastChild());
            }
            for (DisplayObjectContainer levelItem : levelContainer.getChildren()) {
                if (levelItem.getFileName().contains("-error")) levelItem.setImageNormal();
            }
            gameMode = GameMode.GAMEPLAY;
        }
        if (gamePads.size() >= 1) {
            if (gamePads.get(0).isButtonPressed(GamePad.BUTTON_START) && escKeyClock.getElapsedTime() > KEY_DELAY) {
                if (!levelContainer.getLastChild().isPlaced) {
                    levelContainer.removeChild(levelContainer.getLastChild());
                }
                for (DisplayObjectContainer levelItem : levelContainer.getChildren()) {
                    if (levelItem.getFileName().contains("-error")) levelItem.setImageNormal();
                }
                gameMode = GameMode.GAMEPLAY;
            }
        }
    }

    public void gameplayUpdate(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads){
        for(PhysicsSprite player : players) {
            player.update(pressedKeys,gamePads);
            if(player.alive && !player.courseCompleted) {
                handlePlayerMoveInput(player, pressedKeys, gamePads);
                constrainPlayerToLevel(player);
                fallOffPlatforms(player, player.platformPlayerIsOn);
                shootGuns(pressedKeys,gamePads);
                for (DisplayObjectContainer object : levelContainer.getChildren()) {
                    if(player.collidesWith(object)) {
                        if(object.getId().equals("portal")){
                            player.dispatchEvent(new Event(Event.GOAL, player));
                        } else if(object.dangerous) {
                            player.dispatchEvent(new Event(Event.UNSAFE_COLLISION,player));
                        }
                    }
                    for(DisplayObjectContainer objectChild : object.getChildren()) {
                        if(player.collidesWith(objectChild)) {
                            if(objectChild.getId().contains("laserBeam")) {
                                player.dispatchEvent(new Event(Event.UNSAFE_COLLISION,player));
                            }
                        }
                    }
                }
                levelContainer.update(pressedKeys, gamePads); //TODO theres an issue with the update method with beams, figure out why the positions of beams are off
//                for (DisplayObjectContainer guns : laserGunList) {
//                    guns.update(pressedKeys, gamePads);
//                    for(DisplayObjectContainer beams : guns.getChildren()) {
//                        if(player.collidesWith(beams)) {
//
//                        }
//                    }
//                }
            }
            if(player.courseCompleted) gameMode = GameMode.ROUND_COMPLETE;
        }
    }

    // METHODIZED UPDATE SEGMENTS

    public void handleCursorMoveInput(DisplayObject displayObject, int speed, ArrayList<Integer> pressedKeys) {
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

    public void handleGamepadCursorMoveInput(DisplayObject displayObject, int speed,ArrayList<GamePad> gamePads) {
        if(gamePads.size()>=1) {
            if (gamePads.get(0).getLeftStickYAxis() < 0) {
                displayObject.setyPosition(displayObject.getyPosition() - speed);
            } else if (gamePads.get(0).getLeftStickYAxis() > 0) {
                displayObject.setyPosition(displayObject.getyPosition() + speed);
            }
            if (gamePads.get(0).getLeftStickXAxis() < 0) { //Left
                displayObject.setxPosition(displayObject.getxPosition() - speed);
            } else if (gamePads.get(0).getLeftStickXAxis() > 0) { //Right
                displayObject.setxPosition(displayObject.getxPosition() + speed);
            }
        }
    }

    public void handlePlayerMoveInput(PhysicsSprite physicsSprite, ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
        if(pressedKeys.contains(KEY_LEFT)){
            physicsSprite.setxPosition(physicsSprite.getxPosition()-PLAYER_SPEED);
        }
        else if(pressedKeys.contains(KEY_RIGHT)){
            physicsSprite.setxPosition(physicsSprite.getxPosition()+PLAYER_SPEED);
        }
        if(pressedKeys.contains(KEY_UP) && !physicsSprite.airborne){
            physicsSprite.airborne = true;
            physicsSprite.setyVelocity(-15);
        }
        if(gamePads.size()>=1) {
            if (gamePads.get(0).getLeftStickXAxis() < 0) { //Left
                physicsSprite.setxPosition(physicsSprite.getxPosition() - PLAYER_SPEED);
            } else if (gamePads.get(0).getLeftStickXAxis() > 0) { //Right
                physicsSprite.setxPosition(physicsSprite.getxPosition() + PLAYER_SPEED);
            }
            if (gamePads.get(0).isButtonPressed(GamePad.BUTTON_CROSS) && !physicsSprite.airborne) {
                physicsSprite.airborne = true;
                physicsSprite.setyVelocity(-15);
            }
        }
    }

    public void constrainPlayerToLevel(PhysicsSprite player) {
        if(player.getBottom() > GAME_HEIGHT) {
            //TODO there is not currently a way for us to set the global position of a sprite if it is a child
            player.setyPosition(GAME_HEIGHT-player.getScaledHeight());
            player.airborne = false;
            player.setyVelocity(0);
        } else if(player.getTop() < 0) {
            player.setyPosition(0);
            player.airborne = true;
        }
        if(player.getRight() > GAME_WIDTH) {
            player.setxPosition(GAME_WIDTH-player.getScaledWidth());
            player.setxVelocity(0);
        } else if(player.getLeft() < 0) {
            player.setxPosition(0);
            player.setxVelocity(0);
        }
    }

    public void constrainItemToLevel(Sprite sprite) {
        if(sprite.getBottom() > GAME_HEIGHT) {
            //TODO there is not currently a way for us to set the global position of a sprite if it is a child
            sprite.setyPosition(GAME_HEIGHT-sprite.getScaledHeight());
        } else if(sprite.getTop() < 0) {
            sprite.setyPosition(0);
        }
        if(sprite.getRight() > GAME_WIDTH) {
            sprite.setxPosition(GAME_WIDTH-sprite.getScaledWidth());
        } else if(sprite.getLeft() < 0) {
            sprite.setxPosition(0);
        }
    }

    public void fallOffPlatforms(PhysicsSprite player, DisplayObject platform) {
        if (player.isOnPlatform) {
            if (player.getRight() < platform.getLeft() || player.getLeft() > platform.getRight()) {
                if (player.getBottom() > platform.getTop() - 2 && mario.getBottom() < platform.getTop() + 2) {
                    player.airborne = true;
                    player.isOnPlatform = false;
                }
            }
        }
    }

    public void shootGuns(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
        if(frameCounter % 100 == 0) {
            for(Sprite gun : laserGunList) {
                LaserBeam beam = new LaserBeam("laserbeam" + gun.getId(),gun.getRotation());
                beam.dangerous = true;
                beam.setPivotCenter();
                beam.setScale(1,1);
                beam.alignCenterVertical(gun);
                beam.alignCenterHorizontal(gun);
                laserBeams.add(beam);
                levelContainer.addChild(beam);
            }
        }
        for(Iterator<LaserBeam> iterator = laserBeams.iterator(); iterator.hasNext();) {
            LaserBeam beam = iterator.next();
            beam.update(pressedKeys,gamePads);
            if(beam.direction == 0) beam.setxPosition(beam.getxPosition() - BEAM_SPEED);
            else if(beam.direction == Math.PI/2) beam.setyPosition(beam.getyPosition() - BEAM_SPEED);
            else if(beam.direction == Math.PI) beam.setxPosition(beam.getxPosition() + BEAM_SPEED);
            else if(beam.direction == 3*Math.PI/2) beam.setyPosition(beam.getyPosition() + BEAM_SPEED);

            if(beam.direction % Math.PI < 1) {
                if(beam.getRight() < 0)iterator.remove();
                else if(beam.getLeft() > GAME_WIDTH) iterator.remove();
            } else if(beam.direction % Math.PI/2 < 1) {
                if(beam.getBottom() < 0) iterator.remove();
                else if(beam.getTop() > GAME_HEIGHT) iterator.remove();
            }

        }
    }

	@Override
	public void draw(Graphics g){
		super.draw(g);
        if(gameMode != null) {
            switch(gameMode) {
                case ITEM_SELECTION:
                    itemSelectionDraw(g);
                    break;
                case ITEM_PLACEMENT:
                    itemPlacementDraw(g);
                    break;
                case GAMEPLAY:
                    gameplayDraw(g);
                    break;
                case MAIN_MENU:
                    break;
                case ROUND_COMPLETE:
                    roundCompleteDraw(g);
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
                for(DisplayObject displayObject : selectionBackground.getChildren()){
                    test = displayObject.getHitbox();
                    g.fillRect(test.x, test.y, test.width, test.height);
                }
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

    public void gameplayDraw(Graphics g) {
        if(levelContainer != null) {
            levelContainer.draw(g);
            for(PhysicsSprite player : players) {
                if(player.isVisible()) player.draw(g);
            }
        }

        if(debugHitboxes) {
            for(DisplayObjectContainer c : levelContainer.getChildren()) {
                Rectangle test = c.getHitbox();
                g.fillRect(test.x, test.y, test.width, test.height);
            }
            for(PhysicsSprite p : players) {
                Rectangle test = p.getHitbox();
                g.fillRect(test.x, test.y, test.width, test.height);
            }
            for(LaserBeam b : laserBeams) {
                Rectangle test = b.getHitbox();
                g.fillRect(test.x, test.y, test.width, test.height);
            }
        }
    }

    public void roundCompleteDraw(Graphics g) {
        if(levelContainer != null) {
            levelContainer.draw(g);
            for(PhysicsSprite player : players) {
                if(player.isVisible()) player.draw(g);
            }
        }
        g.drawString("Level Completed!",GAME_WIDTH/2,GAME_HEIGHT/2+50);
    }

	public static void main(String[] args) {
		TheMinorsGame game = new TheMinorsGame();
		game.start();

	}
}
