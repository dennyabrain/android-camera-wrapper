package dennymades.space.CameraTwo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by abrain on 10/21/16.
 */
public class PreviewRenderer implements SurfaceTexture.OnFrameAvailableListener,GLSurfaceView.Renderer {
    private String TAG = this.getClass().getName();
    private Context mContext;
    //Camera and SurfaceTexture
    //FOR NOW private CameraWrapper mCameraWrapper;
    private SurfaceTexture mSurfaceTexture;

    //private final FBORenderTarget mRenderTarget = new FBORenderTarget();
    private final OESTexture mCameraTexture = new OESTexture();
    private final Shader mOffscreenShader = new Shader();
    private int mWidth, mHeight;
    public boolean updateTexture = false;

    // OpenGL params
    private ByteBuffer mFullQuadVertices;
    private float[] mTransformM = new float[16];
    public static float[] mOrientationM = new float[16];
    private float[] mRatio = new float[2];

    //Grafika Renderer stuff
    /*FOR NOWprivate static TextureMovieEncoder mVideoEncoder = new TextureMovieEncoder();
    private static File mOutpuFile;*/
    private int mTextureId;
    private boolean beginRecording =false;
    private boolean isRecording = false;
    int camera_width;
    int camera_height;

    private GLSurfaceView parentGLSurfaceView;

    private CameraDwi camera;

    public PreviewRenderer(){

    }

    public void initRenderer(Context context, GLSurfaceView parent){
        mContext = context;
        parentGLSurfaceView = parent;
        final byte FULL_QUAD_COORDS[] = {-1, 1, -1, -1, 1, 1, 1, -1};
        mFullQuadVertices = ByteBuffer.allocateDirect(4 * 2);
        mFullQuadVertices.put(FULL_QUAD_COORDS).position(0);

        //FOR NOW mOutpuFile = FileManager.getOutputMediaFile(2);
    }

