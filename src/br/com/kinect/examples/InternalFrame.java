package br.com.kinect.examples;

/**
 This program it's a simple example the how incorporate Kinect in a Java project, just add this JInternalFrame in other JFrame.
 */
import javax.swing.JInternalFrame;

/**
 * 
 * @author Heitor Guerra Carneiro.
 * @version 1.0
 * @since February 2015.
 */
public class InternalFrame extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private CreateButtons kinect;

	public InternalFrame() {
		kinect = new CreateButtons();
		getContentPane().add(kinect);
		kinect.init();
		pack();
		setVisible(true);
	}
}
