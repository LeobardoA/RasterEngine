/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package renderUtil;

import java.awt.Color;

/**
 *
 * @author avile
 */
public class TextureColor {

    public static final TextureColor WHITE = new TextureColor(Color.WHITE);

    private Color color;
    
    private TextureColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    
    
}
