package UVCUI;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bytedeco.javacv.FrameGrabber;

import UVCInputStream.UVCStreamTask;

public class UVCJFrame extends JFrame {

	private static final long serialVersionUID = -6026250728018827459L;
	
	private JButton startcontrol = new JButton("Start");
	private JButton stopcontrol = new JButton("Stop");
	private JPanel canvasvideo = new JPanel();
	private JPanel canvasimage = new JPanel();
	
	private ArrayList<JLabel> labels = new ArrayList<JLabel>();
	
	private UVCStreamTask uvcst = null;
	
	/**
	 * 
	 */
	public UVCJFrame() {
		setLayout(null);
		setSize(1000, 450);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setTitle("Camera JFrame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvasvideo.setBounds(10, 0, 480, 400);
		getContentPane().add(canvasvideo);
		
		canvasimage.setBounds(500, 0, 480, 400);
		getContentPane().add(canvasimage);
		
		startcontrol.setBounds(350, 415, 50, 20);
		getContentPane().add(startcontrol);
		
		stopcontrol.setBounds(650, 415, 50, 20);
		getContentPane().add(stopcontrol);
		
		startcontrol.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					StartControlActionPerformed(evt);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		stopcontrol.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				StopControlActionPerformed(evt);
			}
		});
	}
	
	private void StartControlActionPerformed(ActionEvent evt)
			throws Exception, FrameGrabber.Exception, InterruptedException {
		uvcst = new UVCStreamTask(canvasvideo, canvasimage, labels);
		Thread uvctd = new Thread(uvcst);
		uvctd.start();
	}
	
	private void StopControlActionPerformed(ActionEvent evt) {
		uvcst.Stop();
	}
	
	public static void main(String[] args) {
		new UVCJFrame();
	}
	
}