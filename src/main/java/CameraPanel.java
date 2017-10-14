import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class CameraPanel extends JPanel implements Runnable, ActionListener{

    BufferedImage image;
    VideoCapture videoCapture;
    JButton screenshot;

    CascadeClassifier faceDetector;
    MatOfRect faceDetections;


    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    CameraPanel(){
        faceDetector = new CascadeClassifier(CameraPanel.class.getResource("haarcascade_frontalface_alt.xml").
                getPath().substring(1));
        faceDetections = new MatOfRect();

        screenshot = new JButton("screenshot");
        screenshot.addActionListener(this);
        add(screenshot);
    }

    public void run() {
        videoCapture = new VideoCapture(0);
        Mat webcam_image = new Mat();
        if(videoCapture.isOpened()){
            while (true){
                videoCapture.read(webcam_image);
                if(!webcam_image.empty()){
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    topFrame.setSize(webcam_image.width()+40, webcam_image.height()+110);
                    matToBufferedImage(webcam_image);
                    faceDetector.detectMultiScale(webcam_image, faceDetections);
                    repaint();
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(this.image == null) return;
        g.drawImage(image, 10,40, image.getWidth(), image.getHeight(), null);
        g.setColor(Color.GREEN);
        for(Rect rect: faceDetections.toArray()){
            g.drawRect(rect.x+10, rect.y+40, rect.width, rect.height);
        }
    }

    private void matToBufferedImage(Mat matRGB) {
        int width =matRGB.width(), height = matRGB.height(), channels = matRGB.channels();
        byte[] source = new byte[width*height*channels];
        matRGB.get(0,0 , source);

        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] target  = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(source,0, target, 0, source.length);
    }

    public void actionPerformed(ActionEvent e) {
        File out = new File("screenshot1.png");
        int i = 0;
        while (out.exists()){
            i++;
            out = new File("screenshot"+i+".png");

        }
        try {
            ImageIO.write(image, "png", out);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void switchCamera(int num) {
        videoCapture = new VideoCapture(num);

    }
}
