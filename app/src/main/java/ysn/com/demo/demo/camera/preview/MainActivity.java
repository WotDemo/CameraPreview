package ysn.com.demo.demo.camera.preview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @Author yangsanning
 * @ClassName MainActivity
 * @Description 一句话概括作用
 * @Date 2019/8/13
 * @History 2019/8/13 author: description:
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_activity_switch_camera).setOnClickListener(this);

        TextureView textureView = findViewById(R.id.main_activity_texture_view);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                new RxPermissions(MainActivity.this)
                        .request(Manifest.permission.CAMERA)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(granted -> {
                            if (granted) {
                                CameraHelper.get().openCamera(MainActivity.this);
                                CameraHelper.get().startPreview(surfaceTexture);
                            } else {
                                Toast.makeText(MainActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                CameraHelper.get().stopCamera();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                // 在这里获取图像
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_activity_switch_camera:
                CameraHelper.get().switchCamera();
                break;
            default:
                break;
        }
    }
}
