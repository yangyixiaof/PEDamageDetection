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
	
	private JButton control = new JButton("Start");
	private JPanel canvas = new JPanel();
	private JPanel bigimageshow = new JPanel();
	private JPanel thumbnails = new JPanel();
	
	private ArrayList<JLabel> labels = new ArrayList<JLabel>();
	
	private UVCStreamTask uvcst = null;
	
	/**
	 * 
	 */
	public UVCJFrame() {
		setSize(800, 450);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setTitle("Camera JFrame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvas.setBounds(0, 0, 480, 400);
		getContentPane().add(canvas);
		
		bigimageshow.setBounds(500, 0, 300, 250);
		getContentPane().add(bigimageshow);
		
		thumbnails.setBounds(500, 295, 300, 100);
		getContentPane().add(thumbnails);
		
		control.setBounds(430, 780, 20, 20);
		getContentPane().add(control);
		
		for (int i=0;i<5;i++)
		{
			JLabel jl = new JLabel();
			jl.setBounds(i*55, 25, 54, 45);
			jl.setHorizontalAlignment(0);
			// jl.setIcon(icon);
			thumbnails.add(jl);
			labels.add(jl);
		}
		
		control.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					ControlActionPerformed(evt);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		// pack();
	}
	
	private void ControlActionPerformed(ActionEvent evt)
			throws Exception, FrameGrabber.Exception, InterruptedException {
		if (control.getText().equals("Stop")) {
			uvcst.Stop();
			control.setText("Start");
		} else {
			control.setText("Stop");
			uvcst = new UVCStreamTask(canvas, bigimageshow, labels);
			Thread uvctd = new Thread(uvcst);
			uvctd.start();
		}
	}
	
	public static void main(String[] args) {
		new UVCJFrame();
	}
	
}