/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.logging.Logger;
import rasterengine.Raster;
import util.Camera;

/**
 *
 * @author avile
 */
public class World {

    private Chunk testChunk;
    private Player player;

    public World(Player player) {
        this.player = player;
        testChunk = new Chunk(Player.mainCamera);
    }

    public void draw(Graphics g) {
        testChunk.draw(g);
    }

    public void tick(int tick) {

        testChunk.update(tick);
        if (Raster.KEY_HANDLER.rightPressed) {
            player.moveX(0.5);
        }
        if (Raster.KEY_HANDLER.leftPressed) {
            player.moveX(-0.5);
        }
        if (Raster.KEY_HANDLER.upPressed) {
            player.moveZ(0.5);
        }
        if (Raster.KEY_HANDLER.downPressed) {
            player.moveZ(-0.5);
        }
        if (Raster.KEY_HANDLER.spacePressed) {
            player.moveY(0.5);
        }
        if (Raster.KEY_HANDLER.shiftPressed) {
            player.moveY(-0.5);
        }
    }

}
