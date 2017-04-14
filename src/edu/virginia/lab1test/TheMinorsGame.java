package edu.virginia.lab1test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

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
        ITEM_SELECTION, ITEM_PLACEMENT, GAMEPLAY, MAIN_MENU, ROUND_COMPLETE, LEVEL_SELECTION, GAME_COMPLETE;
    }

    private GameMode gameMode = GameMode.ITEM_SELECTION;
    private final static int GAME_WIDTH = 1250;
    private final static int GAME_HEIGHT = 700;
    private final static String INPUT_KEYBOARD = "keyboard";
    private final static String INPUT_GAMEPADS = "gamepads";
    // keys
    private final static int KEY_DELAY = 200;
    private final static int KEY_UP = 38;
    private final static int KEY_DOWN = 40;
    private final static int KEY_LEFT = 37;
    private final static int KEY_RIGHT = 39;
    private final static int KEY_SPACE = 32;
    private final static int KEY_R = 82;
    private final static int KEY_ESC = 27;

    // speeds etc
    private final static int CURSOR_SPEED = 10;
    private final static int PLAYER_SPEED = 5;
    private final static int BEAM_SPEED = 15;
    private final static int GRAVITY = 1;
    private final static int JUMP_SPEED = 15;





	// GLOBAL VARIABLES
    private int frameCounter = 0;
    private boolean itemSelectionInitialized = false;
    private int placedItemCounter = 0;
    private boolean debugHitboxes = false;
    private String inputMode = "";
    private int numberOfPlayers = 0;
    private int numberOfSelectedItems = 0;
    private int numberOfPlacedItems = 0;



	// SET UP SPRITE ASSETS
    // Characters
    private ArrayList<Player> players = new ArrayList<>(0);
	private Player player1 = new Player("player1", "player1","cursor-orange.png",0);
	private Player player2 = new Player("player2", "player2","cursor-blue.png",1);
	private Player player3 = new Player("player3", "player3","cursor-green.png",2);
	private Player player4 = new Player("player4", "player4","cursor-pink.png",3);
	// Placeable items
	private Sprite platform1 = new Sprite("platform1", "brick.png");
	private Sprite platform2 = new Sprite("platform2", "brick.png");
	private Sprite portal = new Sprite("portal","portal.png");
	// Placeholder Sprites for randomly selected placeable items - their images are what will be set later, and their ids updated
    private static String[] itemFileNames = {"3x1platform.png","spikerow.png","LaserGun.png","1x1platform.png"};
    private Sprite item1 = new Sprite("item1");
    private Sprite item2 = new Sprite("item2");
    private Sprite item3 = new Sprite("item3");
    private Sprite item4 = new Sprite("item4");
    private Sprite item5 = new Sprite("item5");
	// Backgrounds
    private Sprite selectionBackground = new Sprite("selectionbackground","item-selection-screen.png");
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
    public GameClock roundCompleteClock = new GameClock();


	
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

        // POPULATE ITEM LISTS
        placeableItemList.add(item1);
        placeableItemList.add(item2);
        placeableItemList.add(item3);
        placeableItemList.add(item4);
        placeableItemList.add(item5);

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


        // SET UP TWEENS - TODO - might also be good to methodize
        selectionBackgroundTween.animate(TweenableParam.SCALE_X,0,1.2,100);
        //tweenJuggler.add(selectionBackgroundTween);

        gameMode = GameMode.ITEM_SELECTION;

	}

	public void initializePlayers(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
        if(numberOfPlayers==0) {
            switch (gamePads.size()) {
                case 0:
                    inputMode = INPUT_KEYBOARD;
                    players.add(player1);
                    System.out.println("One player on keyboard");
                    break;
                case 1:
                    inputMode = INPUT_GAMEPADS;
                    players.add(player1);
                    System.out.println("One player on controller");
                    break;
                case 2:
                    inputMode = INPUT_GAMEPADS;
                    players.add(player1);
                    players.add(player2);
                    System.out.println("Two players on controllers");
                    break;
                case 3:
                    inputMode = INPUT_GAMEPADS;
                    players.add(player1);
                    players.add(player2);
                    players.add(player3);
                    System.out.println("Three players on controllers");
                    break;
                case 4:
                    inputMode = INPUT_GAMEPADS;
                    players.add(player1);
                    players.add(player2);
                    players.add(player3);
                    players.add(player4);
                    System.out.println("Four players on controllers");
                    break;
            }
            numberOfPlayers = players.size();
            resetPlayers(pressedKeys,gamePads);
            for(Player player : players) {
                player.addEventListener(eventManager, Event.SAFE_COLLISION);
                player.addEventListener(eventManager, Event.UNSAFE_COLLISION);
                player.addEventListener(eventManager, Event.DEATH);
                player.addEventListener(eventManager, Event.GOAL);
            }
        }
    }

    public void resetPlayers(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
        for(Player player : players) {
            player.setCourseCompleted(false);
            player.setVisible(true);
            player.setAlive(true);
            player.setAirborne(true);
            player.setAlpha(1);
            player.setPivotCenter();
            player.setScale(1, 1);
            player.setPosition(10 + players.indexOf(player) * 10, 130);   //space out players
            player.setyAcceleration(GRAVITY);
            player.setyVelocity(0);
            player.setxVelocity(0);
            player.cursor.setScale(0.25, 0.25);
            player.cursor.setPosition(300, 300);
            player.cursor.alignCenterHorizontal(levelContainer);
            player.cursor.setVisible(true);
            player.item = null;
            //player.cursor.alignFractionHorizontal(levelContainer,players.size()+1,players.indexOf(player)+1);      //space out cursors
            //TODO make the cursors spread nicely
            player.cursor.setxPosition(GAME_WIDTH / 2);
            player.update(pressedKeys,gamePads);
        }
    }

	public void initializeItemSelection() {
	    selectionBackground.removeAll();
        selectionBackground.addChild(item1);
        selectionBackground.addChild(item2);
        selectionBackground.addChild(item3);
        selectionBackground.addChild(item4);
        selectionBackground.addChild(item5);
        for(int i = 0; i <= selectionBackground.getChildren().size() - numberOfPlayers; i++) {
            selectionBackground.removeByIndex(selectionBackground.getChildren().size()-1);
        }
        int itemCount = selectionBackground.getChildren().size();
        placeableItemList.clear();

        for(DisplayObjectContainer item : selectionBackground.getChildren()) {
            int random = ThreadLocalRandom.current().nextInt(0,itemCount+1);
            item.setImage(itemFileNames[random]);
            switch(itemFileNames[random]){
                case "3x1platform.png":
                    item.setScale(1,1);
                    break;
                case "spikerow.png":
                    item.setScale(0.3,0.3);
                    break;
                case "LaserGun.png":
                    item.setScale(0.5,0.5);
                    break;
                case "1x1platform.png":
                    item.setScale(1,1);
                    break;
            }
            item.setVisible(true);
            placeableItemList.add((Sprite)item);
            item.alignCenterHorizontal(selectionBackground);
            item.alignFractionVertical(selectionBackground,
                    itemCount+1,
                    selectionBackground.getChildren().indexOf(item)+1);
        }

        // GIVE ITEMS IMAGES - will be randomized later TODO

//        item1.setScale(0.7,0.3);
//        item1.alignCenterHorizontal(selectionBackground);
//        item1.alignFractionVertical(selectionBackground,100,30);
//
//        item2.setScale(0.3,0.3);
//        item2.alignCenterHorizontal(selectionBackground);
//        item2.alignFractionVertical(selectionBackground,100,52);
//
//        item3.setScale(.5,.5);
//        item3.alignCenterHorizontal(selectionBackground);
//        item3.alignFractionVertical(selectionBackground,100,75);
//
//        for(Sprite item : placeableItemList) {
//            item.setVisible(true);
//        }

        itemSelectionInitialized = true;
    }

	@Override
	public void update(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads){
		super.update(pressedKeys,gamePads);
        frameCounter++;
        if (frameCounter > 3) {
            TweenJuggler.getInstance().nextFrame();
            initializePlayers(pressedKeys,gamePads); //only happens once

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
                    case ROUND_COMPLETE:
                        roundCompleteUpdate(pressedKeys,gamePads);
                        break;
                }
            }
        }
	}

	// UPDATE METHODS FOR MODES

	public void itemSelectionUpdate(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
	    if(! itemSelectionInitialized && frameCounter > 4 && numberOfPlayers > 0) {
	        initializeItemSelection();
        }
        for(Player player : players) {
            if(!levelContainer.getChildren().contains(player.item)) {
                player.cursor.update(pressedKeys, gamePads);
                selectionBackground.update(pressedKeys, gamePads);
                // MOVE CURSOR BASED ON USER INPUT
                if (inputMode.equals(INPUT_GAMEPADS))
                    handleGamepadCursorMoveInput(player.cursor, CURSOR_SPEED, gamePads, player.playerNumber);
                else handleCursorMoveInput(player.cursor, CURSOR_SPEED, pressedKeys);
                constrainItemToLevel(player.cursor);
                // CHECK FOR OVERLAP BETWEEN CURSORS & SELECTABLE ITEMS
                for (Iterator<Sprite> iterator = placeableItemList.iterator(); iterator.hasNext(); ) {
                    Sprite s = iterator.next();
                    s.update(pressedKeys, gamePads);
                    // if the cursor overlaps with a selectable items
                    if (player.cursor.collidesWith(s)) {
                        // add tween stuff here for polish if desired
                        // and the player presses the select button over it
                        if (inputMode.equals(INPUT_GAMEPADS)) {
                            if (gamePads.get(player.playerNumber).isButtonPressed(GamePad.BUTTON_A)) {
                                player.item = selectItem(iterator, s);
                                numberOfSelectedItems++;
                                player.cursor.setVisible(false);
                                break;
                            }
                        } else if (pressedKeys.contains(KEY_SPACE) && spaceKeyClock.getElapsedTime() > KEY_DELAY) {
                            player.item = selectItem(iterator, s);
                            numberOfSelectedItems++;
                            if (numberOfSelectedItems >= numberOfPlayers) {
                                gameMode = GameMode.ITEM_PLACEMENT;
                                numberOfSelectedItems = 0;
                            }
                            spaceKeyClock.resetGameClock();         // make sure it doesn't get placed immediately after selection
                            break;
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
        if (numberOfSelectedItems >= numberOfPlayers) {
            gameMode = GameMode.ITEM_PLACEMENT;
            GameClock gameClock = new GameClock();
            gameClock.resetGameClock();
            while (gameClock.getElapsedTime() < 200) {
                continue;
            }      //wait 200ms to prevent placement
            numberOfSelectedItems = 0;
        }
//        if(pressedKeys.contains(KEY_ESC) && escKeyClock.getElapsedTime() > KEY_DELAY){
//            gameMode = GameMode.GAMEPLAY;
//        }
//        if( gamePads.size()>=1) {
//            if (gamePads.get(0).isButtonPressed(GamePad.BUTTON_START) && escKeyClock.getElapsedTime() > KEY_DELAY) {
//                gameMode = GameMode.GAMEPLAY;
//            }
//        }
    }

    public Sprite selectItem(Iterator<Sprite> spriteIterator, Sprite sprite) {
        String spriteId = "item" + Integer.toString(placedItemCounter);   // we need to make a unique spriteId to make sure that --
        placedItemCounter++;                                              // --> new sprites don't have the same id for checks later
        Sprite newSprite = new Sprite(spriteId, sprite.getFileName());          //duplicate the sprite and add it to our level
        newSprite.setScale(sprite.getxAbsoluteScale(), sprite.getyAbsoluteScale());
        newSprite.setPosition(sprite.getxAbsolutePosition(), sprite.getyAbsolutePosition());
        levelContainer.addChild(newSprite);                                 // the level container will hold everything in the level
        if (sprite.getFileName().contains("Laser")) {
            laserGunList.add(newSprite);
        }
        newSprite.setPivotCenter();                                         // we only want rotation about the center of the sprite
        newSprite.dangerous = sprite.getFileName().contains("spike");            // if its spiky, it kills us
        sprite.setVisible(false);
        spriteIterator.remove();                                                 // the item can no longer be selected
        return newSprite;
    }

    public void itemPlacementUpdate(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
        if (levelContainer != null) {
            levelContainer.update(pressedKeys, gamePads);
            for(Player player : players) {
                // Move sprite based on user input
                if (!player.item.isPlaced()) {
                    handleCursorMoveInput(player.item, CURSOR_SPEED, pressedKeys);
                    handleGamepadCursorMoveInput(player.item, CURSOR_SPEED, gamePads, player.playerNumber);
                    constrainItemToLevel(player.item);
                    // Allow user to rotate image
                    if (inputMode.equals(INPUT_GAMEPADS)) {
                        if (gamePads.get(player.playerNumber).isButtonPressed(GamePad.RIGHT_TRIGGER)) {
                            if (player.item.getRotation() >= 3 * Math.PI / 2) {
                                player.item.setRotation(0);    // prevent rotations past 2 PI
                            } else {
                                player.item.setRotation(player.item.getRotation() + Math.PI / 2);
                            }
                            //rKeyClock.resetGameClock(); TODO figure out how to make delay between rotations
                        }
                    } else if (pressedKeys.contains(KEY_R) && rKeyClock.getElapsedTime() > KEY_DELAY) {
                        if (player.item.getRotation() >= 3 * Math.PI / 2)
                            player.item.setRotation(0);    // prevent rotations past 2 PI
                        else
                            player.item.setRotation(player.item.getRotation() + Math.PI / 2);
                        rKeyClock.resetGameClock();
                    }
                    // Preventing overlaps - image changes to imageName + "-error.png"
                    for (DisplayObjectContainer levelItem : levelContainer.getChildren()) {              // iterate over the sprites
                        DisplayObjectContainer DOCbeingPlaced = player.item;
                        if (!levelItem.getId().equals(DOCbeingPlaced.getId())) {                                  // if it's not itself
                            if (DOCbeingPlaced.getFileName().contains("-error") && levelItem.getFileName().contains("-error")) {
                                if (!levelItem.collidesWith(DOCbeingPlaced)) {                                     //if there NOT a collision
                                    DOCbeingPlaced.setImageNormal();
                                    levelItem.setImageNormal();
                                    break;
                                }
                            } else {
                                if (levelItem.collidesWith(DOCbeingPlaced)
                                        && !levelItem.getFileName().contains("beam")) {                                      //if there IS a collision
                                    DOCbeingPlaced.setImageError();
                                    levelItem.setImageError();
                                    break;
                                }
                            }
                        }
                    }
                    if (inputMode.equals(INPUT_GAMEPADS)) {
                        if (gamePads.get(player.playerNumber).isButtonPressed(GamePad.BUTTON_A)) {     //if space is pressed
                            if (!player.item.getFileName().contains("-error")) {                // and placement is allowed
                                player.item.setPlaced(true);
                                numberOfPlacedItems++;
                                if(numberOfPlacedItems >= numberOfPlayers) {
                                    gameMode = GameMode.GAMEPLAY;
                                    itemSelectionInitialized = false;
                                    GameClock gameClock = new GameClock();
                                    gameClock.resetGameClock();
                                    while(gameClock.getElapsedTime() < 200){continue;}      //wait 200ms to prevent placement
                                    numberOfPlacedItems=0;
                                }
                            }
                        }
                    } else if (pressedKeys.contains(KEY_SPACE) && spaceKeyClock.getElapsedTime() > KEY_DELAY) {     //if space is pressed
                        if (!player.item.getFileName().contains("-error")) {                // and placement is allowed
                            player.item.setPlaced(true);
                            numberOfPlacedItems++;
                            if(numberOfPlacedItems >= numberOfPlayers) {
                                gameMode = GameMode.GAMEPLAY;
                                itemSelectionInitialized = false;
//                                GameClock gameClock = new GameClock();
//                                gameClock.resetGameClock();
//                                while(gameClock.getElapsedTime() < 200){continue;}      //wait 200ms to prevent placement
                                numberOfPlacedItems=0;
                                levelContainer.setAllChildrenImagesNormal();
                            }
                            spaceKeyClock.resetGameClock();
                        }
                    }
                }
                // Manual way to enter gameplay
//                if (inputMode.equals(INPUT_GAMEPADS)) {
//                    if (gamePads.get(0).isButtonPressed(GamePad.BUTTON_START) && escKeyClock.getElapsedTime() > KEY_DELAY) {
//                        if (!levelContainer.getLastChild().isPlaced) {
//                            levelContainer.removeChild(levelContainer.getLastChild());
//                        }
//                        for (DisplayObjectContainer levelItem : levelContainer.getChildren()) {
//                            if (levelItem.getFileName().contains("-error")) levelItem.setImageNormal();
//                        }
//                        gameMode = GameMode.GAMEPLAY;
//                    }
//                } else if (pressedKeys.contains(KEY_ESC) && escKeyClock.getElapsedTime() > KEY_DELAY) {
//                    if (!levelContainer.getLastChild().isPlaced) {
//                        levelContainer.removeChild(levelContainer.getLastChild());
//                    }
//                    for (DisplayObjectContainer levelItem : levelContainer.getChildren()) {
//                        if (levelItem.getFileName().contains("-error")) levelItem.setImageNormal();
//                    }
//                    gameMode = GameMode.GAMEPLAY;
//                }
            }
        }

    }

    public void gameplayUpdate(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads){
        if(levelContainer != null) levelContainer.update(pressedKeys, gamePads);
        for(Player player : players) {
            player.update(pressedKeys,gamePads);
            handleAnimation(player,pressedKeys,gamePads);
            player.animate();
            handlePlayerMoveInput(player, pressedKeys, gamePads);
            player.constrainToLevel(GAME_WIDTH,GAME_HEIGHT);
            player.fallOffPlatforms(player.platformPlayerIsOn);
            shootGuns(pressedKeys,gamePads);
            if(player.isAlive() && !player.isCourseCompleted()) {
                for (DisplayObjectContainer object : levelContainer.getChildren()) {
                    if(player.collidesWith(object)) {
                        if(object.getId().equals("portal")){
                            player.dispatchEvent(new Event(Event.GOAL, player));
                        }
                    }
                }
            }
        }
        int playersDone = 0;
        for(Player player : players) {
            if(!player.isAlive()) playersDone++;
            if(player.isCourseCompleted()) playersDone++;
        }
        if(playersDone >= numberOfPlayers) {
            gameMode = GameMode.ROUND_COMPLETE;
            for(Sprite beam : laserBeams) {
                beam.setVisible(false);
                beam.setHitbox(new Rectangle(0,0,0,0));
                beam.setPosition(-100,-100);    //TODO maybe figure out the right way to do this
            }
            laserBeams.clear();
            roundCompleteClock.resetGameClock();
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

    public void handleGamepadCursorMoveInput(DisplayObject displayObject, int speed,ArrayList<GamePad> gamePads, int playerNumber) {
        if(inputMode.equals(INPUT_GAMEPADS)) {
            if (gamePads.get(playerNumber).getLeftStickYAxis() < 0) {
                displayObject.setyPosition(displayObject.getyPosition() - speed);
            } else if (gamePads.get(playerNumber).getLeftStickYAxis() > 0) {
                displayObject.setyPosition(displayObject.getyPosition() + speed);
            }
            if (gamePads.get(playerNumber).getLeftStickXAxis() < 0) { //Left
                displayObject.setxPosition(displayObject.getxPosition() - speed);
            } else if (gamePads.get(playerNumber).getLeftStickXAxis() > 0) { //Right
                displayObject.setxPosition(displayObject.getxPosition() + speed);
            }
        }
    }

    public void handlePlayerMoveInput(Player player, ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
        if(inputMode.equals(INPUT_GAMEPADS)) {
            if (gamePads.get(player.playerNumber).getLeftStickXAxis() < 0) { //Left
                player.setxPosition(player.getxPosition() - PLAYER_SPEED);
            } else if (gamePads.get(player.playerNumber).getLeftStickXAxis() > 0) { //Right
                player.setxPosition(player.getxPosition() + PLAYER_SPEED);
            }
            if (gamePads.get(player.playerNumber).isButtonPressed(GamePad.BUTTON_A) && !player.isAirborne()) {
                player.setAirborne(true);
                player.setyVelocity(-JUMP_SPEED);
            }
        } else if(pressedKeys.contains(KEY_LEFT)){
            player.setxPosition(player.getxPosition()-PLAYER_SPEED);
        }
        else if(pressedKeys.contains(KEY_RIGHT)){
            player.setxPosition(player.getxPosition()+PLAYER_SPEED);
        }
        if(pressedKeys.contains(KEY_UP) && !player.isAirborne()){
            player.setAirborne(true);
            player.setyVelocity(-JUMP_SPEED);
        }
    }

    public void handleAnimation(Player player, ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {
        if (player.isAirborne()) {
            if(player.getyVelocity() > 0) {
                player.setAnimation(AnimatedSprite.FALLING_ANIMATION);
            }
            if(player.getyVelocity() < 0) {
                player.setAnimation(AnimatedSprite.JUMP_ANIMATION);
            }
        } else if(inputMode.equals(INPUT_GAMEPADS)) {
            if (gamePads.get(player.playerNumber).getLeftStickXAxis() < 0) {
                player.setAnimation(AnimatedSprite.WALK_ANIMATION);
                player.setRight(true);
            } else if (gamePads.get(player.playerNumber).getLeftStickXAxis() > 0) {
                player.setAnimation(AnimatedSprite.WALK_ANIMATION);
                player.setRight(false);
            }

        } else if (pressedKeys.contains(KEY_RIGHT)) {                   // TODO make animations flip images
            player.setAnimation(AnimatedSprite.WALK_ANIMATION);
            player.setRight(true);
        } else if(pressedKeys.contains(KEY_LEFT)){
            player.setAnimation(AnimatedSprite.WALK_ANIMATION);
            player.setRight(false);
        } else {
            player.setAnimation(AnimatedSprite.IDLE_ANIMATION);
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

    public void roundCompleteUpdate(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads){
        levelContainer.update(pressedKeys,gamePads);
        if(roundCompleteClock.getElapsedTime() > 1000){
            gameMode = GameMode.ITEM_SELECTION;
            resetPlayers(pressedKeys,gamePads);
            levelContainer.update(pressedKeys,gamePads);
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
            Rectangle test;
            levelContainer.draw(g);
            selectionBackground.draw(g);
            for(Player player : players) {
                player.cursor.draw(g);
                if(debugHitboxes) {
                    test = player.cursor.getHitbox();
                    g.fillRect(test.x, test.y, test.width, test.height);
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
            for(Player player : players) {
                if(player.isVisible()) player.draw(g);
            }
        }

        if(debugHitboxes) {
            for(DisplayObjectContainer c : levelContainer.getChildren()) {
                Rectangle test = c.getHitbox();
                g.fillRect(test.x, test.y, test.width, test.height);
            }
            for(Player p : players) {
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
            for(Player player : players) {
                if(player.isVisible()) player.draw(g);
            }
        }
    }

	public static void main(String[] args) {
		TheMinorsGame game = new TheMinorsGame();
		game.start();

	}
}
