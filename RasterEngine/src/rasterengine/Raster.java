package rasterengine;

import World.World;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import util.Camera;
import util.KeyHandler;

/**
 *
 * @author avile
 */
public class Raster extends JPanel implements Runnable {

    //PROJECT SETTINGS
    private Thread mainLoop;
    private static final int FPS = 75;
    private final boolean isTesting = true;

    //WINDOWS PROPERTIES
    public static final int SCREEN_WIDTH = 1280, SCREEN_HEIGHT = 720;
    private final Color BACKGROUND_COLOR = Color.BLACK;
    private final KeyHandler keyHandler = new KeyHandler();

    //TIME VARIABLES
    private static final long NANO_IN_SECOND = 1_000_000_000;
    private static final double NANO_PER_FRAME = NANO_IN_SECOND / FPS;
    private int tick = 0;

    private World world;
    private Camera camera;

    //CAMERA            - NOT IMPLEMENTED YET
    /**
     * Constructor for all objects and variables inicialization
     */
    public Raster() {
        camera = new Camera(75, SCREEN_WIDTH, SCREEN_HEIGHT, 0.1, 1000);
        world = new World(camera);
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
     *
     * @param deltaTime a factor of time that defines the difference between
     * frames
     */
    public void update(double deltaTime) {

        world.tick(tick);

    }

    /**
     * This method should be used just to draw objects on the screen
     *
     * @param g The Graphics instance to draw on the screen
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        world.draw(g);

    }

    /**
     * This is the main game loop.
     */
    @Override
    public void run() {
        double delta = 0;
        double deltaTime;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        long tickTimer = System.nanoTime();

        while (mainLoop != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / NANO_PER_FRAME;
            deltaTime = (currentTime - lastTime) / (double) NANO_IN_SECOND;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update(deltaTime);
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= NANO_IN_SECOND) {
                if (isTesting) {
                    System.out.println("FPS: " + drawCount + " Tick: " + (tick + 1));
                }
                drawCount = 0;
                timer = 0;
            }

            long currentTickTime = System.nanoTime();
            long elapsedTickNanos = currentTickTime - tickTimer;
            double elapsedTickSeconds = elapsedTickNanos / (double) NANO_IN_SECOND;

            if (elapsedTickSeconds >= (1.0 / 20.0)) {       //La variable TICK se restablece cada 20 minutos reales
                tick++;
                if (tick >= 24000) {
                    tick = 0;
                }
                tickTimer = currentTickTime;
            }
        }
    }

}
