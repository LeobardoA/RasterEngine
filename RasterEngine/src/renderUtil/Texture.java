/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package renderUtil;

/**
 *
 * @author avile
 */
public class Texture {

    private TextureColor defaultColor = TextureColor.WHITE;

    /**
     * if paramps are empty, could be just air
     */
    public Texture() {
    }

    public Texture(String textureFile) {
        //SOON
    }

    public TextureColor getColor() {
        return defaultColor;
    }

}
