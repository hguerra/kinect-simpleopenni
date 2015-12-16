package br.com.kinect.examples;
/**
This program it's simple example about how create a Next button on screen.
*/
import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.*;

/**
* 
* @author Heitor Guerra Carneiro.
* @version 1.0
* @since February 2015.
*/
public class CreateNextButton extends PApplet {
	private static final long serialVersionUID = 1L;
	private double rightX = 0;
	private double rightY = 0;
	private double leftX = 0;
	private double leftY = 0;
	private double headX;
	

	// -------------------- Kinect init -------------------------
	SimpleOpenNI kinect;
	
	
	

	public void setup() {

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
	}

	// ------------------------------------------------------

	public void draw() {

		background(0);

		// Update the camera
		kinect.update();

		// Static objects on kinect screen

		// ---------------------------------------------------------

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
				//Update in real time with the reference of headX
				createNextButton(200, 50, "LEFT");
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
		
		headX = jointPosH_Proj.x;
	}
	public void buttonEllipse(float w, float h, int size, int option) {
		switch (option) {
		case 0:
			// Normal
			noStroke();
			fill(255, 0, 0);
			ellipse(w, h, size, size);
			stroke(255, 255, 0, 80);
			noFill();
			ellipse(w, h, size + 10, size + 10);
			break;
		case 1:
			// Shiny
			noStroke();
			fill(255, 255, 0);
			ellipse(w, h, size, size);
			stroke(255, 255, 0);
			noFill();
			ellipse(w, h, size + 10, size + 10);
		}
	}
	public void createNextButton(int height, int size, String side) {
		/**
		 * The Height is variable. Choice some point Y in parts of body.
		 * Exemple: headY, leftShoulderY, rightShoulderY, centerMassY.
		 */
		int width;
		
		if (side == "LEFT")
			width = (int) (headX - 100);
		else
			width = (int) (headX + 100);
		
		buttonEllipse(width, height, size, 0);
		
		if(compareHandPosition(width, height, side)){
			buttonEllipse(width, height, size, 1);
		}
	}

	public boolean compareHandPosition(int width, int height, String side) {
		if (side == "LEFT") {
			if (leftX < width + 10 && leftX > width - 10
					&& leftY < height + 10 && leftY > height - 10)
				return true;
		} else if (side == "RIGHT") {
			if (rightX < width + 10 && rightX > width - 10
					&& rightY < height + 10 && rightY > height - 10)
				return true;
		} else {
			if (leftX < width + 10 && leftX > width - 10
					&& leftY < height + 10 && leftY > height - 10
					&& rightX < width + 10 && rightX > width - 10
					&& rightY < height + 10 && rightY > height - 10)
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