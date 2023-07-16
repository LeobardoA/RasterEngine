/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objects;

import Sound.SoundType;
import java.util.ArrayList;
import renderUtil.Shape;
import renderUtil.Texture;
import renderUtil.TextureColor;
import util.Triangle;
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
    protected final TextureColor textureColor;
    protected final boolean blocksMovement;

    public Block(Block.Properties properties) {
        super();
        this.texture = properties.texture;
        this.textureColor = properties.textureColor;
        this.blocksMovement = properties.blocksMovement;
        this.soundType = properties.soundType;
        this.lightValue = properties.lightValue;
        this.blockResistance = properties.resistance;
        this.blockHardness = properties.hardness;
        this.ticksRandomly = properties.ticksRandomly;
    }

    public void createShape() {
        tris = new ArrayList<>();

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
        tris.add(new Triangle(v0, v4, v1));
        tris.add(new Triangle(v4, v5, v1));

        // LEFT FACES
        tris.add(new Triangle(v4, v6, v7));
        tris.add(new Triangle(v4, v7, v5));

        // RIGHT FACES
        tris.add(new Triangle(v0, v1, v3));
        tris.add(new Triangle(v0, v3, v2));

        // NORTH FACES
        tris.add(new Triangle(v1, v5, v7));
        tris.add(new Triangle(v1, v7, v3));

        // SOUTH FACES
        tris.add(new Triangle(v0, v2, v6));
        tris.add(new Triangle(v0, v6, v4));

        // UP FACES
        tris.add(new Triangle(v2, v3, v7));
        tris.add(new Triangle(v2, v7, v6));
    }

    public void removeFaces(String... faces) {
        for (int i = 0; i < faces.length; i++) {
            switch (faces[i]) {
                case "DOWN":
                    tris.get(0).isVisible = false;
                    tris.get(1).isVisible = false;
                    break;
                case "LEFT":
                    tris.get(2).isVisible = false;
                    tris.get(3).isVisible = false;
                    break;
                case "RIGHT":
                    tris.get(4).isVisible = false;
                    tris.get(5).isVisible = false;
                    break;
                case "NORTH":
                    tris.get(6).isVisible = false;
                    tris.get(7).isVisible = false;
                    break;
                case "SOUTH":
                    tris.get(8).isVisible = false;
                    tris.get(9).isVisible = false;
                    break;
                case "UP":
                    tris.get(10).isVisible = false;
                    tris.get(11).isVisible = false;
                    break;
            }
        }
    }

    public static class Properties {

        private Texture texture;
        private TextureColor textureColor;
        private boolean blocksMovement = true;
        private SoundType soundType = null;
        private int lightValue;
        private float resistance;
        private float hardness;
        private boolean ticksRandomly;

        private Properties(Texture texture, TextureColor textureColor) {
            this.texture = texture;
            this.textureColor = textureColor;
        }

        public static Block.Properties create(Texture texture) {
            return create(texture, texture.getColor());
        }

        public static Block.Properties create(Texture texture, TextureColor textureColor) {
            return new Block.Properties(texture, textureColor);
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
