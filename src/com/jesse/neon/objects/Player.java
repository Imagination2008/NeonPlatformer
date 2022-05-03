package com.jesse.neon.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.jesse.neon.framework.GameObject;
import com.jesse.neon.framework.ObjectId;
import com.jesse.neon.framework.Texture;
import com.jesse.neon.window.Animation;
import com.jesse.neon.window.Camera;
import com.jesse.neon.window.Game;
import com.jesse.neon.window.Handler;

public class Player extends GameObject {
	
	private float width = 48, height = 96;
	
	private float gravity = 0.5f;
	private final float MAX_SPEED = 10;
	//1 = right
	//-1 = left
	
	private Handler handler;
	private Camera cam;
	
	Texture tex = Game.getInstance();
	
	private Animation playerWalk, playerWalkLeft;

	public Player(float x, float y, Handler handler, Camera cam, ObjectId id) {
		super(x, y, id);
		this.handler = handler;
		this.cam = cam;
		
		playerWalk = new Animation(6, tex.player[1], tex.player[2], tex.player[3], tex.player[4], tex.player[5], tex.player[6]);
		playerWalkLeft = new Animation(6, tex.player[8], tex.player[9], tex.player[10], tex.player[11], tex.player[12], tex.player[13]);
	}
	public void tick(LinkedList<GameObject> object) {
		x += velX;
		y += velY;
		
		if(velX < 0) facing = -1;
		else if(velX > 0) facing = 1;
		
		if (falling || jumping) {
			velY += gravity;
			
			if (velY > MAX_SPEED) 
				velY = MAX_SPEED;
		}
		Collision(object);
		
		playerWalk.runAnimation();
		playerWalkLeft.runAnimation();
	}
	
	private void Collision(LinkedList<GameObject> object) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.Block) {
				if (getBoundsTop().intersects(tempObject.getBounds())) {
					y = tempObject.getY() + 35;
					velY = 0;
				}
				
				if (getBounds().intersects(tempObject.getBounds())) {
					y = tempObject.getY() - height;
					velY = 0;
					falling = false;
					jumping = false;
				} else
					falling = true;
				
				if (getBoundsRight().intersects(tempObject.getBounds())) {
					y = tempObject.getX() - width;
				}
				
				if (getBoundsLeft().intersects(tempObject.getBounds())) {
					y = tempObject.getX() + 35;
				}
			}else if(tempObject.getId() == ObjectId.Flag) {
				//switch level
				if(getBounds().intersects(tempObject.getBounds()))
				handler.switchLevel();
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.blue);
		if(jumping) {
			if(facing == 1)
				g.drawImage(tex.player_jump[2], (int)x, (int)y, 48, 96, null);
			else if(facing == -1)
				g.drawImage(tex.player_jump[3], (int)x, (int)y, 48, 96, null);
		}else {
			if(velX != 0) {
				if(facing == 1)
					playerWalk.drawAnimation(g, (int)x, (int)y, 48, 96);
				else
					playerWalkLeft.drawAnimation(g, (int)x, (int)y, 48, 96);
			} else {
				if(facing == 1)
					g.drawImage(tex.player[0], (int)x, (int)y, 48, 96, null);
				else if(facing == -1)
					g.drawImage(tex.player[8], (int)x, (int)y, 48, 96, null);
			}
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) ((int)x+(width/2)-((width/2)/2)), (int) ((int)y+(height/2)), (int)width/2, (int)height/2);
	}
	public Rectangle getBoundsTop() {
		return new Rectangle((int) ((int)x+(width/2)-((width/2)/2)), (int)y, (int)width/2, (int)height/2);
	}
	public Rectangle getBoundsRight() {
		return new Rectangle((int) ((int)x+width-5), (int)y+5, (int)5, (int)height-10);
	}
	public Rectangle getBoundsLeft() {
		return new Rectangle((int)x, (int)y+5, (int)5, (int)height-10);
	}
}
