/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rasterengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import util.KeyHandler;

/**
 *
 * @author avile
 */
public class Raster extends JPanel implements Runnable {

    //PROJECT SETTINGS
    private Thread mainLoop;
    private final int FPS = 75;
    private boolean isTesting = true;

    //WINDOWS PROPERTIES
    private final int SCREEN_WIDTH = 1280, SCREEN_HEIGHT = 720;
    private final Color BACKGROUND_COLOR = Color.BLACK;
    private final KeyHandler keyHandler = new KeyHandler();

    //TIME VARIABLES
    private int tick = 0;

    //CAMERA            - NOT IMPLEMENTED YET
    
    /**
     * Constructor for all objects and variables inicialization
     */
    public Raster() {
        this.setPreferredSize(new Dimension((int) SCREEN_WIDTH, (int) SCREEN_HEIGHT));
        this.setBackground(BACKGROUND_COLOR);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    /**
     * just the main loop
     */
    public void startMainLoop() {
        mainLoop = new Thread(this);
        mainLoop.start();
    }

    /**
     * This method should be used to update separetly the logic and position of
     * the objects
     */
    public void update() {
        tick++;
    }

    /**
     * This method should be used just to draw objects on the screen
     * 
     * @param g The Graphics instance to draw on the screen
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    /**
     * This is the main game loop.
     */
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (mainLoop != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                if (isTesting) {
                    System.out.println("FPS: " + drawCount);
                }
                drawCount = 0;
                timer = 0;
            }
        }
    }

}
