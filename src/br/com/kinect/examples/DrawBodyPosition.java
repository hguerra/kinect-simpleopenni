package br.com.kinect.examples;
import SimpleOpenNI.SimpleOpenNI;
/**
 * This program is for draw skeleton user and mark some parts of the body.
 */
import processing.core.PApplet;
import processing.core.PVector;

/**
 * 
 * @author Heitor Guerra Carneiro.
 * @version 1.0
 * @since February 2015.
 */
public class DrawBodyPosition extends PApplet {
	private static final long serialVersionUID = 1L;
	/**
	 * Create a new SimpleOpenNi, for communicate with the Kinect device.
	 */
	SimpleOpenNI kinect;

	public void setup() {
		// Size of window application
		size(640, 480);

		// Initialize the SimpleOpenNi variable.
		kinect = new SimpleOpenNI(this);

		// Initialize and start depth sensor's data
		kinect.enableDepth();

		// Initialize and start receiving User data
		kinect.enableUser();

		// Flips the sensor's data horizontally
		// Enable mirroring
		kinect.setMirror(true);
	}

	public void draw() {
		// Set background.
		background(0);
		// Update the camera
		kinect.update();

		int[] users = kinect.getUsers();

		ellipseMode(CENTER);

		// iterate through users
		for (int i = 0; i < users.length; i++) {
			int uid = users[i];

			// You can play with the skeleton colors!
			// Skeleton stroke
			strokeWeight(2);
			// Skeleton color
			stroke(255, 255, 255);
			drawSkeleton(uid);

			// Draw center of mass of the user
			PVector realCoM = new PVector();
			kinect.getCoM(uid, realCoM);
			PVector projCoM = new PVector();

			// Convert realworld coordinates to projective
			kinect.convertRealWorldToProjective(realCoM, projCoM);
			// Set to red color.
			fill(255, 0, 0);
			ellipse(projCoM.x, projCoM.y, 10, 10);

			// check if user has a skeleton
			if (kinect.isTrackingSkeleton(uid)) {

				// draw head
				PVector realHead = new PVector();

				kinect.getJointPositionSkeleton(uid, SimpleOpenNI.SKEL_HEAD,
						realHead);
				PVector projHead = new PVector();
				kinect.convertRealWorldToProjective(realHead, projHead);
				fill(0, 153, 153);
				ellipse(projHead.x, projHead.y, 50, 50);

				// draw left hand
				PVector realLHand = new PVector();
				kinect.getJointPositionSkeleton(uid,
						SimpleOpenNI.SKEL_LEFT_HAND, realLHand);
				PVector projLHand = new PVector();
				kinect.convertRealWorldToProjective(realLHand, projLHand);
				fill(255, 255, 0);
				ellipse(projLHand.x, projLHand.y, 30, 30);
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

	// is called everytime a new user appears, SimpleOpenNI events.
	public void onNewUser(SimpleOpenNI curContext, int userId) {
		println("onNewUser - userId: " + userId);
		curContext.startTrackingSkeleton(userId);
	}

	// is called everytime a user disappears
	public void onLostUser(SimpleOpenNI curContext, int userId) {
		println("onLostUser - userId: " + userId);

	}
	// END
}