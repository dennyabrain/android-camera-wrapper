package dennymades.space.CameraTwo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import util.Permission;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "Main Activity : ";
    public static String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};

    public static TextureView textureView;
    public static TextureListener textureListener;
    public static CameraDwi camera;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean permissionGranted = Permission.checkPermission(this, permissions);
        if(!permissionGranted){
            Permission.seekPermission(this, permissions, Permission.PERMISSION_ALL);
        }

        camera = new CameraDwi();

        //textureView = (TextureView)findViewById(R.id.textureView);
        //textureListener = new TextureListener();
        //textureView.setSurfaceTextureListener(textureListener);
    }

    /**
     * Callback for the result from requesting CameraTwo. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the CameraTwo request interaction
     * with the user is interrupted. In this case you will receive empty CameraTwo
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested CameraTwo. Never null.
     * @param grantResults The grant results for the corresponding CameraTwo
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*Log.d(TAG, "request code : " + String.valueOf(requestCode));
        for(String permission : CameraTwo){
            Log.d(TAG, "permission : "+permission);
        }
        for(int grantResult : grantResults){
            Log.d(TAG, "permission : "+String.valueOf(grantResult));
        }*/
        switch(requestCode){
            case Permission.PERMISSION_ALL:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                }
                if(grantResults.length>0 && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                }
                if(grantResults.length>0 && grantResults[2]==PackageManager.PERMISSION_GRANTED){
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        camera.startBackgroundThread();
        /*if (textureView.isAvailable()) {
            camera.open();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }*/
    }
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        camera.stopBackgroundThread();
        super.onPause();
    }

    public void btnFlip(View v){
        camera.swapCamera();
    }
}