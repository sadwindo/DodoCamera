package com.dodo.dodocamera;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    private static final int REQUEST_FOR_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.btn_pz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_xc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(StartActivity.this, MainActivity.class);
//                startActivity(intent);
                startGallery();
            }
        });
    }

    private void startGallery() {
        String galleryPackage = "com.android.gallery3d";
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        List<PackageInfo> pList = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < pList.size(); i++) {
            PackageInfo pI = pList.get(i);
            if (pI.packageName.equalsIgnoreCase(galleryPackage)) {
                intent.setPackage(galleryPackage);
            }
        }
        startActivityForResult(intent, REQUEST_FOR_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FOR_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            c.close();

            Intent intent = new Intent();
            intent.putExtra("image", picturePath);
            intent.setClass(this, ImageCropActivity.class);
            startActivity(intent);
        }
    }
}
