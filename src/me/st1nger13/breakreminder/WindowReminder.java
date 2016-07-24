package me.st1nger13.breakreminder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by st1nger13 on 20.07.16.
 * The Window
 */
public class WindowReminder extends JFrame {
    private static WindowReminder instance ;
    private int timerMaxTime ;
    private volatile int timer ;
    private volatile float breakLoadingValue = 0.82f ;
    private long lastUpdateTime ;
    private boolean isRunning ;
    private Color timerBgColor = new Color(46f/255f, 45f/255f, 36f/255f/ 1f) ;
    private BufferedImage image = null ;
    private JPanel breakPane ;
    private boolean isInit ;


    public static void main(String[] args) {
        new WindowReminder(20, WorkRegime.STRICT) ;
    }

    public static void start(int timer, WorkRegime workRegime) {
        if(instance == null)
            instance = new WindowReminder(timer, workRegime) ;
    }

    public static void close() {
        instance.dispose() ;
        instance = null ;
    }

    public WindowReminder(int timer, WorkRegime workRegime) {
        Print.lined("Break started for " + timer + " sec.") ;

        try {
            URL urlImage = this.getClass().getClassLoader().getResource("eye_ico.png") ;
            if(urlImage != null)
                image = ImageIO.read(urlImage) ;
            else
                Print.error("Can't find eye_ico.png!") ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.timer = timerMaxTime = timer ;

        if(workRegime == WorkRegime.STRICT) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize() ;
            this.setBounds(0, 0, screenSize.width-100, screenSize.height-100) ;
            this.addWindowFocusListener(new WindowFocusListener() {
                @Override
                public void windowGainedFocus(WindowEvent windowEvent) {}

                @Override
                public void windowLostFocus (WindowEvent e){
                    if (e.getNewState() != WindowEvent.WINDOW_CLOSED) {
                        WindowReminder.this.setAlwaysOnTop(false) ;
                        WindowReminder.this.setAlwaysOnTop(true) ;
                    }
                }
            });
        } else {
            this.setSize(400, 200) ;
        }

        this.setAlwaysOnTop(true) ;
        this.setLocationByPlatform(true) ;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        this.setUndecorated(true) ;
        this.setBackground(new Color(0, 0, 0, 0.4f));
        this.setLayout(new GridBagLayout()) ;
        this.setLocationRelativeTo(null) ;

        breakPane = new JPanel() {
            public void paintComponent(Graphics g){
                g.clearRect(0, 0, getWidth(), getHeight()) ;
                super.paintComponent(g);
                if(breakLoadingValue > 0 && breakLoadingValue <= 1) {
                    g.setColor(timerBgColor) ;
                    g.fillRect(0, 0, 400, (int) (200 * breakLoadingValue)) ;
                }

                if(image != null)
                    g.drawImage(image, 100, 47, null) ;
            }
        } ;
        breakPane.setPreferredSize(new Dimension(400, 200)) ;
        breakPane.setSize(400, 200);
        breakPane.setBackground(new Color(24f/255f, 24f/255f, 24f/255f, 1f)) ;

        this.add(breakPane) ;
        this.repaint() ;
        this.setVisible(true) ;

        startTimer() ;
    }

    @Override
    public void paint(Graphics g){
        g.clearRect(0, 0, getWidth(), getHeight()) ;
    }

    public void startTimer() {
        isRunning = true ;
        this.paint(this.getGraphics()) ;

        Thread timerThread = new Thread(() -> {
            while(isRunning) {
                if(System.currentTimeMillis() - lastUpdateTime > 1000) {
                    timer-- ;
                    breakLoadingValue = (float) timer / timerMaxTime ;
                    lastUpdateTime = System.currentTimeMillis() ;
                    WindowReminder.this.breakPane.repaint() ;
                }

                if(timer < 0) {
                    isRunning = false ;
                }

                try {
                    Thread.sleep(50) ;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!isInit) {
                    WindowReminder.this.paint(getGraphics()) ;
                    isInit = true ;
                }
            }
            Print.lined("Break finished.") ;
            WindowReminder.close() ;
        }) ;
        timerThread.start() ;
    }
}
