package dennymades.space.CameraTwo;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by abrain on 10/20/16.
 */
public class TextureListener implements TextureView.SurfaceTextureListener {
    private String TAG = this.getClass().getName();
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.d(TAG, "surface created");
        MainActivity.camera.open();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