    @Override
    public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //Log.d("denny", "new frame available");
        updateTexture = true;
        parentGLSurfaceView.requestRender();
    }

    /**
     * Called when the surface is created or recreated.
     * <p>
     * Called when the rendering thread
     * starts and whenever the EGL context is lost. The EGL context will typically
     * be lost when the Android device awakes after going to sleep.
     * <p>
     * Since this method is called at the beginning of rendering, as well as
     * every time the EGL context is lost, this method is a convenient place to put
     * code to create resources that need to be created when the rendering
     * starts, and that need to be recreated when the EGL context is lost.
     * Textures are an example of a resource that you might want to create
     * here.
     * <p>
     * Note that when the EGL context is lost, all OpenGL resources associated
     * with that context will be automatically deleted. You do not need to call
     * the corresponding "glDelete" methods such as glDeleteTextures to
     * manually delete these lost resources.
     * <p>
     *
     * @param gl     the GL interface. Use <code>instanceof</code> to
     *               test if the interface supports GL11 or higher interfaces.
     * @param config the EGLConfig of the created surface. Can be used
     */
    @Override
    public synchronized void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "GLSurface Created");
        try {
            mOffscreenShader.setProgram(R.raw.vertex_shader, R.raw.fragment_shader, mContext);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GLES20.glClearColor(1.0f, 1.0f, 0.2f, 1.0f);

        //generate camera texture------------------------
        mCameraTexture.init();

        //set up surfacetexture------------------
        mTextureId=mCameraTexture.getTextureId();
        mSurfaceTexture = new SurfaceTexture(mTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(this);


        MainActivity.camera.setSurfaceTexture(mSurfaceTexture);

        MainActivity.camera.open();
    }

    /**
     * Called when the surface changed size.
     * <p>
     * Called after the surface is created and whenever
     * the OpenGL ES surface size changes.
     * <p>
     * Typically you will set your viewport here. If your camera
     * is fixed then you could also set your projection matrix here:
     * <pre class="prettyprint">
     * void onSurfaceChanged(GL10 gl, int width, int height) {
     * gl.glViewport(0, 0, width, height);
     * // for a fixed camera, set the projection too
     * float ratio = (float) width / height;
     * gl.glMatrixMode(GL10.GL_PROJECTION);
     * gl.glLoadIdentity();
     * gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
     * }
     * </pre>
     *
     * @param gl     the GL interface. Use <code>instanceof</code> to
     *               test if the interface supports GL11 or higher interfaces.
     * @param width
     * @param height
     */
    @SuppressLint("NewApi")
    @Override
    public synchronized void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport( 0, 0, width, height );

        mWidth = width;
        mHeight= height;



        camera_width =0;
        camera_height =0;



        /*try{
            mCameraWrapper.getCamera().stopPreview();
            mCameraWrapper.getCamera().setPreviewTexture(mSurfaceTexture);
        }catch(IOException ioe){
            Log.d("denny", "IO Exception in setting preview texture", ioe);
        }

        mCameraWrapper.setDimension(width,height);*/

        //set camera para-----------------------------------

        camera_width =camera.getDimension().getWidth();
        camera_height =camera.getDimension().getHeight();

        //get the camera orientation and display dimension------------
        if(mContext.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT){
            Matrix.setRotateM(mOrientationM, 0, 90.0f, 0f, 0f, 1f);
            mRatio[1] = camera_width*1.0f/height;
            mRatio[0] = camera_height*1.0f/width;
        }
        else{
            Matrix.setRotateM(mOrientationM, 0, 0.0f, 0f, 0f, 1f);
            mRatio[1] = camera_height*1.0f/height;
            mRatio[0] = camera_width*1.0f/width;
        }


        //mCameraWrapper.startPreview();

        parentGLSurfaceView.requestRender();


    }

    /**
     * Called to draw the current frame.
     * <p>
     * This method is responsible for drawing the current frame.
     * <p>
     * The implementation of this method typically looks like this:
     * <pre class="prettyprint">
     * void onDrawFrame(GL10 gl) {
     * gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
     * //... other gl calls to render the scene ...
     * }
     * </pre>
     *
     * @param gl the GL interface. Use <code>instanceof</code> to
     *           test if the interface supports GL11 or higher interfaces.
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("Denny", "cameraPreview draw frame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        synchronized (this){
            if(updateTexture){
                mSurfaceTexture.updateTexImage();
                //mVideoEncoder.frameAvailable(mSurfaceTexture);
                mSurfaceTexture.getTransformMatrix(mTransformM);
                updateTexture=false;
            }
        }

        //render the texture to FBO if new frame is available
        //GLES20.glViewport(0, 0, mWidth, mHeight);

        mOffscreenShader.useProgram();

        int uTransformM = mOffscreenShader.getHandle("uTransformM");
        int uOrientationM = mOffscreenShader.getHandle("uOrientationM");
        int uRatioV = mOffscreenShader.getHandle("ratios");

        GLES20.glUniformMatrix4fv(uTransformM, 1, false, mTransformM, 0);
        GLES20.glUniformMatrix4fv(uOrientationM, 1, false, mOrientationM, 0);
        GLES20.glUniform2fv(uRatioV, 1, mRatio, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);

        renderQuad(mOffscreenShader.getHandle("aPosition"));

        GLES20.glFlush();

        /* FOR NOW mVideoEncoder.setTextureId(mTextureId);
        //Begin Recording Code Here
        if(beginRecording==true){
            mVideoEncoder.startRecording(new TextureMovieEncoder.EncoderConfig(mOutpuFile, camera_height, camera_width, 4607406, EGL14.eglGetCurrentContext()));
            beginRecording=false;
        }*/
    }

    private void renderQuad(int aPosition){
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0, mFullQuadVertices);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    /* FOR NOWpublic void initCamera(){
        mCameraWrapper=new CameraWrapper();
    }

    public void stopRecording(){
        //Log.d(TAG, "in stopRecording func");
        mVideoEncoder.stopRecording();
        isRecording=false;
    }

    public void startRecording(){
        //Log.d(TAG, "in startRecording func");
        //Log.d(TAG, "egl context : "+EGL14.eglGetCurrentContext().toString());
        beginRecording=true;
        isRecording=true;
    } */

    /*@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onDestroy(){
        updateTexture = false;
        mSurfaceTexture.release();
        if(mCamera != null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
        }

        mCamera = null;
    }*/

    public boolean isRecording(){
        return isRecording;
    }

    /* FOR NOW public String getFilePath(){
        return mOutpuFile.getPath();
    } */

}

