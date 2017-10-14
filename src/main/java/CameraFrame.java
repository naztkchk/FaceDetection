import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CameraFrame extends JFrame implements ActionListener{

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    CameraPanel cp;

    CameraFrame(){
        VideoCapture list  = new VideoCapture(0);
        cp = new CameraPanel();
        Thread thread = new Thread(cp);
        JMenu camera = new JMenu("Camera");
        JMenuBar bar = new JMenuBar();
        bar.add(camera);
        int i = 1;
        while (list.isOpened()){
            JMenuItem cam = new JMenuItem("Camera"+1);
            cam.addActionListener(this);
            camera.add(cam);
            list.release();
            list = new VideoCapture(i);
            i++;
        }
        thread.start();
        add(cp);
        setJMenuBar(bar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);

    }

    public static void main (String [] args){
        CameraFrame cameraFrame = new CameraFrame();
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem source  = (JMenuItem) e.getSource();
        int num = Integer.parseInt(source.getText().substring(7))-1;
        cp.switchCamera(num);


    }
};
