package World;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import objects.Block;
import objects.Blocks;
import renderer.Pipeline;
import util.Vector3;

/**
 *
 * @author avile
 */
public class Chunk {

    private Block[][][] blocks = new Block[5][5][5];
    private boolean requiresFaceUpdate = true;
    private Pipeline pipe;
    private int spaceBeetween = 2;

    private ArrayList<Block> zBuffer;

    public Chunk(Pipeline pipe) {
        this.pipe = pipe;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                for (int k = 0; k < blocks[0][0].length; k++) {
                    blocks[i][j][k] = Blocks.WOOD();
                    blocks[i][j][k].transform.scale = new Vector3(1, 1, 1);
                    blocks[i][j][k].transform.position = new Vector3(i * spaceBeetween, j * spaceBeetween, k * spaceBeetween);
                }
            }
        }
    }

    public void update(int tick) {
    }

    public void draw(Graphics g) {
        zBuffer = new ArrayList<>();
        Graphics2D g2 = (Graphics2D) g;
        if (requiresFaceUpdate) {
            checkFaces();
        } else {
            //RENDER BLOCKS
            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[0].length; j++) {
                    for (int k = 0; k < blocks[0][0].length; k++) {
                        zBuffer.add(blocks[i][j][k]);
                    }
                }
            }
            renderAll(g2);
        }
    }

    private void renderAll(Graphics2D g2) {
        //RENDERIZADO
        zBuffer.forEach((t) -> {
            pipe.DrawMesh(t);
        });
    }

    private void checkFaces() {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                for (int k = 0; k < blocks[0][0].length; k++) {
                    Block currentBlock = blocks[i][j][k];

                    // Verificar la existencia de bloques adyacentes
                    boolean upExists = (j < blocks[0].length - 1) && (blocks[i][j + 1][k] != null);
                    boolean downExists = (j > 0) && (blocks[i][j - 1][k] != null);
                    boolean northExists = (k > 0) && (blocks[i][j][k - 1] != null);
                    boolean southExists = (k < blocks[0][0].length - 1) && (blocks[i][j][k + 1] != null);
                    boolean leftExists = (i > 0) && (blocks[i - 1][j][k] != null);
                    boolean rightExists = (i < blocks.length - 1) && (blocks[i + 1][j][k] != null);

                    // Eliminar las caras no visibles
                    if (upExists) {
                        currentBlock.removeFaces("UP");
                    }
                    if (downExists) {
                        currentBlock.removeFaces("DOWN");
                    }
                    if (northExists) {
                        currentBlock.removeFaces("SOUTH");
                    }
                    if (southExists) {
                        currentBlock.removeFaces("NORTH");
                    }
                    if (leftExists) {
                        currentBlock.removeFaces("LEFT");
                    }
                    if (rightExists) {
                        currentBlock.removeFaces("RIGHT");
                    }
                }
            }
        }

        requiresFaceUpdate = false;
    }

}
