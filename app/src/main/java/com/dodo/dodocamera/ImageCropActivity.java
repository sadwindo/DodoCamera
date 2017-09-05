package com.dodo.dodocamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImageCropActivity extends Activity {
    private ImageCropView cropView = null;
    private String originalUri = null;
    private ImageView cancel;
    private ImageView ensure;
    public boolean isFront = false;// 默认为后置摄像头

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_image_crop);
        init();
    }

    private void init() {
        originalUri = getIntent().getStringExtra("image");
        isFront = getIntent().getBooleanExtra("isFront", false);
        cropView = (ImageCropView) findViewById(R.id.image_crop_view);

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.outWidth = metrics.widthPixels;
//        options.outHeight = metrics.heightPixels;
        cropView.setBitmap(this, originalUri, isFront);

        cancel = (ImageView) findViewById(R.id.cut_cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageCropActivity.this.finish();
            }
        });

        ensure = (ImageView) findViewById(R.id.cut_ensure);
        ensure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = BitmapUtils.saveBitmap(ImageCropActivity.this,
                        cropView.getSubBitmap(), Bitmap.CompressFormat.JPEG, 100);
                refreshMediaStore(fileName);

                Intent intent = new Intent(ImageCropActivity.this, ResultActivity.class);
                intent.putExtra("image", fileName);
                startActivity(intent);
                ImageCropActivity.this.finish();
            }
        });
    }

    // 由于拍照完成保存后，数据库不会立即更新，故需要调用下面的函数来更新多媒体数据库
    private void refreshMediaStore(String fileName) {
        MediaScannerConnection.scanFile(this, new String[]{fileName}, null, null);
    }


}
