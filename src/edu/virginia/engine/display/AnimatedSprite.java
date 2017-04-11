package edu.virginia.engine.display;

import edu.virginia.engine.controller.GamePad;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class AnimatedSprite extends Sprite {
//test
	protected BufferedImage[] frames = new BufferedImage[14];
	protected int currentIndex = 0;
	protected int startIndex;
	protected int endIndex;
	protected int x = 14;
	protected int y = 10;
	protected int width = 15;
	protected int height = 20;
	protected int speed = 5;
	protected int frameCounter = 0;
	protected boolean firstJump = true;
	protected boolean firstWalk = true;
	protected boolean firstFall = true;
	
	public BufferedImage[] getFrames() { return frames; }

	public int getSpeed() { return speed; }

	public void setSpeed(int speed) { this.speed = speed; }

	public void setFrames(BufferedImage[] frames) { this.frames = frames; }

	public int getCurrentIndex() { return currentIndex; }

	public void setCurrentIndex(int currentIndex) { this.currentIndex = currentIndex; }

	public int getStartIndex() { return startIndex; }

	public void setStartIndex(int startIndex) { this.startIndex = startIndex; }

	public int getEndIndex() { return endIndex; }

	public void setEndIndex(int endIndex) { this.endIndex = endIndex; }

	public void update(ArrayList<Integer> pressedKeys,ArrayList<GamePad> gamePads) { super.update(pressedKeys,gamePads); }

	public AnimatedSprite(String id) { super(id); }

	public AnimatedSprite(String id, String player) {
		super(id);
		BufferedImage idle = readImage(player + "-waiting-00.png");
		frames[0] = idle;
		for(int i = 0; i <= 7; i++) {
			frames[i+1] = readImage(player + "-walk-0" + i + ".png");
		}
		for(int i = 0; i <= 2; i++) {
			frames[i+9] = readImage(player + "-jet-0" + i + ".png");
		}
		for(int i = 0; i <= 1; i++) {
			frames[i+12] = readImage(player + "-falling-0" + i + ".png");
		}

		this.setImage(frames[0]);
//		BufferedImage sheet = readImage(file);
//		frames[0] = sheet.getSubimage(14, 10, 14, 20);
//		for(int i = 1; i < 4; i++) {
//			x = 14+17*(i);
//			BufferedImage temp = sheet.getSubimage(x,10,15,20);
//			frames[i] = temp;
//		}
//		frames[4] = sheet.getSubimage(81, 10, 16, 20);
//		frames[5] = sheet.getSubimage(115, 7, 16, 22);
//		frames[6] = sheet.getSubimage(132,8,16,20);


	}
	
	public void animate(String action) {
		frameCounter++;
		
		if(action.equals("jump")) {
			if(firstJump) {
				firstJump = false;
				firstWalk = true;
				currentIndex = 9;
			}
			startIndex = 9;
			endIndex = 11;
		}

		if(action.equals("falling")) {
			this.setImage(frames[12]);
		}

		if(action.equals("landing")) {
			this.setImage(frames[13]);
		}
		
		if(action.equals("walk")) {
			if(firstWalk) {
				firstWalk = false;
				firstJump = true;
				currentIndex = 1;
			}
			startIndex = 1;
			endIndex = 8;
		}
		
		if(action.equals("idle")) {
			currentIndex = 0;
			startIndex = 0;
			endIndex = 0;
			firstJump = true;
			firstWalk = true;
			firstFall = true;
			this.setImage(frames[currentIndex]);
		} else if(action.equals("walk") || action.equals("jump")) {
			if (frameCounter % speed == 0) {
				if (currentIndex == endIndex) {
					currentIndex = startIndex;
					this.setImage(frames[currentIndex]);
				} else {
					currentIndex++;
					this.setImage(frames[currentIndex]);
				}
			}
		}
	}
	
	
	

}
