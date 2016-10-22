package dennymades.space.CameraTwo;

import android.content.ClipData;
import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by abrain on 10/21/16.
 */
public class StitchGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer,
    View.OnLongClickListener,
    View.OnDragListener{
        private final String TAG = this.getClass().getName();
        private GLSurfaceView.Renderer currentRenderer;
        public PreviewRenderer cameraPreviewRenderer;
        // FOR NOW private VideoRenderer videoRenderer;
        private MediaPlayer mediaPlayer;
        public final static int CAMERA_STATE=1;
        public final static int VIDEO_STATE=2;
        private int currentState = CAMERA_STATE;
        private String videoFilePath;


        //TESTING STUFF
        //FOR NOW private FilterRenderer filterRenderer;

        /**
         * Standard View constructor. In order to render something, you
         * must call {@link #setRenderer} to register a renderer.
         *
         * @param context
         */
        public StitchGLSurfaceView(Context context) {
            super(context);
            //mContext = context;
            initStitchSurface(context, this);
            //initCamera();
        }

        /**
         * Standard View constructor. In order to render something, you
         * must call {@link #setRenderer} to register a renderer.
         *
         * @param context
         * @param attrs
         */
        public StitchGLSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
            //mContext = context;
            initStitchSurface(context, this);
            //initCamera();
        }

        /**
         * standard init function to set the EGL version and set the Renderer for the StitchSurface
         */
    private void initStitchSurface(Context context, GLSurfaceView parent){
        setOnLongClickListener(this);
        setOnDragListener(this);

        cameraPreviewRenderer = new PreviewRenderer();
        cameraPreviewRenderer.initRenderer(context, parent);

        //FOR NOW videoRenderer = new VideoRenderer(context);
        mediaPlayer = new MediaPlayer();
        //FOR NOW videoRenderer.setMediaPlayer(mediaPlayer);

        //test
        //FOR NOW filterRenderer = new FilterRenderer(context);

        //currentRenderer = cameraPreviewRenderer;
        currentRenderer = cameraPreviewRenderer;


        setPreserveEGLContextOnPause(true);
        setEGLConfigChooser(false);
        setEGLContextClientVersion(2);
        setRenderer(this);//replace this
    }

    /*FOR NOW public void changeState(int state){
        if(state==CAMERA_STATE){
            currentRenderer=cameraPreviewRenderer;
        }else if(state==VIDEO_STATE){
            videoRenderer.doMediaPlayerStuff();
            currentRenderer=videoRenderer;
            onHitDone();
            //create Video Renderer on surface created;
            //mediaPlayer.start();
        }
    }*/

    /**
     * Called to draw the current frame.
     * <p/>
     * This method is responsible for drawing the current frame.
     * <p/>
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
        //Log.d(TAG, "on draw frame called");
        //Log.d(TAG, "----");
        currentRenderer.onDrawFrame(gl);
    }

    /**
     * Called when the surface changed size.
     * <p/>
     * Called after the surface is created and whenever
     * the OpenGL ES surface size changes.
     * <p/>
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
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //Log.d(TAG, "onSurfaceChanged");
        currentRenderer.onSurfaceChanged(gl, width, height);
    }

    /**
     * Called when the surface is created or recreated.
     * <p/>
     * Called when the rendering thread
     * starts and whenever the EGL context is lost. The EGL context will typically
     * be lost when the Android device awakes after going to sleep.
     * <p/>
     * Since this method is called at the beginning of rendering, as well as
     * every time the EGL context is lost, this method is a convenient place to put
     * code to create resources that need to be created when the rendering
     * starts, and that need to be recreated when the EGL context is lost.
     * Textures are an example of a resource that you might want to create
     * here.
     * <p/>
     * Note that when the EGL context is lost, all OpenGL resources associated
     * with that context will be automatically deleted. You do not need to call
     * the corresponding "glDelete" methods such as glDeleteTextures to
     * manually delete these lost resources.
     * <p/>
     *
     * @param gl     the GL interface. Use <code>instanceof</code> to
     *               test if the interface supports GL11 or higher interfaces.
     * @param config the EGLConfig of the created surface. Can be used
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //Log.d(TAG, "onSurfaceCreated");
        currentRenderer.onSurfaceCreated(gl, config);
    }

    /*FOR NOW public void setMediaPath(String filePath){
        try {
            mediaPlayer.setDataSource(filePath);
            videoFilePath = filePath;
        } catch (IOException e) {
            Log.d(TAG, "set data source exception ",e);
        }
        mediaPlayer.setLooping(true);
    }*/

    /*public void onHitDone(){
        WritableMap event = Arguments.createMap();
        event.putString("filePath", videoFilePath);
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(),"filePath", event);
    }*/

    @Override
    public boolean onLongClick(View view) {
        Log.d("denny", "long pressed");
        //Write code to support old and new apis
        ClipData data = ClipData.newPlainText("","");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder();
        view.startDrag(data, shadowBuilder, view, 0);
        return true;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        //tv.setText("position : "+dragEvent.getX()/view.getWidth()+","+dragEvent.getY()/view.getHeight());

        switch(dragEvent.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                //initMouseX = dragEvent.getX();
                //initMouseY = dragEvent.getY();
                Log.d("denny", "drag started");
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("denny", "drag ended");
                break;
        }
        return true;
    }
}
