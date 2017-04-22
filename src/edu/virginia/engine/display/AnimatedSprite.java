package edu.virginia.engine.display;

import edu.virginia.engine.controller.GamePad;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class AnimatedSprite extends Sprite {

    public final static String IDLE_ANIMATION = "idle";
    public final static String WALK_ANIMATION = "walk";
    public final static String FALLING_ANIMATION = "falling";
    public final static String JUMP_ANIMATION = "jump";
    public final static String LANDING_ANIMATION = "landing";


    protected BufferedImage[] rightFrames = new BufferedImage[14];
	protected BufferedImage[] leftFrames = new BufferedImage[14];
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
	protected boolean right = true;
	private String animation = "idle";

	public String getAnimation() {
		return animation;
	}

	public void setAnimation(String animation) {
		this.animation = animation;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public BufferedImage[] getFrames() { return rightFrames; }

	public int getSpeed() { return speed; }

	public void setSpeed(int speed) { this.speed = speed; }

	public void setFrames(BufferedImage[] frames) { this.rightFrames = frames; }

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
		BufferedImage rightIdle = readImage(player + "-waiting-00.png");
		rightFrames[0] = rightIdle;
		for(int i = 0; i <= 7; i++) {
			rightFrames[i+1] = readImage(player + "-walk-0" + i + ".png");
		}
		for(int i = 0; i <= 2; i++) {
			rightFrames[i+9] = readImage(player + "-jet-0" + i + ".png");
		}
		for(int i = 0; i <= 1; i++) {
			rightFrames[i+12] = readImage(player + "-falling-0" + i + ".png");
		}
		BufferedImage leftIdle = readImage("left-" + player + "-waiting-00.png");
		leftFrames[0] = leftIdle;
		for(int i = 0; i <= 7; i++) {
			leftFrames[i+1] = readImage("left-" + player + "-walk-0" + i + ".png");
		}
		for(int i = 0; i <= 2; i++) {
			leftFrames[i+9] = readImage("left-" + player + "-jet-0" + i + ".png");
		}
		for(int i = 0; i <= 1; i++) {
			leftFrames[i+12] = readImage("left-" + player + "-falling-0" + i + ".png");
		}

		this.setImage(rightFrames[0]);
	}
	
	public void animate() {
		frameCounter++;
		if(right) {

			if (this.animation.equals(JUMP_ANIMATION)) {
				if (firstJump) {
					firstJump = false;
					firstWalk = true;
					currentIndex = 9;
				}
				startIndex = 9;
				endIndex = 11;
			}

			if (this.animation.equals(FALLING_ANIMATION)) {
				this.setImage(rightFrames[12]);
			}

			if (this.animation.equals(LANDING_ANIMATION)) {
				this.setImage(rightFrames[13]);
			}

			if (this.animation.equals(WALK_ANIMATION)) {
				if (firstWalk) {
					firstWalk = false;
					firstJump = true;
					currentIndex = 1;
				}
				startIndex = 1;
				endIndex = 8;
			}

			if (this.animation.equals(IDLE_ANIMATION)) {
				currentIndex = 0;
				startIndex = 0;
				endIndex = 0;
				firstJump = true;
				firstWalk = true;
				firstFall = true;
				this.setImage(rightFrames[currentIndex]);
			} else if (this.animation.equals(WALK_ANIMATION) || this.animation.equals(JUMP_ANIMATION)) {
				if (frameCounter % speed == 0) {
					if (currentIndex == endIndex) {
						currentIndex = startIndex;
						this.setImage(rightFrames[currentIndex]);
					} else {
						currentIndex++;
						this.setImage(rightFrames[currentIndex]);
					}
				}
			}
		} else {
			if (this.animation.equals(JUMP_ANIMATION)) {
				if (firstJump) {
					firstJump = false;
					firstWalk = true;
					currentIndex = 9;
				}
				startIndex = 9;
				endIndex = 11;
			}

			if (this.animation.equals(FALLING_ANIMATION)) {
				this.setImage(leftFrames[12]);
			}

			if (this.animation.equals(LANDING_ANIMATION)) {
				this.setImage(leftFrames[13]);
			}

			if (this.animation.equals(WALK_ANIMATION)) {
				if (firstWalk) {
					firstWalk = false;
					firstJump = true;
					currentIndex = 1;
				}
				startIndex = 1;
				endIndex = 8;
			}

			if (this.animation.equals(IDLE_ANIMATION)) {
				currentIndex = 0;
				startIndex = 0;
				endIndex = 0;
				firstJump = true;
				firstWalk = true;
				firstFall = true;
				this.setImage(leftFrames[currentIndex]);
			} else if (this.animation.equals(WALK_ANIMATION) || this.animation.equals(JUMP_ANIMATION)) {
				if (frameCounter % speed == 0) {
					if (currentIndex == endIndex) {
						currentIndex = startIndex;
						this.setImage(leftFrames[currentIndex]);
					} else {
						currentIndex++;
						this.setImage(leftFrames[currentIndex]);
					}
				}
			}
		}
	}

}
