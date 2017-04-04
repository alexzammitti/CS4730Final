package edu.virginia.engine.display;

import edu.virginia.engine.event.Event;
import edu.virginia.engine.event.EventDispatcher;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import edu.virginia.engine.event.*;


import javax.imageio.ImageIO;

/**
 * A very basic display object for a java based gaming engine
 * 
 * */
public class DisplayObject extends EventDispatcher {

	// ALL FIELDS ------------------------------------------------------------------------------------------------------
	private String id;
	private BufferedImage displayImage;
	protected boolean visible = true;
	protected int xPosition = 0;
	protected int yPosition = 0;
	protected int xPivot = 0;
	protected int yPivot = 0;
	protected double xScale = 1.0;
	protected double yScale = 1.0;
	protected double rotation = 0;
	protected float alpha = 1;
	protected int xRef;
	protected int yRef;
	protected DisplayObjectContainer parent;
	protected Rectangle hitbox = new Rectangle(0, 0, 0, 0);
    public boolean isPlaced = false;
    protected String fileName = "";
	public boolean dangerous = false;


    // ALL METHODS -----------------------------------------------------------------------------------------------------

	// getters and setters:
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
    public String getFileName(){return fileName;}
	public void setFileName(String fileName) {this.fileName = fileName;}
	public DisplayObjectContainer getParent() {
		return parent;
	}
	public void setParent(DisplayObjectContainer parent) {
		this.parent = parent;
	}
	public int getxRef() {
		return xRef;
	}
	public void setxRef(int xRef) {
		this.xRef = xRef;
	}
	public int getyRef() {
		return yRef;
	}
	public void setyRef(int yRef) {
		this.yRef = yRef;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public int getxPosition() {
		return xPosition;
	}
	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	public int getyPosition() {
		return yPosition;
	}
	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	public int getxPivot() {
		return xPivot;
	}
	public void setxPivot(int xPivot) {
		this.xPivot = xPivot;
	}
	public int getyPivot() {
		return yPivot;
	}
	public void setyPivot(int yPivot) {
		this.yPivot = yPivot;
	}
	public double getxScale() {
		return xScale;
	}
	public void setxScale(double xScale) {
		this.xScale = xScale;
	}
	public double getyScale() {
		return yScale;
	}
	public void setyScale(double yScale) {
		this.yScale = yScale;
	}
	public double getRotation() {
		return rotation;
	}
	public void setRotation(double radians) {
		this.rotation = radians;
	}
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	// Multiple argument setters:
	public void setPosition(int x, int y) {this.xPosition = x; this.yPosition = y;}
	public void setPivotPoint(int x, int y) {this.xPivot = x; this.yPivot = y; }
	public void setScale(double x, double y) {this.xScale = x; this.yScale = y;}


	// POSITIONING METHODS ---------------------------------------------------------------------------------------------

	// alignment
	public void alignCenterVertical(DisplayObject parent) {
		int y = parent.getScaledHeight()/2 - this.getScaledHeight()/2;
		this.setyPosition(y);
	}
	public void alignCenterHorizontal(DisplayObject parent) {
		int x = parent.getScaledWidth()/2 - this.getScaledWidth()/2;
		this.setxPosition(x);
	}
	public void alignFractionVertical(DisplayObject parent, int fraction, int position) {
		int y = position*parent.getScaledHeight()/fraction - this.getScaledHeight()/2;
		this.setyPosition(y);
	}
	public void alignFractionHorizontal(DisplayObject parent, int fraction, int position) {
		int x = position*parent.getScaledWidth()/fraction - this.getScaledWidth()/2;
		this.setxPosition(x);
	}
	public void setPivotCenter() {
        this.setxPivot(this.getScaledWidth()/2);
        this.setyPivot(this.getScaledHeight()/2);
    }

	// Absolute positioning
	public int getxAbsolutePosition() {
		if(this.parent != null) {
			return (this.parent.getxAbsolutePosition()+(int)(this.getxPosition()*this.parent.xScale));
		}
		return this.getxPosition();
	}
	public int getyAbsolutePosition() {
		if(this.parent != null) {
			return this.parent.getyAbsolutePosition()+(int)(this.getyPosition()*this.parent.yScale);
		}
		return this.getyPosition();
	}
    public double getxAbsoluteScale() {
        if(this.parent != null) {
            return (this.parent.getxAbsoluteScale()*this.getxScale());
        }
        return this.getxScale();
    }
    public double getyAbsoluteScale() {
        if(this.parent != null) {
            return (this.parent.getyAbsoluteScale()*this.getyScale());
        }
        return this.getyScale();
    }


	// HITBOXES & BOUNDS CHECKING --------------------------------------------------------------------------------------

	public boolean collidesWith(DisplayObject object) {
		if(object != null) {
			if (object.getHitbox().intersects(this.hitbox)) {
				//event
				this.dispatchEvent(new Event(Event.COLLISION, this));
				object.dispatchEvent(new Event(Event.COLLISION, object));
				return true;
			}
		}
		return false;
	}
	public Rectangle getHitbox() { return hitbox; }
	public void setHitbox(Rectangle r) {this.hitbox = r;}
	public int getBottom() {return this.getHitbox().y+this.getHitbox().height;}
	public int getTop() {return this.getHitbox().y;}
	public int getLeft() {return this.getHitbox().x;}
	public int getRight() {return this.getHitbox().x+this.getHitbox().width;}

	public int getScaledWidth() {
		if(displayImage == null) return 0;
		int answer = (int) (this.getxAbsoluteScale()*this.getUnscaledWidth());
		return answer;
	}

	public int getScaledHeight() {
		if(displayImage == null) return 0;
		int answer = (int) (this.getyAbsoluteScale()*this.getUnscaledHeight());
		return answer;
	}

	public int getUnscaledWidth() {
		if(displayImage == null) {
			return 0;
		}
		return displayImage.getWidth();
	}

	public int getUnscaledHeight() {
		if(displayImage == null) return 0;
		return displayImage.getHeight();
	}


	// IMAGES & FILENAMES ----------------------------------------------------------------------------------------------

	public BufferedImage getDisplayImage() {
		return this.displayImage;
	}

	public void setImage(String imageName) {
		if (imageName == null) {
			return;
		}
		displayImage = readImage(imageName);
		if (displayImage == null) {
			System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
		} else {
			this.fileName = imageName;
		}
	}
	/**
	 * Helper function that simply reads an image from the given image name
	 * (looks in resources\\) and returns the bufferedimage for that filename
	 * */
	public BufferedImage readImage(String imageName) {
		BufferedImage image = null;
		try {
			String file = ("resources" + File.separator + imageName);
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		return image;
	}

	public void setImage(BufferedImage image) {
		if(image == null) return;
		displayImage = image;
	}

	public void setImageNormal() {
		String normalImageFileName = this.getFileName();
		normalImageFileName = normalImageFileName.substring(0,normalImageFileName.indexOf("-"));	// IF YOUR FILENAME HAS ANOTHER DASH THIS WILL GET CONFUSED
		normalImageFileName += ".png";
		this.setImage(normalImageFileName);
	}
	public void setImageError() {
		if(!this.getFileName().contains("-error")) {
			String errorImageFileName = this.getFileName().substring(0, this.getFileName().length() - 4);
			errorImageFileName += "-error.png";
			this.setImage(errorImageFileName);
		}
	}


	// CONSTRUCTORS ----------------------------------------------------------------------------------------------------

	/**
	 * Constructors: can pass in the id OR the id and image's file path and
	 * position OR the id and a buffered image and position
	 */
	public DisplayObject(String id) {
		this.setId(id);
	}

	public DisplayObject(String id, String fileName) {
		this.setId(id);
		this.setImage(fileName);
		this.fileName = fileName;
	}
	
	public DisplayObject(String id, String fileName, DisplayObjectContainer dad) {
		this.setId(id);
		this.setImage(fileName);
		this.fileName = fileName;
		this.parent = dad;
	}


	// UPDATE ----------------------------------------------------------------------------------------------------------

	/**
	 * Invoked on every frame before drawing. Used to update this display
	 * objects state before the draw occurs. Should be overridden if necessary
	 * to update objects appropriately.
	 * */
	protected void update(ArrayList<Integer> pressedKeys) {
		if(this.rotation%(Math.PI) < 1){
			hitbox.setBounds(this.getxAbsolutePosition(), this.getyAbsolutePosition(), this.getScaledWidth(), this.getScaledHeight());
		} else if(this.rotation%Math.PI/2 < 1) {
			hitbox.setBounds(this.getxAbsolutePosition()+this.getScaledWidth()/2-this.getScaledHeight()/2,
					this.getyAbsolutePosition()-this.getScaledWidth()/2+this.getScaledHeight()/2,
					this.getScaledHeight(), this.getScaledWidth());
		}
	}


	// DRAWING METHODS -------------------------------------------------------------------------------------------------

	/**
	 * Draws this image. This should be overloaded if a display object should
	 * draw to the screen differently. This method is automatically invoked on
	 * every frame.
	 * */
	public void draw(Graphics g) {
		
		if (displayImage != null && this.visible) {
			/*
			 * Get the graphics and apply this objects transformations
			 * (rotation, etc.)
			 */
			//System.out.println(displayImage);
			Graphics2D g2d = (Graphics2D) g;
			applyTransformations(g2d);

			//System.out.println(displayImage.getWidth(null) + " " + displayImage.getHeight(null));

			/* Actually draw the image, perform the pivot point translation here */
			
			//System.out.println("Calling getUnscaledWidth before the drawimage");
			
			g2d.drawImage(displayImage, 0, 0, (int) getUnscaledWidth(), (int) getUnscaledHeight(), null);
			
			/*
			 * undo the transformations so this doesn't affect other display
			 * objects
			 */
			reverseTransformations(g2d);
		}
	}

	/**
	 * Applies transformations for this display object to the given graphics
	 * object
	 * */
	protected void applyTransformations(Graphics2D g2d) {
		//System.out.print("x position = " + this.getxPosition() + ", y position = " + this.getyPosition() + " ");
		//System.out.println("x absolute position = " + this.getxAbsolutePosition() + ", y absolute position = " + this.getyAbsolutePosition() + "  Drawing " + this.getId());

		g2d.translate(this.xPosition, this.yPosition);
		g2d.rotate(this.rotation, this.xPivot, this.yPivot);
		g2d.scale(this.xScale, this.yScale);
		g2d.setComposite(makeComposite(this.alpha));
	}

	/**
	 * Reverses transformations for this display object to the given graphics
	 * object
	 * */
	protected void reverseTransformations(Graphics2D g2d) {
		g2d.setComposite(makeComposite(1));
		g2d.scale(1/this.xScale,1/this.yScale);
		g2d.rotate(-this.rotation, this.xPivot, this.yPivot);
		g2d.translate(-this.xPosition, -this.yPosition);
	}

	public AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}

}