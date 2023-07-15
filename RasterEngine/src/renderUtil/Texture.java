/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package renderUtil;

import java.awt.Color;
import util.DefaultConfig;

/**
 *
 * @author avile
 */
public class Texture {

    private Color defaultColor = Color.WHITE;
    private Color edgeColor = DefaultConfig.EDGE_COLOR;

    /**
     * if paramps are empty, could be just air
     */
    public Texture() {
    }

    public Texture(String textureFile) {
        //SOON
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

}
