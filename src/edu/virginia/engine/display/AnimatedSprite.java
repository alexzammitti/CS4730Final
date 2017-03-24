package edu.virginia.engine.display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class AnimatedSprite extends Sprite {

	protected BufferedImage[] frames = new BufferedImage[11];
	protected int currentIndex = 0;
	protected int startIndex;
	protected int endIndex;
	protected int x = 14;
	protected int y = 10;
	protected int width = 15;
	protected int height = 20;
	protected int speed = 15;
	protected int frameCounter = 0;
	protected boolean firstJump = true;
	protected boolean firstWalk = true;
	protected boolean firstFall = true;
	
	public BufferedImage[] getFrames() {
		return frames;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public void update(ArrayList<String> pressedKeys) { super.update(pressedKeys); }

	public AnimatedSprite(String id) { super(id); }

	public AnimatedSprite(String id, String file) {
		super(id);
		BufferedImage sheet = readImage(file);
		frames[0] = sheet.getSubimage(14, 10, 14, 20);
		for(int i = 1; i < 4; i++) {
			x = 14+17*(i);
			BufferedImage temp = sheet.getSubimage(x,10,15,20);
			frames[i] = temp;
		}
		frames[4] = sheet.getSubimage(81, 10, 16, 20);
		frames[5] = sheet.getSubimage(115, 7, 16, 22);
		frames[6] = sheet.getSubimage(132,8,16,20);

		this.setImage(frames[0]);
	}
	
	public void animate(String action) {
		frameCounter++;
		
		if(action.equals("jump")) {
				this.setImage(frames[5]);

		}

		if(action.equals("falling")) {
				this.setImage(frames[6]);
		}
		
		if(action.equals("walk")) {
			if(firstWalk) {
				firstWalk = false;
				currentIndex = 0;
				//setxScale(getxScale()/.7);
				//setyScale(getyScale()/.7);
			}
			startIndex = 0;
			endIndex = 4;
		}
		
		if(action.equals("idle")) {
			currentIndex = 0;
			startIndex = 0;
			endIndex = 0;
			firstJump = true;
			firstWalk = true;
			firstFall = true;
			this.setImage(frames[currentIndex]);
		} else if(action.equals("walk")) {
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
