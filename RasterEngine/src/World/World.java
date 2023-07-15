/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import java.awt.Graphics;
import util.Camera;

/**
 *
 * @author avile
 */
public class World {

    private Chunk testChunk;


    public World(Camera camera) {
        testChunk = new Chunk(camera);
    }

    public void draw(Graphics g) {
        testChunk.draw(g);
    }

    public void tick(int tick) {
        testChunk.update(tick);
    }

}
