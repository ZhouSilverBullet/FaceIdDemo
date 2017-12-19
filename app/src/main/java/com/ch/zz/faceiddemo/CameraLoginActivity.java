package com.ch.zz.faceiddemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.widget.TextView;

import com.ch.zz.faceiddemo.http.FDRequest;
import com.ch.zz.faceiddemo.http.HttpCallback;
import com.ch.zz.faceiddemo.utils.AutoFitTextureView;
import com.ch.zz.faceiddemo.utils.FDLocation;
import com.ch.zz.faceiddemo.utils.FaceHelper;
import com.ch.zz.faceiddemo.utils.T;
import com.ch.zz.faceiddemo.wight.LoadingDialog;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class CameraLoginActivity extends AppCompatActivity implements Camera.PreviewCallback, EasyPermissions.PermissionCallbacks {

    private TextView hintText;
    private AutoFitTextureView textureView;
    private SurfaceTexture mSurface;
    private Camera mCamera;
    private int mOrienta;
    private boolean isFirst = true;
    private FaceHelper mFaceHelper;
    private HandlerThread mFaceHandleThread;
    private Handler mFaceHandle;
    private long time;
    private int index;
    private int loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_login);
        initView();
        mFaceHelper = FaceHelper.getInstance();
        mFaceHelper.setZoom(1);
        mFaceHelper.setRectFlagOffset(2.0f);

        mFaceHandleThread = new HandlerThread("face");
        mFaceHandleThread.start();
        mFaceHandle = new Handler(mFaceHandleThread.getLooper());
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 100) {
            T.show("定位权限已经打开");
            FDLocation.getInstance().location();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 100) {
            T.show("请开启权限在打开app");
            finish();
        }
    }

    private void initView() {
        hintText = (TextView) findViewById(R.id.ch_login_hint_text);
        textureView = (AutoFitTextureView) findViewById(R.id.ch_login_camera);

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurface = surface;
                initCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                if (mCamera != null) {
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            loginType = intent.getIntExtra("type", -1);
        }
    }

    private boolean loading = false;

    private class FaceThread implements Runnable {
        private byte[] mData;
        private ByteArrayOutputStream mBitmapOutput;//mUploadOutp1ut
        private Matrix mMatrix;
        private Camera mtCamera;
        private int index;

        public FaceThread(byte[] data, Camera camera, int index) {
            mData = data;
            mBitmapOutput = new ByteArrayOutputStream();
            mMatrix = new Matrix();
            switch (mOrienta) {
                case 90:
                    mMatrix.postRotate(270);
                    break;
                case 270:
                    mMatrix.postRotate(90);
                    break;
                default:
                    mMatrix.postRotate(mOrienta);
                    break;
            }
            mtCamera = camera;
            this.index = index;
        }

        @Override
        public void run() {
            Bitmap bitmap = null;
            Bitmap mFaceBitmap = null;
            try {
                Camera.Size size = mtCamera.getParameters().getPreviewSize();
                YuvImage yuvImage = new YuvImage(mData, ImageFormat.NV21, size.width, size.height, null);
                mData = null;
                yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, mBitmapOutput);
                BitmapFactory.Options options = new BitmapFactory.Options();
//               options.inSampleSize =2;
                options.inPreferredConfig = Bitmap.Config.RGB_565;//必须设置为565，否则无法检测
                // 转换成图片
                bitmap = BitmapFactory.decodeByteArray(mBitmapOutput.toByteArray(), 0, mBitmapOutput.toByteArray().length, options);
                if (bitmap != null) {
                    mBitmapOutput.reset();
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mMatrix, false);
                    FaceDetector.Face[] faces = mFaceHelper.findFaces(bitmap);
                    if (faces != null) {
                        for (FaceDetector.Face face : faces) {
                            if (face == null) {
                                bitmap.recycle();
                                bitmap = null;
                                mBitmapOutput.close();
                                mBitmapOutput = null;
                                Logger.e("无人脸");
                                break;
                            } else {
                                final Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mMatrix, false);
                                Logger.e("有人脸");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap2 != null) {
                                            synchronized (CameraLoginActivity.this) {
                                                if (!loading) {
                                                    loginImg(bitmap2);
                                                }
                                            }
                                        }
                                    }
                                });
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
                if (mFaceBitmap != null) {
                    mFaceBitmap.recycle();
                    mFaceBitmap = null;
                }
                if (mBitmapOutput != null) {
                    try {
                        mBitmapOutput.close();
                        mBitmapOutput = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void loginImg(final Bitmap bitmap) {
        loading = true;
        if (bitmap != null && !bitmap.isRecycled()) {
            if (Gloab.getInstance().getBean() == null) {
                T.show("请进行登录");
            } else {
                if (isFinishing()) {
                    return;
                }
                final LoadingDialog dialog = new LoadingDialog(this, "");
                dialog.show();
                new Thread() {
                    @Override
                    public void run() {
                        File cacheDir = CameraLoginActivity.this.getCacheDir();
                        try {
                            FileOutputStream fos = new FileOutputStream(cacheDir + "/name.jpeg");
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        String url;
                        if (loginType == 10) {
                            url = "http://face.zhouyang.space/baiduface/verifyUser";
                        } else {
                            url = "http://face.zhouyang.space/baiduface/addUser";
                        }
                        FDRequest.post(url, new File(cacheDir + "/name.jpeg"), new HttpCallback<String>() {
                            @Override
                            public void onSuccess(final String s) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
//                                    mCamera.startPreview();
                                        T.show(s);
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }
                                        setResult(101);
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(int code, final String error) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
//                                    mCamera.startPreview();
                                        T.show(error);
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }
                                        loading = false;
                                    }
                                });
                            }
                        });
                    }
                }.start();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hintText.setText("加載完成");
                    initCamera();
                }
            }, 1000);
        } else {
            EasyPermissions.requestPermissions(this, "请允许开启手机摄像头权限", 100, perms);
        }
    }

    private void initCamera() {
        try {
            mCamera = Camera.open(1);
            mCamera.setPreviewTexture(mSurface);
        } catch (Exception e) {
        }
        if (mCamera != null) {
            setOrientationForCamera();
            mCamera.setPreviewCallback(this);
        }
    }

    private void setOrientationForCamera() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(1, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;   // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mOrienta = result;
        mCamera.setDisplayOrientation(result);
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        if (isFirst) {
            isFirst = false;
            StringBuffer sb = new StringBuffer();
            int index = 0;
            for (Camera.Size cs : previewSizes) {
                sb.append((index++) + "=:");
                sb.append(cs.width + "," + cs.height).append("  ");
            }
        } else {
            int index = previewSizes.size() - 1;
            if (previewSizes.size() - 1 >= 7) {
                index = 7;
            }
            Camera.Size size1 = previewSizes.get(index);//default 2,4
            parameters.setPreviewSize(size1.width, size1.height);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (data != null && data.length > 0 && System.currentTimeMillis() - time > 500) {
            time = System.currentTimeMillis();
            if (mFaceHandle != null) {
                mFaceHandle.post(new FaceThread(data, camera, (++index)));
            }
        }
    }
}
