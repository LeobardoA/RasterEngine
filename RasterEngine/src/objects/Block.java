/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objects;

import Sound.SoundType;
import java.io.File;
import renderutil.ObjLoader;
import renderutil.Shader;
import renderutil.Texture;
import renderutil.Transform;
import util.Triangle;

/**
 *
 * @author avile
 */
public class Block {

    private static final File onlyFace = new File(Block.class.getResource("/Assets/onlyFace.obj").getFile());
    private static final File twoFaces = new File(Block.class.getResource("/Assets/twoFaces.obj").getFile());
    private static String DIRECTION = "UP";
    protected final int lightValue;
    protected final float blockHardness;
    protected final float blockResistance;
    protected final boolean ticksRandomly;
    protected final SoundType soundType;
    protected final Texture texture;
    protected final boolean blocksMovement;
    public final Transform transform;
    public Shader shader;
    public Triangle[] tris;

    public Block(Block.Properties properties) {
        super();
        this.texture = properties.texture;
        this.blocksMovement = properties.blocksMovement;
        this.soundType = properties.soundType;
        this.lightValue = properties.lightValue;
        this.blockResistance = properties.resistance;
        this.blockHardness = properties.hardness;
        this.ticksRandomly = properties.ticksRandomly;
        this.transform = new Transform();
        this.shader = new Shader();
        shader.setTexture(texture);
        switch (properties.textureFaces) {
            case 1:
                tris = Triangle.CreateIndexedTriangleStream(ObjLoader.Load(onlyFace));
                break;
            case 2:
                tris = Triangle.CreateIndexedTriangleStream(ObjLoader.Load(twoFaces));
                break;
        }
    }

    public void removeFaces(String... faces) {
        for (int i = 0; i < faces.length; i++) {
            switch (faces[i]) {
                case "DOWN":
                    tris[3].isVisible = false;
                    tris[9].isVisible = false;
                    break;
                case "UP":
                    tris[0].isVisible = false;
                    tris[6].isVisible = false;
                    break;
                case "LEFT":
                    tris[4].isVisible = false;
                    tris[10].isVisible = false;
                    break;
                case "RIGHT":
                    tris[2].isVisible = false;
                    tris[8].isVisible = false;
                    break;
                case "NORTH":
                    tris[5].isVisible = false;
                    tris[11].isVisible = false;
                    break;
                case "SOUTH":
                    tris[1].isVisible = false;
                    tris[7].isVisible = false;
                    break;
            }
        }
    }

    public static class Properties {

        private Texture texture;
        private boolean blocksMovement = true;
        private SoundType soundType = null;
        private int lightValue;
        private float resistance;
        private float hardness;
        private boolean ticksRandomly;
        private int textureFaces = 1;

        private Properties(Texture texture, int faces) {
            this.texture = texture;
            this.textureFaces = faces;
        }

        public static Block.Properties create(Texture texture, int faces) {
            return new Block.Properties(texture, faces);
        }

        public Block.Properties doesNotBlockMovement() {
            this.blocksMovement = false;
            return this;
        }

        protected Block.Properties sound(SoundType soundTypeIn) {
            this.soundType = soundTypeIn;
            return this;
        }

        protected Block.Properties lightValue(int lightValueIn) {
            this.lightValue = lightValueIn;
            return this;
        }

        public Block.Properties hardnessAndResistance(float hardnessIn, float resistanceIn) {
            this.hardness = hardnessIn;
            this.resistance = Math.max(0, resistanceIn);
            return this;
        }

        protected Block.Properties zeroHardnessAndResistance() {
            return this.hardnessAndResistance(0);
        }

        protected Block.Properties hardnessAndResistance(float hardnessAndResistance) {
            this.hardnessAndResistance(hardnessAndResistance, hardnessAndResistance);
            return this;
        }

        protected Block.Properties tickRandomly() {
            this.ticksRandomly = true;
            return this;
        }

    }

}
