package br.com.kinect.examples;

/**
 This program it's exemple how control the mouse. 
 */

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * 
 * @author Heitor Guerra Carneiro.
 * @version 1.0
 * @since February 2015.
 */
public class MouseControl extends PApplet {
	private static final long serialVersionUID = 1L;
	// Mouse

	private Robot robot;
	private int xx = 0, yy = 0;
	private int stageWidth = 1366;
	private int stageHeight = 768;
	private int prW, prH;
	private int stageScale = 5 / 3;

	private double rightX = 0;
	private double rightY = 0;
	private double leftX = 0;
	private double leftY = 0;

	// -------------------- Kinect init -------------------------
	SimpleOpenNI kinect;

	public void setup() {

		// Using this for take the screen size.
		prW = stageWidth / width;
		prH = stageHeight / height;

		kinect = new SimpleOpenNI(this);
		if (kinect.isInit() == false) {
			println("Sorry! The system cannot initialize SimpleOpenNi, check if the kinect is connected or your drivers were installed!");
			exit();
		}

		// enable depthMap generation
		kinect.enableDepth();

		// enable skeleton generation for all joints
		kinect.enableUser();
		// Size defined automatic
		size(kinect.depthWidth(), kinect.depthHeight());

		// set Font for write on screen
		textFont(createFont("Arial", 36));

		// Create the object robot
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------

	public void draw() {

		background(0);

		// Update the camera
		kinect.update();

		// Static objects on kinect screen

		buttonEllipse(50, 100, 60, 0, "Left");
		buttonEllipse(50, 200, 60, 0, "Right");

		int[] userList = kinect.getUsers();
		for (int i = 0; i < userList.length; i++) {
			if (kinect.isTrackingSkeleton(userList[i])) {

				// Objects that update in real time

				PVector myRHand = new PVector();
				kinect.getJointPositionSkeleton(userList[i],
						SimpleOpenNI.SKEL_RIGHT_HAND, myRHand);
				PVector myRHand_Proj = new PVector();
				kinect.convertRealWorldToProjective(myRHand, myRHand_Proj);

				PVector myLHand = new PVector();
				kinect.getJointPositionSkeleton(userList[i],
						SimpleOpenNI.SKEL_LEFT_HAND, myLHand);
				PVector myLHand_Proj = new PVector();
				kinect.convertRealWorldToProjective(myLHand, myLHand_Proj);

				// You can play with the skeleton colors!

				// Skeleton stroke
				strokeWeight(2);

				// Skeleton color
				stroke(255, 255, 255);

				drawSkeleton(userList[i]);
				drawHead(userList[i]);

				stroke(255, 255, 255);
				strokeWeight(2);
				fill(255, 0, 0);
				ellipse(myRHand_Proj.x, myRHand_Proj.y, 50, 50);
				ellipse(myLHand_Proj.x, myLHand_Proj.y, 50, 50);
				stroke(255, 255, 255);
				strokeWeight(2);
				fill(0);
				ellipse(myRHand_Proj.x, myRHand_Proj.y, 25, 25);
				ellipse(myLHand_Proj.x, myLHand_Proj.y, 25, 25);

				leftX = myLHand_Proj.x;
				leftY = myLHand_Proj.y;

				rightX = myRHand_Proj.x;
				rightY = myRHand_Proj.y;

				// Compare hand position with the button's position
				control(50, 100, 50, 200);
			}
		}
	}

	// draw the skeleton with the selected joints
	public void drawSkeleton(int userId) {

		kinect.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

		kinect.drawLimb(userId, SimpleOpenNI.SKEL_NECK,
				SimpleOpenNI.SKEL_LEFT_SHOULDER);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,
				SimpleOpenNI.SKEL_LEFT_ELBOW);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW,
				SimpleOpenNI.SKEL_LEFT_HAND);

