package renderUtil;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author avile
 */
public class TextureLoader {

    private static final int TILE_SIZE = 16;
    private static final int NUM_TILES_X = 16;
    private static final int NUM_TILES_Y = 16;

    //LOAD FULL RESOURCES
    public static final BufferedImage TEXTURES = getTexture("terrain.png");

    //LOAD INDIVIDUAL SPRITES
    public static final BufferedImage[] individualTextures;

    static {
        individualTextures = new BufferedImage[NUM_TILES_X * NUM_TILES_Y];
        for (int y = 0; y < NUM_TILES_Y; y++) {
            for (int x = 0; x < NUM_TILES_X; x++) {
                individualTextures[y * NUM_TILES_X + x] = TEXTURES.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public static BufferedImage getComplexTexture(int a, int b) {
        BufferedImage img = new BufferedImage(TILE_SIZE * 2, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(individualTextures[a], 0, 0, null);
        g2d.drawImage(individualTextures[b], TILE_SIZE, 0, null);
        g2d.dispose();

        return img;
    }

    private static BufferedImage getTexture(String path) {
        try {
            File file = new File(TextureLoader.class.getResource("/Assets/" + path).getFile());
            if (file.exists()) {
                return ImageIO.read(file);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
