/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objects;

import Sound.SoundType;
import java.util.ArrayList;
import renderUtil.Shape;
import renderUtil.Texture;
import util.Quad;
import util.Vect3D;

/**
 *
 * @author avile
 */
public class Block extends Shape {

    private static String DIRECTION = "UP";
    protected final int lightValue;
    protected final float blockHardness;
    protected final float blockResistance;
    protected final boolean ticksRandomly;
    protected final SoundType soundType;
    protected final Texture texture;
    protected final boolean blocksMovement;

    public Block(Block.Properties properties) {
        super();
        this.texture = properties.texture;
        this.blocksMovement = properties.blocksMovement;
        this.soundType = properties.soundType;
        this.lightValue = properties.lightValue;
        this.blockResistance = properties.resistance;
        this.blockHardness = properties.hardness;
        this.ticksRandomly = properties.ticksRandomly;
    }

    public void createShape() {
        quads = new ArrayList<>();

        // DOWN VERTS
        Vect3D v0 = new Vect3D(-1, -1, -1);
        Vect3D v1 = new Vect3D(-1, -1, 1);
        Vect3D v4 = new Vect3D(1, -1, -1);
        Vect3D v5 = new Vect3D(1, -1, 1);

        // UP VERTS
        Vect3D v2 = new Vect3D(-1, 1, -1);
        Vect3D v3 = new Vect3D(-1, 1, 1);
        Vect3D v6 = new Vect3D(1, 1, -1);
        Vect3D v7 = new Vect3D(1, 1, 1);

        // DOWN FACES
        quads.add(new Quad(v0, v1, v4, v5));
//        quads.add(new Quad(v0, v0, v0, v0));

        // LEFT FACES
        quads.add(new Quad(v4, v5, v6, v7));
//        quads.add(new Quad(v0, v0, v0, v0));

        // RIGHT FACES
        quads.add(new Quad(v0, v1, v2, v3));
//        quads.add(new Quad(v0, v0, v0, v0));

        // NORTH FACES
        quads.add(new Quad(v1, v3, v5, v7));
//        quads.add(new Quad(v0, v0, v0, v0));

        // SOUTH FACES
        quads.add(new Quad(v0, v2, v4, v6));
//        quads.add(new Quad(v0, v0, v0, v0));

        // UP FACES
        quads.add(new Quad(v2, v3, v6, v7));
//        quads.add(new Quad(v0, v0, v0, v0));
    }

    public void setFaces(String faces, boolean value) {
        switch (faces) {
            case "DOWN":
                quads.get(0).setVisible(value);
                break;
            case "LEFT":
                quads.get(1).setVisible(value);
                break;
            case "RIGHT":
                quads.get(2).setVisible(value);
                break;
            case "NORTH":
                quads.get(3).setVisible(value);
                break;
            case "SOUTH":
                quads.get(4).setVisible(value);
                break;
            case "UP":
                quads.get(5).setVisible(value);
                break;
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

        private Properties(Texture texture) {
            this.texture = texture;
        }

        public static Block.Properties create(Texture texture) {
            return new Block.Properties(texture);
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
