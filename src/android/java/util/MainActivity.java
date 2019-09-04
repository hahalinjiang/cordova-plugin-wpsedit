//package com.cdbhe.filedemo;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PermissionInfo;
//import android.net.Uri;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.lzy.okgo.OkGo;
//import com.lzy.okgo.callback.FileCallback;
//import com.lzy.okgo.model.Response;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity implements WpsUtil.WpsInterface {
//    public static int REQUEST_CODE = 0;
//    private WpsUtil wpsUtil;
//
//    private EditText text;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        text = findViewById(R.id.text);
//        Button fileBtn = findViewById(R.id.file_btn);
//        fileBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 在线编辑
//                wpsUtil = new WpsUtil(MainActivity.this, "", text.getText().toString().trim().replace("\\", "/"), false, MainActivity.this);
//                wpsUtil.openDocument();
//                // 本地打开
////                OkGo.<File>get(text.getText().toString().trim()).tag(this).execute(new FileCallback() {
////
////                    @Override
////                    public void onSuccess(final Response<File> response) {
////
////                        MainActivity.this.runOnUiThread(new Runnable() {
////                            @Override
////                            public void run() {
////                                File file = response.body();
////                                wpsUtil.openDocument(file);
////                            }
////                        });
////
////                    }
////                });
//            }
//        });
//
//        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
//
//    }
//
//    public void requestPermission(String[] permissions, int requestCode) {
//        REQUEST_CODE = requestCode;
//        if (this.checkPermissions(permissions)) {
//            this.permissionSucceed(REQUEST_CODE);
//        } else {
//            List<String> needPermissions = this.getPermissions(permissions);
//            ActivityCompat.requestPermissions(this, (String[])needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE);
//        }
//
//    }
//
//    private boolean checkPermissions(String[] permissions) {
//        if (Build.VERSION.SDK_INT < 23) {
//            return true;
//        } else {
//            String[] var2 = permissions;
//            int var3 = permissions.length;
//
//            for(int var4 = 0; var4 < var3; ++var4) {
//                String permission = var2[var4];
//                if (ContextCompat.checkSelfPermission(this, permission) != 0) {
//                    return false;
//                }
//            }
//
//            return true;
//        }
//    }
//    private List<String> getPermissions(String[] permissions) {
//        List<String> permissionList = new ArrayList();
//        String[] var3 = permissions;
//        int var4 = permissions.length;
//
//        for(int var5 = 0; var5 < var4; ++var5) {
//            String permission = var3[var5];
//            if (ContextCompat.checkSelfPermission(this, permission) != 0 || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//                permissionList.add(permission);
//            }
//        }
//
//        return permissionList;
//    }
//
//    public void permissionSucceed(int code) {
//        Log.d("MainActivity", "获取权限成功=" + code);
//    }
//
//    @Override
//    public void doRequest(String filePath) {
//        Log.d("MainActivity", "这里处理你的文件保存事件");
//
//    }
//
//    @Override
//    public void doFinish() {
//        wpsUtil.appBack();
//    }
//}
