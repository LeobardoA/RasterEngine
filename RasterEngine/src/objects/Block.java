/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objects;

import Sound.SoundType;
import renderUtil.Texture;
import renderUtil.TextureColor;
import renderUtil.VoxelRenderer;
import util.Matrix4x4;

/**
 *
 * @author avile
 */
public class Block {
    
    private static String DIRECTION = "UP";
    protected final int lightValue;
    protected final float blockHardness;
    protected final float blockResistance;
    protected final boolean ticksRandomly;
    protected final SoundType soundType;
    protected final Texture texture;
    protected final TextureColor textureColor;
    protected final boolean blocksMovement;
    private final VoxelRenderer voxelRenderer;

    public Block(Block.Properties properties) {
        this.texture = properties.texture;
        this.textureColor = properties.textureColor;
        this.blocksMovement = properties.blocksMovement;
        this.soundType = properties.soundType;
        this.lightValue = properties.lightValue;
        this.blockResistance = properties.resistance;
        this.blockHardness = properties.hardness;
        this.ticksRandomly = properties.ticksRandomly;
        this.voxelRenderer = new VoxelRenderer();
    }
    
    public VoxelRenderer getVoxelRenderer() {
        return voxelRenderer;
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
