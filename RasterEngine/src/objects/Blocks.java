/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objects;

import renderUtil.TextureLoader;
import renderutil.Texture;

/**
 *
 * @author avile
 */
public class Blocks {

    public static Block AIR() {
        return new Block(Block.Properties.create(new Texture(), 1));
    }

    public static Block STONE() {
        return new Block(Block.Properties.create(new Texture(TextureLoader.individualTextures[1]), 1));
    }

    public static Block DIRT() {
        return new Block(Block.Properties.create(new Texture(TextureLoader.individualTextures[2]), 1));
    }

    public static Block WOOD() {
        return new Block(Block.Properties.create(new Texture(TextureLoader.getComplexTexture(21, 20)), 2));
    }

}
