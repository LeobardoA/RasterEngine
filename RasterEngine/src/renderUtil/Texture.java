/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package renderUtil;

import java.awt.color.ICC_ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author avile
 */
public class Texture {

    private TextureColor defaultColor = TextureColor.WHITE;
    private BufferedImage texture;

    /**
     * if paramps are empty, could be just air
     */
    public Texture() {
    }

    public Texture(String textureFile) {
        try {
            URL imageURL = Texture.class.getResource("test.png");
            texture = ImageIO.read(new File("C:/Users/52331/Documents/NetBeansProjects/RasterEngine/RasterEngine/build/classes/renderUtil/test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TextureColor getColor() {
        return defaultColor;
    }
    
    public int getRGB(int x, int y) {
        if (x < 0 || y < 0 || x >= texture.getWidth() || y >= texture.getHeight()) {
            // Si las coordenadas están fuera de los límites de la textura, devuelve un color negro (puede ajustarse según las necesidades)
            return 0xFF000000; // Color negro en formato RGB (alfa: 255, rojo: 0, verde: 0, azul: 0)
        }
        return texture.getRGB(x, y);
    }
    
    public int getRGBPercent(double x, double y) {
        
        
        if (x < 0 || y < 0 || x >= 1 || y >= 1) {
            // Si las coordenadas están fuera de los límites de la textura, devuelve un color negro (puede ajustarse según las necesidades)
            return 0xFF000000; // Color negro en formato RGB (alfa: 255, rojo: 0, verde: 0, azul: 0)
        }
        int absoluteX = (int) (x * texture.getWidth());
        int absoluteY = (int) (y * texture.getHeight());
        return texture.getRGB(absoluteX, absoluteY);
    }

}
