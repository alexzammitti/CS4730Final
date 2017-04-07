package edu.virginia.engine.display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class DisplayObjectContainer extends DisplayObject {
	
	protected ArrayList<DisplayObjectContainer> children = new ArrayList<DisplayObjectContainer>(0);
	public ArrayList<DisplayObjectContainer> allChildren = new ArrayList<>();
	protected boolean visible = true;

	public DisplayObjectContainer(String id) {
		super(id);
	}
	
	public DisplayObjectContainer(String id, String filename) {
		super(id, filename);
	}
	
	public boolean isDOCVisible() {
		return visible;
	}
	
	public void setDOCVisibility(boolean visibility) {
		this.visible = visibility;
	}
	
	public void addChild(DisplayObjectContainer child) {
		children.add(child);
		child.setParent(this);
	}
	
	public void addChildAtIndex(DisplayObjectContainer child, int index) {
		children.add(index, child);
		child.setParent(this);
	}
	
	public boolean contains(DisplayObjectContainer child) {
		return children.contains(child);
	}
	
	public void removeChild(DisplayObjectContainer child) {
		if(children.contains(child)) {
			children.remove(child);
		}
	}
	
	public void removeByIndex(int index) {
		children.remove(index);
	}
	
	public void removeAll() {
		children.clear();
	}
	
	public DisplayObjectContainer getById(String id) {
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i).getId().equals(id)) {
				return children.get(i);
			}
		}
		return null;
	}

	public DisplayObjectContainer getByIndex(int index) {
		return children.get(index);
	}

	public int getNumberOfChildren() {
		return children.size();
	}
	
	public ArrayList<DisplayObjectContainer> getChildren() { return children; }

	public ArrayList<DisplayObjectContainer> getAllChildren() {
		if (children.size() == 0) {
			return children;
		} else {
			allChildren.addAll(children);
			for (DisplayObjectContainer c : children) {
				allChildren.addAll(c.getAllChildren());
			}
			return allChildren;
		}
	}




	public DisplayObjectContainer getLastChild() {
		return this.getByIndex(this.getNumberOfChildren()-1);	// we sometimes want the most recently added child
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);
		for(DisplayObjectContainer c : children) {
			c.update(pressedKeys);								// each call to update should affect the whole display tree
		}
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		Graphics2D g2d = (Graphics2D) g;
		applyTransformations(g2d);
		if(children.size() > 0) {
			//System.out.println("should draw children");
			for(int i = 0; i < children.size(); i++) {
				children.get(i).draw(g);
			}
		}
		reverseTransformations(g2d);
	}
	
}