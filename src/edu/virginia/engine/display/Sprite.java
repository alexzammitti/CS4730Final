package edu.virginia.engine.display;

import edu.virginia.engine.controller.GamePad;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Nothing in this class (yet) because there is nothing specific to a Sprite yet that a DisplayObject
 * doesn't already do. Leaving it here for convenience later. you will see!
 * */
public class Sprite extends DisplayObjectContainer {

	private int startX;
	private int startY;
	private boolean slidingPlatformDirection = true;

	public boolean isSlidingPlatformDirection() {
		return slidingPlatformDirection;
	}

	public void setSlidingPlatformDirection(boolean slidingPlatformDirection) {
		this.slidingPlatformDirection = slidingPlatformDirection;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public Sprite(String id) {
		super(id);
	}

	public Sprite(String id, String imageFileName) {
		super(id, imageFileName);
	}
	
	public void draw(Graphics g) {
		super.draw(g);
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) {
		super.update(pressedKeys,gamePads);
	}

	}