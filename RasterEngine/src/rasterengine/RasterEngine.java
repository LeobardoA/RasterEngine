package rasterengine;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author avile
 */
public class RasterEngine {

    private static final String GAME_VERSION = "1.0.0";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame ventana = new JFrame();
        ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle("RasterEngine - Version: " + GAME_VERSION);

        Raster raster = new Raster();
        ventana.add(raster);

        raster.startMainLoop();

        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

}
