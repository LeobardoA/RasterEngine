package rasterengine;

import World.World;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JPanel;
import renderutil.DisplayRenderer;
import util.KeyHandler;
import util.Vector2;

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
    public static final KeyHandler KEY_HANDLER = new KeyHandler();

    //TIME VARIABLES
    private static final long NANO_IN_SECOND = 1_000_000_000;
    private static final double NANO_PER_FRAME = NANO_IN_SECOND / FPS;
    private int tick = 0;

    private BufferedImage frame;
    private World world;

    /**
     * Constructor for all objects and variables inicialization
     */
    public Raster() {
        frame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        DisplayRenderer.Initialize(SCREEN_WIDTH, SCREEN_HEIGHT, ((DataBufferInt) frame.getRaster().getDataBuffer()).getData());
        world = new World();
        this.setPreferredSize(new Dimension((int) SCREEN_WIDTH, (int) SCREEN_HEIGHT));
        this.setBackground(BACKGROUND_COLOR);
        this.setDoubleBuffered(true);
        this.addKeyListener(KEY_HANDLER);
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

        g.drawImage(frame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        g.dispose();
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

    public Vector2 getFrameSize() {

        return new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT);

    }

}