		kinect.drawLimb(userId, SimpleOpenNI.SKEL_NECK,
				SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,
				SimpleOpenNI.SKEL_RIGHT_ELBOW);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW,
				SimpleOpenNI.SKEL_RIGHT_HAND);

		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,
				SimpleOpenNI.SKEL_TORSO);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,
				SimpleOpenNI.SKEL_TORSO);

		kinect.drawLimb(userId, SimpleOpenNI.SKEL_TORSO,
				SimpleOpenNI.SKEL_LEFT_HIP);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP,
				SimpleOpenNI.SKEL_LEFT_KNEE);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE,
				SimpleOpenNI.SKEL_LEFT_FOOT);

		kinect.drawLimb(userId, SimpleOpenNI.SKEL_TORSO,
				SimpleOpenNI.SKEL_RIGHT_HIP);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP,
				SimpleOpenNI.SKEL_RIGHT_KNEE);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE,
				SimpleOpenNI.SKEL_RIGHT_FOOT);
	}

	// Draw the Head
	public void drawHead(int userId) {
		PVector jointPosH = new PVector();

		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_HEAD,
				jointPosH);
		PVector jointPosH_Proj = new PVector();
		kinect.convertRealWorldToProjective(jointPosH, jointPosH_Proj);
		fill(255, 255, 255);
		stroke(255, 255, 255);
		ellipse(jointPosH_Proj.x, jointPosH_Proj.y, 50, 50);
	}

	public void titleString(String menssage, int sizeFont, float positionX,
			float positionY) {
		textSize(sizeFont);
		fill(255, 255, 255);
		text(menssage, positionX, positionY);
	}

	public void buttonEllipse(float w, float h, int size, int option,
			String menssage) {
		switch (option) {
		case 0:
			// Normal
			fill(0, 153, 153);
			ellipse(w, h, size, size);
			titleString(menssage, (int) (size / 3), w - (size / 2) + 5, h);
			break;
		case 1:
			// Shiny
			fill(0, 255, 0);
			ellipse(w, h, size, size);
		}
	}

	public void control(int leftButtonX, int leftButtonY, int rightButtonX,
			int rightButtonY) {
		xx = (((int) (rightX) * stageScale) * prW);
		yy = (((int) (rightY) * stageScale) * prH);

		robot.mouseMove(xx, yy);

		if (compareHandPosition(leftButtonX, leftButtonY, "LEFT")) {
			fill(0, 255, 0);
			ellipse(50, 100, 60, 60);
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

		}
		if (compareHandPosition(rightButtonX, rightButtonY, "LEFT")) {
			fill(0, 255, 0);
			ellipse(50, 200, 60, 60);
			robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		}
	}

	public boolean compareHandPosition(int width, int height, String side) {
		if (side == "LEFT") {
			if (leftX < width + 10 && leftX > width - 10 && leftY < height + 10
					&& leftY > height - 10)
				return true;
		} else if (side == "RIGHT") {
			if (rightX < width + 10 && rightX > width - 10
					&& rightY < height + 10 && rightY > height - 10)
				return true;
		} else {
			if (leftX < width + 10 && leftX > width - 10 && leftY < height + 10
					&& leftY > height - 10 && rightX < width + 10
					&& rightX > width - 10 && rightY < height + 10
					&& rightY > height - 10)
				return true;
		}
		return false;
	}

	// -----------------------------------------------------------------
	// SimpleOpenNI events

	public void onNewUser(SimpleOpenNI curContext, int userId) {
		println("onNewUser - userId: " + userId);
		println("\tstart tracking skeleton");

		kinect.startTrackingSkeleton(userId);
	}

	public void onLostUser(SimpleOpenNI curContext, int userId) {
		println("onLostUser - userId: " + userId);
	}

	public void onVisibleUser(SimpleOpenNI curContext, int userId) {
		println("onVisibleUser - userId: " + userId);
	}
	// -----------------------------------------------------------------
	// END
}