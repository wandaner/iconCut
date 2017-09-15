package com.situ.iconproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.*;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by rrdev on 2017/7/26.
 */

public class AvatarCutActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "xukai";
    private AvatarCutView avatarCutView;
    private TextView tvCancel;
    private TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pk_avatar_cut_view);
        avatarCutView = (AvatarCutView) findViewById(R.id.avatarCutView);
        tvConfirm = (TextView) findViewById(R.id.btn_confirm);
        tvCancel = (TextView) findViewById(R.id.btn_cancel);
        tvConfirm.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        if(avatarCutView==null){
            Log.e(TAG,"onCreate AvatarCutView is Null");
        }else{
            Log.e(TAG,"onCreate AvatarCutView is not Null");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(avatarCutView==null){
            Log.e(TAG,"onResume AvatarCutView is Null");
        }
        else {
            avatarCutView.setImageSrc(getIntent().getData());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                generateUriAndReturn();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void generateUriAndReturn() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap = avatarCutView.clip();
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent();
            intent.setData(mSaveUri);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
