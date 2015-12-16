package br.com.kinect.examples;

import processing.core.*;
import SimpleOpenNI.*;
/**
 * 
 * @author Heitor Guerra Carneiro.
 * @version 1.0
 * @since February 2015.
 */
public class DepthMapAndCamera extends PApplet{
	private static final long serialVersionUID = 1L;
	SimpleOpenNI kinect;
	
	public void setup(){
		
		size(640*2, 480);
		
		kinect = new SimpleOpenNI(this);
		kinect.enableDepth();
		kinect.enableRGB();
	}
	public void draw(){
		kinect.update();
		image(kinect.depthImage(), 0, 0);
		image(kinect.rgbImage(), 640, 0);
	}
}

