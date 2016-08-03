package UVCUI;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.bytedeco.javacv.FrameGrabber;

import UVCInputStream.UVCStreamTask;

public class UVCJFrame extends JFrame {

	private static final long serialVersionUID = -6026250728018827459L;
	
	private JButton startcontrol = new JButton("Start");
	private JButton stopcontrol = new JButton("Stop");
	private JPanel canvasvideo = new JPanel();
	private JPanel canvasimage = new JPanel();
	
	private UVCStreamTask uvcst = null;
	
	/**
	 * 
	 */
	public UVCJFrame() {
		setLayout(null);
		setSize(1000, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setTitle("Camera JFrame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvasvideo.setBounds(10, 0, 480, 400);
		getContentPane().add(canvasvideo);
		
		canvasimage.setBounds(510, 0, 480, 400);
		getContentPane().add(canvasimage);
		
		startcontrol.setBounds(320, 415, 80, 40);
		getContentPane().add(startcontrol);
		
		stopcontrol.setBounds(620, 415, 80, 40);
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
		uvcst = new UVCStreamTask(canvasvideo, canvasimage);
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