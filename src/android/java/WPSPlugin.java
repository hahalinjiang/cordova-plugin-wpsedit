package com.skytech.wps;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.skytech.wps.util.JsonUtil;
import com.skytech.wps.util.WpsUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This class echoes a string called from JavaScript.
 */
public class WPSPlugin extends CordovaPlugin implements WpsUtil.WpsInterface {
    public static int REQUEST_CODE = 0;
    private WpsUtil wpsUtil;
    private CallbackContext McallbackContext;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String backUrl = "";
    private final static int REQ_PERMISSION_CODE = 0x1000;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.McallbackContext = callbackContext;

        if (action.equals("OpenWps")) {
            openWps(args);
            return true;
        } else if (action.equals("deleteFile")) {
            deleteDirectory(args.getString(0));
            return true;
        } else if (action.equals("upLoadFile")) {
            JSONObject myJson = args.getJSONObject(2);
            Map<String, Object> map = JsonUtil.toMap(myJson);

            saveToInternet(args.getString(0), args.getString(1), map);
            return true;
        }
        return false;
    }

    private void openWps(JSONArray args) throws JSONException {
        wpsUtil = new WpsUtil(this, "", "", false, cordova.getActivity(), args.getString(2));
        // 本地打开
//            wpsUtil.openDocument(new F);
        //  wpsUtil.openDocument(new File(args.getString(0).trim()));
        OkGo.<File>get(args.getString(0).trim()).tag(this).execute(new FileCallback(Environment.getExternalStorageDirectory().getPath() + "/nicai", args.getString(1)) {

            @Override
            public void onSuccess(final Response<File> response) {

                cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Log.d("MainActivity", response.body().getAbsoluteFile());

                        File file = response.body();

                        wpsUtil.openDocument(file);
                    }
                });

            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                McallbackContext.error("下载失败");

            }
        });
        wpsUtil.setGetUrl(new WpsUtil.WpsSave() {
            @Override
            public void saveinfo(String url) {
                backUrl = url;

//                    Toast.makeText(cordova.getContext(),url,Toast.LENGTH_SHORT).show();
            }
        });
    }


    public boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
//        if (!dir.endsWith(File.separator))
//            dir = dir + File.separator;
        File file = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!file.exists()) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            McallbackContext.error("删除目录失败：" + dir + "不存在！");

            return false;
        } else {
            file.delete();
            McallbackContext.success("true");

            return true;
        }


    }

    /**
     * @param url     上传地址的URL
     * @param fileUrl 本地路径
     * @param map     参数集合
     */
    private void saveToInternet(String url, String fileUrl, Map<String, Object> map) {
        HttpParams params = new HttpParams();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            params.put(entry.getKey(), (String) entry.getValue());
        }
        //上传单个文件
        File file = new File(fileUrl);
        params.put("file", file);
        OkGo.<String>post(url)
                .tag(this)
                .params(params)
                .isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        McallbackContext.success("true");
                    }

                    @Override
                    public void onError(Response<String> response) {
//                        LogUtil.e("上传失败" + response.body());
                        McallbackContext.error(response.body());

                    }
                });
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(cordova.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(cordova.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }


            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(cordova.getActivity(),
                        (String[]) permissions.toArray(new String[0]),
                        REQ_PERMISSION_CODE);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION_CODE:
                for (int ret : grantResults) {
                    if (PackageManager.PERMISSION_GRANTED != ret) {
//                        Toast.makeText(LoginActivity.this, "用户没有允许需要的权限，使用可能会受到限制！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void doRequest(String filePath) {
        Log.d("MainActivity", "这里处理你的文件保存事件");

    }

    private void closeWPS(String packageName) {

        ActivityManager am = (ActivityManager) cordova.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = am.getRunningAppProcesses();
        int pid = -1;
        //int i = 1;
        for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
            if (amProcess.processName.equals(packageName)) {
                pid = amProcess.pid;
                break;
            }
            Log.i("zhuming", "PID: " + amProcess.pid + "(processName=" + amProcess.processName + "UID=" + amProcess.uid + ")");

        }
        ActivityManager mAm = (ActivityManager) cordova.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
//        mAm.forceStopPackage("com.bbk.audiofx");
        // mAm.killBackgroundProcesses(packageName);
//        android.os.Process.killProcess(pid);

    }

    @Override
    public void doFinish() {

        Log.d("MainActivity", "in doFinish");
        wpsUtil.appBack();
        McallbackContext.success(backUrl);
        // closeWPS("cn.wps.moffice_eng");

    }
}
