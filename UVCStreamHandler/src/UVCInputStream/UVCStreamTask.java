package UVCInputStream;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class UVCStreamTask implements Runnable {

	private JPanel canvas = null;
	private JPanel bigimageshow = null;
	
	private long lasttimestamp = System.currentTimeMillis();
	
	private boolean run = false;
	
	public UVCStreamTask(JPanel canvas, JPanel bigimageshow) {
		this.canvas = canvas;
		this.bigimageshow = bigimageshow;
	}
	
	@Override
	public void run() {
		run = true;
		try {
			OpenCVFrameGrabber grabber = InitialGrabber();
			// FFmpegFrameRecorder recorder = InitialRecorder();

			Frame capturedFrame = null;
			Java2DFrameConverter paintConverter = new Java2DFrameConverter();
			while ((capturedFrame = grabber.grab()) != null && run) {
				
				// System.err.println("testing.");
				
				BufferedImage buff = paintConverter.getBufferedImage(capturedFrame, 1);
				Graphics g = canvas.getGraphics();
				g.drawImage(buff, 0, 0, UVCStreamMetaInfo.CAPTUREWIDTH, UVCStreamMetaInfo.CAPTUREHRIGHT, 0, 0,
						buff.getWidth(), buff.getHeight(), null);
				// recorder.record(capturedFrame);
				
				if (System.currentTimeMillis() > lasttimestamp + 5000)
				{
					lasttimestamp = System.currentTimeMillis();
					Graphics bisg = bigimageshow.getGraphics();
					bisg.drawImage(buff, 0, 0, UVCStreamMetaInfo.CAPTUREWIDTH, UVCStreamMetaInfo.CAPTUREHRIGHT, 0, 0, buff.getWidth(), buff.getHeight(), null);
				}
			}
			
			// recorder.stop();
			grabber.stop();
		} catch (FrameGrabber.Exception ex) {
			Logger.getLogger(UVCStreamTask.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(UVCStreamTask.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void Stop()
	{
		run = false;
	}
	
	/*
	 * public void GrabFrames() { OpenCVFrameGrabber grabber = new
	 * OpenCVFrameGrabber(0); grabber.start(); IplImage grabbedImage =
	 * grabber.grab();
	 * 
	 * CanvasFrame canvasFrame = new CanvasFrame("Cam");
	 * canvasFrame.setCanvasSize(grabbedImage.width(), grabbedImage.height());
	 * 
	 * System.out.println("framerate = " + grabber.getFrameRate());
	 * grabber.setFrameRate(grabber.getFrameRate()); FFmpegFrameRecorder
	 * recorder = new FFmpegFrameRecorder(FILENAME,
	 * grabber.getImageWidth(),grabber.getImageHeight());
	 * 
	 * recorder.setVideoCodec(13); recorder.setFormat("mp4");
	 * recorder.setPixelFormat(avutil.PIX_FMT_YUV420P);
	 * recorder.setFrameRate(30); recorder.setVideoBitrate(10 * 1024 * 1024);
	 * 
	 * recorder.start(); while (canvasFrame.isVisible() && (grabbedImage =
	 * grabber.grab()) != null) { canvasFrame.showImage(grabbedImage);
	 * recorder.record(grabbedImage); } recorder.stop(); grabber.stop();
	 * canvasFrame.dispose(); }
	 */
	
	private OpenCVFrameGrabber InitialGrabber() {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(UVCStreamMetaInfo.WEBCAM_DEVICE_INDEX);
		grabber.setImageWidth(UVCStreamMetaInfo.CAPTUREWIDTH);
		grabber.setImageHeight(UVCStreamMetaInfo.CAPTUREHRIGHT);
		try {
			grabber.start();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
			e.printStackTrace();
		}
		return grabber;
	}
	
	/*private FFmpegFrameRecorder InitialRecorder() {
		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("output.mp4", UVCStreamMetaInfo.CAPTUREWIDTH,
				UVCStreamMetaInfo.CAPTUREHRIGHT, 2);
		recorder.setInterleaved(true);
		// video options
		recorder.setVideoOption("tune", "zerolatency");
		recorder.setVideoOption("preset", "ultrafast");
		recorder.setVideoOption("crf", "28");
		recorder.setVideoBitrate(2000000);
		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
		recorder.setFormat("mp4");
		recorder.setFrameRate(UVCStreamMetaInfo.FRAME_RATE);
		recorder.setGopSize(UVCStreamMetaInfo.GOP_LENGTH_IN_FRAMES);
		// audio options
		recorder.setAudioOption("crf", "0");
		recorder.setAudioQuality(0);
		recorder.setAudioBitrate(192000);
		recorder.setSampleRate(44100);
		recorder.setAudioChannels(2);
		recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
		try {
			recorder.start();
		} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
			e.printStackTrace();
		}
		return recorder;
	}*/
	
}