/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objects;

import renderUtil.Texture;

/**
 *
 * @author avile
 */
public class Blocks {

    public static final Block AIR = new Block(Block.Properties.create(new Texture()));
    public static final Block STONE = new Block(Block.Properties.create(new Texture("stone")));

}
