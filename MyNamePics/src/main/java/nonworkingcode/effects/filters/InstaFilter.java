package nonworkingcode.effects.filters;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.OpenGlUtils;
import jp.co.cyberagent.android.gpuimage.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

public abstract class InstaFilter extends GPUImageFilter {
    protected static final String VERTEX_SHADER = "attribute vec4 position;\n attribute vec4 inputTextureCoordinate;\n \n varying vec2 textureCoordinate;\n \n void main()\n {\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n }\n";
    protected int[] GL_TEXTURES;
    protected Bitmap[] bitmaps;
    protected int[] coordinateAttributes;
    protected ByteBuffer[] coordinatesBuffers;
    protected int[] inputTextureUniforms;
    protected int[] sourceTextures;
    protected int textureNum;

    public InstaFilter(String fragmentShader, int textures) {
        this(VERTEX_SHADER, fragmentShader, textures);
    }

    public InstaFilter(String vertexShader, String fragmentShader, int textures) {
        super(vertexShader, fragmentShader);
        this.GL_TEXTURES = new int[]{33987, 33988, 33989, 33990, 33991, 33992};
        this.textureNum = textures;
        this.coordinateAttributes = new int[this.textureNum];
        this.inputTextureUniforms = new int[this.textureNum];
        this.sourceTextures = new int[this.textureNum];
        for (int i = 0; i < this.textureNum; i++) {
            this.sourceTextures[i] = -1;
        }
        this.coordinatesBuffers = new ByteBuffer[this.textureNum];
        this.bitmaps = new Bitmap[this.textureNum];
        setRotation(Rotation.NORMAL, false, false);
    }

    public void setRotation(Rotation rotation, boolean flipHorizontal, boolean flipVertical) {
        float[] buffer = TextureRotationUtil.getRotation(rotation, flipHorizontal, flipVertical);
        ByteBuffer bBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder());
        FloatBuffer fBuffer = bBuffer.asFloatBuffer();
        fBuffer.put(buffer);
        fBuffer.flip();
        for (int i = 0; i < this.textureNum; i++) {
            this.coordinatesBuffers[i] = bBuffer;
        }
    }

    public void onInit() {
        super.onInit();
        int i = 0;
        while (i < this.textureNum) {
            int k = i + 2;
            this.coordinateAttributes[i] = GLES20.glGetAttribLocation(getProgram(), String.format("inputTextureCoordinate%d", new Object[]{Integer.valueOf(k)}));
            this.inputTextureUniforms[i] = GLES20.glGetUniformLocation(getProgram(), String.format("inputImageTexture%d", new Object[]{Integer.valueOf(k)}));
            GLES20.glEnableVertexAttribArray(this.coordinateAttributes[i]);
            if (!(this.bitmaps[i] == null || this.bitmaps[i].isRecycled())) {
                loadBitmap(i, this.bitmaps[i]);
            }
            i++;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.textureNum > 0) {
            try {
                GLES20.glDeleteTextures(1, this.sourceTextures, 0);
                int i = 0;
                while (i < this.textureNum) {
                    this.sourceTextures[i] = -1;
                    if (!(this.bitmaps[i] == null || this.bitmaps[i].isRecycled())) {
                        this.bitmaps[i].recycle();
                        this.bitmaps[i] = null;
                    }
                    i++;
                }
            } catch (Exception e) {
            }
        }
    }

    public void setBitmap(int index, Bitmap bitmap) {
        if ((bitmap == null || !bitmap.isRecycled()) && bitmap != null) {
            this.bitmaps[index] = bitmap;
        }
    }

    private void loadBitmap(int index, Bitmap bitmap) {
        if ((bitmap == null || !bitmap.isRecycled()) && bitmap != null) {
            runOnDraw(new C01521(bitmap, index));
        }
    }

    protected void onDrawArraysPre() {
        for (int i = 0; i < this.textureNum; i++) {
            GLES20.glEnableVertexAttribArray(this.coordinateAttributes[i]);
            GLES20.glActiveTexture(this.GL_TEXTURES[i]);
            GLES20.glBindTexture(3553, this.sourceTextures[i]);
            GLES20.glUniform1i(this.inputTextureUniforms[i], i + 3);
            this.coordinatesBuffers[i].position(0);
            GLES20.glVertexAttribPointer(this.coordinateAttributes[i], 2, 5126, false, 0, this.coordinatesBuffers[i]);
        }
    }

    /* renamed from: com.beautify.effects.InstaFilter.1 */
    class C01521 implements Runnable {
        private final Bitmap val$bitmap;
        private final int val$index;

        C01521(Bitmap bitmap, int i) {
            this.val$bitmap = bitmap;
            this.val$index = i;
        }

        public void run() {
            if (this.val$bitmap != null && !this.val$bitmap.isRecycled() && InstaFilter.this.sourceTextures[this.val$index] == -1) {
                GLES20.glActiveTexture(InstaFilter.this.GL_TEXTURES[this.val$index]);
                InstaFilter.this.sourceTextures[this.val$index] = OpenGlUtils.loadTexture(this.val$bitmap, -1, false);
            }
        }
    }
}
