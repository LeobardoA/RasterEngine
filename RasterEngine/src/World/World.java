/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import java.awt.Graphics;
import rasterengine.Raster;
import renderer.Pipeline;
import util.Camera;

/**
 *
 * @author avile
 */
public class World {

    private Chunk testChunk;
    private Camera camera;
    private Pipeline pipe;

    public World() {
        camera = new Camera();
        camera.transform.position.z = -10;
        camera.transform.position.y = 7;
        camera.transform.position.x = -2;
        pipe = new Pipeline();
        pipe.setCamera(camera);
        testChunk = new Chunk(pipe);
    }

    public void draw(Graphics g) {
        pipe.clear();
        testChunk.draw(g);
    }

    public void tick(int tick) {

        if(Raster.KEY_HANDLER.leftPressed){
            camera.transform.position.x += -0.2;
        }
        if(Raster.KEY_HANDLER.rightPressed){
            camera.transform.position.x += 0.2;
        }
        
        testChunk.update(tick);
    }

}
